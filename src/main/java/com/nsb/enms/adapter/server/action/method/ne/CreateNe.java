package com.nsb.enms.adapter.server.action.method.ne;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.action.entity.NeEntity;
import com.nsb.enms.adapter.server.action.method.ExecExternalScript;
import com.nsb.enms.adapter.server.common.ExternalScriptType;
import com.nsb.enms.adapter.server.common.Pair;
import com.nsb.enms.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.adapter.server.common.conf.ConfigKey;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.exception.AdapterExceptionType;
import com.nsb.enms.adapter.server.manager.Q3EmlImMgr;

public class CreateNe
{
    private final static Logger log = LogManager.getLogger( CreateNe.class );

    private static String createNeScenario = ConfLoader.getInstance().getConf(
        ConfigKey.CREATE_NE_REQ, ConfigKey.DEFAULT_CREATE_NE_REQ );

    private static String setNeAddressScenario = ConfLoader.getInstance()
            .getConf( ConfigKey.SET_NE_ADDR_REQ,
                ConfigKey.DEFAULT_SET_NE_ADDR_REQ );

    private static String setNeIsaAddressScenario = ConfLoader.getInstance()
            .getConf( ConfigKey.SET_NE_ISA_ADDR_REQ,
                ConfigKey.DEFAULT_SET_NE_ISA_ADDR_REQ );

    private static Pattern pattern = Pattern
            .compile( "\\d+\\.\\d+\\.\\d+\\.\\d+:\\d+" );

    public static NeEntity createNe( String neRelease, String neType,
            String userLabel, String locationName, String neAddress )
            throws AdapterException
    {
        Pair<Integer, Integer> groupNeId = Q3EmlImMgr.instance()
                .getGroupNeId();
        int groupId = groupNeId.getFirst();
        int neId = groupNeId.getSecond();
        log.debug( "The groupId = " + groupId + ", neId = " + neId );
        boolean flag = false;
        flag = createNe( groupId, neId, neRelease, neType, userLabel,
            locationName );
        if( flag )
        {
            flag = setNeAddress( groupId, neId, neAddress );
        }

        if( flag )
        {
            return GetNe.getNe( groupId, neId );
        }
        return null;
    }

    private static boolean createNe( int groupId, int neId, String neRelease,
            String neType, String userLabel, String locationName )
            throws AdapterException
    {
        try
        {
            Process process = ExecExternalScript.run(
                ExternalScriptType.TSTMGR, createNeScenario,
                String.valueOf( groupId ), String.valueOf( neId ), neRelease,
                neType, userLabel, locationName );

            InputStream inputStream = process.getInputStream();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader( inputStream ) );
            String line;
            boolean flag = false;
            while( (line = br.readLine()) != null )
            {
                if( line.contains( "CreateReply received" ) )
                {
                    flag = true;
                }
            }
            br.close();

            if( process.waitFor() != 0 || !flag )
            {
                throw new AdapterException(
                        AdapterExceptionType.EXCPT_INTERNAL_ERROR,
                        "Create ne failed!!!" );
            }
            return flag;
        }
        catch( Exception e )
        {
            log.error( "createNe", e );
            throw new AdapterException(
                    AdapterExceptionType.EXCPT_INTERNAL_ERROR, e.getMessage() );
        }
    }

    private static boolean setNeAddress( int groupId, int neId,
            String neAddress ) throws AdapterException
    {
        try
        {
            String scenario = setNeAddressScenario;
            if( pattern.matcher( neAddress ).find() )
            {
                scenario = setNeIsaAddressScenario;
            }
            Process process = ExecExternalScript.run(
                ExternalScriptType.TSTMGR, scenario, String.valueOf( groupId ),
                String.valueOf( neId ), neAddress );
            InputStream inputStream = process.getInputStream();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader( inputStream ) );
            boolean flag = false;
            String line;
            while( (line = br.readLine()) != null )
            {
                if( line.contains( "SetReply received" ) )
                {
                    flag = true;
                }
            }
            br.close();
            if( process.waitFor() != 0 || !flag )
            {
                throw new AdapterException(
                        AdapterExceptionType.EXCPT_INTERNAL_ERROR,
                        "Set ne address failed!!!" );
            }
            return flag;
        }
        catch( Exception e )
        {
            log.error( "setNeAddress", e );
            throw new AdapterException(
                    AdapterExceptionType.EXCPT_INTERNAL_ERROR, e.getMessage() );
        }
    }
}
