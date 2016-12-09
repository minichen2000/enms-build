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

    public static boolean deleteNe( String groupId, String neId )
            throws AdapterException
    {
        boolean flag = false;
        stopSupervision( groupId, neId );
        flag = removeNe( groupId, neId );
        return flag;
    }

    private static void stopSupervision( String groupId, String neId )
            throws AdapterException
    {
        Process process = null;
        try
        {
            process = ExecExternalScript.run( ExternalScriptType.TSTMGR,
                stopSupervisionScenario, groupId, neId );

            InputStream inputStream = process.getInputStream();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader( inputStream ) );
            String line = null;
            boolean flag = false;
            while( (line = br.readLine()) != null )
            {
                if (line.contains( "ActionReply received" ))
                {
                    flag = true;
                    break;
                }
            }
            br.close();
            process.waitFor();
            if (!flag)
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
        finally {
            if (process != null)
                ExecExternalScript.destroyProcess( process );
        }
    }

    private static boolean removeNe( String groupId, String neId )
            throws AdapterException
    {
        Process process = null;
        try
        {
            process = ExecExternalScript.run( ExternalScriptType.TSTMGR,
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
                    break;
                }
            }
            br.close();
            process.waitFor();
            if( !flag )
            {
                throw new AdapterException( ErrorCode.FAIL_DELETE_NE_BY_EMLIM );
            }
            Q3EmlImMgr.instance().removeNe( Integer.valueOf( neId ) );
            return flag;
        }
        catch( Exception e )
        {
            log.error( "removeNe", e );
            throw new AdapterException( ErrorCode.FAIL_DELETE_NE_BY_EMLIM );
        }
        finally {
            if (process != null)
                ExecExternalScript.destroyProcess( process );
        }
    }
}
