package com.nsb.enms.adapter.server.manager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

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

    private static final int MAX_COUNT = ConfLoader.getInstance()
            .getInt( "MAX_EMLIM_MONITOR_NUM", 10 );

    private static final int PERIOD = 3000;

    private static int count = 0;

    public static void check( final int groupId )
    {
        final Timer timer = new Timer();
        timer.schedule( new TimerTask()
        {
            @Override
            public void run()
            {
                boolean isOk = checkQ3EmlIm( groupId );
                if( isOk )
                {
                    log.debug( "Connect to emlim_" + groupId + " success!!!" );
                    count = 0;
                    timer.cancel();
                    return;
                }
                count++;
                if( count == MAX_COUNT )
                {
                    // todo
                    // adapter把所有网元communicationState置为unreachable，并发通知,记日志，后退出。
                    Q3EmlImMgr.instance().updateCommunicationState();
                    log.error(
                        "The emlim_" + groupId + " died!!!" );
                    System.exit( 1 );
                }
                log.debug( "Connect to emlim_" + groupId + " failed!!!" );
            }
        }, 0, PERIOD );
    }

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
                    break;
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
