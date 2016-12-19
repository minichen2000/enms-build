package com.nsb.enms.adapter.server.wdm.business.equ;

import java.util.ArrayList;
import java.util.List;

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

    public void syncEquip( int neId, String ip, int port, String community )
            throws AdapterException
    {
        SnmpClient client = new SnmpClient( ip, port, community );
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
        adpEqu.setId( equ.getId() );
        adpEqu.setNeId( neId );
        adpEqu.setPosition( equ.getIndex() );
        adpEqu.setType( getType( equ.getIndex() ) );
        adpEqu.setExpectedType( equ.getProgrammedType() );
        adpEqu.setActualType( equ.getPresentType() );
        adpEqu.setKeyOnNe( "" );

        List<AdpKVPair> params = new ArrayList<AdpKVPair>();
        for( SnmpEquEntity equipment : equs )
        {
            String parentIndex = equipment.getIndex();
            if( equ.getIndex().matches( parentIndex + "/[0-9]+" ) )
            {
                AdpKVPair parentIdPair = new AdpKVPair();
                parentIdPair.setKey( "parentId" );
                parentIdPair.setValue( String.valueOf( equipment.getId() ) );
                params.add( parentIdPair );
            }
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
}