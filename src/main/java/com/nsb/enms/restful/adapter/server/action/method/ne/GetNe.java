package com.nsb.enms.restful.adapter.server.action.method.ne;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.restful.adapter.server.action.entity.NeEntity;
import com.nsb.enms.restful.adapter.server.action.method.ExecExternalScript;
import com.nsb.enms.restful.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.restful.adapter.server.common.conf.ConfigKey;
import com.nsb.enms.restful.adapter.server.common.exception.AdapterException;
import com.nsb.enms.restful.adapter.server.common.exception.AdapterExceptionType;
import com.nsb.enms.restful.adapter.server.util.CommonConstants;
import com.nsb.enms.restful.adapter.server.util.ParseUtil;

public class GetNe
{
    private static final Logger log = LogManager.getLogger( GetNe.class );

    private static final String SCENARIO = ConfLoader.getInstance()
            .getConf( ConfigKey.NE_GET_REQ, ConfigKey.DEFAULT_NE_GET_REQ );

    public NeEntity getNe( int groupId, int neId ) throws AdapterException
    {
        try
        {
            Process process = new ExecExternalScript().run(
                CommonConstants.TSTMGR_SCRIPT_TYPE, SCENARIO, groupId + "",
                neId + "" );
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
                            String moc = ParseUtil
                                    .parseAttrWithSingleValue( line );
                            neEntity.setMoc( moc );
                            continue;
                        }

                        if( line.startsWith( "managedObjectInstance" ) )
                        {
                            String moi = ParseUtil
                                    .parseAttrWithMultiValue( line );
                            neEntity.setMoi( moi );
                            continue;
                        }

                        if( line.startsWith( "userLabel" ) )
                        {
                            neEntity.setUserLabel(
                                ParseUtil.parseAttr( line ) );
                        }

                        if( line.startsWith( "neType" ) )
                        {
                            neEntity.setNeType( ParseUtil.parseAttr1( line ) );
                            continue;
                        }

                        if( line.startsWith( "neRelease" ) )
                        {
                            neEntity.setNeRelease(
                                ParseUtil.parseRelease( line ) );
                            continue;
                        }

                        if( line.startsWith( "locationName" ) )
                        {
                            neEntity.setLocationName(
                                ParseUtil.parseAttr( line ) );
                            continue;
                        }

                        if( line.startsWith( "ntpEnabled" ) )
                        {
                            neEntity.setNtpEnabled(
                                ParseUtil.parseBooleanAttr( line ) );
                        }

                        if( line.startsWith( "networkAddress" ) )
                        {
                            neEntity.setNetworkAddress(
                                ParseUtil.parseAttrWithSingleValue( line ) );
                        }

                        if( line.startsWith( "administrativeState" ) )
                        {
                            neEntity.setAdministrativeState(
                                ParseUtil.parseAttr( line ) );
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
            return neEntity;
        }
        catch( Exception e )
        {
            log.error( e.getMessage(), e );
            throw new AdapterException(
                    AdapterExceptionType.EXCPT_INTERNAL_ERROR, e.getMessage() );
        }
    }
}
