package com.nsb.enms.adapter.server.notification.util;

import java.util.ArrayList;
import java.util.List;

import com.nsb.enms.restful.model.common.AvcBody;
import com.nsb.enms.restful.model.common.Message;
import com.nsb.enms.restful.model.common.OcBody;
import com.nsb.enms.restful.model.common.OdBody;

public class NotificationUtil
{
    private String messageType = null;

    private String eventTime = null;

    private String objectType = null;

    private String objectID = null;

    private String key = null;

    private String valueType = null;

    private String value = null;

    private String oldValue = null;

    public Message createNotification()
    {
        Message message = new Message();
        message.setSender( NotificationConverter.getSender() );
        message.setMessageType( messageType );
        if( message.equals( "oc" ) )
        {
            message.setOcDetail( createOcBody() );
        }
        else if( message.equals( "od" ) )
        {
            message.setOdDetail( createOdBody() );
        }
        else if( message.equals( "avc" ) )
        {
            List<AvcBody> avcs = new ArrayList<AvcBody>();
            avcs.add( createAvcBody() );
            message.setAvcs( avcs );
        }
        return message;
    }

    private AvcBody createAvcBody()
    {
        AvcBody avc = new AvcBody();
        avc.setEventTime( eventTime );
        avc.setKey( key );
        avc.setObjectID( objectID );
        avc.setObjectType( objectType );
        avc.setValue( value );
        avc.setOldValue( oldValue );
        avc.setValueType( valueType );
        return avc;
    }

    private OcBody createOcBody()
    {
        OcBody oc = new OcBody();
        oc.setEventTime( eventTime );
        oc.setObjectID( objectID );
        oc.setObjectType( objectType );
        return oc;
    }

    private OdBody createOdBody()
    {
        OdBody od = new OdBody();
        od.setEventTime( eventTime );
        od.setObjectID( objectID );
        od.setObjectType( objectType );
        return od;
    }

    public void setMessageType( String messageType )
    {
        this.messageType = messageType;
    }

    public void setEventTime( String eventTime )
    {
        this.eventTime = eventTime;
    }

    public void setObjectType( String objectType )
    {
        this.objectType = objectType;
    }

    public void setObjectID( String objectID )
    {
        this.objectID = objectID;
    }

    public void setKey( String key )
    {
        this.key = key;
    }

    public void setValueType( String valueType )
    {
        this.valueType = valueType;
    }

    public void setValue( String value )
    {
        this.value = value;
    }

    public void setOldValue( String oldValue )
    {
        this.oldValue = oldValue;
    }
}