package com.nsb.enms.restful.adapter.server.manager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.restful.adapter.server.action.method.ExecExternalScript;
import com.nsb.enms.restful.adapter.server.common.Pair;
import com.nsb.enms.restful.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.restful.adapter.server.common.conf.ConfigKey;
import com.nsb.enms.restful.adapter.server.common.exception.AdapterException;
import com.nsb.enms.restful.adapter.server.common.exception.AdapterExceptionType;

public class Q3EmlImMgr
{
    private static final Logger log = LogManager.getLogger( Q3EmlImMgr.class );

    private static Map<Integer, List<Integer>> groupToNeId = new HashMap<>();

    private static Q3EmlImMgr q3EmlImMgr = new Q3EmlImMgr();

    private static final int MAX_NE_COUNT = 200;

    private static String q3EmlImScript = ConfLoader.getInstance().getConf(
        ConfigKey.Q3_EMLIM_SCRIPT, ConfigKey.DEFAULT_Q3_EMLIM_SCRIPT );

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
                try
                {
                    Process process = new ExecExternalScript()
                            .run( q3EmlImScript, groupId + "" );
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
                            AdapterExceptionType.EXCPT_INTERNAL_ERROR,
                            e.getMessage() );
                }
            }
        }
    }

    public synchronized Pair getGroupNeId()
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
                return new Pair( groupId + "", neId + "" );
            }
        }
        return null;
    }
}
