package com.nsb.enms.restful.adapter.server.manager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.restful.adapter.server.action.method.ExecExternalScript;
import com.nsb.enms.restful.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.restful.adapter.server.common.conf.ConfigKey;

public class Q3EmlImMonitor implements Runnable
{
    private static final Logger log = LogManager
            .getLogger( Q3EmlImMonitor.class );

    private static String monitorScript = ConfLoader.getInstance().getConf(
        ConfigKey.LIST_GROUP_SCRIPT, ConfigKey.DEFAULT_LIST_GROUP_SCRIPT );

    private Set<Integer> groupIds;

    public Q3EmlImMonitor( Set<Integer> groupIds )
    {
        this.groupIds = groupIds;
    }

    @Override
    public void run()
    {
        while( true )
        {
            for( int groupId : groupIds )
            {
                try
                {
                    boolean flag = false;
                    Process process = new ExecExternalScript()
                            .run( monitorScript, groupId + "" );
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader( process.getInputStream() ) );
                    String line;
                    while( (line = br.readLine()) != null )
                    {
                        if( line.contains( "GetReply received" ) )
                        {
                            flag = true;
                        }
                    }

                    if( process.waitFor() != 0 )
                    {
                        // TODO
                    }

                    if( !flag )
                    {
                        //kill the emlim process
                        //restart emlim process
                        //recreateNe
                    }

                    Thread.sleep( 10000 );
                }
                catch( Exception e )
                {
                    log.error( e.getMessage(), e );
                }
            }
        }
    }
}
