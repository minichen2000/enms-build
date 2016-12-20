package com.nsb.enms.adapter.server.wdm.business.equ;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.db.mgr.AdpEqusDbMgr;
import com.nsb.enms.adapter.server.common.db.mgr.AdpSeqDbMgr;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.wdm.action.entity.SnmpEquEntity;
import com.nsb.enms.adapter.server.wdm.action.method.eq.GetAllEquipments;
import com.nsb.enms.common.EquType;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.common.utils.snmpclient.SnmpClient;
import com.nsb.enms.restful.model.adapter.AdpEquipment;
import com.nsb.enms.restful.model.adapter.AdpKVPair;

public class AdpSnmpEqusMgr
{
    private static final Logger log = LogManager
            .getLogger( AdpSnmpEqusMgr.class );

    private static AdpEqusDbMgr equsDbMgr = new AdpEqusDbMgr();

    public AdpSnmpEqusMgr()
    {

    }

    public void syncEquip( SnmpClient client, Integer neId )
            throws AdapterException
    {
        List<SnmpEquEntity> equs = GetAllEquipments.getEquipments( client );
        for( SnmpEquEntity equ : equs )
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

        for( SnmpEquEntity equ : equs )
        {
            log.debug( equ );
            AdpEquipment newEqu = constructEquip( equ, equs, neId );
            try
            {
                equsDbMgr.addEquipment( newEqu );
            }
            catch( Exception e )
            {
                log.error( "addEquipment:", e );
                throw new AdapterException( ErrorCode.FAIL_DB_OPERATION );
            }
        }
    }

    private AdpEquipment constructEquip( SnmpEquEntity equ,
            List<SnmpEquEntity> equs, int neId )
    {
        AdpEquipment adpEqu = new AdpEquipment();
        List<AdpKVPair> params = new ArrayList<AdpKVPair>();

        adpEqu.setId( equ.getId() );
        adpEqu.setNeId( neId );
        adpEqu.setPosition( equ.getIndex() );
        adpEqu.setType( getType( equ.getIndex() ) );
        adpEqu.setExpectedType( equ.getProgrammedType() );
        adpEqu.setActualType( equ.getPresentType() );
        adpEqu.setKeyOnNe( equ.getIndex() );

        for( SnmpEquEntity equipment : equs )
        {
            String parentIndex = equipment.getIndex();
            String index = equ.getIndex();
            if( index.matches( parentIndex + "/[0-9]+" ) )
            {
                AdpKVPair parentIdPair = new AdpKVPair();
                parentIdPair.setKey( "parentId" );
                parentIdPair.setValue( String.valueOf( equipment.getId() ) );
                params.add( parentIdPair );
            }
        }

        if( !StringUtils.isEmpty( equ.getPresentType() )
                && !"Empty".equals( equ.getPresentType() ) )
        {
            String serialNumber = equ.getSerialNumber();
            String unitPartNumber = equ.getUnitPartNumber();
            String softwarePartNumber = equ.getSoftwarePartNumber();
            AdpKVPair serialNumberPair = new AdpKVPair();
            serialNumberPair.setKey( "serialNumber" );
            serialNumberPair.setValue( serialNumber );
            params.add( serialNumberPair );

            AdpKVPair unitPartNumberPair = new AdpKVPair();
            unitPartNumberPair.setKey( "unitPartNumber" );
            unitPartNumberPair.setValue( unitPartNumber );
            params.add( unitPartNumberPair );

            AdpKVPair softwarePartNumberPair = new AdpKVPair();
            softwarePartNumberPair.setKey( "softwarePartNumber" );
            softwarePartNumberPair.setValue( softwarePartNumber );
            params.add( softwarePartNumberPair );
        }

        adpEqu.setParams( params );

        return adpEqu;
    }

    private String getType( String index )
    {
        int len = index.split( "/" ).length;
        switch( len )
        {
            case 1:
                return EquType.rack.name();
            case 2:
                return EquType.shelf.name();
            case 3:
                return EquType.slot.name();
            case 4:
                return EquType.subslot.name();
            default:
                return null;
        }
    }

    public static void main( String[] args ) throws AdapterException
    {
        AdpSnmpEqusMgr snmpEqusMgr = new AdpSnmpEqusMgr();
        SnmpClient client = new SnmpClient( "135.251.96.5", 161, "admin_snmp" );
        snmpEqusMgr.syncEquip( client, 10 );
    }
}