package com.nsb.enms.adapter.server.notification;

public class Test
{
    public static void main( String[] args )
    {
        Test test = new Test();
        test.new Thread1().start();
        test.new Thread2().start();
    }
    
    private class Thread1 extends Thread
    {
        public Thread1(){}
        public void run()
        {
            String destUri = "ws://135.251.96.42:9999";
            new NotificationClient( destUri ).start();
        }
    }
    
    private class Thread2 extends Thread
    {
        public void run()
        {
            new NotificationServer( 7778 ).start();
        }
    }
}
