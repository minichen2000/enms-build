package com.nsb.enms.adapter.server.notification;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.adapter.server.common.conf.ConfigKey;
import com.nsb.enms.adapter.server.common.utils.TimeUtil;
import com.nsb.enms.adapter.server.db.mgr.AdpNesDbMgr;
import com.nsb.enms.adapter.server.notification.entity.EventType;
import com.nsb.enms.adapter.server.notification.entity.NotificationEntity;
import com.nsb.enms.common.enms_mq.EnmsPubFactory;
import com.nsb.enms.common.enms_mq.EnmsPublisher;
import com.nsb.enms.common.util.ObjectType;
import com.nsb.enms.restful.model.notif.Alarm;
import com.nsb.enms.restful.model.notif.AvcBody;
import com.nsb.enms.restful.model.notif.OcBody;
import com.nsb.enms.restful.model.notif.OdBody;

public class NotificationSender
{
    private static NotificationSender inst_ = null;

    private static EnmsPublisher publisher = null;    

    private static final Logger log = LogManager
            .getLogger( NotificationSender.class );

    private NotificationSender()
    {

    }

    public static NotificationSender instance()
    {
        if( inst_ == null )
        {
            inst_ = new NotificationSender();
        }

        return inst_;
    }

    public void init()
    {
        int port = ConfLoader.getInstance().getInt( ConfigKey.NOTIF_SERVER_PORT,
            ConfigKey.DEFAULT_NOTIF_SERVER_PORT );
        publisher = EnmsPubFactory.createMqPub( port );
    }

    public void send( NotificationEntity entity )
    {
        EventType eventType = entity.getEventType();
        String eventTime = entity.getEventTime();
        ObjectType objectType = getObjectType( entity.getMoc().getMoc() );
        String objectID = getObjectId( objectType, entity.getMoi().getMoi() );
        switch( eventType )
        {
            case OBJECT_CREATION:
                OcBody ocBody = publisher.createOcBody( eventTime, objectType,
                    objectID );
                send( ocBody );
                break;
            case OBJECT_DELETION:
                OdBody odBody = publisher.createOdBody( eventTime, objectType,
                    objectID );
                send( odBody );
                break;
            case ATTRIBUTE_VALUE_CHANGE:
            case STATE_CHANGE:
                AvcBody avc = publisher.createAvcBody( eventTime, objectType,
                    objectID, entity.getDefinition().getAttributeID(), "String",
                    entity.getDefinition().getNewAttributeValue(),
                    entity.getDefinition().getOldAttributeValue() );
                send( avc );
                break;
            case ALARM:

                break;
            default:
                break;
        }
    }

    private void send( Object object )
    {
        publisher.sendMessage( object );
    }

    public void sendAvcNotif( Date date, ObjectType objectType, String objectID,
            String key, String valueType, String value, String oldValue )
    {
        String eventTime = TimeUtil.getLocalTmfTime( date );

        AvcBody avc = publisher.createAvcBody( eventTime, objectType, objectID,
            key, valueType, value, oldValue );

        send( avc );
    }

    public void sendOcNotif( Date date, ObjectType objectType, String objectID )
    {
        String eventTime = TimeUtil.getLocalTmfTime( date );
        OcBody oc = publisher.createOcBody( eventTime, objectType, objectID );
        send( oc );
    }

    public void sendOdNotif( Date date, ObjectType objectType,
            String objectID )
    {
        String eventTime = TimeUtil.getLocalTmfTime( date );
        OdBody od = publisher.createOdBody( eventTime, objectType, objectID );
        send( od );
    }

    public void sendAlarm( String alarmCode, String alarmType,
            String severity, String eventTime, String occureTime, String clearTime,
            String probableCause, String objectType, String objectId,
            String ackStatus, String ackTime, String description )
    {
        Alarm alarm = publisher.createAlarm( alarmCode, alarmType, severity,
            eventTime, occureTime, clearTime, probableCause, objectType,
            objectId, ackStatus, ackTime, description );
        send( alarm );
    }

    private ObjectType getObjectType( String moc )
    {
        if( moc.contains( "NetworkElement" )
                || moc.toLowerCase().contains( "ne" ) )
        {
            return ObjectType.NE;
        }

        if( moc.contains( "TTP" ) )
        {
            return ObjectType.TP;
        }

        if( moc.contains( "Equipment" ) )
        {
            return ObjectType.Equipment;
        }

        return ObjectType.NE;
    }

    private String getObjectId( ObjectType objectType, String moi )
    {
        switch( objectType )
        {
            case NE:
                AdpNesDbMgr nesDbMgr = new AdpNesDbMgr();                
                String groupId = moi.split( "/" )[0].split( "=" )[1];
                String neId = moi.split( "/" )[1].split( "=" )[1];
                try
                {
                    return nesDbMgr.getIdByGroupAndNeId(groupId, neId);
                }
                catch( Exception e )
                {
                    log.error( "getIdByKeOneNe", e );
                    return "";
                }
            case TP:

                return "";
            case Equipment:

                return "";
            case Connection:

                return "";
            default:
                return null;
        }
    }
}