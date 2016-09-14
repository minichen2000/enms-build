package com.nsb.enms.adapter.server.api.factories;

import com.nsb.enms.adapter.server.api.TerminationsApiService;
import com.nsb.enms.adapter.server.api.impl.TerminationsApiServiceImpl;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-08-31T16:19:02.183+08:00")
public class TerminationsApiServiceFactory {
    private final static TerminationsApiService service = new TerminationsApiServiceImpl();

    public static TerminationsApiService getTerminationsApi() {
        return service;
    }
}
