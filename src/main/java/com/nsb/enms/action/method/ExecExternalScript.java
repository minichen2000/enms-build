package com.nsb.enms.action.method;

import com.nsb.enms.action.common.CommonConstants;
import com.nsb.enms.action.common.conf.ConfLoader;
import com.nsb.enms.action.common.conf.ConfigKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ExecExternalScript
{
    private static final Logger log = LogManager
            .getLogger( ExecExternalScript.class );

    private static String tstmgr = ConfLoader.getInstance()
            .getConf( ConfigKey.TSTMGR_SCRIPT, CommonConstants.TSTMGR_SCRIPT );

    private static String tstmgrDir = ConfLoader.getInstance().getConf(
        ConfigKey.TSTMGR_SCRIPT_DIR, CommonConstants.TSTMGR_SCRIPT_DIR );

    private static File fileDir = new File( tstmgrDir );

    public Process run( String... params )
    {
        String[] cmdArray = new String[params.length + 1];
        cmdArray[0] = tstmgr;
        System.arraycopy( params, 0, cmdArray, 1, params.length );
        log.debug( "exec:" + Arrays.asList( cmdArray ) );

        try
        {
            return Runtime.getRuntime().exec( cmdArray, null,
                fileDir );
        }
        catch( IOException e )
        {
            log.error( e.getMessage(), e );
        }
        return null;
    }
}
