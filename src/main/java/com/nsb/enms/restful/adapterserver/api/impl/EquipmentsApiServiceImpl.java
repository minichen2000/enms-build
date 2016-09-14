package com.nsb.enms.restful.adapterserver.api.impl;

import java.util.List;
import java.io.InputStream;


import com.nsb.enms.adapter.server.api.*;
import com.nsb.enms.restful.adapterserver.api.ApiResponseMessage;
import com.nsb.enms.restful.adapterserver.api.EquipmentsApiService;
import com.nsb.enms.restful.adapterserver.api.NotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-07-29T17:16:31.406+08:00")
public class EquipmentsApiServiceImpl extends EquipmentsApiService {
    @Override
    public Response getEquipmentById( String arg0, SecurityContext arg1 )
            throws NotFoundException
    {
     // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response getEquipmentsByNeId( String arg0, SecurityContext arg1 )
            throws NotFoundException
    {
     // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
