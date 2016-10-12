package com.nsb.enms.adapter.server.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtil
{
    private static final DateFormat LOCAL_TMF_FORMAT = new SimpleDateFormat(
            "yyyyMMddHHmmss.0Z" );

    static
    {
        LOCAL_TMF_FORMAT.setTimeZone( TimeZone.getTimeZone( "UTC" ) );
    }

    public static String getLocalTmfTime( Date date )
    {
        return LOCAL_TMF_FORMAT.format( date );
    }
}
