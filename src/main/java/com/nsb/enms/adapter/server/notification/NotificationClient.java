package com.nsb.enms.adapter.server.notification;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

public class NotificationClient
{
    private static final Logger log = LogManager
            .getLogger( NotificationClient.class );

    private String uri;

    public NotificationClient( String uri )
    {
        this.uri = uri;
    }    

    public void start()
    {
        final WebSocketClient client = new WebSocketClient();
        final NotificationClientHandler socket = new NotificationClientHandler();
        new Thread( new Runnable()
        {

            @Override
            public void run()
            {
                try
                {
                    client.start();
                    URI serverUri = new URI( uri );
                    ClientUpgradeRequest request = new ClientUpgradeRequest();
                    client.connect( socket, serverUri, request );
                    log.debug( "Connecting to ", serverUri );
                    socket.awaitClose( Integer.MAX_VALUE, TimeUnit.DAYS );
                }
                catch( Exception e )
                {
                    log.error( "StartWebSocektClient", e );
                }
                finally
                {
                    try
                    {
                        client.stop();
                    }
                    catch( Exception e )
                    {
                        log.error( "StopWebSocketClient", e );
                    }
                }

            }
        } ).start();
    }
}