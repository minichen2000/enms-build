package com.nsb.enms.restful.msgserver.api.factories;

import com.nsb.enms.restful.msgserver.api.EnmsApiService;
import com.nsb.enms.restful.msgserver.api.impl.EnmsApiServiceImpl;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-09-21T13:47:47.162+08:00")
public class EnmsApiServiceFactory {
    private final static EnmsApiService service = new EnmsApiServiceImpl();

    public static EnmsApiService getEnmsApi() {
        return service;
    }
}
