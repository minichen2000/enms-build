package com.nsb.enms.restful.adapter.server.api.factories;

import com.nsb.enms.restful.adapter.server.api.EquipmentsApiService;
import com.nsb.enms.restful.adapter.server.api.impl.EquipmentsApiServiceImpl;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-07-27T16:22:16.601+08:00")
public class EquipmentsApiServiceFactory {
    private final static EquipmentsApiService service = new EquipmentsApiServiceImpl();

    public static EquipmentsApiService getEquipmentsApi() {
        return service;
    }
}