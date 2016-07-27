package com.nsb.enms.restful.adapter.server.api.factories;

import com.nsb.enms.restful.adapter.server.api.TpsApiService;
import com.nsb.enms.restful.adapter.server.api.impl.TpsApiServiceImpl;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-07-27T16:22:16.601+08:00")
public class TpsApiServiceFactory {
    private final static TpsApiService service = new TpsApiServiceImpl();

    public static TpsApiService getTpsApi() {
        return service;
    }
}
