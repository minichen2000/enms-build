package com.nsb.enms.restful.adapter.server.manager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.restful.adapter.server.action.method.ExecExternalScript;
import com.nsb.enms.restful.adapter.server.common.ExternalScriptType;
import com.nsb.enms.restful.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.restful.adapter.server.common.conf.ConfigKey;

public class Q3EmlImMonitorTask extends TimerTask
{
    private static final Logger log = LogManager
            .getLogger( Q3EmlImMonitorTask.class );

    private static String monitorScript = ConfLoader.getInstance().getConf(
        ConfigKey.LIST_GROUP_SCRIPT, ConfigKey.DEFAULT_LIST_GROUP_SCRIPT );

    private static String killEmlimScript = ConfLoader.getInstance().getConf(
        ConfigKey.KILL_EMLIM_SCRIPT, ConfigKey.DEFAULT_KILL_EMLIM_SCRIPT );

    private Set<Integer> groupIds;

    public Q3EmlImMonitorTask( Set<Integer> groupIds )
    {
        this.groupIds = groupIds;
    }

    @Override
    public void run()
    {
        for( int groupId : groupIds )
        {
            try
            {
                boolean flag = false;
                Process process = new ExecExternalScript().run(
                    ExternalScriptType.TSTMGR, monitorScript, groupId + "" );
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
                    log.error( "Execute external script " + monitorScript
                            + "failed, groupId=" + groupId );
                }

                if( !flag )
                {
                    Q3EmlImMgr.getInstance().removeGroup( groupId );
                    process = new ExecExternalScript().run(
                        ExternalScriptType.TSTMGR, killEmlimScript,
                        groupId + "" );
                    process.waitFor();
                    Q3EmlImMgr.getInstance().startEmlIm( groupId );
                    Q3EmlImMgr.getInstance().reCreateNe( groupId );
                }

            }
            catch( Exception e )
            {
                log.error( e.getMessage(), e );
            }
        }
    }
}
