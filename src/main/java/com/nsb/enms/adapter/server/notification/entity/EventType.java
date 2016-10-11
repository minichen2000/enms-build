package com.nsb.enms.adapter.server.notification.entity;

public enum EventType {
    
    OBJECT_CREATION("objectCreation"), OBJECT_DELETION("objectDeletion"), ATTRIBUTE_VALUE_CHANGE("attributeValueChange"), STATE_CHANGE("stateChange"), ALARM("Alarm");

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
