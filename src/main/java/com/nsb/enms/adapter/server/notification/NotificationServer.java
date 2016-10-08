package com.nsb.enms.adapter.server.notification;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;

public class NotificationServer
{
    private static final Logger log = LogManager
            .getLogger( NotificationServer.class );

    private int port;

    public NotificationServer( int port )
    {
        this.port = port;
    }

    public void start()
    {
        final Server server = new Server( port );
        ContextHandler context = new ContextHandler();
        context.setContextPath( "/Adapter/message" );
        NotificationServerHandler handler = new NotificationServerHandler();
        context.setHandler( handler );
        server.setHandler( handler );
        new Thread( new Runnable()
        {

            @Override
            public void run()
            {
                try
                {
                    server.start();
                    server.join();
                }
                catch( Exception e )
                {
                    log.error( "StartWebSocketServer", e );
                }
                finally
                {
                    try
                    {
                        server.stop();
                    }
                    catch( Exception e )
                    {
                        log.error( "StopWebSocketServer", e );
                    }
                }
            }
        } ).start();
    }
}