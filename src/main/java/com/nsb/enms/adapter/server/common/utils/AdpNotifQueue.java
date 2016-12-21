package com.nsb.enms.adapter.server.common.utils;

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AdpNotifQueue<E>
{
    private static Logger log = LogManager.getLogger( AdpNotifQueue.class );

    private INotifProcessor<E> processor;

    public AdpNotifQueue( INotifProcessor<E> processor )
    {
        this.processor = processor;
        new QueueThread().start();
    }

    private LinkedBlockingQueue<E> queue = new LinkedBlockingQueue<E>();

    public void push( E event )
    {
        try
        {
            queue.put( event );
            log.debug( "Put: Queue size: " + queue.size() );
        }
        catch( Exception e1 )
        {
            log.error( "putNotification", e1 );
        }
    }

    private class QueueThread extends Thread
    {
        @Override
        public void run()
        {
            while( true )
            {
                try
                {
                    E event = queue.take();
                    processor.process( event );
                    log.debug( "Take: Queue size: " + queue.size() );
                }
                catch( Exception e1 )
                {
                    log.error( "takeNotification", e1 );
                }
            }
        }
    }
}