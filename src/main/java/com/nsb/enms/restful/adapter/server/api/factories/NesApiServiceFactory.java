package com.nsb.enms.restful.adapter.server.api.factories;

import com.nsb.enms.restful.adapter.server.api.NesApiService;
import com.nsb.enms.restful.adapter.server.api.impl.NesApiServiceImpl;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-07-29T12:31:04.853+08:00")
public class NesApiServiceFactory {
    private final static NesApiService service = new NesApiServiceImpl();

    public static NesApiService getNesApi() {
        return service;
    }
}
