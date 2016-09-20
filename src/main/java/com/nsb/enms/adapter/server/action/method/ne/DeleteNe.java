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
import com.nsb.enms.adapter.server.common.exception.AdapterExceptionType;
import com.nsb.enms.adapter.server.manager.Q3EmlImMgr;

public class DeleteNe
{
    private final static Logger log = LogManager.getLogger( DeleteNe.class );

    private static String deleteNeScenario = ConfLoader.getInstance().getConf(
        ConfigKey.DELETE_NE_REQ, ConfigKey.DEFAULT_DELETE_NE_REQ );

    private static String stopSupervisionScenario = ConfLoader.getInstance()
            .getConf( ConfigKey.STOP_SUPERVISION_REQ,
                ConfigKey.DEFAULT_STOP_SUPERVISION_REQ );

    public static boolean deleteNe( int groupId, int neId )
            throws AdapterException
    {
        boolean flag = false;
        stopSuppervision( groupId, neId );
        flag = removeNe( groupId, neId );
        return flag;
    }

    private static void stopSuppervision( int groupId, int neId )
            throws AdapterException
    {
        try
        {
            Process process = ExecExternalScript.run(
                ExternalScriptType.TSTMGR, stopSupervisionScenario,
                String.valueOf( groupId ), String.valueOf( neId ) );

            InputStream inputStream = process.getInputStream();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader( inputStream ) );
            String line;
            while( (line = br.readLine()) != null )
            {

            }
            br.close();
            process.waitFor();
        }
        catch( Exception e )
        {
            log.error( "stopSuppervision", e );
            throw new AdapterException(
                    AdapterExceptionType.EXCPT_INTERNAL_ERROR, e.getMessage() );
        }
    }

    private static boolean removeNe( int groupId, int neId )
            throws AdapterException
    {
        try
        {
            Process process = ExecExternalScript.run(
                ExternalScriptType.TSTMGR, deleteNeScenario,
                String.valueOf( groupId ), String.valueOf( neId ) );
            InputStream inputStream = process.getInputStream();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader( inputStream ) );
            boolean flag = false;
            String line;
            while( (line = br.readLine()) != null )
            {
                if( line.contains( "DeleteReply received" ) )
                {
                    flag = true;
                }
            }
            br.close();

            if( process.waitFor() != 0 || !flag )
            {
                throw new AdapterException(
                        AdapterExceptionType.EXCPT_INTERNAL_ERROR,
                        "Delete ne failed!!!" );
            }
            Q3EmlImMgr.getInstance().removeNe( neId );
            return flag;
        }
        catch( Exception e )
        {
            log.error( "removeNe", e );
            throw new AdapterException(
                    AdapterExceptionType.EXCPT_INTERNAL_ERROR, e.getMessage() );
        }
    }
}
