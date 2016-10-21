package com.nsb.enms.adapter.server.notification.entity;

public class NotificationEntity
{
    private Moc moc;

    private Moi moi;

    private String eventTime;

    private EventType eventType;
    
    private AttributeInfo attributeInfo;
    
    private DefinitionEntity definition;

    public Moc getMoc()
    {
        return moc;
    }

    public void setMoc( Moc moc )
    {
        this.moc = moc;
    }

    public Moi getMoi()
    {
        return moi;
    }

    public void setMoi( Moi moi )
    {
        this.moi = moi;
    }

    public String getEventTime()
    {
        return eventTime;
    }

    public void setEventTime( String eventTime )
    {
        this.eventTime = eventTime;
    }

    public EventType getEventType()
    {
        return eventType;
    }

    public void setEventType( EventType eventType )
    {
        this.eventType = eventType;
    }    
    
    public AttributeInfo getAttributeInfo()
    {
        return attributeInfo;
    }

    public void setAttributeInfo( AttributeInfo attributeInfo )
    {
        this.attributeInfo = attributeInfo;
    }

    public DefinitionEntity getDefinition()
    {
        return definition;
    }

    public void setDefinition(
            DefinitionEntity definition )
    {
        this.definition = definition;
    }
}
