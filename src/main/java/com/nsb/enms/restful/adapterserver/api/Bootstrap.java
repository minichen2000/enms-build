package com.nsb.enms.restful.adapterserver.api;

import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.business.ping.PingApp;
import com.nsb.enms.adapter.server.business.register.RegisterManager;
import com.nsb.enms.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.manager.Q3EmlImMgr;
import com.nsb.enms.adapter.server.notification.NotificationClient;
import com.nsb.enms.adapter.server.notification.NotificationSender;
import com.nsb.enms.adapter.server.statemachine.ne.NeStateMachineApp;

import io.swagger.jaxrs.config.SwaggerContextService;
import io.swagger.models.Contact;
import io.swagger.models.Info;
import io.swagger.models.License;
import io.swagger.models.Swagger;

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

        register2Controller();

        String q3WSServerUri = ConfLoader.getInstance()
                .getConf( "Q3_WS_SERVER_URI", "" );
        NotificationClient client = new NotificationClient( q3WSServerUri );
        client.start();
        
        /*int adapterWSServerPort = ConfLoader.getInstance()
                .getInt( "ADP_WS_SERVER_PORT", 7778 );
        NotificationServer server = new NotificationServer( adapterWSServerPort );
        server.start();*/
        
        try
        {
            //The real groupId should be set
            Q3EmlImMgr.instance().init( 100 );
        }
        catch( AdapterException e )
        {
            e.printStackTrace();
        }
        
        NeStateMachineApp.instance().init();
//        NotificationSender.instance().init();
        
//        PingApp pingApp = new PingApp();
//        pingApp.checkPing();
        
        Swagger swagger = new Swagger().info( info );
        new SwaggerContextService().withServletConfig( config )
                .updateSwagger( swagger );
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

    private void register2Controller()
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
                log.debug( "the result of registering to Controller is :{}",
                    isOk );
                if( isOk )
                {
                    timer.cancel();
                }
            }
        }, 0, period );
    }    
}
