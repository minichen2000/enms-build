package com.nsb.enms.adapter.server.api.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.api.ApiResponseMessage;
import com.nsb.enms.adapter.server.api.NotFoundException;
import com.nsb.enms.adapter.server.api.TpsApiService;
import com.nsb.enms.adapter.server.model.TP;
import com.nsb.enms.restful.db.client.ApiException;
import com.nsb.enms.restful.db.client.api.TpsApi;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-07-29T17:16:31.406+08:00")
public class TpsApiServiceImpl extends TpsApiService {
	private final static Logger log = LogManager.getLogger(TpsApiServiceImpl.class);

	private TpsApi tpsApi = new TpsApi();

	@Override
	public Response addTPs(List<TP> body, SecurityContext securityContext) throws NotFoundException {
		return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
	}

	@Override
	public Response getTPById(String tpid, SecurityContext securityContext) throws NotFoundException {
		com.nsb.enms.restful.db.client.model.TP tp = new com.nsb.enms.restful.db.client.model.TP();
		try {
			tp = tpsApi.getTPById(tpid);
		} catch (ApiException e) {
			log.error("getTPById", e);
			return Response.serverError().entity(e).build();
		}
		return Response.ok().entity(tp).build();
	}

	@Override
	public Response getTPByNEId(String neid, SecurityContext securityContext) throws NotFoundException {
		System.out.println("getTPByNEId, neId = " + neid);
		List<com.nsb.enms.restful.db.client.model.TP> tpList = new ArrayList<com.nsb.enms.restful.db.client.model.TP>();
		try {
			tpList = tpsApi.getTPByNEId(neid);
		} catch (ApiException e) {
			log.error("getTPByNEId", e);
			return Response.serverError().entity(e).build();
		}
		return Response.ok().entity(tpList).build();
	}

	@Override
	public Response getTPsByLayerrate(String neid, String layerrate, SecurityContext securityContext)
			throws NotFoundException {
		List<com.nsb.enms.restful.db.client.model.TP> tpList = new ArrayList<com.nsb.enms.restful.db.client.model.TP>();
		try {
			tpList = tpsApi.getTPsByLayerrate(neid, layerrate);
		} catch (ApiException e) {
			log.error("getTPsByLayerrate", e);
			return Response.serverError().entity(e).build();
		}
		return Response.ok().entity(tpList).build();
	}

	@Override
	public Response updateTP(TP body, SecurityContext securityContext) throws NotFoundException {
		return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
	}

	@Override
	public Response getTPs(SecurityContext securityContext) throws NotFoundException {
		Date begin = new Date();
		List<com.nsb.enms.restful.db.client.model.TP> tpList = new ArrayList<com.nsb.enms.restful.db.client.model.TP>();
		try {
			tpList = tpsApi.getTPs();
		} catch (ApiException e) {
			log.error("getTPs", e);
			return Response.serverError().entity(e).build();
		}

		Date end = new Date();
		log.debug("adapter.getTPs cost time = " + (end.getTime() - begin.getTime()));

		return Response.ok().entity(tpList).build();
	}

	@Override
	public Response getTPsByType(String tptype, SecurityContext securityContext) throws NotFoundException {
		Date begin = new Date();
		List<com.nsb.enms.restful.db.client.model.TP> tpList = new ArrayList<com.nsb.enms.restful.db.client.model.TP>();
		try {
			tpList = tpsApi.getTPsByType(tptype);
		} catch (ApiException e) {
			log.error("getTPsByType", e);
			return Response.serverError().entity(e).build();
		}

		Date end = new Date();
		log.debug("adapter.getTPsByType cost time = " + (end.getTime() - begin.getTime()));

		return Response.ok().entity(tpList).build();
	}

	@Override
	public Response getCTPsByTP(String neid, String ptpid, SecurityContext securityContext) throws NotFoundException {
		try {
			tpsApi.getCTPsByTP(neid, ptpid);
		} catch (ApiException e) {
			log.error("getCTPsByTP", e);
			return Response.serverError().entity(e).build();
		}
		return null;
	}

	@Override
	public Response getChildrenTPs(String tpid, SecurityContext securityContext) throws NotFoundException {
		return null;
	}
}
