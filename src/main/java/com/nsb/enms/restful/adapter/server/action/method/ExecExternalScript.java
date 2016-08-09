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

    private static String tstmgr = ConfLoader.getInstance().getConf(
        ConfigKey.TSTMGR_SCRIPT, ConfigKey.DEFAULT_TSTMGR_SCRIPT );
    
    private static String q3EmlImScript = ConfLoader.getInstance().getConf(
        ConfigKey.Q3_EMLIM_SCRIPT, ConfigKey.DEFAULT_Q3_EMLIM_SCRIPT );

    private static String tstmgrDir = ConfLoader.getInstance().getConf(
        ConfigKey.TSTMGR_SCRIPT_DIR, ConfigKey.DEFAULT_TSTMGR_SCRIPT_DIR );

    private static String emlImDir = ConfLoader.getInstance().getConf(
        ConfigKey.EMLIM_SCRIPT_DIR, ConfigKey.DEFAULT_EMLIM_SCRIPT_DIR );

    private File fileDir;

    public Process run( ExternalScriptType scriptType, String... params ) throws AdapterException
    {
        String[] cmdArray = new String[params.length + 1];
        switch( scriptType )
        {
            case TSTMGR:
                fileDir = new File( tstmgrDir );
                cmdArray[0] = tstmgr;
                break;
            case EMLIM:
                fileDir = new File( emlImDir );
                cmdArray[0] = q3EmlImScript;
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
            log.error( e.getMessage(), e );
            throw new AdapterException(
                    AdapterExceptionType.EXCPT_INTERNAL_ERROR, e.getMessage() );
        }
    }
}
