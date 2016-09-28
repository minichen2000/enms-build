package com.nsb.enms.adapter.server.notification;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import com.nsb.enms.adapter.server.common.util.JsonUtils;
import com.nsb.enms.adapter.server.notification.entity.NotificationEntity;
import com.nsb.enms.adapter.server.notification.util.NotificationConverter;
import com.nsb.enms.adapter.server.notification.util.NotificationQueue;
import com.nsb.enms.restful.model.common.Message;

@WebSocket
public class NotificationServerHandler extends WebSocketHandler
{
    private static final Logger log = LogManager
            .getLogger( NotificationServerHandler.class );

    private static List<Session> clients = new ArrayList<Session>();

    private boolean flag = false;

    @OnWebSocketConnect
    public void onConnect( Session session )
    {
        log.debug( "Got connection : " + session.getRemoteAddress() );
        clients.add( session );
        flag = true;
        sendEvent();
    }

    @OnWebSocketClose
    public void onClose( Session session, int statusCode, String reason )
    {
        log.debug( "WebSocket client closed. statusCode:" + statusCode
                + ", reason:" + reason );        
        clients.remove( session );
        session = null;
        if( clients.isEmpty() )
        {
            flag = false;
        }
    }

    public void sendEvent()
    {
        while( flag )
        {
            try
            {
                String event = NotificationQueue.pull();
                if( event != null )
                {
                    log.debug( new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" )
                            .format( new Date() ) );
                    log.debug( "Server:" + event );
                    event = constructEvent( event );
                    for( Session session : clients )
                    {
                        if( session != null )
                        {
                            session.getRemote().sendString( event );
                        }
                    }
                }
            }
            catch( Exception e )
            {
                log.error( "sendEvent", e );
            }
        }
    }

    private String constructEvent( String event )
    {
        Message message = NotificationConverter.convert( JsonUtils.json2Entity( event, NotificationEntity.class ) );
        return JsonUtils.entity2Json( message );
    }

    @Override
    public void configure( WebSocketServletFactory factory )
    {
        factory.register( NotificationServerHandler.class );
    }
}