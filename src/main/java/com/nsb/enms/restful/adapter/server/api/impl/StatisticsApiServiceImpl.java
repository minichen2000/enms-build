package com.nsb.enms.restful.adapter.server.api.impl;

import com.nsb.enms.restful.adapter.server.api.*;
import com.nsb.enms.restful.adapter.server.model.*;

import com.nsb.enms.restful.adapter.server.model.NESTATISTICS;

import java.util.List;
import com.nsb.enms.restful.adapter.server.api.NotFoundException;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-07-29T12:31:04.853+08:00")
public class StatisticsApiServiceImpl extends StatisticsApiService {
    @Override
    public Response statisticsNesGet(SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response statisticsSupportedNeTypesGet(SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
