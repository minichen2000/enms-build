package com.nsb.enms.adapter.server.action.method.ne;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.action.method.ExecExternalScript;
import com.nsb.enms.adapter.server.common.ExternalScriptType;
import com.nsb.enms.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.adapter.server.common.conf.ConfigKey;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.common.ErrorCode;

public class StartSupervision
{
    private static final Logger log = LogManager
            .getLogger( StartSupervision.class );

    private static final int MAX_COUNT = 3;

    private static final long SLEEP_TIME = 10000;

    private static String startSupervisionScenario = ConfLoader.getInstance()
            .getConf( ConfigKey.START_SUPERVISION_REQ,
                ConfigKey.DEFAULT_START_SUPERVISION_REQ );

    public static boolean startSupervision( String groupId, String neId )
            throws AdapterException
    {
        boolean flag = false;
        int count = 0;
        while( count < MAX_COUNT )
        {
            try
            {
                Process process = ExecExternalScript.run(
                    ExternalScriptType.TSTMGR, startSupervisionScenario,
                    groupId, neId );
                InputStream inputStream = process.getInputStream();
                BufferedReader br = new BufferedReader(
                        new InputStreamReader( inputStream ) );
                String line;

                while( (line = br.readLine()) != null )
                {
                    if( line.contains( "ActionReply received" ) )
                    {
                        flag = true;
                    }
                }
                br.close();
                if( process.waitFor() != 0 )
                {
                    return false;
                }

                if( flag )
                {
                    return true;
                }
                count++;

                Thread.sleep( SLEEP_TIME );

            }
            catch( Exception e )
            {
                log.error( "startSupervision", e );
                throw new AdapterException(
                        ErrorCode.FAIL_SUPERVISE_NE_BY_EMLIM );
            }
        }
        return false;
    }
}
