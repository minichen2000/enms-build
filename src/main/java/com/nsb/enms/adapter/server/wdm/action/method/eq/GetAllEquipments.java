package com.nsb.enms.adapter.server.wdm.action.method.eq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.wdm.action.entity.SnmpEquEntity;
import com.nsb.enms.adapter.server.wdm.action.util.TypeMapUtil;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.common.utils.Pair;
import com.nsb.enms.common.utils.snmpclient.SnmpClient;

public class GetAllEquipments
{
    private static final Logger log = LogManager
            .getLogger( GetAllEquipments.class );

    public static List<SnmpEquEntity> getEquipments( SnmpClient client )
            throws AdapterException
    {
        List<SnmpEquEntity> equipments = new ArrayList<SnmpEquEntity>();
        equipments.addAll( getRacks( client ) );
        equipments.addAll( getShelfs( client ) );
        equipments.addAll( getSlotCards( client ) );
        return equipments;
    }

    private static List<SnmpEquEntity> getRacks( SnmpClient client )
            throws AdapterException
    {
        List<SnmpEquEntity> racks = new ArrayList<SnmpEquEntity>();
        SnmpEquEntity rack = new SnmpEquEntity();
        rack.setIndex( "1" );
        rack.setProgrammedType( "RACK" );
        rack.setPresentType( "RACK" );
        racks.add( rack );
        return racks;
    }

    private static List<SnmpEquEntity> getShelfs( SnmpClient client )
            throws AdapterException
    {
        log.debug( "--------------Start getShelfs--------------" );
        List<String> leafOids = new ArrayList<String>();
        leafOids.add( "1.3.6.1.4.1.7483.2.2.1.1.2.1.1.1.2" );
        leafOids.add( "1.3.6.1.4.1.7483.2.2.1.1.2.1.1.1.5" );
        leafOids.add( "1.3.6.1.4.1.7483.2.2.1.1.2.1.1.1.6" );

        leafOids.add( "1.3.6.1.4.1.7483.2.2.1.1.2.1.3.1.4" );
        leafOids.add( "1.3.6.1.4.1.7483.2.2.1.1.2.1.3.1.5" );
        leafOids.add( "1.3.6.1.4.1.7483.2.2.1.1.2.1.3.1.7" );
        List<SnmpEquEntity> shelfs = new ArrayList<SnmpEquEntity>();
        try
        {
            List<List<Pair<String, String>>> rows = client
                    .snmpWalkTableView( leafOids );
            for( List<Pair<String, String>> row : rows )
            {
                SnmpEquEntity shelf = new SnmpEquEntity();
                String index = row.get( 0 ).getSecond().replace( ".", "/" );
                shelf.setIndex( "1/" + index );
                shelf.setName( row.get( 1 ).getSecond() );
                shelf.setProgrammedType(
                    TypeMapUtil.getShelfType( row.get( 2 ).getSecond() ) );
                shelf.setPresentType(
                    TypeMapUtil.getShelfType( row.get( 3 ).getSecond() ) );
                shelf.setUnitPartNumber( row.get( 4 ).getSecond() );
                shelf.setSoftwarePartNumber( row.get( 5 ).getSecond() );
                shelf.setSerialNumber( row.get( 6 ).getSecond() );
                shelfs.add( shelf );
            }
        }
        catch( IOException e )
        {
            throw new AdapterException( ErrorCode.FAIL_GET_EQUIPMENT_BY_SNMP );
        }
        log.debug( "--------------End getShelfs--------------" );
        return shelfs;
    }

    private static List<SnmpEquEntity> getSlotCards( SnmpClient client )
            throws AdapterException
    {
        log.debug( "--------------Start getSlotCards--------------" );
        List<String> leafOids = new ArrayList<String>();
        leafOids.add( "1.3.6.1.4.1.7483.2.2.2.1.2.2.1.1.2" );
        leafOids.add( "1.3.6.1.4.1.7483.2.2.2.1.2.2.1.1.3" );
        // leafOids.add( "1.3.6.1.4.1.7483.2.2.2.1.2.2.1.1.4" );
        // leafOids.add( "1.3.6.1.4.1.7483.2.2.2.1.2.2.1.1.5" );

        leafOids.add( "1.3.6.1.4.1.7483.2.2.3.1.2.1.2.1.1" );
        leafOids.add( "1.3.6.1.4.1.7483.2.2.3.1.2.1.2.1.5" );
        leafOids.add( "1.3.6.1.4.1.7483.2.2.3.1.2.1.2.1.6" );
        // leafOids.add( "1.3.6.1.4.1.7483.2.2.3.1.2.1.2.1.7" );
        leafOids.add( "1.3.6.1.4.1.7483.2.2.3.1.2.1.2.1.23" );

        List<SnmpEquEntity> slotCards = new ArrayList<SnmpEquEntity>();
        try
        {
            List<List<Pair<String, String>>> rows = client
                    .snmpWalkTableView( leafOids );
            for( List<Pair<String, String>> row : rows )
            {
                SnmpEquEntity slotCard = new SnmpEquEntity();
                String index = "1/"
                        + row.get( 0 ).getSecond().replace( ".", "/" );
                slotCard.setIndex( index );
                slotCard.setProgrammedType(
                    TypeMapUtil.getSlotCardType( row.get( 1 ).getSecond() ) );
                slotCard.setPresentType(
                    TypeMapUtil.getSlotCardType( row.get( 2 ).getSecond() ) );
                slotCard.setName( row.get( 3 ).getSecond() );
                slotCard.setSerialNumber( row.get( 4 ).getSecond() );
                slotCard.setUnitPartNumber( row.get( 5 ).getSecond() );
                slotCard.setSoftwarePartNumber( row.get( 6 ).getSecond() );
                slotCards.add( slotCard );
            }
        }
        catch( IOException e )
        {
            throw new AdapterException( ErrorCode.FAIL_GET_EQUIPMENT_BY_SNMP );
        }
        log.debug( "--------------End getSlotCards--------------" );
        return slotCards;
    }
}