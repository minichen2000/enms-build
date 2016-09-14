package com.nsb.enms.restful.adapter.server.api.factories;

import com.nsb.enms.restful.adapter.server.api.XcsApiService;
import com.nsb.enms.restful.adapter.server.api.impl.XcsApiServiceImpl;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-08-31T16:19:02.183+08:00")
public class XcsApiServiceFactory {
    private final static XcsApiService service = new XcsApiServiceImpl();

    public static XcsApiService getXcsApi() {
        return service;
    }
}
