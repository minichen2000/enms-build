package com.nsb.enms.adapter.server.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtil
{
    private static final DateFormat LOCAL_TMF_FORMAT = new SimpleDateFormat(
            "yyyyMMddHHmmss.0Z" );

    private static final DateFormat UTC_TMF_FORMAT = new SimpleDateFormat(
            "yyyyMMddHHmmss.0'Z'" );

    private static final DateFormat LABEL_TMF_FORMAT = new SimpleDateFormat(
            "yyyyMMddHHmm" );

    static
    {
        UTC_TMF_FORMAT.setTimeZone( TimeZone.getTimeZone( "Etc/Greenwich" ) );
    }

    public static String getUtcTime( boolean utc )
    {
        return utc ? getUtcTmfTime() : getLocalTmfTime();
    }
    
    public static String getLocalTmfTime()
    {
        return LOCAL_TMF_FORMAT.format( new Date() );
    }

    public static String getUtcTmfTime()
    {
        return UTC_TMF_FORMAT.format( new Date() );
    }

    public static String getUtcTmfTime( Date date )
    {
        return UTC_TMF_FORMAT.format( date );
    }

    public static String getLabelTmfTime()
    {
        return LABEL_TMF_FORMAT.format( new Date() );
    }

    public static String getLabelTmfTime( Date date )
    {
        return LABEL_TMF_FORMAT.format( date );
    }
}
