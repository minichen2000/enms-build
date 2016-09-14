package com.nsb.enms.restful.adapter.server.notification;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;

public class NotificationServer
{
    private int port;

    public NotificationServer( int port )
    {
        this.port = port;
    }

    public void start()
    {
        Server server = new Server( port );
        ContextHandler context = new ContextHandler();
        context.setContextPath( "/Adapter/message" );
        NotificationServerHandler handler = new NotificationServerHandler();
        context.setHandler( handler );
        server.setHandler( handler );
        try
        {
            server.start();
            server.join();
        }
        catch( Exception e )
        {

            e.printStackTrace();
        }
    }

    public static void main( String args[] )
    {
        new NotificationServer( 7778 ).start();
    }
}