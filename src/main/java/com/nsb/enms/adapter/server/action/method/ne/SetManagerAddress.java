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

public class SetManagerAddress
{
    private final static Logger log = LogManager
            .getLogger( SetManagerAddress.class );

    private static String setMainOSAddressScenario = ConfLoader.getInstance()
            .getConf( ConfigKey.SET_MAIN_OS_ADDR_REQ,
                ConfigKey.DEFAULT_SET_MAIN_OS_ADDR_REQ );

    private static String setSpareOSAddressScenario = ConfLoader.getInstance()
            .getConf( ConfigKey.SET_SPARE_OS_ADDR_REQ,
                ConfigKey.DEFAULT_SET_SPARE_OS_ADDR_REQ );

    public static boolean setMainOSAddress( String groupId, String neId,
            String mainOSAddress ) throws AdapterException
    {
        Process process = null;
        try
        {
            process = ExecExternalScript.run( ExternalScriptType.TSTMGR,
                setMainOSAddressScenario, groupId, neId );

            InputStream inputStream = process.getInputStream();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader( inputStream ) );
            String line;
            boolean flag = false;
            while( (line = br.readLine()) != null )
            {
                if( line.contains( "SetReply received" ) )
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
            log.error( "setMainOSAddress", e );
            throw new AdapterException(
                    ErrorCode.FAIL_SET_MANAGER_ADDRESS_BY_EMLIM );
        }
        finally {
            if (process != null)
                ExecExternalScript.destroyProcess( process );
        }
    }

    public static boolean setSpareOSAddress( String groupId, String neId,
            String spareOSAddress ) throws AdapterException
    {
        Process process = null;
        try
        {
            process = ExecExternalScript.run( ExternalScriptType.TSTMGR,
                setSpareOSAddressScenario, groupId, neId );

            InputStream inputStream = process.getInputStream();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader( inputStream ) );
            String line;
            boolean flag = false;
            while( (line = br.readLine()) != null )
            {
                if( line.contains( "SetReply received" ) )
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
            log.error( "setSpareOSAddress", e );
            throw new AdapterException(
                    ErrorCode.FAIL_SET_MANAGER_ADDRESS_BY_EMLIM );
        }
        finally {
            if (process != null)
                ExecExternalScript.destroyProcess( process );
        }
    }
}
