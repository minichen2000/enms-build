package com.nsb.enms.adapter.server.manager;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.action.method.ne.DeleteNe;
import com.nsb.enms.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.adapter.server.common.conf.ConfigKey;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.exception.AdapterExceptionType;
import com.nsb.enms.adapter.server.common.utils.TimeUtil;
import com.nsb.enms.adapter.server.db.mgr.AdpNesDbMgr;
import com.nsb.enms.adapter.server.db.mgr.AdpTpsDbMgr;
import com.nsb.enms.adapter.server.db.mgr.AdpXcsDbMgr;
import com.nsb.enms.adapter.server.db.mongodb.mgr.AdpMaxNeIdMgr;
import com.nsb.enms.adapter.server.notification.NotificationSender;
import com.nsb.enms.common.AlarmSeverity;
import com.nsb.enms.common.AlarmType;
import com.nsb.enms.common.EntityType;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.common.utils.Pair;
import com.nsb.enms.restful.model.adapter.AdpNe;

public class Q3EmlImMgr
{
    private static final Logger log = LogManager.getLogger( Q3EmlImMgr.class );

    private static List<Integer> neIdList = new LinkedList<Integer>();

    private static Q3EmlImMgr inst_ = new Q3EmlImMgr();

    private static final int MAX_NE_NUM = ConfLoader.getInstance().getInt(
        ConfigKey.MAX_NE_OF_ONE_EMLIM, ConfigKey.DEFAULT_MAX_NE_OF_ONE_EMLIM );

    private AdpNesDbMgr nesDbMgr = new AdpNesDbMgr();

    private ReadWriteLock rwLock = new ReentrantReadWriteLock();

    private int groupId;

    private Q3EmlImMgr()
    {

    }

    public static Q3EmlImMgr instance()
    {
        if( inst_ == null )
        {
            inst_ = new Q3EmlImMgr();
        }
        return inst_;
    }

    public void init( final int groupId ) throws AdapterException
    {
        this.groupId = groupId;
        try
        {
            neIdList = nesDbMgr.getNeIdsByGroupId( String.valueOf( groupId ) );
        }
        catch( Exception e )
        {
            log.error( "getNeIdsByGroupId", e );
            throw new AdapterException(
                    AdapterExceptionType.EXCPT_INTERNAL_ERROR, e.getMessage() );
        }

        // adapter被启动后，需要不断地连接emlim进程，仅此而已(每隔3秒连一次，十次失败，退出)
        CheckQ3EmlImApp.check( groupId );

        Timer timer = new Timer();
        long period = ConfLoader.getInstance().getInt(
            ConfigKey.EMLIM_MONITOR_INTERVAL,
            ConfigKey.DEFAULT_EMLIM_MONITOR_INTERVAL );
        timer.scheduleAtFixedRate( new Q3EmlImListener( groupId ), period,
            period );
    }

    public synchronized Pair<Integer, Integer> getGroupNeId()
            throws AdapterException
    {
        if( neIdList.size() < MAX_NE_NUM )
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

    public void destroy()
    {
        for( int i = neIdList.size() - 1; i >= 0; i-- )
        {
            try
            {
                DeleteNe.deleteNe( groupId, neIdList.get( i ) );
            }
            catch( AdapterException e )
            {
                log.error( "DeleteNe", e );
            }
        }

        try
        {
            AdpTpsDbMgr tpsDbMgr = new AdpTpsDbMgr();
            AdpXcsDbMgr xcsDbMgr = new AdpXcsDbMgr();
            List<AdpNe> nes = nesDbMgr
                    .getNesByGroupId( String.valueOf( groupId ) );
            for( AdpNe ne : nes )
            {
                xcsDbMgr.deleteXcsByNeId( ne.getId() );
                tpsDbMgr.deleteTpsbyNeId( ne.getId() );
            }
            nesDbMgr.deleteNesByGroupId( groupId );
            AdpMaxNeIdMgr.updateNeIdByGroupId( String.valueOf( groupId ),
                String.valueOf( 0 ) );
        }
        catch( Exception e )
        {
            log.error( "deleteNe", e );
        }
        System.exit( 0 );
    }

    public void sendAlarm()
    {
        try
        {
            List<AdpNe> neList = nesDbMgr
                    .getNesByGroupId( String.valueOf( groupId ) );
            for( AdpNe ne : neList )
            {
                String id = ne.getId();
                Date date = new Date();
                String eventTime = TimeUtil.getLocalTmfTime( date );
                String occureTime = eventTime;
                NotificationSender.instance().sendAlarm(
                    String.valueOf(
                        ErrorCode.ALM_NE_MISALIGNMENT.getErrorCode() ),
                    AlarmType.ALM_COMMUNICATION.toString(), AlarmSeverity.CRITICAL.toString(),
                    eventTime, occureTime, "",
                    ErrorCode.ALM_NE_MISALIGNMENT.getMessage(),
                    EntityType.NE.toString(), id, "", "",
                    "emlim_" + groupId + " is unreachable" );
            }
        }
        catch( Exception e )
        {
            log.error( "sendAlarm", e );
        }
    }
}