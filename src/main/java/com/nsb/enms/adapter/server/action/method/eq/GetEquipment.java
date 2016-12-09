package com.nsb.enms.adapter.server.action.method.eq;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
import com.nsb.enms.restful.model.adapter.AdpKVPair;

public class GetEquipment
{
    private static final Logger log = LogManager
            .getLogger( GetEquipment.class );

    private static final String RI_FILE_PATH = ConfLoader.getInstance()
            .getConf( ConfigKey.RI_FILE_PATH, ConfigKey.DEFAULT_RI_FILE_PATH );

    private static final String GET_RI_SCENARIO = ConfLoader.getInstance()
            .getConf( ConfigKey.GET_RI_REQ, ConfigKey.DEFAULT_GET_RI_REQ );

    private static final String GET_ISA_SCENARIO = ConfLoader.getInstance()
            .getConf( ConfigKey.GET_ISA_REQ, ConfigKey.DEFAULT_GET_ISA_REQ );

    private static final String GET_EQ_SCENARIO = ConfLoader.getInstance()
            .getConf( ConfigKey.GET_EQ_REQ, ConfigKey.DEFAULT_GET_EQ_REQ );

    public static Map<String, List<AdpKVPair>> getRIs( String groupId,
            String neId ) throws AdapterException
    {
        log.debug( "--------------Start getRIs--------------" );
        Map<String, List<AdpKVPair>> map = new HashMap<String, List<AdpKVPair>>();
        try
        {
            Process process = ExecExternalScript.run( ExternalScriptType.TSTMGR,
                GET_RI_SCENARIO, groupId, neId );

            InputStream inputStream = process.getInputStream();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader( inputStream ) );
            String line = null;
            boolean flag = false;
            while( (line = br.readLine()) != null )
            {
                if( line.contains( "ActionReply received" ) )
                {
                    flag = true;
                }
            }
            br.close();
            process.waitFor();
            if( !flag )
            {
                throw new AdapterException(
                        ErrorCode.FAIL_GET_EQUIPMENT_BY_EMLIM );
            }
            File file = new File(
                    RI_FILE_PATH + "/" + groupId + "_" + neId + ".ri" );

            br = new BufferedReader( new FileReader( file ) );
            List<AdpKVPair> pairList = null;
            while( (line = br.readLine()) != null )
            {
                line = line.trim();
                if( line.startsWith( "USER LABEL" ) )
                {
                    pairList = new ArrayList<AdpKVPair>();
                    String key = line.split( ":" )[1].trim();
                    key = key.substring( key.indexOf( "/" ) + 1 );
                    map.put( key, pairList );
                    continue;
                }

                if( line.startsWith( "Unit part number" ) )
                {
                    AdpKVPair pair = new AdpKVPair();
                    pair.setKey( "unitPartNumber" );
                    pair.setValue( line.split( ":" )[1].trim() );
                    pairList.add( pair );
                    continue;
                }

                if( line.startsWith( "Software part number" ) )
                {
                    AdpKVPair pair = new AdpKVPair();
                    pair.setKey( "softwarePartNumber" );
                    pair.setValue( line.split( ":" )[1].trim() );
                    pairList.add( pair );
                    continue;
                }

                if( line.startsWith( "Serial number" ) )
                {
                    AdpKVPair pair = new AdpKVPair();
                    pair.setKey( "serialNumber" );
                    pair.setValue( line.split( ":" )[1].trim() );
                    pairList.add( pair );
                    continue;
                }
            }
            br.close();
            log.debug( "--------------End getRIs--------------" );
            return map;
        }
        catch( Exception e )
        {
            log.error( "getRIs", e );
            throw new AdapterException( ErrorCode.FAIL_GET_EQUIPMENT_BY_EMLIM );
        }
    }

    public static List<TptCoordinatorEntity> getISAs( String groupId,
            String neId ) throws AdapterException
    {
        log.debug( "--------------Start getCardIpAddress--------------" );
        try
        {
            Process process = ExecExternalScript.run( ExternalScriptType.TSTMGR,
                GET_ISA_SCENARIO, groupId, neId );

            InputStream inputStream = process.getInputStream();
            List<TptCoordinatorEntity> tptCorrdinatorList = new LinkedList<TptCoordinatorEntity>();

            BufferedReader br = new BufferedReader(
                    new InputStreamReader( inputStream ) );
            String line = null;
            while( (line = br.readLine()) != null )
            {
                if( line.contains( "GetReply received" ) )
                {
                    TptCoordinatorEntity tptCoordinatorEntity = new TptCoordinatorEntity();
                    while( (line = br.readLine()) != null )
                    {
                        line = line.trim();
                        if( line.startsWith( "managedObjectClass" ) )
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

                        if( line.startsWith( "dataTPList" ) )
                        {
                            Pattern pattern = Pattern.compile(
                                "objectClass\\s*\\{\\s*(vc.*?)\\s*\\},\\s*concatenationLevel\\s*onlyMaxLevelGiven\\s*(\\d+)\\s*\\}" );
                            Matcher matcher = pattern.matcher( line );
                            while( matcher.find() )
                            {
                                if( matcher.group( 1 ).equals(
                                    "vc4XVirtualTTPBidirectional" ) )
                                {
                                    tptCoordinatorEntity
                                            .setMaxVc4nv( matcher.group( 2 ) );
                                    continue;
                                }

                                if( matcher.group( 1 ).equals(
                                    "vc12XVirtualTTPBidirectional" ) )
                                {
                                    tptCoordinatorEntity
                                            .setMaxVc12nv( matcher.group( 2 ) );
                                    continue;
                                }

                                if( matcher.group( 1 ).equals(
                                    "vc3XVirtualTTPBidirectional" ) )
                                {
                                    tptCoordinatorEntity
                                            .setMaxVc3nv( matcher.group( 2 ) );
                                    continue;
                                }

                            }
                            continue;
                        }

                        if( line.startsWith( "portToStmRelationship" ) )
                        {
                            Pattern pattern = Pattern
                                    .compile( ".*maxPosition\\s*(\\d+).*" );
                            Matcher match = pattern.matcher( line );
                            if( match.find() )
                            {
                                tptCoordinatorEntity
                                        .setMaxPosition( match.group( 1 ) );
                            }
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
                            continue;
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
        log.debug( "------------Start getEquipment-------------------" );
        try
        {
            Process process = ExecExternalScript.run( ExternalScriptType.TSTMGR,
                GET_EQ_SCENARIO, groupId, neId );

            InputStream inputStream = process.getInputStream();
            List<EquipmentEntity> eqList = new LinkedList<EquipmentEntity>();

            BufferedReader br = new BufferedReader(
                    new InputStreamReader( inputStream ) );

            String line = null;
            while( (line = br.readLine()) != null )
            {
                if( line.contains( "GetReply received" ) )
                {
                    EquipmentEntity equipmentEntity = new EquipmentEntity();
                    while( (line = br.readLine()) != null )
                    {
                        line = line.trim();
                        if( line.startsWith( "managedObjectClass" ) )
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
                            Pattern pattern2 = Pattern.compile(
                                "boardType\\s*\\{\\s*(type.*?)\\s*\\}[,|\\s*\\},]" );
                            Matcher matcher = pattern2.matcher( line );
                            List<String> allowedEquipmentTypes = new ArrayList<String>();
                            String value = "";
                            while( matcher.find() )
                            {
                                value = matcher.group( 1 );
                                String[] elements = value.split( ",\\s*" );
                                for( String element : elements )
                                {
                                    allowedEquipmentTypes.add(
                                        element.replace( "type", "" ).trim() );
                                }
                            }
                            equipmentEntity.setAllowedEquipmentTypes(
                                allowedEquipmentTypes );
                            continue;
                        }

                        if( line.startsWith( "specificPhysicalInstance" ) )
                        {
                            String instance = ParseUtil.parseAttr1( line );
                            Pattern pattern = Pattern
                                    .compile( "(\\w+\\-?\\s*\\w+\\s*\\w*)" );
                            List<String> params = new ArrayList<String>();
                            Matcher match = pattern.matcher( instance );
                            while( match.find() )
                            {
                                params.add( match.group( 1 ) );
                            }

                            if( params.size() == 2 )
                            {
                                equipmentEntity.setSoftwarePartNumber(
                                    params.get( 1 ) );
                                continue;
                            }

                            if( params.size() == 3 )
                            {
                                equipmentEntity
                                        .setUnitPartNumber( params.get( 1 ) );
                                String[] elements = params.get( 2 )
                                        .split( "\\s+" );
                                if( elements.length == 1 )
                                {
                                    equipmentEntity
                                            .setSerialNumber( elements[0] );
                                }
                                else if( elements.length == 3 )
                                {
                                    equipmentEntity
                                            .setSerialNumber( elements[1] );
                                }
                                continue;
                            }
                        }

                        if( line.startsWith( "equipmentActual" ) )
                        {
                            String equipmentActual = ParseUtil
                                    .parseAttr1( line );
                            if( !StringUtils.isEmpty( equipmentActual )
                                    && !"NULL".equals( equipmentActual ) )
                            {
                                equipmentEntity
                                        .setEquipmentActual( equipmentActual );
                            }
                            continue;
                        }

                        if( line.startsWith( "equipmentExpected" ) )
                        {
                            String equipmentExpected = ParseUtil
                                    .parseAttr1( line );
                            if( !StringUtils.isEmpty( equipmentExpected )
                                    && !"NULL".equals( equipmentExpected ) )
                            {
                                equipmentEntity.setEquipmentExpected(
                                    equipmentExpected );
                            }
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