package com.nsb.enms.adapter.server.business.eq;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.action.entity.EquipmentEntity;
import com.nsb.enms.adapter.server.action.entity.TptCoordinatorEntity;
import com.nsb.enms.adapter.server.action.method.eq.GetEquipment;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.utils.GenerateKeyOnNeUtil;
import com.nsb.enms.adapter.server.db.mgr.AdpEqusDbMgr;
import com.nsb.enms.adapter.server.db.mgr.AdpSeqDbMgr;
import com.nsb.enms.common.EntityType;
import com.nsb.enms.common.EquType;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.restful.model.adapter.AdpEquipment;
import com.nsb.enms.restful.model.adapter.AdpKVPair;

public class AdpEqusMgr
{
    private final static Logger log = LogManager.getLogger( AdpEqusMgr.class );

    private AdpEqusDbMgr equsDbMgr = new AdpEqusDbMgr();

    public AdpEqusMgr()
    {
    }

    public void syncEquip( String groupId, String neId, int id )
            throws AdapterException
    {
        Map<String, List<AdpKVPair>> map = GetEquipment.getRIs( groupId, neId );

        if( null != map && !map.isEmpty() )
        {
            for( String key : map.keySet() )
            {
                log.debug( "key is " + key );
                for( AdpKVPair pair : map.get( key ) )
                {
                    log.debug( pair.getKey() + ":" + pair.getValue() );
                }
            }
        }

        List<TptCoordinatorEntity> tptCoordinatorList = GetEquipment
                .getISAs( groupId, neId );
        if( null != tptCoordinatorList && !tptCoordinatorList.isEmpty() )
        {
            for( TptCoordinatorEntity entity : tptCoordinatorList )
            {
                log.debug( entity );
            }
        }

        List<EquipmentEntity> equList = GetEquipment.getEquipments( groupId,
            neId );
        log.debug( "equList=" + equList.size() + ", neId=" + neId );

        for( EquipmentEntity equ : equList )
        {
            try
            {
                equ.setId( AdpSeqDbMgr.getMaxEquipmentId() );
            }
            catch( Exception e )
            {
                throw new AdapterException( ErrorCode.FAIL_DB_OPERATION );
            }
        }

        for( EquipmentEntity equ : equList )
        {
            log.debug( equ );
            AdpEquipment newEqu = constructEquip( equ, equList,
                tptCoordinatorList, map, id );
            try
            {
                equsDbMgr.addEquipment( newEqu );
            }
            catch( Exception e )
            {
                log.error( "addEquipment:", e );
                throw new AdapterException( ErrorCode.FAIL_DB_OPERATION );
            }

            /*
             * AdpEquipment adpEqu; try { adpEqu = equsDbMgr.getEquipmentById(
             * Integer.valueOf( neId ), newEqu.getId() ); } catch( Exception e )
             * { log.error( "getEquipmentById:", e ); throw new
             * AdapterException( ErrorCode.FAIL_DB_OPERATION ); } if( null ==
             * adpEqu || adpEqu.getId() == null ) { try {
             * equsDbMgr.addEquipment( newEqu ); } catch( Exception e ) {
             * log.error( "addEquipment:", e ); throw new AdapterException(
             * ErrorCode.FAIL_DB_OPERATION ); } }
             */
        }
        log.debug( "sync equipment end" );
    }

