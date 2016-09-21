package com.nsb.enms.restful.msgserver.api.impl;

import com.nsb.enms.restful.msgserver.api.*;
import com.nsb.enms.restful.model.common.*;

import com.nsb.enms.restful.model.common.Alarm;
import com.nsb.enms.restful.model.common.Message;

import java.util.List;
import com.nsb.enms.restful.msgserver.api.NotFoundException;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-09-21T13:47:47.162+08:00")
public class EnmsApiServiceImpl extends EnmsApiService {
    @Override
    public Response getAlarmsByNe(String neid, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response getAlarmsBySeverity(String severity, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response getAllAlarms(SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response getAllNotifications(SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
