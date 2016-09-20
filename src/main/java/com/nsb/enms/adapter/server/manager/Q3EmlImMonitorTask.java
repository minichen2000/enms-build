package com.nsb.enms.adapter.server.manager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.action.method.ExecExternalScript;
import com.nsb.enms.adapter.server.common.ExternalScriptType;
import com.nsb.enms.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.adapter.server.common.conf.ConfigKey;

public class Q3EmlImMonitorTask extends TimerTask
{
    private static final Logger log = LogManager
            .getLogger( Q3EmlImMonitorTask.class );

    private static String monitorScript = ConfLoader.getInstance().getConf(
        ConfigKey.LIST_GROUP_SCRIPT, ConfigKey.DEFAULT_LIST_GROUP_SCRIPT );
    
    private int groupId;
    
    public Q3EmlImMonitorTask(int groupId)
    {
        this.groupId = groupId;
    }

    @Override
    public void run()
    {
        try
        {
            boolean flag = false;
            Process process = ExecExternalScript.run(
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
                Q3EmlImMgr.getInstance().clearNeList();
                Q3EmlImMgr.getInstance().killEmlImProcess();
            }

        }
        catch( Exception e )
        {
            log.error( e.getMessage(), e );
        }
    }
}
