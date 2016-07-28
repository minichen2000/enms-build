package com.nsb.enms.restful.adapter.server.action.method.xc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.restful.adapter.server.action.entity.XcEntity;
import com.nsb.enms.restful.adapter.server.action.method.ExecExternalScript;
import com.nsb.enms.restful.adapter.server.common.conf.CommonConstants;
import com.nsb.enms.restful.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.restful.adapter.server.common.conf.ConfigKey;
import com.nsb.enms.restful.adapter.server.common.exception.AdapterException;
import com.nsb.enms.restful.adapter.server.common.exception.AdapterExceptionType;
import com.nsb.enms.restful.adapter.server.util.JsonUtils;
import com.nsb.enms.restful.adapter.server.util.ParseUtil;

public class GetXc
{
    private static final Logger log = LogManager.getLogger( GetXc.class );

    private static final String SCENARIO = ConfLoader.getInstance()
            .getConf( ConfigKey.XC_GET_REQ, CommonConstants.XC_GET_REQ );

    public String getXc( int groupId, int neId ) throws AdapterException
    {
        try
        {
            Process process = new ExecExternalScript().run( SCENARIO,
                groupId + "", neId + "" );
            InputStream inputStream = process.getInputStream();
            List<XcEntity> xcList = new LinkedList<XcEntity>();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader( inputStream ) );

            String line = null;
            while( (line = br.readLine()) != null )
            {
                if( line.indexOf( "GetReply received" ) >= 0 )
                {
                    XcEntity xcEntity = new XcEntity();
                    while( (line = br.readLine()) != null )
                    {
                        line = line.trim();
                        if( line.startsWith( "managedObjectClass" ) )
                        {
                            String moc = ParseUtil
                                    .parseAttrWithSingleValue( line );
                            xcEntity.setMoc( moc );
                            continue;
                        }

                        if( line.startsWith( "managedObjectInstance" ) )
                        {
                            String moi = ParseUtil
                                    .parseAttrWithMultiValue( line );
                            xcEntity.setMoi( moi );
                            continue;
                        }

                        if( line.startsWith( "directionality" ) )
                        {
                            xcEntity.setDirectionality(
                                ParseUtil.parseAttr( line ) );
                            continue;
                        }

                        if( line.startsWith( "toTermination" ) )
                        {
                            xcEntity.setToTermination(
                                ParseUtil.parseAttrWithMultiValue( line ) );
                            continue;
                        }

                        if( line.startsWith( "fromTermination" ) )
                        {
                            xcEntity.setFromTermination(
                                ParseUtil.parseAttrWithMultiValue( line ) );
                            continue;
                        }

                        if( line.startsWith( "signalType" ) )
                        {
                            xcEntity.setSignalType(
                                ParseUtil.parseAttr( line ) );
                            continue;
                        }

                        if( line.startsWith( "operationalState" ) )
                        {
                            xcEntity.setOperationalState(
                                ParseUtil.parseAttr( line ) );
                            continue;
                        }

                        if( line.startsWith( "administrativeState" ) )
                        {
                            xcEntity.setAdministrativeState(
                                ParseUtil.parseAttr( line ) );
                            continue;
                        }

                        if( line.startsWith( "-----------------" ) )
                        {
                            xcList.add( xcEntity );
                            break;
                        }
                    }
                }
            }
            br.close();

            if( process.waitFor() != 0 || xcList.size() < 1 )
            {
                throw new AdapterException( AdapterExceptionType.EXCPT_INTERNAL_ERROR,
                        "failed to get xc!!!" );
            }
            return JsonUtils.toJson( xcList );

        }
        catch( IOException e )
        {
            log.error( e.getMessage(), e );
        }
        catch( InterruptedException e )
        {
            log.error( e.getMessage(), e );
        }

        return null;
    }
}