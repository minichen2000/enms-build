package com.nsb.enms.adapter.server.manager;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.action.method.ExecExternalScript;
import com.nsb.enms.adapter.server.common.ExternalScriptType;
import com.nsb.enms.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.adapter.server.common.conf.ConfigKey;

public class CheckQ3EmlImApp
{
    private static final Logger log = LogManager
            .getLogger( CheckQ3EmlImApp.class );

    private static String monitorScript = ConfLoader.getInstance().getConf(
        ConfigKey.LIST_GROUP_SCRIPT, ConfigKey.DEFAULT_LIST_GROUP_SCRIPT );

    public static boolean checkQ3EmlIm( int groupId )
    {
        try
        {
            boolean flag = false;
            Process process = ExecExternalScript.run( ExternalScriptType.TSTMGR,
                monitorScript, String.valueOf( groupId ) );
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

            process.waitFor();
            return flag;
        }
        catch( Exception e )
        {
            log.error( "checkQ3EmlIm", e );
        }
        return false;
    }
}
