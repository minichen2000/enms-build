package com.nsb.enms.restful.init;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;

public class ResourceApplication extends ResourceConfig
{
    public ResourceApplication()
    {
        packages( "com.nsb.enms.restful" );
        // register( LoggingFeature.class );
        register( LoggingFilter.class );
    }
}
