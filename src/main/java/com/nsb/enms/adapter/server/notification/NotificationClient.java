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
        WebSocketClient client = new WebSocketClient();
        NotificationClientHandler socket = new NotificationClientHandler();
        try
        {
            client.start();
            URI serverUri = new URI( uri );
            ClientUpgradeRequest request = new ClientUpgradeRequest();
            client.connect( socket, serverUri, request );
            log.debug( "Connecting to ", serverUri );
            socket.awaitClose( Integer.MAX_VALUE, TimeUnit.DAYS );
        }
        catch( Throwable t )
        {
            t.printStackTrace();
        }
        finally
        {
            try
            {
                client.stop();
            }
            catch( Exception e )
            {
                e.printStackTrace();
            }
        }                
    }

    public static void main( String args[] )
    {
        String destUri = "ws://135.251.96.42:9999";
        new NotificationClient( destUri ).start();
    }
}