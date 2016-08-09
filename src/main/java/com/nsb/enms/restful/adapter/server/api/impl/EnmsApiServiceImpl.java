package com.nsb.enms.restful.adapter.server.api.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.nsb.enms.restful.adapter.server.api.ApiResponseMessage;
import com.nsb.enms.restful.adapter.server.api.EnmsApiService;
import com.nsb.enms.restful.adapter.server.api.NotFoundException;
import com.nsb.enms.restful.adapter.server.model.NE;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-08-09T15:45:26.159+08:00")
public class EnmsApiServiceImpl extends EnmsApiService {
	@Override
	public Response assignNE(NE body, SecurityContext securityContext) throws NotFoundException {
		// do some magic!
		return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
	}

	@Override
	public Response checkHeartbeat(SecurityContext securityContext) throws NotFoundException {
		// do some magic!
		return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
	}

	@Override
	public Response getAssignedNEs(SecurityContext securityContext) throws NotFoundException {
		// do some magic!
		return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
	}

	@Override
	public Response unAssignNE(NE body, SecurityContext securityContext) throws NotFoundException {
		// do some magic!
		return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
	}
}
