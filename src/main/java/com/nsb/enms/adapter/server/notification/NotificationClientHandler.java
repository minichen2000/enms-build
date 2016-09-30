package com.nsb.enms.adapter.server.notification;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

import com.nsb.enms.adapter.server.notification.entity.NotificationEntity;
import com.nsb.enms.adapter.server.notification.util.NotificationParseUtil;


public class NotificationClientHandler extends WebSocketAdapter
{
    private static final Logger log = LogManager
            .getLogger( NotificationClientHandler.class );

    private final CountDownLatch closeLatch;

    public NotificationClientHandler()
    {
        closeLatch = new CountDownLatch( 1 );
    }

    public boolean awaitClose( int duration, TimeUnit unit )
            throws InterruptedException
    {
        return closeLatch.await( duration, unit );
    }

    @Override
    public void onWebSocketConnect( Session session )
    {
        log.debug( "Got connection, session:" + session.getRemoteAddress() );
    }

    @Override
    public void onWebSocketClose( int statusCode, String reason )
    {
        super.onWebSocketClose( statusCode, reason );
        log.debug( "WebSocket server connection closed. statusCode:"
                + statusCode + ", reason:" + reason );
        closeLatch.countDown();
    };

    @Override
    public void onWebSocketText( String event )
    {
        NotificationEntity entity;
        if( event.contains( "eventType { stateChange }" )
                || event.contains( "eventType { attributeValueChange }" ) )
        {
            entity = NotificationParseUtil
                    .parseModificationNotification( event );
        }
        else
        {
            entity = NotificationParseUtil.parseOtherNotification( event );
        }
        //NotificationQueue.push( event );
        NotificationSender.instance().send( entity );
    }
}