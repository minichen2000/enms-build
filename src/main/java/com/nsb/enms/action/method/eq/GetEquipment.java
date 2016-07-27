package com.nsb.enms.action.method.eq;

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
import com.nsb.enms.action.common.exception.NbiException;
import com.nsb.enms.action.common.exception.NbiExceptionType;
import com.nsb.enms.action.entity.EquipmentEntity;
import com.nsb.enms.action.method.ExecExternalScript;
import com.nsb.enms.action.util.JsonUtils;
import com.nsb.enms.action.util.ParseUtil;

public class GetEquipment
{
    private static final Logger log = LogManager
            .getLogger( GetEquipment.class );

    private static final String SCENARIO = ConfLoader.getInstance()
            .getConf( ConfigKey.EQ_GET_REQ, CommonConstants.EQ_GET_REQ );

    public String getEquipment( int groupId, int neId ) throws NbiException
    {
        try
        {
            Process process = new ExecExternalScript().run( SCENARIO,
                groupId + "", neId + "" );

            InputStream inputStream = process.getInputStream();
            List<EquipmentEntity> eqList = new LinkedList<EquipmentEntity>();

            BufferedReader br = new BufferedReader(
                    new InputStreamReader( inputStream ) );

            String line = null;

            while( (line = br.readLine()) != null )
            {
                if( line.indexOf( "GetReply received" ) >= 0 )
                {
                    EquipmentEntity equipmentEntity = new EquipmentEntity();
                    while( (line = br.readLine()) != null )
                    {
                        line = line.trim();
                        if( line.indexOf( "managedObjectClass" ) >= 0 )
                        {
                            String moc = ParseUtil
                                    .parseAttrWithSingleValue( line );
                            equipmentEntity.setMoc( moc );
                            continue;
                        }

                        if( line.startsWith( "managedObjectInstance" ) )
                        {
                            String moi = ParseUtil
                                    .parseAttrWithMultiValue( line );
                            equipmentEntity.setMoi( moi );
                            continue;
                        }

                        if( line.startsWith( "equipmentActual" ) )
                        {
                            equipmentEntity.setEquipmentActual(
                                ParseUtil.parseAttr1( line ) );
                            continue;
                        }

                        if( line.startsWith( "equipmentExpected" ) )
                        {
                            equipmentEntity.setEquipmentExpected(
                                ParseUtil.parseAttr1( line ) );
                            continue;
                        }

                        if( line.startsWith( "version" ) )
                        {
                            equipmentEntity
                                    .setVersion( ParseUtil.parseAttr( line ) );
                            continue;
                        }

                        if( line.startsWith( "availabilityStatus" ) )
                        {
                            equipmentEntity.setAvailabilityStatus(
                                ParseUtil.parseAttr( line ) );
                            continue;
                        }

                        if( line.startsWith( "alarmStatus" ) )
                        {
                            equipmentEntity.setAlarmStatus(
                                ParseUtil.parseAttr( line ) );
                            continue;
                        }

                        if( line.startsWith( "locationName" ) )
                        {
                            equipmentEntity.setLocationName(
                                ParseUtil.parseAttr( line ) );
                            continue;
                        }

                        if( line.startsWith( "operationalState" ) )
                        {
                            equipmentEntity.setOperationalState(
                                ParseUtil.parseAttr( line ) );
                            continue;
                        }

                        if( line.startsWith( "administrativeState" ) )
                        {
                            equipmentEntity.setAdministrativeState(
                                ParseUtil.parseAttr( line ) );
                            continue;
                        }

                        if( line.startsWith( "-----------------" ) )
                        {
                            eqList.add( equipmentEntity );
                            break;
                        }
                    }
                }
            }
            br.close();

            if( process.waitFor() != 0 || eqList.size() < 1 )
            {
                throw new NbiException( NbiExceptionType.EXCPT_INTERNAL_ERROR,
                        "failed to get equipment!!!" );
            }
            return JsonUtils.toJson( eqList );
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
