package com.nsb.enms.restful.adapter.server.action.method.ne;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.restful.adapter.server.action.common.conf.CommonConstants;
import com.nsb.enms.restful.adapter.server.action.common.conf.ConfLoader;
import com.nsb.enms.restful.adapter.server.action.common.conf.ConfigKey;
import com.nsb.enms.restful.adapter.server.action.common.exception.NbiException;
import com.nsb.enms.restful.adapter.server.action.common.exception.NbiExceptionType;
import com.nsb.enms.restful.adapter.server.action.method.ExecExternalScript;
import com.nsb.enms.restful.adapter.server.action.util.IdGenUtil;

public class CreateNe
{
    private final static Logger log = LogManager.getLogger( CreateNe.class );

    private static String createNeScenario = ConfLoader.getInstance()
            .getConf( ConfigKey.NE_CREATE_REQ, CommonConstants.NE_CREATE_REQ );

    private static String setNeAddressScenario = ConfLoader.getInstance()
            .getConf( ConfigKey.SET_NE_ADDR_REQ,
                CommonConstants.SET_NE_ADDR_REQ );

    private static String setNeIsaAddressScenario = ConfLoader.getInstance()
            .getConf( ConfigKey.SET_NE_ISA_ADDR_REQ,
                CommonConstants.SET_NE_ISA_ADDR_REQ );

    private static Pattern pattern = Pattern
            .compile( "\\d+\\.\\d+\\.\\d+\\.\\d+:\\d+" );

    public String createNe( String neRelease, String neType, String userLabel,
            String locationName, String neAddress ) throws NbiException
    {
        String groupId = "100";
        String neId = IdGenUtil.getId();
        try
        {
            Process process = new ExecExternalScript().run( createNeScenario,
                groupId, neId, neRelease, neType, userLabel, locationName );

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
                throw new NbiException( NbiExceptionType.EXCPT_INTERNAL_ERROR,
                        "Create ne failed!!!" );
            }
            String scenario = setNeAddressScenario;
            if( pattern.matcher( neAddress ).find() )
            {
                scenario = setNeIsaAddressScenario;
            }
            process = new ExecExternalScript().run( scenario, groupId, neId,
                neAddress );
            inputStream = process.getInputStream();
            br = new BufferedReader( new InputStreamReader( inputStream ) );
            flag = false;
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
                throw new NbiException( NbiExceptionType.EXCPT_INTERNAL_ERROR,
                        "Set ne address failed!!!" );
            }

        }
        catch( InterruptedException e )
        {
            log.error( e.getMessage(), e );
        }
        catch( IOException e )
        {
            log.error( e.getMessage(), e );
        }

        return new GetNe().getNe( groupId, neId );
    }
}
