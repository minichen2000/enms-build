package com.nsb.enms.restful.adapterserver.api.factories;

import com.nsb.enms.adapter.server.api.ConnectionsApiService;
import com.nsb.enms.restful.adapterserver.api.impl.ConnectionsApiServiceImpl;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-07-29T17:16:31.406+08:00")
public class ConnectionsApiServiceFactory {
    private final static ConnectionsApiService service = new ConnectionsApiServiceImpl();

    public static ConnectionsApiService getConnectionsApi() {
        return service;
    }
}