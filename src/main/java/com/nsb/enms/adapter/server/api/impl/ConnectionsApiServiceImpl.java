package com.nsb.enms.adapter.server.api.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.api.ConnectionsApiService;
import com.nsb.enms.adapter.server.api.NotFoundException;
import com.nsb.enms.adapter.server.model.CONNECTION;
import com.nsb.enms.restful.db.client.ApiException;
import com.nsb.enms.restful.db.client.api.ConnectionsApi;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-07-29T17:16:31.406+08:00")
public class ConnectionsApiServiceImpl extends ConnectionsApiService {
	private static final Logger log = LogManager.getLogger(ConnectionsApiServiceImpl.class);
	private ConnectionsApi connectionApi = new ConnectionsApi();

	@Override
	public Response addConnection(CONNECTION body, SecurityContext securityContext) throws NotFoundException {
		com.nsb.enms.restful.db.client.model.CONNECTION connection = new com.nsb.enms.restful.db.client.model.CONNECTION();
		try {
			BeanUtils.copyProperties(connection, body);
		} catch (Exception e) {
			log.error("copyProperties", e);
			return Response.serverError().entity(e).build();
		}

		try {
			connection = connectionApi.addConnection(connection);
		} catch (ApiException e) {
			log.error("addConnection", e);
		}
		return Response.ok().entity(connection).build();
	}

	@Override
	public Response getConnectionByID(String connectionid, SecurityContext securityContext) throws NotFoundException {
		com.nsb.enms.restful.db.client.model.CONNECTION connection = new com.nsb.enms.restful.db.client.model.CONNECTION();
		try {
			connection = connectionApi.getConnectionByID(connectionid);
		} catch (ApiException e) {
			log.error("getConnectionByID", e);
			return Response.serverError().entity(e).build();
		}
		return Response.ok().entity(connection).build();
	}

	@Override
	public Response getConnectionsByLayerrate(String layerrate, SecurityContext securityContext)
			throws NotFoundException {
		List<com.nsb.enms.restful.db.client.model.CONNECTION> connections = new ArrayList<com.nsb.enms.restful.db.client.model.CONNECTION>();
		try {
			connections = connectionApi.getConnectionsByLayerrate(layerrate);
		} catch (ApiException e) {
			log.error("getConnectionsByLayerrate", e);
			return Response.serverError().entity(e).build();
		}
		return Response.ok().entity(connections).build();
	}

	@Override
	public Response connectionsGet(SecurityContext securityContext) throws NotFoundException {
		List<com.nsb.enms.restful.db.client.model.CONNECTION> connections = new ArrayList<com.nsb.enms.restful.db.client.model.CONNECTION>();
		try {
			connections = connectionApi.connectionsGet();
		} catch (ApiException e) {
			log.error("connectionsGet", e);
			return Response.serverError().entity(e).build();
		}
		return Response.ok().entity(connections).build();
	}

	@Override
	public Response getConnectionByType(String connectiontype, SecurityContext securityContext)
			throws NotFoundException {
		List<com.nsb.enms.restful.db.client.model.CONNECTION> connections = new ArrayList<com.nsb.enms.restful.db.client.model.CONNECTION>();
		try {
			connections = connectionApi.getConnectionByType(connectiontype);
		} catch (ApiException e) {
			log.error("getConnectionByType", e);
			return Response.serverError().entity(e).build();
		}
		return Response.ok().entity(connections).build();
	}

	@Override
	public Response getConnectionByUserLabel(String userlabel, SecurityContext securityContext)
			throws NotFoundException {
		com.nsb.enms.restful.db.client.model.CONNECTION connection = new com.nsb.enms.restful.db.client.model.CONNECTION();
		try {
			connection = connectionApi.getConnectionByUserLabel(userlabel);
		} catch (ApiException e) {
			log.error("getConnectionByUserLabel", e);
			return Response.serverError().entity(e).build();
		}
		return Response.ok().entity(connection).build();
	}
}
