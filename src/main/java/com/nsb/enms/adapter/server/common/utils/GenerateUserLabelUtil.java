package com.nsb.enms.adapter.server.common.utils;

import java.util.List;

import com.nsb.enms.adapter.server.action.entity.TpEntity;

public class GenerateUserLabelUtil
{
    public static String generateTpUserLabel( TpEntity entity )
    {
        String moc = entity.getMoc();
        String moi = entity.getMoi();
        String tpId = moi.split( "/" )[2];
        String userLabel = "";
        if( "labelledOpticalSPITTPBidirectional".equals( moc )
                || "pPITTPBidirectionalR1".equals( moc ) )
        {
            userLabel = generateTpUserLabel( tpId, entity, "port" );
            return userLabel;
        }

        if( "au4CTPBidirectionalR1".equals( moc ) )
        {
            userLabel = "/AU4=" + moi.split( "/" )[3].split( "=" )[1];
            return userLabel;
        }

        if( "tu12CTPBidirectionalR1".equals( moc ) )
        {
            String[] ids = moi.split( "/" );
            userLabel = "/TU12=" + ids[3].split( "=" )[1] + "/"
                    + ids[4].split( "=" )[1] + "/" + ids[5].split( "=" )[1];
            return userLabel;
        }

        if( "tu3CTPBidirectionalR1".equals( moc ) )
        {
            userLabel = "/TU3=" + moi.split( "/" )[3].split( "=" )[1];
            return userLabel;
        }

        if( "vc12PathTraceTTPBidirectional".equals( moc ) )
        {
            userLabel = generateTpUserLabel( tpId, entity, "vc12" );
            return userLabel;
        }
        
        if ("vc3TTPBidirectionalR1".equals( moc ))
        {
            userLabel = generateTpUserLabel( tpId, entity, "vc3" );
            return userLabel;
        }

        if( "modifiableVC4TTPBidirectionalR1".equals( moc ) )
        {
            userLabel = generateTpUserLabel( tpId, entity, "vc4" );
            return userLabel;
        }

        return "";
    }

    private static String generateTpUserLabel( String tpId, TpEntity entity,
            String type )
    {
        String id = tpId.split( "=" )[1];
        StringBuilder userLabel = new StringBuilder();
        String rack = id.substring( 0, 1 );
        String shelf = id.substring( 1, 2 );
        String slot = id.substring( 2, 4 );
        if (slot.startsWith( "0" ))
        {
            slot = slot.substring( 1 );
        }
        userLabel.append( "/rack=" + rack );
        userLabel.append( "/shelf=" + shelf );
        userLabel.append( "/slot=" + slot );

        List<String> supporedtByObjectList = entity.getSupportedByObjectList();
        for( String value : supporedtByObjectList )
        {
            String[] ids = value.split( "/" );
            if( ids.length == 4 )
            {
                String rack1 = ids[0].split( "=" )[1];
                String shelf1 = ids[1].split( "=" )[1];
                String slot1 = ids[2].split( "=" )[1];
                if( rack1.equals( rack ) && shelf1.equals( shelf )
                        && slot1.equals( slot ) )
                {
                    userLabel.append( "/sub-slot=" + ids[3].split( "=" )[1] );
                    break;
                }
            }
        }
        userLabel.append( "/" + type + "=" + id.substring( 4, 6 ) );
        return userLabel.toString();
    }
}
