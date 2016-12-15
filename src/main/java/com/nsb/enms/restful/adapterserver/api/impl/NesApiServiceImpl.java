package com.nsb.enms.restful.adapterserver.api.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.api.factories.AdpNeFactory;
import com.nsb.enms.adapter.server.common.db.mgr.AdpEqusDbMgr;
import com.nsb.enms.adapter.server.common.db.mgr.AdpNesDbMgr;
import com.nsb.enms.adapter.server.common.db.mgr.AdpTpsDbMgr;
import com.nsb.enms.adapter.server.common.db.mgr.AdpXcsDbMgr;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.utils.ErrorWrapperUtils;
import com.nsb.enms.adapter.server.sdh.business.xc.AdpXcsMgr;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.restful.adapterserver.api.NesApiService;
import com.nsb.enms.restful.adapterserver.api.NotFoundException;
import com.nsb.enms.restful.model.adapter.AdpEquipment;
import com.nsb.enms.restful.model.adapter.AdpNe;
import com.nsb.enms.restful.model.adapter.AdpTp;
import com.nsb.enms.restful.model.adapter.AdpXc;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-07-29T17:16:31.406+08:00")
public class NesApiServiceImpl extends NesApiService {
	private final static Logger log = LogManager.getLogger(NesApiServiceImpl.class);

	private AdpNesDbMgr nesDbMgr = new AdpNesDbMgr();
	private AdpTpsDbMgr tpsDbMgr = new AdpTpsDbMgr();
	private AdpEqusDbMgr equsDbMgr = new AdpEqusDbMgr();
	private AdpXcsDbMgr xcsDbMgr = new AdpXcsDbMgr();
	private AdpXcsMgr adpXcMgr = new AdpXcsMgr();

	@Override
	public Response addNe(AdpNe body, SecurityContext securityContext) throws NotFoundException {
		try {
			AdpNe ne = AdpNeFactory.getInstance().getApi(body).addNe();
			return Response.ok().entity(ne).build();
		} catch (AdapterException e) {
			log.error("addNe", e);
			return ErrorWrapperUtils.adapterException(e);
		}
	}

	private Response failDbOperation() {
		return ErrorWrapperUtils.failDbOperation();
	}

	private Response constructErrorInfo(ErrorCode errorCode) {
		return ErrorWrapperUtils.constructErrorInfo(errorCode);
	}

	@Override
	public Response deleteNe(Integer neid, SecurityContext securityContext) throws NotFoundException {
		log.debug("adapter------deleteNE");

		try {
			AdpNeFactory.getInstance().getApi(neid).delete();
			return Response.ok().build();
		} catch (AdapterException e) {
			log.error("deleteNe", e);
			return ErrorWrapperUtils.adapterException(e);
		}
	}

	private Response failObjNotExist() {
		return constructErrorInfo(ErrorCode.FAIL_OBJ_NOT_EXIST);
	}

	@Override
	public Response getNeById(Integer neid, SecurityContext securityContext) throws NotFoundException {
		try {
			AdpNe ne = AdpNeFactory.getInstance().getApi(neid).getNeById();
			return Response.ok().entity(ne).build();
		} catch (AdapterException e) {
			log.error("getNeById", e);
			return ErrorWrapperUtils.adapterException(e);
		}
	}

	@Override
	public Response updateNe(AdpNe body, SecurityContext securityContext) throws NotFoundException {
		return null;
	}

	@Override
	public Response getNes(SecurityContext securityContext) throws NotFoundException {
		log.debug("adapter-------nesGet");
		List<AdpNe> nes = new ArrayList<AdpNe>();
		try {
			nes = nesDbMgr.getNes();
		} catch (Exception e) {
			log.error("nesGet", e);
			return failDbOperation();
		}
		return Response.ok().entity(nes).build();
	}

	@Override
	public Response addTps(Integer neid, List<AdpTp> tps, SecurityContext securityContext) throws NotFoundException {
		return null;
	}

	@Override
	public Response getChildrenTps(Integer neid, Integer tpid, SecurityContext securityContext)
			throws NotFoundException {
		List<AdpTp> tpList = new ArrayList<AdpTp>();

		try {
			tpList = AdpNeFactory.getInstance().getApi(neid).getChildrenTps(tpid);
			return Response.ok().entity(tpList).build();
		} catch (AdapterException e) {
			log.error("getChildrenTps", e);
			return ErrorWrapperUtils.adapterException(e);
		}
	}

	@Override
	public Response getNeTps(Integer neid, SecurityContext securityContext) throws NotFoundException {
		log.debug("getNeTps, neId = " + neid);
		List<AdpTp> tpList = new ArrayList<AdpTp>();
		try {
			tpList = AdpNeFactory.getInstance().getApi(neid).getNeTps();
			return Response.ok().entity(tpList).build();
		} catch (AdapterException e) {
			log.error("getNeTps", e);
			return ErrorWrapperUtils.adapterException(e);
		}
	}

