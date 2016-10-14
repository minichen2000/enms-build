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

public class SetAddress
{
    private final static Logger log = LogManager.getLogger( SetAddress.class );

    private static String setMainOSAddressScenario = ConfLoader.getInstance()
            .getConf( ConfigKey.SET_MAIN_OS_ADDR_REQ,
                ConfigKey.DEFAULT_SET_MAIN_OS_ADDR_REQ );

    private static String setSpareOSAddressScenario = ConfLoader.getInstance()
            .getConf( ConfigKey.SET_SPARE_OS_ADDR_REQ,
                ConfigKey.DEFAULT_SET_SPARE_OS_ADDR_REQ );

    public static boolean setMainOSAddress( int groupId, int neId,
            String mainOSAddress ) throws AdapterException
    {
        try
        {
            Process process = ExecExternalScript.run( ExternalScriptType.TSTMGR,
                setMainOSAddressScenario, String.valueOf( groupId ),
                String.valueOf( neId ) );

            InputStream inputStream = process.getInputStream();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader( inputStream ) );
            String line;
            boolean flag = false;
            while( (line = br.readLine()) != null )
            {
                if(line.contains( "SetReply received" ))
                {
                    flag = true;
                }
            }
            br.close();
            process.waitFor();
            return flag;
        }
        catch( Exception e )
        {
            log.error( "stopSuppervision", e );
            throw new AdapterException(
                    AdapterExceptionType.EXCPT_INTERNAL_ERROR, e.getMessage() );
        }
    }

    public static boolean setSpareOSAddress( int groupId, int neId,
            String spareOSAddress ) throws AdapterException
    {
        try
        {
            Process process = ExecExternalScript.run( ExternalScriptType.TSTMGR,
                setSpareOSAddressScenario, String.valueOf( groupId ),
                String.valueOf( neId ) );

            InputStream inputStream = process.getInputStream();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader( inputStream ) );
            String line;
            boolean flag = false;
            while( (line = br.readLine()) != null )
            {
                if(line.contains( "SetReply received" ))
                {
                    flag = true;
                }
            }
            br.close();
            process.waitFor();
            return flag;
        }
        catch( Exception e )
        {
            log.error( "stopSuppervision", e );
            throw new AdapterException(
                    AdapterExceptionType.EXCPT_INTERNAL_ERROR, e.getMessage() );
        }
    }
}
