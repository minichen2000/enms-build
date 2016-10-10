package com.nsb.enms.adapter.server.notification;

import com.nsb.enms.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.adapter.server.notification.entity.EventType;
import com.nsb.enms.adapter.server.notification.entity.NotificationEntity;
import com.nsb.enms.common.enms_mq.EnmsPubFactory;
import com.nsb.enms.common.enms_mq.EnmsPublisher;
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
        switch( eventType )
        {
            case OBJECT_CREATION:
                OcBody ocBody = new OcBody();
                ocBody.setEventTime( entity.getEventTime() );
                ocBody.setObjectID( entity.getMoi().toString() );
                ocBody.setObjectType( entity.getMoc().toString() );
                publisher.sendMessage( ocBody );
                break;
            case OBJECT_DELETION:
                OdBody odBody = new OdBody();
                odBody.setEventTime( entity.getEventTime() );
                odBody.setObjectID( entity.getMoi().toString() );
                odBody.setObjectType( entity.getMoc().toString() );
                publisher.sendMessage( odBody );
                break;
            case ATTRIBUTE_VALUE_CHANGE:
            case STATE_CHANGE:
                AvcBody avc = new AvcBody();
                avc.setEventTime( entity.getEventTime() );
                avc.setKey( entity.getDefinition().getAttributeID() );
                avc.setObjectID( entity.getMoi().toString() );
                avc.setObjectType( entity.getMoc().toString() );
                avc.setOldValue(
                    entity.getDefinition().getOldAttributeValue() );
                avc.setValue( entity.getDefinition().getNewAttributeValue() );
                avc.setValueType( "String" );
                publisher.sendMessage( avc );
                break;
            default:
                break;
        }
    }
}
