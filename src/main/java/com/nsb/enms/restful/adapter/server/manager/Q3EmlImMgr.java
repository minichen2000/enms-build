package com.nsb.enms.restful.adapter.server.manager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.restful.adapter.server.action.method.ExecExternalScript;
import com.nsb.enms.restful.adapter.server.action.method.ne.CreateNe;
import com.nsb.enms.restful.adapter.server.action.method.ne.StartSuppervision;
import com.nsb.enms.restful.adapter.server.common.Pair;
import com.nsb.enms.restful.adapter.server.common.exception.AdapterException;
import com.nsb.enms.restful.adapter.server.common.exception.AdapterExceptionType;
import com.nsb.enms.restful.adapter.server.util.CommonConstants;
import com.nsb.enms.restful.adapter.server.util.NeInfo;

public class Q3EmlImMgr
{
    private static final Logger log = LogManager.getLogger( Q3EmlImMgr.class );

    private static Map<Integer, List<Integer>> groupToNeId = new HashMap<>();

    private static Q3EmlImMgr q3EmlImMgr = new Q3EmlImMgr();

    private static final int MAX_NE_COUNT = 200;

    private static Map<Pair<Integer, Integer>, NeInfo> groupNeIdToNe = new HashMap<>();

    private ReadWriteLock rwLock = new ReentrantReadWriteLock();

    private Q3EmlImMgr()
    {

    }

    public static Q3EmlImMgr getInstance()
    {
        return Q3EmlImMgr.q3EmlImMgr;
    }

    public void init( int emlImCount, int groupId ) throws AdapterException
    {
        for( int i = 0; i < emlImCount; i++ )
        {
            if( !groupToNeId.containsKey( groupId ) )
            {
                startEmlIm( groupId );
            }
        }

        new Thread( new Q3EmlImMonitor( groupToNeId.keySet() ) ).start();
    }

    public void startEmlIm( int groupId ) throws AdapterException
    {
        try
        {
            Process process = new ExecExternalScript()
                    .run( CommonConstants.EMLIM_SCRIPT_TYPE, groupId + "" );
            BufferedReader br = new BufferedReader(
                    new InputStreamReader( process.getInputStream() ) );
            String line;
            while( (line = br.readLine()) != null )
            {

            }
            if( process.waitFor() != 0 )
            {
                throw new AdapterException(
                        AdapterExceptionType.EXCPT_INTERNAL_ERROR,
                        "Start emlim failed!!!" );
            }

            br.close();
            groupToNeId.put( groupId, new ArrayList<Integer>() );
            groupId++;
        }
        catch( Exception e )
        {
            log.error( e.getMessage(), e );
            throw new AdapterException(
                    AdapterExceptionType.EXCPT_INTERNAL_ERROR, e.getMessage() );
        }
    }

    public synchronized Pair<Integer, Integer> getGroupNeId()
            throws AdapterException
    {
        int neId = 0;
        for( int groupId : groupToNeId.keySet() )
        {
            List<Integer> neIds = groupToNeId.get( groupId );
            if( neIds.size() < MAX_NE_COUNT )
            {
                if( neIds.size() == 0 )
                {
                    neId = 1;
                }
                else
                {
                    neId = neIds.get( neIds.size() - 1 ) + 1;
                }
                neIds.add( neId );
                return new Pair<Integer, Integer>( groupId, neId );
            }
        }
        throw new AdapterException( AdapterExceptionType.EXCPT_INTERNAL_ERROR,
                "There isn't any EMLIM has capacity to manager NE!!!" );
    }

    public void removeGroup( int groupId )
    {
        rwLock.writeLock().lock();
        groupToNeId.remove( new Integer( groupId ) );
        rwLock.writeLock().unlock();
    }

    public void addGroupNe( int groupId, int neId )
    {
        rwLock.writeLock().lock();
        if( !groupToNeId.containsKey( groupId ) )
        {
            groupToNeId.put( groupId, new ArrayList<Integer>() );
        }
        groupToNeId.get( groupId ).add( neId );
        rwLock.writeLock().unlock();
    }

    public void removeNe( int groupId, int neId )
    {
        rwLock.writeLock().lock();
        groupNeIdToNe.remove( new Pair<Integer, Integer>( groupId, neId ) );
        groupToNeId.get( groupId ).remove( new Integer( neId ) );
        rwLock.writeLock().unlock();
    }

    public void storeNeInfo( int groupId, int neId, String neRelease,
            String neType, String userLabel, String locationName,
            String neAddress )
    {
        rwLock.writeLock().lock();
        List<Integer> neIds = groupToNeId.get( groupId );
        if( !neIds.contains( neId ) )
        {
            neIds.add( neId );
        }

        Pair<Integer, Integer> key = new Pair<>( groupId, neId );
        NeInfo neInfo = groupNeIdToNe.get( key );
        if( neInfo == null )
        {
            groupNeIdToNe.put( key, new NeInfo( groupId, neId, neRelease,
                    neType, userLabel, locationName, neAddress ) );
        }
        else
        {
            neInfo.setNeRelease( neRelease );
            neInfo.setNeType( neType );
            neInfo.setUserLabel( userLabel );
            neInfo.setLocationName( locationName );
            neInfo.setNeAddress( neAddress );
        }
        rwLock.writeLock().unlock();
    }

    public void reCreateNe( int groupId )
    {
        for( Pair<Integer, Integer> groupNeId : groupNeIdToNe.keySet() )
        {
            if( groupNeId.getFirst().equals( new Integer( groupId ) ) )
            {
                NeInfo neInfo = groupNeIdToNe.get( groupNeId );
                try
                {
                    int neId = (int) groupNeId.getSecond();
                    addGroupNe( groupId, neId );
                    new CreateNe().createNe( groupId, neId,
                        neInfo.getNeRelease(), neInfo.getNeType(),
                        neInfo.getUserLabel(), neInfo.getLocationName(),
                        neInfo.getNeAddress() );

                    new StartSuppervision().startSuppervision( groupId, neId );
                }
                catch( AdapterException e )
                {
                    log.error( e.getMessage(), e );
                }
            }
        }
    }
}
