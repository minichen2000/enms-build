package com.nsb.enms.restful.adapter.server.notification.util;

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NotificationQueue
{
    private final static Logger log = LogManager
            .getLogger( NotificationQueue.class );

    private static LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();

    public static void push( String event )
    {
        try
        {
            queue.put( event );
            log.debug( "Put: queue size is " + queue.size() );
        }
        catch( InterruptedException e )
        {
            log.error( "putEvent", e );
        }
    }

    public static String pull()
    {
        String event = null;
        try
        {
            if( !queue.isEmpty() )
            {
                event = queue.take();
                log.debug( "Take: queue size is " + queue.size() );
            }
        }
        catch( InterruptedException e )
        {
            log.error( "takeEvent", e );
        }
        return event;
    }
}
