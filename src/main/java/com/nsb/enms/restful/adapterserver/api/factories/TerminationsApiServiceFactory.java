package com.nsb.enms.restful.adapterserver.api.factories;

import com.nsb.enms.restful.adapterserver.api.TerminationsApiService;
import com.nsb.enms.restful.adapterserver.api.impl.TerminationsApiServiceImpl;

public class TerminationsApiServiceFactory {
    private final static TerminationsApiService service = new TerminationsApiServiceImpl();

    public static TerminationsApiService getTerminationsApi() {
        return service;
    }
}
