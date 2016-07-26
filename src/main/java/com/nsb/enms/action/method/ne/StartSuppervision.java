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

public class StartSuppervision
{
    // private static String setNeIsaAddressScenario = ConfLoader.getInstance()
    // .getConf( ConfigKey.SET_NE_ISA_ADDR_REQ,
    // CommonConstants.SET_NE_ISA_ADDR_REQ );

    private static final Logger log = LogManager
            .getLogger( StartSuppervision.class );

    private static String startSupervisionScenario = ConfLoader.getInstance()
            .getConf( ConfigKey.START_SUPERVISION_REQ,
                CommonConstants.START_SUPERVISION_REQ );

    private boolean flag = false;

    public boolean startSuppervision( String groupId, String neId )
    {
        int count = 0;
        while( count < 3 )
        {
            try
            {
                Process process = new ExecExternalScript()
                        .run( startSupervisionScenario, groupId, neId );
                InputStream inputStream = process.getInputStream();
                BufferedReader br = new BufferedReader(
                        new InputStreamReader( inputStream ) );
                String line = null;
                while( (line = br.readLine()) != null )
                {
                    if( line.indexOf( "ActionReply received" ) >= 0 )
                    {
                        flag = true;
                    }
                }
                br.close();
                if( process.waitFor() != 0 )
                {
                    return false;
                }

                if( flag )
                {
                    return true;
                }
                count++;

                Thread.sleep( 10000 );

            }
            catch( IOException e )
            {
                log.error( e.getMessage(), e );
            }
            catch( InterruptedException e )
            {
                log.error( e.getMessage(), e );
            }
        }
        return false;
    }
}
