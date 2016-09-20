package com.nsb.enms.adapter.server.manager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.action.method.ExecExternalScript;
import com.nsb.enms.adapter.server.action.method.ne.DeleteNe;
import com.nsb.enms.adapter.server.common.ExternalScriptType;
import com.nsb.enms.adapter.server.common.Pair;
import com.nsb.enms.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.adapter.server.common.conf.ConfigKey;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.exception.AdapterExceptionType;
import com.nsb.enms.adapter.server.db.mongodb.mgr.MaxNeIdMgr;

public class Q3EmlImMgr
{
    private static final Logger log = LogManager.getLogger( Q3EmlImMgr.class );

    List<Integer> neIdList = new LinkedList<Integer>();

    private static Q3EmlImMgr q3EmlImMgr = new Q3EmlImMgr();

    private static final int MAX_NE_COUNT = 200;

    private ReadWriteLock rwLock = new ReentrantReadWriteLock();

    private int groupId;

    private Q3EmlImMgr()
    {

    }

    public static Q3EmlImMgr getInstance()
    {
        return Q3EmlImMgr.q3EmlImMgr;
    }

    public void init( int groupId ) throws AdapterException
    {
        this.groupId = groupId;
        startEmlIm( groupId );

        Timer timer = new Timer();
        long period = ConfLoader.getInstance().getInt(
            ConfigKey.EMLIM_MONITOR_INTERVAL,
            ConfigKey.DEFAULT_EMLIM_MONITOR_INTERVAL );
        timer.scheduleAtFixedRate( new Q3EmlImMonitorTask( groupId ), period,
            period );
    }

    public void startEmlIm( int groupId ) throws AdapterException
    {
        try
        {
            Process process = ExecExternalScript
                    .run( ExternalScriptType.EMLIM, groupId + "" );
            BufferedReader br = new BufferedReader(
                    new InputStreamReader( process.getInputStream() ) );
            String line;
            boolean flag = false;
            while( (line = br.readLine()) != null )
            {
                if( line.endsWith( "is running" ) )
                {
                    flag = true;
                }
            }
            if( process.waitFor() != 0 || !flag )
            {
                throw new AdapterException(
                        AdapterExceptionType.EXCPT_INTERNAL_ERROR,
                        "Start emlim failed!!!" );
            }

            br.close();
        }
        catch( Exception e )
        {
            log.error( "startEmlIm", e );
            throw new AdapterException(
                    AdapterExceptionType.EXCPT_INTERNAL_ERROR, e.getMessage() );
        }
    }

    public synchronized Pair<Integer, Integer> getGroupNeId()
            throws AdapterException
    {
        int neId = 1;

        if( neIdList.size() < MAX_NE_COUNT )
        {
            if( !neIdList.isEmpty() )
            {
                Collections.sort( neIdList );
                neId = neIdList.get( neIdList.size() - 1 ) + 1;
            }
            else
            {
                neId = getMaxNeIdFromDb() + 1;
            }
            updateMaxNeId2Db( neId );
            neIdList.add( neId );
            return new Pair<Integer, Integer>( groupId, neId );
        }

        throw new AdapterException( AdapterExceptionType.EXCPT_INTERNAL_ERROR,
                "The emlim doesn't has capacity to manager NE!!!" );
    }

    private void updateMaxNeId2Db( int neId )
    {
        MaxNeIdMgr.updateId( String.valueOf( neId ) );
    }

    private int getMaxNeIdFromDb()
    {
        String maxNeId = StringUtils.EMPTY;
        maxNeId = MaxNeIdMgr.getId();
        if( StringUtils.isEmpty( maxNeId ) )
        {
            return 1;
        }
        return Integer.valueOf( maxNeId );
    }

    public void clearNeList()
    {
        rwLock.writeLock().lock();
        neIdList.clear();
        rwLock.writeLock().unlock();
    }

    public void removeNe( int neId )
    {
        rwLock.writeLock().lock();
        neIdList.remove( new Integer( neId ) );
        rwLock.writeLock().unlock();
    }

    public void deleteAllNes()
    {
        if( neIdList != null )
        {
            for( int neId : neIdList )
            {
                try
                {
                    DeleteNe.deleteNe( groupId, neId );
                }
                catch( AdapterException e )
                {
                    log.error( "deleteAllNes", e );
                }
            }
        }

    }

    public void destory() throws AdapterException
    {
        deleteAllNes();
        killEmlImProcess();
    }

    public void killEmlImProcess() throws AdapterException
    {
        try
        {
            Process process = ExecExternalScript
                    .run( ExternalScriptType.KILL_EMLIM, groupId + "" );
            BufferedReader br = new BufferedReader(
                    new InputStreamReader( process.getInputStream() ) );
            String line;
            boolean flag = false;
            while( (line = br.readLine()) != null )
            {
                if( line.contains( "emlim_" + groupId ) )
                {
                    flag = true;
                }
            }
            if( process.waitFor() != 0 || !flag )
            {
                throw new AdapterException(
                        AdapterExceptionType.EXCPT_INTERNAL_ERROR,
                        "Kill emlim process with groupId " + groupId
                                + " failed!!!" );
            }
        }
        catch( Exception e )
        {
            log.error( "killEmlImProcess", e );
            throw new AdapterException(
                    AdapterExceptionType.EXCPT_INTERNAL_ERROR, e.getMessage() );
        }
    }
}
