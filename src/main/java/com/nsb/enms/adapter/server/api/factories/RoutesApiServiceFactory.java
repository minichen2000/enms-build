package com.nsb.enms.adapter.server.api.factories;

import com.nsb.enms.adapter.server.api.RoutesApiService;
import com.nsb.enms.adapter.server.api.impl.RoutesApiServiceImpl;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-07-29T17:16:31.406+08:00")
public class RoutesApiServiceFactory {
    private final static RoutesApiService service = new RoutesApiServiceImpl();

    public static RoutesApiService getRoutesApi() {
        return service;
    }
}
