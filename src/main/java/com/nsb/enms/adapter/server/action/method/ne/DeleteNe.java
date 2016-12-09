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
import com.nsb.enms.adapter.server.manager.Q3EmlImMgr;
import com.nsb.enms.common.ErrorCode;

public class DeleteNe
{
    private final static Logger log = LogManager.getLogger( DeleteNe.class );

    private static String deleteNeScenario = ConfLoader.getInstance().getConf(
        ConfigKey.DELETE_NE_REQ, ConfigKey.DEFAULT_DELETE_NE_REQ );

    private static String stopSupervisionScenario = ConfLoader.getInstance()
            .getConf( ConfigKey.STOP_SUPERVISION_REQ,
                ConfigKey.DEFAULT_STOP_SUPERVISION_REQ );

    public static void deleteNe( String groupId, String neId )
            throws AdapterException
    {
        stopSupervision( groupId, neId );
        removeNe( groupId, neId );
    }

    private static void stopSupervision( String groupId, String neId )
            throws AdapterException
    {
        log.debug( "------------Start stopSupervision-------------------" );
        try
        {
            Process process = ExecExternalScript.run( ExternalScriptType.TSTMGR,
                stopSupervisionScenario, groupId, neId );

            InputStream inputStream = process.getInputStream();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader( inputStream ) );
            while( br.readLine() != null )
            {
                
            }
            br.close();
            if (process.waitFor() != 0)
            {
                throw new AdapterException(
                    ErrorCode.FAIL_UNSUPERVISE_NE_BY_EMLIM );
            }
        }
        catch( Exception e )
        {
            log.error( "stopSupervision", e );
            throw new AdapterException(
                    ErrorCode.FAIL_UNSUPERVISE_NE_BY_EMLIM );
        }
        log.debug( "------------End stopSupervision-------------------" );
    }

    private static void removeNe( String groupId, String neId )
            throws AdapterException
    {
        log.debug( "------------Start removeNe-------------------" );
        try
        {
            Process process = ExecExternalScript.run( ExternalScriptType.TSTMGR,
                deleteNeScenario, groupId, neId );
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
            process.waitFor();
            if( !flag )
            {
                throw new AdapterException( ErrorCode.FAIL_DELETE_NE_BY_EMLIM );
            }
            Q3EmlImMgr.instance().removeNe( Integer.valueOf( neId ) );
        }
        catch( Exception e )
        {
            log.error( "removeNe", e );
            throw new AdapterException( ErrorCode.FAIL_DELETE_NE_BY_EMLIM );
        }
        log.debug( "------------End removeNe-------------------" );
    }
}
