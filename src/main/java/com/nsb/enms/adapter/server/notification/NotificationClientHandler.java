package com.nsb.enms.restful.adapter.server.notification;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

import com.nsb.enms.restful.adapter.server.notification.util.NotificationParseUtil;
import com.nsb.enms.restful.adapter.server.notification.util.NotificationQueue;

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
        if( event.contains( "eventType { stateChange }" )
                || event.contains( "eventType { attributeValueChange }" ) )
        {
            event = NotificationParseUtil
                    .parseModificationNotification( event );
        }
        else
        {
            event = NotificationParseUtil.parseOtherNotification( event );
        }
        NotificationQueue.push( event );
    }
}