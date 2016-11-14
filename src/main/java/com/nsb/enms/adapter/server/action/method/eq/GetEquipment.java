package com.nsb.enms.adapter.server.action.method.eq;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.action.entity.EquipmentEntity;
import com.nsb.enms.adapter.server.action.method.ExecExternalScript;
import com.nsb.enms.adapter.server.common.ExternalScriptType;
import com.nsb.enms.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.adapter.server.common.conf.ConfigKey;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.utils.ParseUtil;
import com.nsb.enms.common.ErrorCode;

public class GetEquipment
{
    private static final Logger log = LogManager
            .getLogger( GetEquipment.class );

    private static final String SCENARIO = ConfLoader.getInstance()
            .getConf( ConfigKey.GET_EQ_REQ, ConfigKey.DEFAULT_GET_EQ_REQ );

    public static List<EquipmentEntity> getEquipment( String groupId,
            String neId ) throws AdapterException
    {
        try
        {
            log.debug( "------------Start getEquipment-------------------" );
            Process process = ExecExternalScript.run( ExternalScriptType.TSTMGR,
                SCENARIO, groupId, neId );

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

                        if( line.startsWith( "allowedEquipmentType" ) )
                        {
                            equipmentEntity.setAllowedEquipmentTypes(
                                ParseUtil.parseAllowedEquipmentType( line ) );
                            continue;
                        }

                        if( line.startsWith( "specificPhysicalInstance" ) )
                        {
                            equipmentEntity.setSpecificPhysicalInstance(
                                ParseUtil.parseAttr1( line ) );
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
                            String availabilityStatus = ParseUtil
                                    .parseAttrWithSingleValue( line );
                            if( !StringUtils.isEmpty( availabilityStatus ) )
                            {
                                equipmentEntity.setAvailabilityStatus(
                                    availabilityStatus.split( "\\s*" )[0] );
                            }
                            continue;
                        }

                        if( line.startsWith( "alarmStatus" ) )
                        {
                            String alarmStatus = ParseUtil.parseAttr( line );
                            String[] elements = alarmStatus.split( "_" );
                            equipmentEntity.setAlarmStatus( elements[0] );
                            if( elements.length == 2 )
                            {
                                equipmentEntity.setAlarmStatus( elements[1] );
                            }
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

            if( process.waitFor() != 0 )
            {
                throw new AdapterException(
                        ErrorCode.FAIL_GET_EQUIPMENT_BY_EMLIM );
            }
            log.debug( "------------End getEquipment-------------------" );
            return eqList;
        }
        catch( Exception e )
        {
            log.error( "getEquipment", e );
            throw new AdapterException( ErrorCode.FAIL_GET_EQUIPMENT_BY_EMLIM );
        }
    }
}