package com.nsb.enms.adapter.server.api;

import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.business.register.RegisterManager;
import com.nsb.enms.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.notification.NotificationClient;
import com.nsb.enms.adapter.server.notification.NotificationServer;

public class Bootstrap extends HttpServlet
{
    private static final long serialVersionUID = 6213433092343311611L;

    private static final Logger log = LogManager.getLogger( Bootstrap.class );

    @Override
    public void init( ServletConfig config ) throws ServletException
    {
        Info info = new Info().title( "ENMS Adapter Server" )
                .description( "ENMS Adapter RESTful interface. " )
                .termsOfService( "" ).contact( new Contact().email( "" ) )
                .license( new License().name( "" ).url( "" ) );

        ServletContext context = config.getServletContext();
        loadConf( context );

        Swagger swagger = new Swagger().info( info );
        new SwaggerContextService().withServletConfig( config )
                .updateSwagger( swagger );

        register2Controller();
    }

    private void loadConf( ServletContext context )
    {
        String confPath = context.getRealPath( "/WEB-INF/conf" );
        log.debug( "The confPath is " + confPath );
        try
        {
            ConfLoader.getInstance().loadConf( confPath + "/conf.properties" );
        }
        catch( AdapterException e )
        {
            e.printStackTrace();
        }

        String dbUrl = ConfLoader.getInstance().getConf( "DB_URL", "" );
        log.debug( "The dbUrl is " + dbUrl );
        initDbApiClient( dbUrl );

        String ctrlUrl = ConfLoader.getInstance().getConf( "CTRL_URL", "" );
        log.debug( "The ctrlUrl is " + ctrlUrl );
        initControllerApiClient( ctrlUrl );

        /*String q3WSServerUri = ConfLoader.getInstance()
                .getConf( "Q3_WS_SERVER_URI", "" );
        new Thread( new WSClientThread( q3WSServerUri ) ).start();

        int adapterWSServerPort = ConfLoader.getInstance()
                .getInt( "ADP_WS_SERVER_PORT", 7778 );
        new Thread( new WSServerThread( adapterWSServerPort ) ).start();*/
    }

    private void initControllerApiClient( String ctrlUrl )
    {
        com.nsb.enms.restful.controller.client.Configuration
                .setDefaultApiClient(
                    new com.nsb.enms.restful.controller.client.ApiClient()
                            .setBasePath( ctrlUrl ) );
    }

    private void initDbApiClient( String dbUrl )
    {
        Configuration
                .setDefaultApiClient( new ApiClient().setBasePath( dbUrl ) );
    }

    private void register2Controller()
    {
        long period = Long.valueOf(
            ConfLoader.getInstance().getConf( "REG_PERIOD", "60000" ) );
        final RegisterManager register = new RegisterManager();
        final Timer timer = new Timer();
        timer.scheduleAtFixedRate( new TimerTask()
        {
            public void run()
            {
                log.debug( "start to register to Controller" );
                boolean isOk = register.register2Controller();
                log.debug( "the result of registering to Controller is :{}",
                    isOk );
                if( isOk )
                {
                    timer.cancel();
                }
            }
        }, 0, period );
    }

    private class WSClientThread extends Thread
    {
        private String uri;

        public WSClientThread( String uri )
        {
            this.uri = uri;
        }

        public void run()
        {
            new NotificationClient( uri ).start();
        }
    }

    private class WSServerThread extends Thread
    {
        private int port;

        public WSServerThread( int port )
        {
            this.port = port;
        }

        public void run()
        {
            new NotificationServer( port ).start();
        }
    }
}
