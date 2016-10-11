package com.nsb.enms.adapter.server.notification;

import com.nsb.enms.adapter.server.common.TYPES;
import com.nsb.enms.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.adapter.server.common.util.GenerateKeyOnNeUtils;
import com.nsb.enms.adapter.server.db.mgr.AdpNesDbMgr;
import com.nsb.enms.adapter.server.notification.entity.EventType;
import com.nsb.enms.adapter.server.notification.entity.NotificationEntity;
import com.nsb.enms.common.enms_mq.EnmsPubFactory;
import com.nsb.enms.common.enms_mq.EnmsPublisher;
import com.nsb.enms.common.util.ObjectType;
import com.nsb.enms.restful.model.notif.AvcBody;
import com.nsb.enms.restful.model.notif.OcBody;
import com.nsb.enms.restful.model.notif.OdBody;

public class NotificationSender
{
    private static NotificationSender inst_ = null;

    private static EnmsPublisher publisher = null;

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
        int port = ConfLoader.getInstance().getInt( "NOTIF_SERVER_PORT", 8025 );
        publisher = EnmsPubFactory.createMqPub( port );
    }

    public void send( NotificationEntity entity )
    {
        EventType eventType = entity.getEventType();
        String eventTime = entity.getEventTime();
        ObjectType objectType = getObjectType( entity.getMoc().getMoc() );
        String objectID = getObjectId( objectType, entity.getMoc().getMoc(),
            entity.getMoi().getMoi() );
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
    
    public void send(Object object)
    {
        publisher.sendMessage( object );
    }

    private ObjectType getObjectType( String moc )
    {
        if( "sdhNetworkElement".equals( moc ) )
        {
            return ObjectType.NE;
        }

        if( moc.contains( "TTP" ) )
        {
            return ObjectType.TP;
        }

        if( moc.contains( "Equipment" ) )
        {
            return ObjectType.Board;
        }

        return null;
    }

    private String getObjectId( ObjectType objectType, String moc, String moi )
    {
        switch( objectType )
        {
            case NE:
                AdpNesDbMgr nesDbMgr = new AdpNesDbMgr();
                String keyOnNe = GenerateKeyOnNeUtils.generateKeyOnNe( TYPES.NE,
                    moc, moi );
                try
                {
                    return nesDbMgr.getIdByKeyOnNe( keyOnNe );
                }
                catch( Exception e )
                {
                    e.printStackTrace();
                    return "";
                }
            case TP:

                return "";
            case Board:

                return "";
            case Connection:

                return "";
            default:
                return null;
        }
    }
}