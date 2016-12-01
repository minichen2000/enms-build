package com.nsb.enms.restful.adapterserver.api.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.db.mgr.AdpTpsDbMgr;
import com.nsb.enms.restful.adapterserver.api.ApiResponseMessage;
import com.nsb.enms.restful.adapterserver.api.NotFoundException;
import com.nsb.enms.restful.model.adapter.AdpTp;

public class TpsApiServiceImpl {
	private final static Logger log = LogManager.getLogger(TpsApiServiceImpl.class);

	private AdpTpsDbMgr tpsDbMgr = new AdpTpsDbMgr();

	/*
	 * @Override public Response addTps(List<AdpTp> body, SecurityContext
	 * securityContext) throws NotFoundException { return
	 * Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK,
	 * "magic!")).build(); }
	 */

	public Response getTpById(String tpid, SecurityContext securityContext) throws NotFoundException {
		AdpTp tp = new AdpTp();
		try {
			tp = tpsDbMgr.getTpById(Integer.valueOf(tpid));
		} catch (Exception e) {
			log.error("getTPById", e);
			return Response.serverError().entity(e).build();
		}
		return Response.ok().entity(tp).build();
	}

	public Response getTpsByNeId(String neid, SecurityContext securityContext) throws NotFoundException {
		System.out.println("getTPByNEId, neId = " + neid);
		List<AdpTp> tpList = new ArrayList<AdpTp>();
		try {
			tpList = tpsDbMgr.getTpsByNeId(neid);
		} catch (Exception e) {
			log.error("getTPByNEId", e);
			return Response.serverError().entity(e).build();
		}
		return Response.ok().entity(tpList).build();
	}

	public Response getTpsByLayerRate(String neid, String layerrate, SecurityContext securityContext)
			throws NotFoundException {
		List<AdpTp> tpList = new ArrayList<AdpTp>();
		try {
			tpList = tpsDbMgr.getTpsByLayerRate(neid, layerrate);
		} catch (Exception e) {
			log.error("getTPsByLayerrate", e);
			return Response.serverError().entity(e).build();
		}
		return Response.ok().entity(tpList).build();
	}

	public Response updateTp(AdpTp body, SecurityContext securityContext) throws NotFoundException {
		return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
	}

	/*
	 * @Override public Response getTps(SecurityContext securityContext) throws
	 * NotFoundException { Date begin = new Date(); List<AdpTp> tpList = new
	 * ArrayList<AdpTp>(); try { tpList = tpsDbMgr.getTps(); } catch (Exception
	 * e) { log.error("getTPs", e); return
	 * Response.serverError().entity(e).build(); }
	 * 
	 * Date end = new Date(); log.debug("adapter.getTPs cost time = " +
	 * (end.getTime() - begin.getTime()));
	 * 
	 * return Response.ok().entity(tpList).build(); }
	 */

	public Response getTpsByType(String neid, String tptype, SecurityContext securityContext) throws NotFoundException {
		Date begin = new Date();
		List<AdpTp> tpList = new ArrayList<AdpTp>();
		try {
			tpList = tpsDbMgr.getTpsByType(neid, tptype);
		} catch (Exception e) {
			log.error("getTPsByType", e);
			return Response.serverError().entity(e).build();
		}

		Date end = new Date();
		log.debug("adapter.getTPsByType cost time = " + (end.getTime() - begin.getTime()));

		return Response.ok().entity(tpList).build();
	}

	/*
	 * @Override public Response getCtpsByTpId(String neid, String ptpid,
	 * SecurityContext securityContext) throws NotFoundException { try {
	 * tpsDbMgr.getCtpsByTpId(neid, ptpid); } catch (Exception e) {
	 * log.error("getCTPsByTP", e); return
	 * Response.serverError().entity(e).build(); } return null; }
	 */

	public Response getChildrenTps(String tpid, SecurityContext securityContext) throws NotFoundException {
		List<AdpTp> tpList = new ArrayList<AdpTp>();
		try {
			tpList = tpsDbMgr.getChildrenTps(tpid);
		} catch (Exception e) {
			log.error("getChildrenTps", e);
			return Response.serverError().entity(e).build();
		}
		return Response.ok().entity(tpList).build();
	}

}
