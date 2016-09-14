package com.nsb.enms.restful.adapter.server.notification.entity;

public enum EventType {
    
    OBJECT_CREATION("objectCreation"), OBJECT_DELETION("objectDeletion"), ATTRIBUTE_VALUE_CHANGE("attributeValueChange"), STATE_CHANGE("stateChange");

    private String eventType;

    EventType( String eventType )
    {
        this.eventType = eventType;
    }
    
    public String toString()
    {
        return eventType;
    }
}
