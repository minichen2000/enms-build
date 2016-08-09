package com.nsb.enms.restful.adapter.server.api.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.nsb.enms.restful.adapter.server.api.ApiResponseMessage;
import com.nsb.enms.restful.adapter.server.api.ConnectionsApiService;
import com.nsb.enms.restful.adapter.server.api.NotFoundException;
import com.nsb.enms.restful.adapter.server.model.CONNECTION;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-07-29T17:16:31.406+08:00")
public class ConnectionsApiServiceImpl extends ConnectionsApiService {
	@Override
	public Response addConnection(CONNECTION body, SecurityContext securityContext) throws NotFoundException {
		// do some magic!
		return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
	}

	@Override
	public Response getConnectionByID(String connectionid, SecurityContext securityContext) throws NotFoundException {
		// do some magic!
		return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
	}

	@Override
	public Response getConnectionsByLayerrate(String layerrate, SecurityContext securityContext)
			throws NotFoundException {
		// do some magic!
		return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
	}

	@Override
	public Response connectionsGet(SecurityContext securityContext) throws NotFoundException {
		return null;
	}

	@Override
	public Response getConnectionByType(String connectiontype, SecurityContext securityContext)
			throws NotFoundException {
		return null;
	}

	@Override
	public Response getConnectionByUserLabel(String userlabel, SecurityContext securityContext)
			throws NotFoundException {
		return null;
	}
}
