package com.nsb.enms.restful.adapterserver.api.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.db.mgr.ConnectionsDbMgr;
import com.nsb.enms.restful.adapterserver.api.ConnectionsApiService;
import com.nsb.enms.restful.adapterserver.api.NotFoundException;
import com.nsb.enms.restful.model.adapter.Connection;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-07-29T17:16:31.406+08:00")
public class ConnectionsApiServiceImpl extends ConnectionsApiService {
	private static final Logger log = LogManager.getLogger(ConnectionsApiServiceImpl.class);
	private ConnectionsDbMgr connectionsDbMgr = new ConnectionsDbMgr();

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
			connection = connectionsDbMgr.addConnection(connection);
		} catch (Exception e) {
			log.error("addConnection", e);
		}
		return Response.ok().entity(connection).build();
	}

	@Override
	public Response getConnectionById(String connectionid, SecurityContext securityContext) throws NotFoundException {
		Connection connection = new Connection();
		try {
			connection = connectionsDbMgr.getConnectionById(connectionid);
		} catch (Exception e) {
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
			connections = connectionsDbMgr.getConnectionsByLayerRate(layerrate);
		} catch (Exception e) {
			log.error("getConnectionsByLayerrate", e);
			return Response.serverError().entity(e).build();
		}
		return Response.ok().entity(connections).build();
	}

	@Override
	public Response getConnections(SecurityContext securityContext) throws NotFoundException {
		List<Connection> connections = new ArrayList<Connection>();
		try {
			connections = connectionsDbMgr.getConnections();
		} catch (Exception e) {
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
			connections = connectionsDbMgr.getConnectionsByType(connectiontype);
		} catch (Exception e) {
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
			connection = connectionsDbMgr.getConnectionByUserLabel(userlabel);
		} catch (Exception e) {
			log.error("getConnectionByUserLabel", e);
			return Response.serverError().entity(e).build();
		}
		return Response.ok().entity(connection).build();
	}
}
