package com.nsb.enms.restful.adapterserver.api;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import com.nsb.enms.adapter.server.business.ping.PingApp;
import com.nsb.enms.adapter.server.business.register.RegisterManager;
import com.nsb.enms.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.statemachine.ne.NeStateMachineApp;
import com.nsb.enms.adapter.server.filter.AccessControlFilter;
import com.nsb.enms.adapter.server.manager.Q3EmlImMgr;
import com.nsb.enms.adapter.server.notification.NotificationClient;
import com.nsb.enms.adapter.server.notification.NotificationSender;

public class Main
{
    private static Logger log = LogManager.getLogger( Main.class );

    public static void main( String[] args )
    {
        String[] packages = new String[] {"io.swagger.jaxrs.listing",
                "io.swagger.sample.resource",
                "com.nsb.enms.restful.adapterserver.api"};

        ResourceConfig config = new ResourceConfig().packages( packages )
                .register( JacksonFeature.class )
                .register( AccessControlFilter.class );

        ServletHolder servlet = new ServletHolder(
                new ServletContainer( config ) );

        final Server server1 = new Server( 9090 );
        ServletContextHandler context = new ServletContextHandler( server1,
                "/*" );
        context.addServlet( servlet, "/*" );
        new Thread( new Runnable()
        {

            @Override
            public void run()
            {
                try
                {
                    try
                    {
                        server1.start();
                        server1.join();
                    }
                    catch( Exception e )
                    {
                        e.printStackTrace();
                    }
                }
                finally
                {
                    server1.destroy();
                }

            }
        } ).start();

        loadConf();

        register2Controller();

        String q3WSServerUri = ConfLoader.getInstance()
                .getConf( "Q3_WS_SERVER_URI", "" );
        NotificationClient client = new NotificationClient( q3WSServerUri );
        client.start();

        // int adapterWSServerPort = ConfLoader.getInstance()
        // .getInt( "ADP_WS_SERVER_PORT", 7778 );
        /*
         * NotificationServer server = new NotificationServer(
         * adapterWSServerPort ); server.start();
         */

        try
        {
            // The real groupId should be set
            Q3EmlImMgr.instance().init( 100 );
        }
        catch( AdapterException e )
        {
            e.printStackTrace();
        }

        NeStateMachineApp.instance().init();
        NotificationSender.instance().init();

        PingApp pingApp = new PingApp();
        pingApp.checkPing();

    }

    private static void loadConf()
    {
        String confPath = "D:\\gitrepo\\enms\\adapter\\src\\main\\webapp\\WEB-INF\\conf";
        log.debug( "The confPath is " + confPath );
        try
        {
            ConfLoader.getInstance().loadConf( confPath + "/conf.properties" );
        }
        catch( AdapterException e )
        {
            e.printStackTrace();
        }

        String ctrlUrl = ConfLoader.getInstance().getConf( "CTRL_URL", "" );
        log.debug( "The ctrlUrl is " + ctrlUrl );
        initControllerApiClient( ctrlUrl );
    }

    private static void initControllerApiClient( String ctrlUrl )
    {
        com.nsb.enms.restful.controllerclient.Configuration.setDefaultApiClient(
            new com.nsb.enms.restful.controllerclient.ApiClient()
                    .setBasePath( ctrlUrl ) );
    }

    private static void register2Controller()
    {
        long period = ConfLoader.getInstance().getInt( "REG_PERIOD", 60000 );
        final Timer timer = new Timer();
        final RegisterManager register = new RegisterManager();
        timer.scheduleAtFixedRate( new TimerTask()
        {
            public void run()
            {
                log.debug( "start to register to Controller" );
                boolean isOk = register.register2Controller();
                log.debug(
                    "the result of registering to Controller is :" + isOk );
                if( isOk )
                {
                    timer.cancel();
                }
            }
        }, 0, period );
    }
}
