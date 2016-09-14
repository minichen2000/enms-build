package com.nsb.enms.restful.adapter.server.notification.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.restful.adapter.server.common.util.JsonUtils;
import com.nsb.enms.restful.adapter.server.notification.entity.DefinitionEntity;
import com.nsb.enms.restful.adapter.server.notification.entity.EventType;
import com.nsb.enms.restful.adapter.server.notification.entity.Moc;
import com.nsb.enms.restful.adapter.server.notification.entity.Moi;
import com.nsb.enms.restful.adapter.server.notification.entity.NotificationEntity;

public class NotificationParseUtil
{
    private static final Logger log = LogManager
            .getLogger( NotificationParseUtil.class );

    public static String parseModificationNotification( String event )
    {
        List<String> msgList = splitMsg( event );
        NotificationEntity entity = new NotificationEntity();
        DefinitionEntity definition = new DefinitionEntity();
        String temp = StringUtils.EMPTY;
        int length = msgList.size();
        for( int i = 0; i < length; i++ )
        {
            String line = msgList.get( i );
            if( line.startsWith( "managedObjectClass" ) )
            {
                entity.setMoc( new Moc( parseValue2( line ) ) );
                continue;
            }

            if( line.startsWith( "managedObjectInstance" ) )
            {
                continue;
            }

            if( line.startsWith( "attributeType" ) )
            {
                temp = temp + parseValue1( line ) + "=";
                continue;
            }

            if( line.startsWith( "attributeValue " ) )
            {
                temp = temp + parseValue2( line ) + "/";
                continue;
            }

            if( line.startsWith( "eventTime" ) )
            {
                if( !temp.isEmpty() )
                {
                    entity.setMoi(
                        new Moi( temp.substring( 0, temp.length() - 1 ) ) );
                    temp = StringUtils.EMPTY;
                }
                else
                {
                    entity.setMoi( new Moi( StringUtils.EMPTY ) );
                }
                entity.setEventTime( parseValue1( line ) );
                continue;
            }

            if( line.startsWith( "eventType" ) )
            {
                entity.setEventType( getEventType( parseValue1( line ) ) );
                continue;
            }

            if( line.startsWith( "eventInfo" ) )
            {
                continue;
            }

            if( line.startsWith( "stateChangeDefinition" ) )
            {
                continue;
            }

            if( line.startsWith( "attributeValueChangeDefinition" ) )
            {
                continue;
            }

            if( line.startsWith( "attributeID" ) )
            {
                definition.setAttributeID( parseValue1( line ) );
                continue;
            }

            if( line.startsWith( "oldAttributeValue" ) )
            {
                if( hasValue( line ) )
                {
                    definition.setOldAttributeValue( parseValue1( line ) );
                }
                continue;
            }

            if( line.startsWith( "newAttributeValue" ) )
            {
                if( definition.getOldAttributeValue() == null )
                {
                    if( !temp.isEmpty() )
                    {
                        if( temp.startsWith( "nAddresses:" ) )
                        {
                            temp = temp.replace( "nAddresses:",
                                StringUtils.EMPTY );
                        }
                        if( temp.length() > 1 )
                        {
                            temp = temp.substring( 0, temp.length() - 1 );
                        }

                        definition.setOldAttributeValue( temp );
                        temp = StringUtils.EMPTY;
                    }
                    else
                    {
                        definition.setOldAttributeValue( StringUtils.EMPTY );
                    }
                }

                if( hasValue( line ) )
                {
                    definition.setNewAttributeValue( parseValue1( line ) );
                    entity.setDefinitions( definition );
                    break;
                }
                continue;
            }

            if( line.startsWith( "notificationIdentifier" ) )
            {
                if( definition.getNewAttributeValue() == null )
                {
                    if( !temp.isEmpty() )
                    {
                        if( temp.startsWith( "nAddresses:" ) )
                        {
                            temp = temp.replace( "nAddresses:",
                                StringUtils.EMPTY );
                        }
                        if( temp.endsWith( ":" ) )
                        {
                            temp = temp.substring( 0, temp.length() - 1 );
                        }
                        definition.setNewAttributeValue( temp );
                        temp = StringUtils.EMPTY;
                    }
                    else
                    {
                        definition.setNewAttributeValue( StringUtils.EMPTY );
                    }
                }
                entity.setDefinitions( definition );
                break;
            }

            if( i == (length - 1) )
            {
                if( temp.startsWith( "nAddresses:" ) )
                {
                    temp = temp.replace( "nAddresses:", StringUtils.EMPTY )
                            + line;
                }
                else
                {
                    temp += parseValue( line );
                }
                if( definition.getNewAttributeValue() == null )
                {
                    if( !temp.isEmpty() )
                    {
                        definition.setNewAttributeValue( temp );
                        temp = StringUtils.EMPTY;
                    }
                    else
                    {
                        definition.setNewAttributeValue( StringUtils.EMPTY );
                    }
                }
                entity.setDefinitions( definition );
                break;
            }

            temp += parseValue( line ) + ":";

        }

        return JsonUtils.entity2Json( entity );
    }

