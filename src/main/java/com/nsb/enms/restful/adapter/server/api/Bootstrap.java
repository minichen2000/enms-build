package com.nsb.enms.restful.adapter.server.api;

import java.io.File;
import java.io.FileInputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import com.nsb.enms.restful.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.restful.adapter.server.common.exception.AdapterException;
import com.nsb.enms.restful.db.client.ApiClient;
import com.nsb.enms.restful.db.client.Configuration;

import io.swagger.jaxrs.config.SwaggerContextService;
import io.swagger.models.Contact;
import io.swagger.models.Info;
import io.swagger.models.License;
import io.swagger.models.Swagger;

public class Bootstrap extends HttpServlet
{
    private static final long serialVersionUID = 6213433092343311611L;

    @Override
    public void init( ServletConfig config ) throws ServletException
    {
        Info info = new Info().title( "Swagger Server" )
                .description( "ENMS API.  Adapter RESTful interface. " )
                .termsOfService( "" ).contact( new Contact().email( "" ) )
                .license( new License().name( "" ).url( "" ) );

        ServletContext context = config.getServletContext();
        loadConf( context );

        Swagger swagger = new Swagger().info( info );

        new SwaggerContextService().withServletConfig( config )
                .updateSwagger( swagger );
    }

    private void loadConf( ServletContext context )
    {
        String confPath = context.getRealPath( "/WEB-INF/conf" );
        try
        {
            ConfLoader.getInstance().loadConf( confPath + "/conf.properties" );
        }
        catch( AdapterException e )
        {
            e.printStackTrace();
        }

        ConfigurationSource source;
        try
        {
            File config = new File( confPath + "/log4j2.xml" );
            source = new ConfigurationSource( new FileInputStream( config ),
                    config );
            Configurator.initialize( null, source );
            Logger log = LogManager.getLogger( Bootstrap.class );
            log.debug( "The confPath is " + confPath );
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }

        String dbUrl = ConfLoader.getInstance().getConf( "DB_URL", "" );
        initDbApiClient( dbUrl );

        String ctrlUrl = ConfLoader.getInstance().getConf( "CTRL_URL", "" );
        initControllerApiClient( ctrlUrl );
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
}
