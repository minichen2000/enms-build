package com.nsb.enms.restful.adapter.server.api.factories;

import com.nsb.enms.restful.adapter.server.api.ConnectionsApiService;
import com.nsb.enms.restful.adapter.server.api.impl.ConnectionsApiServiceImpl;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-07-29T12:31:04.853+08:00")
public class ConnectionsApiServiceFactory {
    private final static ConnectionsApiService service = new ConnectionsApiServiceImpl();

    public static ConnectionsApiService getConnectionsApi() {
        return service;
    }
}
