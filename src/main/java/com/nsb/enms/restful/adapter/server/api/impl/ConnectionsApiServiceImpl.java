package com.nsb.enms.restful.adapter.server.api.impl;

import com.nsb.enms.restful.adapter.server.api.*;
import com.nsb.enms.restful.adapter.server.model.*;

import com.nsb.enms.restful.adapter.server.model.CONNECTION;

import java.util.List;
import com.nsb.enms.restful.adapter.server.api.NotFoundException;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-07-29T17:16:31.406+08:00")
public class ConnectionsApiServiceImpl extends ConnectionsApiService {
    @Override
    public Response addConnection(CONNECTION body, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response connectionsGet(String userlabel, String connectiontype, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response getConnectionByID(String connectionid, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response getConnectionsByLayerrate(String layerrate, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