    private AdpEquipment constructEquip( EquipmentEntity equ,
            List<EquipmentEntity> equList,
            List<TptCoordinatorEntity> tptCoordinatorList,
            Map<String, List<AdpKVPair>> map, int id )
    {
        AdpEquipment adpEqu = new AdpEquipment();
        List<AdpKVPair> params = new ArrayList<AdpKVPair>();
        String moc = equ.getMoc();
        String moi = equ.getMoi();
        String expectedType = equ.getEquipmentExpected();
        String actualType = equ.getEquipmentActual();
        String keyOnNe = GenerateKeyOnNeUtil.generateKeyOnNe( EntityType.BOARD,
            moc, moi );
        adpEqu.setId( equ.getId() );
        adpEqu.setNeId( id );
        String position = getPosition( moi );
        adpEqu.setPosition( position );
        String type = getType( moi );
        adpEqu.setType( type );
        adpEqu.setExpectedType( expectedType );
        adpEqu.setActualType( actualType );
        adpEqu.setKeyOnNe( keyOnNe );
        // adpEqu.setAlarmState(equ.getAlarmStatus());
        if( !StringUtils.isEmpty( actualType ) )
        {
            String key = getKey( moi );
            List<AdpKVPair> pairList = map.get( key );
            if( pairList != null )
            {
                params.addAll( pairList );
            }

            // AdpKVPair unitPartNumberPair = new AdpKVPair();
            // unitPartNumberPair.setKey( "unitPartNumber" );
            // unitPartNumberPair.setValue( equ.getUnitPartNumber() );
            // params.add( unitPartNumberPair );
            // AdpKVPair softwarePartNumberPair = new AdpKVPair();
            // softwarePartNumberPair.setKey( "softwarePartNumber" );
            // softwarePartNumberPair.setValue( equ.getSoftwarePartNumber() );
            // params.add( softwarePartNumberPair );
            // AdpKVPair serialNumberPair = new AdpKVPair();
            // serialNumberPair.setKey( "serialNumber" );
            // serialNumberPair.setValue( equ.getSerialNumber() );
            // params.add( serialNumberPair );
        }

        for( int i = tptCoordinatorList.size() - 1; i >= 0; i-- )
        {
            TptCoordinatorEntity tptCoordinatorEntity = tptCoordinatorList
                    .get( i );
            if( moi.equals( tptCoordinatorEntity.getEquMoi() ) )
            {
                if( !StringUtils.isEmpty( tptCoordinatorEntity.getIpMask() ) )
                {
                    AdpKVPair ipAddressPair = new AdpKVPair();
                    ipAddressPair.setKey( "ipAddress" );
                    ipAddressPair
                            .setValue( tptCoordinatorEntity.getIpAddress() );
                    params.add( ipAddressPair );

                    AdpKVPair ipMaskPair = new AdpKVPair();
                    ipMaskPair.setKey( "ipMask" );
                    ipMaskPair.setValue( tptCoordinatorEntity.getIpMask() );
                    params.add( ipMaskPair );

                    AdpKVPair maxPositionPair = new AdpKVPair();
                    maxPositionPair.setKey( "maxPosition" );
                    maxPositionPair
                            .setValue( tptCoordinatorEntity.getMaxPosition() );
                    params.add( maxPositionPair );

                    AdpKVPair maxVc4nvPair = new AdpKVPair();
                    maxVc4nvPair.setKey( "maxVc4nv" );
                    maxVc4nvPair.setValue( tptCoordinatorEntity.getMaxVc4nv() );
                    params.add( maxVc4nvPair );

                    AdpKVPair maxVc3nvPair = new AdpKVPair();
                    maxVc3nvPair.setKey( "maxVc3nv" );
                    maxVc3nvPair.setValue( tptCoordinatorEntity.getMaxVc3nv() );
                    params.add( maxVc3nvPair );

                    AdpKVPair maxVc12nvPair = new AdpKVPair();
                    maxVc12nvPair.setKey( "maxVc12nv" );
                    maxVc12nvPair
                            .setValue( tptCoordinatorEntity.getMaxVc12nv() );
                    params.add( maxVc12nvPair );

                }
                tptCoordinatorList.remove( i );
                break;
            }
        }

        for( EquipmentEntity equipment : equList )
        {
            String parentMoi = equipment.getMoi();
            if( moi.matches( parentMoi + "/equipmentId=[0-9]+" ) )
            {
                AdpKVPair parentIdPair = new AdpKVPair();
                parentIdPair.setKey( "parentId" );
                parentIdPair.setValue( String.valueOf( equipment.getId() ) );
                params.add( parentIdPair );
                break;
            }
        }
        adpEqu.setParams( params );
        return adpEqu;
    }

    private String getType( String moi )
    {
        String[] elements = moi.split( "/" );
        switch( elements.length )
        {
            case 3:
                return EquType.rack.name();
            case 4:
                return EquType.shelf.name();
            case 5:
                return EquType.slot.name();
            case 6:
                return EquType.subslot.name();
            default:
                return null;
        }
    }

    private String getPosition( String moi )
    {
        StringBuilder position = new StringBuilder( "" );
        String[] elements = moi.split( "/" );
        for( int i = 2; i < elements.length; i++ )
        {
            position.append( elements[i].split( "=" )[1] ).append( "/" );
        }
        return position.substring( 0, position.length() - 1 );
    }

    private String getKey( String moi )
    {
        String[] elements = moi.split( "/" );

        for( int i = 0; i < elements.length; i++ )
        {
            elements[i] = elements[i].split( "=" )[1];
        }

        if( elements.length == 5 )
        {
            String rack = addPrefix( elements[2] );
            String shelf = elements[3];
            String slot = addPrefix( elements[4] );
            return "r" + rack + "sr" + shelf + "/board#" + slot;
        }

        if( elements.length == 6 )
        {
            String rack = addPrefix( elements[2] );
            String shelf = elements[3];
            String slot = addPrefix( elements[4] );
            String subslot = addPrefix( elements[5] );
            return "r" + rack + "sr" + shelf + "sl" + slot + "/daughter#"
                    + subslot;
        }
        return "";
    }

    private String addPrefix( String value )
    {
        if( value.length() == 1 )
        {
            return "0" + value;
        }
        return value;
    }
}