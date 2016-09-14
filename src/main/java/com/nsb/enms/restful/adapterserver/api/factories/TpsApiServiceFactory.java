package com.nsb.enms.restful.adapterserver.api.factories;

import com.nsb.enms.restful.adapterserver.api.TpsApiService;
import com.nsb.enms.restful.adapterserver.api.impl.TpsApiServiceImpl;

public class TpsApiServiceFactory {
    private final static TpsApiService service = new TpsApiServiceImpl();

    public static TpsApiService getTpsApi() {
        return service;
    }
}
