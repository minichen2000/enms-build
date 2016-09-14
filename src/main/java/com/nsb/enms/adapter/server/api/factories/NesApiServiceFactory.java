package com.nsb.enms.adapter.server.api.factories;

import com.nsb.enms.adapter.server.api.NesApiService;
import com.nsb.enms.adapter.server.api.impl.NesApiServiceImpl;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-07-29T17:16:31.406+08:00")
public class NesApiServiceFactory {
    private final static NesApiService service = new NesApiServiceImpl();

    public static NesApiService getNesApi() {
        return service;
    }
}
