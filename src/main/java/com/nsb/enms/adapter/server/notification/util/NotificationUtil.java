package com.nsb.enms.adapter.server.notification.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.nsb.enms.restful.model.notif.Alarm;
import com.nsb.enms.restful.model.notif.AvcBody;
import com.nsb.enms.restful.model.notif.OcBody;
import com.nsb.enms.restful.model.notif.OdBody;

public class NotificationUtil
{
    private static SimpleDateFormat format = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss" );

    public static AvcBody createAvcNotif( Date date, String objectType,
            String objectID, String key, String valueType, String value,
            String oldValue )
    {
        AvcBody avc = new AvcBody();
        avc.setEventTime( format.format( date ) );
        avc.setObjectType( objectType );
        avc.setObjectID( objectID );
        avc.setKey( key );
        avc.setValueType( valueType );
        avc.setValue( value );
        avc.setOldValue( oldValue );

        return avc;
    }

    public static OcBody createOcNotif( Date date, String objectID,
            String objectType )
    {
        OcBody oc = new OcBody();
        oc.setEventTime( format.format( date ) );
        oc.setObjectID( objectID );
        oc.setObjectType( objectType );
        return oc;
    }

    public static OdBody createOdNotif( Date date, String objectType,
            String objectID )
    {
        OdBody od = new OdBody();
        od.setEventTime( format.format( date ) );
        od.setObjectType( objectType );
        od.setObjectID( objectID );
        return od;
    }

    public static Alarm createAlarm( String alarmCode, String alarmType,
            String severity, Date date, String occureTime,
            String clearTime, String probableCause, String objectType,
            String objectId, String ackStatus, String ackTime,
            String description )
    {
        Alarm alarm = new Alarm();
        alarm.setAlarmCode( alarmCode );
        alarm.setAlarmType( alarmType );
        alarm.setSeverity( severity );
        alarm.setEventTime( format.format( date ) );
        alarm.setOccureTime( occureTime );
        alarm.setClearTime( clearTime );
        alarm.setProbableCause( probableCause );
        alarm.setObjectType( objectType );
        alarm.setObjectId( objectId );
        alarm.setAckStatus( ackStatus );
        alarm.setAckTime( ackTime );
        alarm.setDescription( description );
        return alarm;
    }
}