package com.nsb.enms.restful.adapterserver.api;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.business.ping.PingApp;
import com.nsb.enms.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.util.Register2ControllerUtils;
import com.nsb.enms.adapter.server.manager.Q3EmlImMgr;
import com.nsb.enms.adapter.server.notification.NotificationClient;
import com.nsb.enms.adapter.server.notification.NotificationServer;

import io.swagger.jaxrs.config.SwaggerContextService;
import io.swagger.models.*;

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

        /*String q3WSServerUri = ConfLoader.getInstance()
                .getConf( "Q3_WS_SERVER_URI", "" );
        new Thread( new WSClientThread( q3WSServerUri ) ).start();

        int adapterWSServerPort = ConfLoader.getInstance()
                .getInt( "ADP_WS_SERVER_PORT", 7778 );
        new Thread( new WSServerThread( adapterWSServerPort ) ).start();*/

        PingApp pingApp = new PingApp();
        pingApp.checkPing();
        
        try
        {
            //The real groupId should be set
            Q3EmlImMgr.getInstance().init( 100 );
        }
        catch( AdapterException e )
        {
            log.error( "initQ3EmlImMgr", e );
            throw new ServletException( e.getMessage() );
        }
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

        /*String dbUrl = ConfLoader.getInstance().getConf( "DB_URL", "" );
        log.debug( "The dbUrl is " + dbUrl );
        initDbApiClient( dbUrl );*/

        String ctrlUrl = ConfLoader.getInstance().getConf( "CTRL_URL", "" );
        log.debug( "The ctrlUrl is " + ctrlUrl );
        initControllerApiClient( ctrlUrl );
    }

    private void initControllerApiClient( String ctrlUrl )
    {
        com.nsb.enms.restful.controllerclient.Configuration.setDefaultApiClient(
            new com.nsb.enms.restful.controllerclient.ApiClient()
                    .setBasePath( ctrlUrl ) );
    }

    /*private void initDbApiClient( String dbUrl )
    {
        com.nsb.enms.restful.dbclient.Configuration.setDefaultApiClient(
            new com.nsb.enms.restful.dbclient.ApiClient()
                    .setBasePath( dbUrl ) );
    }*/

    private void register2Controller()
    {
        long period = ConfLoader.getInstance().getInt( "REG_PERIOD", 60000 );
        Register2ControllerUtils.register( period );
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
