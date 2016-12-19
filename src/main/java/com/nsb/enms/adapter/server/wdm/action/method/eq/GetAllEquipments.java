package com.nsb.enms.adapter.server.wdm.action.method.eq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.wdm.action.entity.SnmpEquEntity;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.common.utils.Pair;
import com.nsb.enms.common.utils.snmpclient.SnmpClient;

public class GetAllEquipments
{
    public static List<SnmpEquEntity> getEquipments( SnmpClient client )
            throws AdapterException
    {
        List<SnmpEquEntity> equipments = new ArrayList<SnmpEquEntity>();
        equipments.addAll( getRacks( client ) );
        equipments.addAll( getShelfs( client ) );
        equipments.addAll( getSlots( client ) );
        equipments.addAll( getCards( client ) );
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
        return racks;
    }

    private static List<SnmpEquEntity> getShelfs( SnmpClient client )
            throws AdapterException
    {
        List<String> oids = new ArrayList<String>();
        oids.add( "1.3.6.1.4.1.7483.2.2.1.1.2.1.1" );
        //oids.add( "1.3.6.1.4.1.7483.2.2.1.1.2.1.3" );
        List<String> leafOids = new ArrayList<String>();
        // leafOids.add( "1.3.6.1.4.1.7483.2.2.1.1.2.1.1.1.1" );
        leafOids.add( "1.3.6.1.4.1.7483.2.2.1.1.2.1.1.1.2" );
        leafOids.add( "1.3.6.1.4.1.7483.2.2.1.1.2.1.1.1.5" );
        leafOids.add( "1.3.6.1.4.1.7483.2.2.1.1.2.1.1.1.6" );

        // leafOids.add( "1.3.6.1.4.1.7483.2.2.1.1.2.1.3.1.4" );
        // leafOids.add( "1.3.6.1.4.1.7483.2.2.1.1.2.1.3.1.5" );
        // leafOids.add( "1.3.6.1.4.1.7483.2.2.1.1.2.1.3.1.7" );
        List<SnmpEquEntity> shelfs = new ArrayList<SnmpEquEntity>();
        try
        {
            List<List<Pair<String, String>>> rows = client
                    .snmpWalkTableView( oids, leafOids );
            for( List<Pair<String, String>> row : rows )
            {
                SnmpEquEntity shelf = new SnmpEquEntity();
                String[] elements = row.get( 0 ).getFirst().split( "\\." );
                String index = elements[elements.length - 1];
                shelf.setIndex( "1/" + index );
                shelf.setName( row.get( 0 ).getSecond() );
                shelf.setProgrammedType(
                    TypeMapUtil.getShelfType( row.get( 1 ).getSecond() ) );
                shelf.setPresentType(
                    TypeMapUtil.getShelfType( row.get( 2 ).getSecond() ) );
                shelfs.add( shelf );
            }
        }
        catch( IOException e )
        {
            throw new AdapterException( ErrorCode.FAIL_GET_EQUIPMENT_BY_SNMP );
        }
        return shelfs;
    }

    private static Map<String, Pair<String, String>> indexTypeMap = new LinkedHashMap<String, Pair<String, String>>();

