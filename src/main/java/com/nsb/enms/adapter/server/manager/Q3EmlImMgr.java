package com.nsb.enms.adapter.server.manager;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.Pair;
import com.nsb.enms.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.adapter.server.common.conf.ConfigKey;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.exception.AdapterExceptionType;
import com.nsb.enms.adapter.server.db.mgr.AdpNesDbMgr;
import com.nsb.enms.adapter.server.db.mongodb.mgr.AdpMaxNeIdMgr;

public class Q3EmlImMgr
{
    private static final Logger log = LogManager.getLogger( Q3EmlImMgr.class );

    private static List<Integer> neIdList = new LinkedList<Integer>();

    private static Q3EmlImMgr q3EmlImMgr = new Q3EmlImMgr();

    private static final int MAX_NE_OF_ONE_EMLIM = ConfLoader.getInstance().getInt( "MAX_NE_OF_ONE_EMLIM", 200 );

    private ReadWriteLock rwLock = new ReentrantReadWriteLock();

    private int groupId;

    private Q3EmlImMgr()
    {

    }

    public static Q3EmlImMgr getInstance()
    {
        return Q3EmlImMgr.q3EmlImMgr;
    }

    public void init( final int groupId ) throws AdapterException
    {
        this.groupId = groupId;
        AdpNesDbMgr nesDbMgr = new AdpNesDbMgr();
        try
        {
            neIdList = nesDbMgr.getNeIdsByGroupId( String.valueOf( groupId ) );
        }
        catch( Exception e )
        {
            throw new AdapterException(
                    AdapterExceptionType.EXCPT_INTERNAL_ERROR, e.getMessage() );
        }

        // adapter被启动后，需要不断地连接emlim进程，仅此而已
        final Timer timer = new Timer();
        long period = ConfLoader.getInstance().getInt(
            ConfigKey.EMLIM_CONNECT_INTERVAL,
            ConfigKey.DEFAULT_EMLIM_CONNECT_INTERVAL );
        timer.scheduleAtFixedRate( new TimerTask()
        {
            @Override
            public void run()
            {
                log.debug( "start to connect to emlim_" + groupId );
                boolean isOk = CheckQ3EmlImApp.checkQ3EmlIm( groupId );
                log.debug( "the result of connect to emlim_" + groupId + " is "
                        + isOk );
                if( isOk )
                {
                    timer.cancel();
                }

            }
        }, 0, period );

        Timer timer1 = new Timer();
        long period1 = ConfLoader.getInstance().getInt(
            ConfigKey.EMLIM_MONITOR_INTERVAL,
            ConfigKey.DEFAULT_EMLIM_MONITOR_INTERVAL );
        timer1.scheduleAtFixedRate( new Q3EmlImListener( groupId ), period1,
            period1 );
    }

    public synchronized Pair<Integer, Integer> getGroupNeId()
            throws AdapterException
    {
        if( neIdList.size() < MAX_NE_OF_ONE_EMLIM )
        {
            int neId = getMaxNeIdFromDb() + 1;
            updateMaxNeId2Db( neId );
            neIdList.add( neId );
            return new Pair<Integer, Integer>( groupId, neId );
        }

        throw new AdapterException( AdapterExceptionType.EXCPT_INTERNAL_ERROR,
                "The emlim doesn't has capacity to manager NE!!!" );
    }

    private void updateMaxNeId2Db( int neId )
    {
        AdpMaxNeIdMgr.updateNeIdByGroupId( String.valueOf( groupId ),
            String.valueOf( neId ) );
    }

    private int getMaxNeIdFromDb()
    {
        String maxNeId = StringUtils.EMPTY;
        maxNeId = AdpMaxNeIdMgr.getNeIdByGroupId( String.valueOf( groupId ) );
        if( StringUtils.isEmpty( maxNeId ) )
        {
            return 1;
        }
        return Integer.valueOf( maxNeId );
    }

    public void removeNe( int neId )
    {
        rwLock.writeLock().lock();
        neIdList.remove( new Integer( neId ) );
        rwLock.writeLock().unlock();
    }
}