	@Override
	public Response getTpById(Integer neid, Integer tpid, SecurityContext securityContext) throws NotFoundException {
		log.debug("getTpById, neId = " + neid);
		AdpTp tp;
		try {
			tp = tpsDbMgr.getTpById(neid, tpid);
		} catch (Exception e) {
			log.error("getTPByNEId", e);
			return Response.serverError().entity(e).build();
		}
		return Response.ok().entity(tp).build();
	}

	@Override
	public Response getTpsByLayerRate(Integer neid, String layerrate, SecurityContext securityContext)
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

	@Override
	public Response getTpsByType(Integer neid, String tptype, SecurityContext securityContext)
			throws NotFoundException {
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

	@Override
	public Response updateTp(Integer neid, AdpTp tp, SecurityContext securityContext) throws NotFoundException {
		return null;
	}

	@Override
	public Response deleteXc(Integer neid, Integer xcid, SecurityContext securityContext) throws NotFoundException {
		try {
			adpXcMgr.deleteXcById(neid, xcid);
		} catch (AdapterException e) {
			log.error("deleteXC", e);
			return ErrorWrapperUtils.adapterException(e);
		}
		return Response.ok().build();
	}

	@Override
	public Response getEquipmentById(Integer neid, Integer eqid, SecurityContext securityContext)
			throws NotFoundException {
		AdpEquipment equipment = null;
		try {
			equipment = equsDbMgr.getEquipmentById(neid, eqid);
		} catch (Exception e) {
			log.error("getEquipmentById", e);
			return Response.serverError().entity(e).build();
		}
		return Response.ok().entity(equipment).build();
	}

	@Override
	public Response getXcById(Integer neid, Integer xcid, SecurityContext securityContext) throws NotFoundException {
		AdpXc xc = new AdpXc();
		try {
			xc = xcsDbMgr.getXcById(neid, xcid);
		} catch (Exception e) {
			log.error("getXCById", e);
		}
		return Response.ok().entity(xc).build();
	}

	@Override
	public Response startAlignment(Integer neid, SecurityContext securityContext) throws NotFoundException {
		try {
			AdpNeFactory.getInstance().getApi(neid).synchronize();
			return Response.ok().build();
		} catch (AdapterException e) {
			log.error("startAlignment", e);
			return ErrorWrapperUtils.adapterException(e);
		}
	}

	@Override
	public Response startSupervision(Integer neid, SecurityContext securityContext) throws NotFoundException {
		try {
			AdpNeFactory.getInstance().getApi(neid).supervise();
			return Response.ok().build();
		} catch (AdapterException e) {
			log.error("startSupervision", e);
			return ErrorWrapperUtils.adapterException(e);
		}
	}

	@Override
	public Response createXc(Integer neid, AdpXc xc, SecurityContext securityContext) throws NotFoundException {
		AdpXc newXc = null;
		try {
			newXc = adpXcMgr.createXc(neid, xc);
		} catch (AdapterException e) {
			log.error("createXc", e);
			return ErrorWrapperUtils.adapterException(e);
		}
		return Response.ok().entity(newXc).build();
	}

	@Override
	public Response deleteXcsByNeId(Integer neid, SecurityContext securityContext) throws NotFoundException {
		try {
			adpXcMgr.deleteXcsByNeId(neid);
		} catch (AdapterException e) {
			log.error("deleteXcsByNeId", e);
			return ErrorWrapperUtils.adapterException(e);
		}
		return Response.ok().build();
	}

	@Override
	public Response getEquipmentsByNeId(Integer neid, SecurityContext securityContext) throws NotFoundException {
		List<AdpEquipment> equipments = new ArrayList<AdpEquipment>();
		try {
			equipments = equsDbMgr.getEquipmentsByNeId(neid);
			if (equipments.isEmpty()) {
				return failObjNotExist();
			}
		} catch (Exception e) {
			log.error("getEquipmentsByNeId", e);
			return failDbOperation();
		}
		return Response.ok().entity(equipments).build();
	}

	@Override
	public Response getXcsByNeId(Integer neid, SecurityContext securityContext) throws NotFoundException {
		List<AdpXc> xcs = null;
		try {
			xcs = xcsDbMgr.getXcsByNeId(neid);
		} catch (Exception e) {
			log.error("getXcsByNeId", e);
			return ErrorWrapperUtils.failDbOperation();
		}
		return Response.ok().entity(xcs).build();
	}

	@Override
	public Response addEquipment(Integer neid, AdpEquipment equipment, SecurityContext securityContext)
			throws NotFoundException {
		return null;
	}
}
