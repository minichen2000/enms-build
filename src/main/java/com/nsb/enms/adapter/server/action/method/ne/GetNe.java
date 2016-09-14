package com.nsb.enms.adapter.server.action.method.ne;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.action.entity.NeEntity;
import com.nsb.enms.adapter.server.action.method.ExecExternalScript;
import com.nsb.enms.adapter.server.common.ExternalScriptType;
import com.nsb.enms.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.adapter.server.common.conf.ConfigKey;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.exception.AdapterExceptionType;
import com.nsb.enms.adapter.server.common.util.ParseUtils;

public class GetNe
{
    private static final Logger log = LogManager.getLogger( GetNe.class );

    private static final String SCENARIO = ConfLoader.getInstance()
            .getConf( ConfigKey.GET_NE_REQ, ConfigKey.DEFAULT_GET_NE_REQ );

    public static NeEntity getNe( int groupId, int neId )
            throws AdapterException
    {
        log.debug( "------------Start getNe-------------------" );
        try
        {
            Process process = new ExecExternalScript().run(
                ExternalScriptType.TSTMGR, SCENARIO, String.valueOf( groupId ),
                String.valueOf( neId ) );
            InputStream inputStream = process.getInputStream();
            NeEntity neEntity = new NeEntity();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader( inputStream ) );

            String line;
            while( (line = br.readLine()) != null )
            {
                if( line.contains( "GetReply received" ) )
                {
                    while( (line = br.readLine()) != null )
                    {
                        line = line.trim();
                        if( line.startsWith( "managedObjectClass" ) )
                        {
                            String moc = ParseUtils
                                    .parseAttrWithSingleValue( line );
                            neEntity.setMoc( moc );
                            continue;
                        }

                        if( line.startsWith( "managedObjectInstance" ) )
                        {
                            String moi = ParseUtils
                                    .parseAttrWithMultiValue( line );
                            neEntity.setMoi( moi );
                            continue;
                        }

                        if( line.startsWith( "userLabel" ) )
                        {
                            neEntity.setUserLabel(
                                ParseUtils.parseAttr( line ) );
                            continue;
                        }

                        if( line.startsWith( "neType" ) )
                        {
                            neEntity.setNeType( ParseUtils.parseAttr1( line ) );
                            continue;
                        }

                        if( line.startsWith( "neRelease" ) )
                        {
                            neEntity.setNeRelease(
                                ParseUtils.parseAttr( line ) );
                            continue;
                        }

                        if( line.startsWith( "locationName" ) )
                        {
                            neEntity.setLocationName(
                                ParseUtils.parseAttr( line ) );
                            continue;
                        }

                        if( line.startsWith( "ntpEnabled" ) )
                        {
                            neEntity.setNtpEnabled(
                                ParseUtils.parseBooleanAttr( line ) );
                            continue;
                        }

                        if( line.startsWith( "networkAddress" ) )
                        {
                            neEntity.setNetworkAddress(
                                ParseUtils.parseAttrWithSingleValue( line ) );
                            continue;
                        }

                        if( line.startsWith( "administrativeState" ) )
                        {
                            neEntity.setAdministrativeState(
                                ParseUtils.parseAttr( line ) );
                            continue;
                        }

                        if( line.startsWith( "-----------------" ) )
                        {
                            break;
                        }
                    }
                }
            }
            br.close();

            if( process.waitFor() != 0 )
            {
                throw new AdapterException(
                        AdapterExceptionType.EXCPT_INTERNAL_ERROR,
                        "Get ne failed!!!" );
            }
            log.debug( "------------End getNe-------------------" );
            return neEntity;
        }
        catch( Exception e )
        {
            log.error( "getNe", e );
            throw new AdapterException(
                    AdapterExceptionType.EXCPT_INTERNAL_ERROR, e.getMessage() );
        }
    }
}