    public static String parseOtherNotification( String event )
    {
        List<String> msgList = splitMsg( event );
        NotificationEntity entity = new NotificationEntity();
        String temp = StringUtils.EMPTY;
        for( String line : msgList )
        {
            if( line.startsWith( "managedObjectClass" ) )
            {
                entity.setMoc( new Moc( parseValue2( line ) ) );
                continue;
            }

            if( line.startsWith( "managedObjectInstance" ) )
            {
                continue;
            }

            if( line.startsWith( "attributeType" ) )
            {
                temp = temp + parseValue1( line ) + "=";
                continue;
            }

            if( line.startsWith( "attributeValue" ) )
            {
                temp = temp + parseValue2( line ) + "/";
                continue;
            }

            if( line.startsWith( "eventTime" ) )
            {
                if( !temp.isEmpty() )
                {
                    entity.setMoi(
                        new Moi( temp.substring( 0, temp.length() - 1 ) ) );
                    temp = StringUtils.EMPTY;
                }
                else
                {
                    entity.setMoi( new Moi( StringUtils.EMPTY ) );
                }

                entity.setEventTime( parseValue1( line ) );
                continue;
            }

            if( line.startsWith( "eventType" ) )
            {
                entity.setEventType( getEventType( parseValue1( line ) ) );
                break;
            }
        }

        return JsonUtils.entity2Json( entity );
    }

    private static List<String> splitMsg( String event )
    {
        log.debug( "Original Notification:" + event );
        event = event
                .replaceAll( "-- SEQUENCE\\s*\\w* --", StringUtils.EMPTY )
                .replace( "-- SET OF --", StringUtils.EMPTY )
                .replace( ",", StringUtils.EMPTY )
                .replace( "{", StringUtils.EMPTY )
                .replace( "}", StringUtils.EMPTY ).trim();
        String[] msgs = event.split( "\n" );
        List<String> msgList = new ArrayList<>();
        for( String msg : msgs )
        {
            String line = msg.trim();
            if( !line.isEmpty() )
            {
                msgList.add( line );
            }
        }
        return msgList;
    }

    private static boolean hasValue( String line )
    {
        if( line.split( "\\s+" ).length > 1 )
        {
            return true;
        }
        return false;
    }

    private static String parseValue( String line )
    {
        return line.split( "\\s+" )[0];
    }

    private static String parseValue1( String line )
    {
        return line.split( "\\s+" )[1];
    }

    private static String parseValue2( String line )
    {
        return line.split( "\\s+" )[2];
    }

    private static EventType getEventType( String type )
    {
        /*
         * switch( line ) { case "objectCreation": return
         * EventType.OBJECT_CREATION; case "objectDeletion": return
         * EventType.OBJECT_DELETION; case "stateChange": return
         * EventType.STATE_CHANGE; case "attributeValueChange": return
         * EventType.ATTRIBUTE_VALUE_CHANGE; default: return null; }
         */

        if( "objectCreation".equals( type ) )
        {
            return EventType.OBJECT_CREATION;
        }
        else if( "objectDeletion".equals( type ) )
        {
            return EventType.OBJECT_DELETION;
        }
        else if( "attributeValueChange".equals( type ) )
        {
            return EventType.ATTRIBUTE_VALUE_CHANGE;
        }
        else if( "stateChange".equals( type ) )
        {
            return EventType.STATE_CHANGE;
        }
        return null;
    }
}
