package com.nsb.enms.restful.adapterserver.api.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.restful.adapterserver.api.ConnectionsApiService;
import com.nsb.enms.restful.adapterserver.api.NotFoundException;
import com.nsb.enms.restful.dbclient.ApiException;
import com.nsb.enms.restful.dbclient.api.DbConnectionsApi;
import com.nsb.enms.restful.model.Connection;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-07-29T17:16:31.406+08:00")
public class ConnectionsApiServiceImpl extends ConnectionsApiService {
	private static final Logger log = LogManager.getLogger(ConnectionsApiServiceImpl.class);
	private DbConnectionsApi dbConnectionApi = new DbConnectionsApi();

	@Override
	public Response addConnection(Connection body, SecurityContext securityContext) throws NotFoundException {
		Connection connection = new Connection();
		try {
			BeanUtils.copyProperties(connection, body);
		} catch (Exception e) {
			log.error("copyProperties", e);
			return Response.serverError().entity(e).build();
		}

		try {
			connection = dbConnectionApi.addConnection(connection);
		} catch (ApiException e) {
			log.error("addConnection", e);
		}
		return Response.ok().entity(connection).build();
	}

	@Override
	public Response getConnectionById(String connectionid, SecurityContext securityContext) throws NotFoundException {
		Connection connection = new Connection();
		try {
			connection = dbConnectionApi.getConnectionById(connectionid);
		} catch (ApiException e) {
			log.error("getConnectionByID", e);
			return Response.serverError().entity(e).build();
		}
		return Response.ok().entity(connection).build();
	}

	@Override
	public Response getConnectionsByLayerRate(String layerrate, SecurityContext securityContext)
			throws NotFoundException {
		List<Connection> connections = new ArrayList<Connection>();
		try {
			connections = dbConnectionApi.getConnectionsByLayerRate(layerrate);
		} catch (ApiException e) {
			log.error("getConnectionsByLayerrate", e);
			return Response.serverError().entity(e).build();
		}
		return Response.ok().entity(connections).build();
	}

	@Override
	public Response getConnections(SecurityContext securityContext) throws NotFoundException {
		List<Connection> connections = new ArrayList<Connection>();
		try {
			connections = dbConnectionApi.getConnections();
		} catch (ApiException e) {
			log.error("connectionsGet", e);
			return Response.serverError().entity(e).build();
		}
		return Response.ok().entity(connections).build();
	}

	@Override
	public Response getConnectionsByType(String connectiontype, SecurityContext securityContext)
			throws NotFoundException {
		List<Connection> connections = new ArrayList<Connection>();
		try {
			connections = dbConnectionApi.getConnectionsByType(connectiontype);
		} catch (ApiException e) {
			log.error("getConnectionByType", e);
			return Response.serverError().entity(e).build();
		}
		return Response.ok().entity(connections).build();
	}

	@Override
	public Response getConnectionByUserLabel(String userlabel, SecurityContext securityContext)
			throws NotFoundException {
		Connection connection = new Connection();
		try {
			connection = dbConnectionApi.getConnectionByUserLabel(userlabel);
		} catch (ApiException e) {
			log.error("getConnectionByUserLabel", e);
			return Response.serverError().entity(e).build();
		}
		return Response.ok().entity(connection).build();
	}
}
