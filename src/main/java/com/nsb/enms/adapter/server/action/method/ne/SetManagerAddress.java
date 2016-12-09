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

    public static void setMainOSAddress( String groupId, String neId,
            String mainOSAddress ) throws AdapterException
    {
        log.debug( "------------Start setMainOSAddress-------------------" );
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
                    break;
                }
            }
            br.close();
            process.waitFor();
            if( !flag )
            {
                throw new AdapterException(
                        ErrorCode.FAIL_SET_MANAGER_ADDRESS_BY_EMLIM );
            }
        }
        catch( Exception e )
        {
            log.error( "setMainOSAddress", e );
            throw new AdapterException(
                    ErrorCode.FAIL_SET_MANAGER_ADDRESS_BY_EMLIM );
        }
        finally
        {
            if( process != null )
                ExecExternalScript.destroyProcess( process );
        }
        log.debug( "------------End setMainOSAddress-------------------" );
    }

    public static void setSpareOSAddress( String groupId, String neId,
            String spareOSAddress ) throws AdapterException
    {
        log.debug( "------------Start setSpareOSAddress-------------------" );
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
                    break;
                }
            }
            br.close();
            process.waitFor();
            if (!flag)
            {
                throw new AdapterException(
                    ErrorCode.FAIL_SET_MANAGER_ADDRESS_BY_EMLIM );
            }
        }
        catch( Exception e )
        {
            log.error( "setSpareOSAddress", e );
            throw new AdapterException(
                    ErrorCode.FAIL_SET_MANAGER_ADDRESS_BY_EMLIM );
        }
        finally
        {
            if( process != null )
                ExecExternalScript.destroyProcess( process );
        }
        log.debug( "------------End setSpareOSAddress-------------------" );
    }
}
