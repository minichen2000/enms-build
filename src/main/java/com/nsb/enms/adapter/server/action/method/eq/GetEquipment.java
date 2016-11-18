package com.nsb.enms.adapter.server.action.method.eq;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.action.entity.EquipmentEntity;
import com.nsb.enms.adapter.server.action.entity.TptCoordinatorEntity;
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

    private static final String GET_TPT_COORDINATOR_SCENARIO = ConfLoader
            .getInstance().getConf( ConfigKey.GET_TPT_COORDINATOR_REQ,
                ConfigKey.DEFAULT_GET_TPT_COORDINATOR_REQ );

    private static final String GET_EQ_SCENARIO = ConfLoader.getInstance()
            .getConf( ConfigKey.GET_EQ_REQ, ConfigKey.DEFAULT_GET_EQ_REQ );

    public static List<TptCoordinatorEntity> getTptCoordinators( String groupId,
            String neId ) throws AdapterException
    {
        try
        {
            log.debug( "--------------Start getCardIpAddress--------------" );
            Process process = ExecExternalScript.run( ExternalScriptType.TSTMGR,
                GET_TPT_COORDINATOR_SCENARIO, groupId, neId );

            InputStream inputStream = process.getInputStream();
            List<TptCoordinatorEntity> tptCorrdinatorList = new LinkedList<TptCoordinatorEntity>();

            BufferedReader br = new BufferedReader(
                    new InputStreamReader( inputStream ) );

            String line = null;

            while( (line = br.readLine()) != null )
            {
                if( line.indexOf( "GetReply received" ) >= 0 )
                {
                    TptCoordinatorEntity tptCoordinatorEntity = new TptCoordinatorEntity();
                    while( (line = br.readLine()) != null )
                    {
                        line = line.trim();
                        if( line.indexOf( "managedObjectClass" ) >= 0 )
                        {
                            String moc = ParseUtil
                                    .parseAttrWithSingleValue( line );
                            tptCoordinatorEntity.setMoc( moc );
                            continue;
                        }

                        if( line.startsWith( "managedObjectInstance" ) )
                        {
                            String moi = ParseUtil
                                    .parseAttrWithMultiValue( line );
                            String[] elements = moi.split( "/" );
                            String tptCoordinatorId = elements[2]
                                    .split( "=" )[1];
                            String rack = "/equipmentId="
                                    + tptCoordinatorId.substring( 0, 1 );
                            String shelf = "/equipmentId="
                                    + tptCoordinatorId.substring( 1, 2 );
                            String slotId = tptCoordinatorId.substring( 2, 4 );
                            if( slotId.startsWith( "0" ) )
                            {
                                slotId = slotId.substring( 1, 2 );
                            }
                            String slot = "/equipmentId=" + slotId;
                            String equMoi = elements[0] + "/" + elements[1]
                                    + rack + shelf + slot;
                            tptCoordinatorEntity.setMoi( moi );
                            tptCoordinatorEntity.setEquMoi( equMoi );
                            continue;
                        }

                        if( line.startsWith( "cardIpAddress" ) )
                        {
                            String cardIpAddress = ParseUtil
                                    .parseAttrWithSingleValue( line );
                            Pattern pattern = Pattern.compile(
                                "ipAddress\\s*=(.*),\\s*ipMask\\s*=(.*)" );
                            Matcher matcher = pattern.matcher( cardIpAddress );
                            if( matcher.find() )
                            {
                                tptCoordinatorEntity
                                        .setIpAddress( matcher.group( 1 ) );
                                tptCoordinatorEntity
                                        .setIpMask( matcher.group( 2 ) );
                            }
                        }

                        if( line.startsWith( "-----------------" ) )
                        {
                            tptCorrdinatorList.add( tptCoordinatorEntity );
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

            log.debug( "--------------End getCardIpAddress--------------" );
            return tptCorrdinatorList;
        }
        catch( Exception e )
        {
            log.error( "getCardIpAddress", e );
            throw new AdapterException( ErrorCode.FAIL_GET_EQUIPMENT_BY_EMLIM );
        }
    }

    public static List<EquipmentEntity> getEquipments( String groupId,
            String neId ) throws AdapterException
    {
        try
        {
            log.debug( "------------Start getEquipment-------------------" );
            Process process = ExecExternalScript.run( ExternalScriptType.TSTMGR,
                GET_EQ_SCENARIO, groupId, neId );

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
                                String[] elements = availabilityStatus
                                        .split( ",\\s*" );
                                availabilityStatus = "";
                                for( String element : elements )
                                {
                                    availabilityStatus += element.trim()
                                            .split( "\\s*" )[0] + ":";
                                }
                                equipmentEntity.setAvailabilityStatus(
                                    availabilityStatus.substring( 0,
                                        availabilityStatus.length() - 1 ) );
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