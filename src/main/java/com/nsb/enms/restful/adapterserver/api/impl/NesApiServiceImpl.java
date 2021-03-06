package com.nsb.enms.restful.adapterserver.api.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.api.factories.AdpFactory;
import com.nsb.enms.adapter.server.common.db.mgr.AdpEqusDbMgr;
import com.nsb.enms.adapter.server.common.db.mgr.AdpNesDbMgr;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.utils.ErrorWrapperUtils;
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
	private AdpEqusDbMgr equsDbMgr = new AdpEqusDbMgr();

	@Override
	public Response addNe(AdpNe body, SecurityContext securityContext) throws NotFoundException {
		try {
			AdpNe ne = AdpFactory.getInstance().getNeApi(body).addNe();
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
	public Response deleteNe(String neid, SecurityContext securityContext) throws NotFoundException {
		log.debug("adapter------deleteNE");

		try {
			AdpFactory.getInstance().getNeApi(neid).deleteNe();
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
	public Response getNeById(String neid, SecurityContext securityContext) throws NotFoundException {
		try {
			AdpNe ne = AdpFactory.getInstance().getNeApi(neid).getNeById();
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
	public Response startAlignment(String neid, SecurityContext securityContext) throws NotFoundException {
		try {
			AdpFactory.getInstance().getNeApi(neid).synchronize();
			return Response.ok().build();
		} catch (AdapterException e) {
			log.error("startAlignment", e);
			return ErrorWrapperUtils.adapterException(e);
		}
	}

	@Override
	public Response addTps(String neid, List<AdpTp> tps, SecurityContext securityContext) throws NotFoundException {
		return null;
	}

	@Override
	public Response getChildrenTps(String neid, String tpid, SecurityContext securityContext)
			throws NotFoundException {
		try {
			List<AdpTp> tpList = AdpFactory.getInstance().getTpApi(neid).getChildrenTps(tpid);
			return Response.ok().entity(tpList).build();
		} catch (AdapterException e) {
			log.error("getChildrenTps", e);
			return ErrorWrapperUtils.adapterException(e);
		}
	}

	@Override
	public Response getNeTps(String neid, SecurityContext securityContext) throws NotFoundException {
		log.debug("getNeTps, neId = " + neid);
		try {
			List<AdpTp> tpList = AdpFactory.getInstance().getTpApi(neid).getNeTps();
			return Response.ok().entity(tpList).build();
		} catch (AdapterException e) {
			log.error("getNeTps", e);
			return ErrorWrapperUtils.adapterException(e);
		}
	}

	@Override
	public Response getTpById(String neid, String tpid, SecurityContext securityContext) throws NotFoundException {
		log.debug("getTpById, neId = " + neid);
		try {
			AdpTp tp = AdpFactory.getInstance().getTpApi(neid).getTpById(tpid);
			return Response.ok().entity(tp).build();
		} catch (AdapterException e) {
			log.error("getTpById", e);
			return ErrorWrapperUtils.adapterException(e);
		}
	}

	@Override
	public Response getTpsByLayerRate(String neid, String layerrate, SecurityContext securityContext)
			throws NotFoundException {
		try {
			List<AdpTp> tpList = AdpFactory.getInstance().getTpApi(neid).getTpsByLayerrate(layerrate);
			return Response.ok().entity(tpList).build();
		} catch (AdapterException e) {
			log.error("getTpsByLayerRate", e);
			return ErrorWrapperUtils.adapterException(e);
		}
	}

	@Override
	public Response getTpsByType(String neid, String tptype, SecurityContext securityContext)
			throws NotFoundException {
		try {
			List<AdpTp> tpList = AdpFactory.getInstance().getTpApi(neid).getTpsByType(tptype);
			return Response.ok().entity(tpList).build();
		} catch (AdapterException e) {
			log.error("getTpsByType", e);
			return Response.serverError().entity(e).build();
		}
	}

	@Override
	public Response updateTp(String neid, AdpTp tp, SecurityContext securityContext) throws NotFoundException {
		return null;
	}

	@Override
	public Response getEquipmentById(String neid, String eqid, SecurityContext securityContext)
			throws NotFoundException {
		try {
			AdpEquipment equipment = AdpFactory.getInstance().getEquApi(neid).getEquipmentById(eqid);
			return Response.ok().entity(equipment).build();
		} catch (Exception e) {
			log.error("getEquipmentById", e);
			return Response.serverError().entity(e).build();
		}
	}

	@Override
	public Response getEquipmentsByNeId(String neid, SecurityContext securityContext) throws NotFoundException {
		try {
			List<AdpEquipment> equipments = AdpFactory.getInstance().getEquApi(neid).getEquipmentsByNeId(neid);
			if (equipments.isEmpty()) {
				return failObjNotExist();
			}
			return Response.ok().entity(equipments).build();
		} catch (Exception e) {
			log.error("getEquipmentsByNeId", e);
			return failDbOperation();
		}
	}

	@Override
	public Response addEquipment(String neid, AdpEquipment equipment, SecurityContext securityContext)
			throws NotFoundException {
		return null;
	}

	@Override
	public Response getXcById(String neid, String xcid, SecurityContext securityContext) throws NotFoundException {
		try {
			AdpXc xc = AdpFactory.getInstance().getXcApi(neid).getXcById(xcid);
			return Response.ok().entity(xc).build();
		} catch (AdapterException e) {
			log.error("getXCById", e);
			return ErrorWrapperUtils.adapterException(e);
		}
	}

	@Override
	public Response createXc(String neid, AdpXc xc, SecurityContext securityContext) throws NotFoundException {
		try {
			AdpXc newXc = AdpFactory.getInstance().getXcApi(neid).createXc(xc);
			return Response.ok().entity(newXc).build();
		} catch (AdapterException e) {
			log.error("createXc", e);
			return ErrorWrapperUtils.adapterException(e);
		}
	}

	@Override
	public Response deleteXcsByNeId(String neid, SecurityContext securityContext) throws NotFoundException {
		try {
			AdpFactory.getInstance().getXcApi(neid).deleteXcsByNeId();
			return Response.ok().build();
		} catch (AdapterException e) {
			log.error("deleteXcsByNeId", e);
			return ErrorWrapperUtils.adapterException(e);
		}
	}

	@Override
	public Response deleteXc(String neid, String xcid, SecurityContext securityContext) throws NotFoundException {
		try {
			AdpFactory.getInstance().getXcApi(neid).deleteXcById(xcid);
			return Response.ok().build();
		} catch (AdapterException e) {
			log.error("deleteXC", e);
			return ErrorWrapperUtils.adapterException(e);
		}
	}

	@Override
	public Response getXcsByNeId(String neid, SecurityContext securityContext) throws NotFoundException {
		try {
			List<AdpXc> xcs = AdpFactory.getInstance().getXcApi(neid).getXcsByNeId();
			return Response.ok().entity(xcs).build();
		} catch (AdapterException e) {
			log.error("getXcsByNeId", e);
			return ErrorWrapperUtils.adapterException(e);
		}
	}

	@Override
	public Response startNeMaintenance(String neid, SecurityContext securityContext) throws NotFoundException {
		return null;
	}

	@Override
	public Response startNeSupervision(String neid, SecurityContext securityContext) throws NotFoundException {
		try {
			AdpFactory.getInstance().getNeApi(neid).supervise();
			return Response.ok().build();
		} catch (AdapterException e) {
			log.error("startSupervision", e);
			return ErrorWrapperUtils.adapterException(e);
		}
	}

	@Override
	public Response stopNeMaintenance(String neid, SecurityContext securityContext) throws NotFoundException {
		return null;
	}

	@Override
	public Response stopNeSupervision(String neid, SecurityContext securityContext) throws NotFoundException {
		try {
			AdpFactory.getInstance().getNeApi(neid).stopNeSupervision();
			return Response.ok().build();
		} catch (AdapterException e) {
			log.error("stopNeSupervision", e);
			return ErrorWrapperUtils.adapterException(e);
		}
	}
}
