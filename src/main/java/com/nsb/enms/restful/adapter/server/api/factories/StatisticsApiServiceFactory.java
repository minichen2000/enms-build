package com.nsb.enms.restful.adapter.server.api.factories;

import com.nsb.enms.restful.adapter.server.api.StatisticsApiService;
import com.nsb.enms.restful.adapter.server.api.impl.StatisticsApiServiceImpl;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-07-27T16:22:16.601+08:00")
public class StatisticsApiServiceFactory {
    private final static StatisticsApiService service = new StatisticsApiServiceImpl();

    public static StatisticsApiService getStatisticsApi() {
        return service;
    }
}
