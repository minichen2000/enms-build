package com.nsb.enms.adapter.server.notification.util;

import java.util.ArrayList;
import java.util.List;

import com.nsb.enms.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.adapter.server.notification.entity.EventType;
import com.nsb.enms.adapter.server.notification.entity.NotificationEntity;
import com.nsb.enms.restful.model.common.AvcBody;
import com.nsb.enms.restful.model.common.Message;
import com.nsb.enms.restful.model.common.OcBody;
import com.nsb.enms.restful.model.common.OdBody;

public class NotificationConverter
{
    public static Message convert(NotificationEntity entity)
    {
        Message message = new Message();
        EventType eventType = entity.getEventType();
        switch( eventType )
        {
            case OBJECT_CREATION:
                OcBody ocBody = new OcBody();
                ocBody.setEventTime( entity.getEventTime() );
                ocBody.setObjectID( entity.getMoi().toString() );
                ocBody.setObjectType( entity.getMoc().toString() );
                message.setOcDetail( ocBody );
                message.setMessageType( "oc" );
                break;
            case OBJECT_DELETION:
                OdBody odBody = new OdBody();
                odBody.setEventTime( entity.getEventTime() );
                odBody.setObjectID( entity.getMoi().toString() );
                odBody.setObjectType( entity.getMoc().toString() );
                message.setOdDetail( odBody );
                message.setMessageType( "od" );
                break;
            case ATTRIBUTE_VALUE_CHANGE:
            case STATE_CHANGE:
                List<AvcBody> avcs = new ArrayList<AvcBody>();
                AvcBody avc = new AvcBody();
                avc.setEventTime( entity.getEventTime() );
                avc.setKey( entity.getDefinition().getAttributeID() );
                avc.setObjectID( entity.getMoi().toString() );
                avc.setObjectType( entity.getMoc().toString() );
                avc.setOldValue( entity.getDefinition().getOldAttributeValue() );
                avc.setValue( entity.getDefinition().getNewAttributeValue() );
                avc.setValueType( "String" );
                avcs.add( avc );
                message.setAvcs( avcs );
                message.setMessageType( "avc" );
                break;
            default:
                break;
        }
        message.setMessageType( "Notification" );
        String sender = ConfLoader.getInstance().getConf( "ADP_ID",
            "adapter_" + System.currentTimeMillis() );
        message.setSender( sender );
        return message;
    }
}
