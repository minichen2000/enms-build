package com.nsb.enms.restful.adapter.server.action.method;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.restful.adapter.server.common.ExternalScriptType;
import com.nsb.enms.restful.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.restful.adapter.server.common.conf.ConfigKey;
import com.nsb.enms.restful.adapter.server.common.exception.AdapterException;
import com.nsb.enms.restful.adapter.server.common.exception.AdapterExceptionType;

public class ExecExternalScript
{
    private static final Logger log = LogManager
            .getLogger( ExecExternalScript.class );

    private static String tstmgrScript = ConfLoader.getInstance().getConf(
        ConfigKey.TSTMGR_SCRIPT, ConfigKey.DEFAULT_TSTMGR_SCRIPT );
    
    private static String q3EmlImScript = ConfLoader.getInstance().getConf(
        ConfigKey.Q3_EMLIM_SCRIPT, ConfigKey.DEFAULT_Q3_EMLIM_SCRIPT );

    private static String tstmgrDir = ConfLoader.getInstance().getConf(
        ConfigKey.TSTMGR_SCRIPT_DIR, ConfigKey.DEFAULT_TSTMGR_SCRIPT_DIR );

    private static String emlImDir = ConfLoader.getInstance().getConf(
        ConfigKey.EMLIM_SCRIPT_DIR, ConfigKey.DEFAULT_EMLIM_SCRIPT_DIR );
    
    private static String killEmlImScript = ConfLoader.getInstance().getConf(
        ConfigKey.KILL_EMLIM_SCRIPT, ConfigKey.DEFAULT_KILL_EMLIM_SCRIPT );

    private File fileDir;

    public Process run( ExternalScriptType scriptType, String... params ) throws AdapterException
    {
        String[] cmdArray = new String[params.length + 1];
        switch( scriptType )
        {
            case TSTMGR:
                fileDir = new File( tstmgrDir );
                cmdArray[0] = tstmgrScript;
                break;
            case EMLIM:
                fileDir = new File( emlImDir );
                cmdArray[0] = q3EmlImScript;
                break;
            case KILL_EMLIM:
                //to fill correct dir later
                fileDir = new File( "XXX" );
                cmdArray[0] = killEmlImScript;
                break;
            default:
                break;
        }
        
        System.arraycopy( params, 0, cmdArray, 1, params.length );
        log.debug( "exec:" + Arrays.asList( cmdArray ) );

        try
        {
            return Runtime.getRuntime().exec( cmdArray, null, fileDir );
        }
        catch( IOException e )
        {
            log.error( "execExternalScript", e );
            throw new AdapterException(
                    AdapterExceptionType.EXCPT_INTERNAL_ERROR, e.getMessage() );
        }
    }
}
