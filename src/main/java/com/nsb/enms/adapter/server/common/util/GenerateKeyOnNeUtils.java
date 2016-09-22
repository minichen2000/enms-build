package com.nsb.enms.adapter.server.common.util;

import org.apache.commons.lang3.StringUtils;

import com.nsb.enms.adapter.server.common.TYPES;

public class GenerateKeyOnNeUtils
{
    public static String generateKeyOnNe(TYPES type, String moc, String moi)
    {
        switch( type )
        {
            case NE:
                return moc + ":" + moi;
            case TP:
                return moc + ":" + moi.split( "/" )[2];
            default:
                return StringUtils.EMPTY;
        }
        
    }
    
    public static String getNeMoi(String keyOnNe)
    {
        return keyOnNe.split( ":" )[1];
    }
}
