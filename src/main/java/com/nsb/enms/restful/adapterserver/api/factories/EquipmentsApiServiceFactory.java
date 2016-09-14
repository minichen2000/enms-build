package com.nsb.enms.restful.adapterserver.api.factories;

import com.nsb.enms.restful.adapterserver.api.EquipmentsApiService;
import com.nsb.enms.restful.adapterserver.api.impl.EquipmentsApiServiceImpl;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-07-29T17:16:31.406+08:00")
public class EquipmentsApiServiceFactory {
    private final static EquipmentsApiService service = new EquipmentsApiServiceImpl();

    public static EquipmentsApiService getEquipmentsApi() {
        return service;
    }
}
