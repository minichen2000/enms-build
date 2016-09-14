package com.nsb.enms.restful.adapter.server.notification.entity;

public class Message
{
    private String sender;

    private String messageType;

    private String sendTime;

    private NotificationEntity notification;

    private AlarmEntity alarm;

    public String getSender()
    {
        return sender;
    }

    public void setSender( String sender )
    {
        this.sender = sender;
    }

    public String getMessageType()
    {
        return messageType;
    }

    public void setMessageType( String messageType )
    {
        this.messageType = messageType;
    }

    public String getSendTime()
    {
        return sendTime;
    }

    public void setSendTime( String sendTime )
    {
        this.sendTime = sendTime;
    }

    public NotificationEntity getNotification()
    {
        return notification;
    }

    public void setNotificationEntity( NotificationEntity notification )
    {
        this.notification = notification;
    }

    public AlarmEntity getAlarm()
    {
        return alarm;
    }

    public void setAlarm( AlarmEntity alarm )
    {
        this.alarm = alarm;
    }
}
