package com.nsb.enms.restful.adapter.server.notification;

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

import com.nsb.enms.restful.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.restful.adapter.server.common.util.JsonUtils;
import com.nsb.enms.restful.adapter.server.notification.entity.Message;
import com.nsb.enms.restful.adapter.server.notification.entity.NotificationEntity;
import com.nsb.enms.restful.adapter.server.notification.util.NotificationQueue;

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
        System.out.println( "Got connection : " + session.getRemoteAddress() );
        clients.add( session );
        System.out.println( "clients size:" + clients.size() );
        flag = true;
        sendEvent();
    }

    @OnWebSocketClose
    public void onClose( Session session, int statusCode, String reason )
    {
        log.debug( "WebSocket client closed. statusCode:" + statusCode
                + ", reason:" + reason );
        System.out.println(
            "WebSocket client closed. session:" + session.getRemoteAddress()
                    + ", statusCode:" + statusCode + ", reason:" + reason );
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
                log.error( e );
                e.printStackTrace();
            }
        }
    }

    private String constructEvent( String event )
    {
        Message message = new Message();
        message.setMessageType( "Notification" );
        String sender = ConfLoader.getInstance().getConf( "ADP_ID",
            "adapter_" + System.currentTimeMillis() );
        message.setSender( sender );
        message.setSendTime( new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" )
                .format( new Date() ) );
        message.setNotificationEntity(
            JsonUtils.json2Entity( event, NotificationEntity.class ) );
        return JsonUtils.entity2Json( message );
    }

    @Override
    public void configure( WebSocketServletFactory factory )
    {
        factory.register( NotificationServerHandler.class );
    }
}