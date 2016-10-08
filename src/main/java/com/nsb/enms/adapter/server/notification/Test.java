package com.nsb.enms.adapter.server.notification;

public class Test
{
    public static void main( String[] args )
    {
        String destUri = "ws://135.251.96.42:9999";
        new NotificationClient( destUri ).start();
        new NotificationServer( 7778 ).start();
    }
}
