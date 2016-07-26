package com.nsb.enms.action.method.ne;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.action.common.CommonConstants;
import com.nsb.enms.action.common.conf.ConfLoader;
import com.nsb.enms.action.common.conf.ConfigKey;
import com.nsb.enms.action.method.ExecExternalScript;
import com.nsb.enms.action.util.IdGenUtil;

public class CreateNe
{
    private final static Logger log = LogManager.getLogger( CreateNe.class );

    private static String createNeScenario = ConfLoader.getInstance()
            .getConf( ConfigKey.NE_CREATE_REQ, CommonConstants.NE_CREATE_REQ );

    private static String setNeAddressScenario = ConfLoader.getInstance()
            .getConf( ConfigKey.SET_NE_ADDR_REQ,
                CommonConstants.SET_NE_ADDR_REQ );

    public String createNe( String neRelease, String neType, String userLabel,
            String locationName, String neAddress )
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
            String line = null;
            boolean flag = false;
            while( (line = br.readLine()) != null )
            {
                if( line.indexOf( "CreateReply received" ) >= 0 )
                {
                    flag = true;
                }
            }
            br.close();

            if( process.waitFor() != 0 || !flag )
            {
                log.error( "Create ne failed!!!" );
                return "Create ne failed";
            }

            process = new ExecExternalScript().run( setNeAddressScenario,
                groupId, neId, neAddress );
            inputStream = process.getInputStream();
            br = new BufferedReader( new InputStreamReader( inputStream ) );
            line = null;
            flag = false;
            while( (line = br.readLine()) != null )
            {
                if( line.indexOf( "SetReply received" ) >= 0 )
                {
                    flag = true;
                }
            }
            br.close();
            if( process.waitFor() != 0 || !flag )
            {
                log.error( "Set ne address failed!!!" );
                return "Set ne address failed";
            }

        }
        catch( InterruptedException e )
        {
            log.error( e.getMessage() );
        }
        catch( IOException e )
        {
            log.error( e.getMessage() );
        }

        return new GetNe().getNe( groupId, neId );
    }

    public static void main( String[] args )
    {
        String neRelease = "2.7B";
        String neType = "ne1662smc";
        String userLabel = "1662_CD_TEST";
        String locationName = "CD";
        String neAddress = "47000580000000000000010001002060620A021D";
        System.out.println( new CreateNe().createNe( neRelease, neType,
            userLabel, locationName, neAddress ) );
    }
}
