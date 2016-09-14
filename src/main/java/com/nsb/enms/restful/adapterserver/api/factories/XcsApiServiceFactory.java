package com.nsb.enms.restful.adapterserver.api.factories;

import com.nsb.enms.restful.adapterserver.api.XcsApiService;
import com.nsb.enms.restful.adapterserver.api.impl.XcsApiServiceImpl;

public class XcsApiServiceFactory {
    private final static XcsApiService service = new XcsApiServiceImpl();

    public static XcsApiService getXcsApi() {
        return service;
    }
}
