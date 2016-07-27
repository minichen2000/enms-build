package com.nsb.enms.action.method.xc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.action.common.conf.CommonConstants;
import com.nsb.enms.action.common.conf.ConfLoader;
import com.nsb.enms.action.common.conf.ConfigKey;
import com.nsb.enms.action.entity.XcEntity;
import com.nsb.enms.action.method.ExecExternalScript;
import com.nsb.enms.action.util.JsonUtils;
import com.nsb.enms.action.util.ParseUtil;

public class GetXc
{
    private static final Logger log = LogManager.getLogger( GetXc.class );

    private static final String SCENARIO = ConfLoader.getInstance()
            .getConf( ConfigKey.XC_GET_REQ, CommonConstants.XC_GET_REQ );

    public String getXc( int groupId, int neId )
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
                log.error( "get xc failed" );
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

    public static void main( String[] args )
    {
        String xcList = new GetXc().getXc( 100, 1 );
        System.out.println( xcList );
    }
}