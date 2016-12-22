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
	public Response deleteNe(Integer neid, SecurityContext securityContext) throws NotFoundException {
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
	public Response getNeById(Integer neid, SecurityContext securityContext) throws NotFoundException {
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
	public Response startAlignment(Integer neid, SecurityContext securityContext) throws NotFoundException {
		try {
			AdpFactory.getInstance().getNeApi(neid).synchronize();
			return Response.ok().build();
		} catch (AdapterException e) {
			log.error("startAlignment", e);
			return ErrorWrapperUtils.adapterException(e);
		}
	}

	@Override
	public Response addTps(Integer neid, List<AdpTp> tps, SecurityContext securityContext) throws NotFoundException {
		return null;
	}

	@Override
	public Response getChildrenTps(Integer neid, Integer tpid, SecurityContext securityContext)
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
	public Response getNeTps(Integer neid, SecurityContext securityContext) throws NotFoundException {
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
	public Response getTpById(Integer neid, Integer tpid, SecurityContext securityContext) throws NotFoundException {
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
	public Response getTpsByLayerRate(Integer neid, String layerrate, SecurityContext securityContext)
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
	public Response getTpsByType(Integer neid, String tptype, SecurityContext securityContext)
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
	public Response updateTp(Integer neid, AdpTp tp, SecurityContext securityContext) throws NotFoundException {
		return null;
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
	public Response addEquipment(Integer neid, AdpEquipment equipment, SecurityContext securityContext)
			throws NotFoundException {
		return null;
	}

	@Override
	public Response getXcById(Integer neid, Integer xcid, SecurityContext securityContext) throws NotFoundException {
		try {
			AdpXc xc = AdpFactory.getInstance().getXcApi(neid).getXcById(xcid);
			return Response.ok().entity(xc).build();
		} catch (AdapterException e) {
			log.error("getXCById", e);
			return ErrorWrapperUtils.adapterException(e);
		}
	}

	@Override
	public Response createXc(Integer neid, AdpXc xc, SecurityContext securityContext) throws NotFoundException {
		try {
			AdpXc newXc = AdpFactory.getInstance().getXcApi(neid).createXc(xc);
			return Response.ok().entity(newXc).build();
		} catch (AdapterException e) {
			log.error("createXc", e);
			return ErrorWrapperUtils.adapterException(e);
		}
	}

	@Override
	public Response deleteXcsByNeId(Integer neid, SecurityContext securityContext) throws NotFoundException {
		try {
			AdpFactory.getInstance().getXcApi(neid).deleteXcsByNeId();
			return Response.ok().build();
		} catch (AdapterException e) {
			log.error("deleteXcsByNeId", e);
			return ErrorWrapperUtils.adapterException(e);
		}
	}

	@Override
	public Response deleteXc(Integer neid, Integer xcid, SecurityContext securityContext) throws NotFoundException {
		try {
			AdpFactory.getInstance().getXcApi(neid).deleteXcById(xcid);
			return Response.ok().build();
		} catch (AdapterException e) {
			log.error("deleteXC", e);
			return ErrorWrapperUtils.adapterException(e);
		}
	}

	@Override
	public Response getXcsByNeId(Integer neid, SecurityContext securityContext) throws NotFoundException {
		try {
			List<AdpXc> xcs = AdpFactory.getInstance().getXcApi(neid).getXcsByNeId();
			return Response.ok().entity(xcs).build();
		} catch (AdapterException e) {
			log.error("getXcsByNeId", e);
			return ErrorWrapperUtils.adapterException(e);
		}
	}

	@Override
	public Response startNeMaintenance(Integer neid, SecurityContext securityContext) throws NotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response startNeSupervision(Integer neid, SecurityContext securityContext) throws NotFoundException {
		try {
			AdpFactory.getInstance().getNeApi(neid).supervise();
			return Response.ok().build();
		} catch (AdapterException e) {
			log.error("startSupervision", e);
			return ErrorWrapperUtils.adapterException(e);
		}
	}

	@Override
	public Response stopNeMaintenance(Integer neid, SecurityContext securityContext) throws NotFoundException {
		return null;
	}

	@Override
	public Response stopNeSupervision(Integer neid, SecurityContext securityContext) throws NotFoundException {
		return null;
	}
}
