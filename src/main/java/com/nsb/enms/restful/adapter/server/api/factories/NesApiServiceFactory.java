package com.nsb.enms.restful.adapter.server.api.factories;

import com.nsb.enms.restful.adapter.server.api.NesApiService;
import com.nsb.enms.restful.adapter.server.api.impl.NesApiServiceImpl;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-07-27T16:22:16.601+08:00")
public class NesApiServiceFactory {
    private final static NesApiService service = new NesApiServiceImpl();

    public static NesApiService getNesApi() {
        return service;
    }
}