    private static List<SnmpEquEntity> getSlots( SnmpClient client )
            throws AdapterException
    {
        List<String> oids = new ArrayList<String>();
        oids.add( "1.3.6.1.4.1.7483.2.2.2.1.2.2.1" );
        List<String> leafOids = new ArrayList<String>();
        // leafOids.add( "1.3.6.1.4.1.7483.2.2.2.1.2.2.1.1.1" );
        leafOids.add( "1.3.6.1.4.1.7483.2.2.2.1.2.2.1.1.2" );
        leafOids.add( "1.3.6.1.4.1.7483.2.2.2.1.2.2.1.1.3" );
        leafOids.add( "1.3.6.1.4.1.7483.2.2.2.1.2.2.1.1.4" );
        leafOids.add( "1.3.6.1.4.1.7483.2.2.2.1.2.2.1.1.5" );

        List<SnmpEquEntity> slots = new ArrayList<SnmpEquEntity>();
        try
        {
            List<List<Pair<String, String>>> rows = client
                    .snmpWalkTableView( oids, leafOids );
            for( List<Pair<String, String>> row : rows )
            {
                System.out.println( "row:" + row );
                SnmpEquEntity slot = new SnmpEquEntity();
                String[] elements = row.get( 0 ).getFirst().split( "\\." );
                String index = "1/" + elements[elements.length - 2] + "/"
                        + elements[elements.length - 1];
                slot.setIndex( index );
                String programmedType = TypeMapUtil
                        .getSlotCardType( row.get( 0 ).getSecond() );
                slot.setProgrammedType( programmedType );
                String presentType = TypeMapUtil
                        .getSlotCardType( row.get( 1 ).getSecond() );
                slot.setPresentType( presentType );
                indexTypeMap.put( index,
                    new Pair<String, String>( programmedType, presentType ) );
                slots.add( slot );
            }
        }
        catch( IOException e )
        {
            throw new AdapterException( ErrorCode.FAIL_GET_EQUIPMENT_BY_SNMP );
        }
        return slots;
    }

    private static List<SnmpEquEntity> getCards( SnmpClient client )
            throws AdapterException
    {
        List<String> oids = new ArrayList<String>();
        oids.add( "1.3.6.1.4.1.7483.2.2.3.1.2.1.2" );
        
        List<String> leafOids = new ArrayList<String>();
        leafOids.add( "1.3.6.1.4.1.7483.2.2.3.1.2.1.2.1.1" );
        leafOids.add( "1.3.6.1.4.1.7483.2.2.3.1.2.1.2.1.5" );
        leafOids.add( "1.3.6.1.4.1.7483.2.2.3.1.2.1.2.1.6" );
        leafOids.add( "1.3.6.1.4.1.7483.2.2.3.1.2.1.2.1.7" );
        leafOids.add( "1.3.6.1.4.1.7483.2.2.3.1.2.1.2.1.23" );

        List<SnmpEquEntity> cards = new ArrayList<SnmpEquEntity>();
        try
        {
            List<List<Pair<String, String>>> rows = client
                    .snmpWalkTableView( oids, leafOids );
            for( List<Pair<String, String>> row : rows )
            {
                SnmpEquEntity card = new SnmpEquEntity();
                String[] elements = row.get( 0 ).getFirst().split( "\\." );
                String index = "1/" + elements[elements.length - 2] + "/"
                        + elements[elements.length - 1];
                card.setName( row.get( 0 ).getSecond() );
                card.setIndex( index );
                Pair<String, String> typePair = indexTypeMap.get( index );
                card.setProgrammedType( typePair.getFirst() );
                card.setPresentType( typePair.getSecond() );
                cards.add( card );
            }
        }
        catch( IOException e )
        {
            throw new AdapterException( ErrorCode.FAIL_GET_EQUIPMENT_BY_SNMP );
        }
        return cards;
    }

    public static void main( String[] args ) throws AdapterException
    {
        SnmpClient client = new SnmpClient( "135.251.96.5", 161, "admin_snmp" );
        List<SnmpEquEntity> shelfs = GetAllEquipments.getEquipments( client );
        for (SnmpEquEntity shelf : shelfs)
        {
            System.out.println( shelf );
        }
        /*List<String> oids = new ArrayList<>();
        oids.add( "1.3.6.1.4.1.7483.2.2.1.1.2.1.3" );
        try
        {
            List<Pair<String, String>> snmpWalkTable = client.snmpWalkTable( oids );
            for (Pair<String, String> pair : snmpWalkTable)
            {
                System.out.println( pair.getFirst() + " = " + pair.getSecond() );
            }
        }
        catch( IOException e )
        {
            e.printStackTrace();
        }*/
    }
}