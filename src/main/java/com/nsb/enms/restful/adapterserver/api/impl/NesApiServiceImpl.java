package com.nsb.enms.restful.adapterserver.api.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.action.entity.NeEntity;
import com.nsb.enms.adapter.server.action.method.ne.CreateNe;
import com.nsb.enms.adapter.server.action.method.ne.DeleteNe;
import com.nsb.enms.adapter.server.business.SyncTpThread;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.db.mgr.EquipmentsDbMgr;
import com.nsb.enms.adapter.server.db.mgr.NesDbMgr;
import com.nsb.enms.adapter.server.db.mgr.TpsDbMgr;
import com.nsb.enms.adapter.server.db.mgr.XcsDbMgr;
import com.nsb.enms.restful.adapterserver.api.ApiResponseMessage;
import com.nsb.enms.restful.adapterserver.api.NesApiService;
import com.nsb.enms.restful.adapterserver.api.NotFoundException;
import com.nsb.enms.restful.model.adapter.Addresses;
import com.nsb.enms.restful.model.adapter.Ne;
import com.nsb.enms.restful.model.adapter.NeExtraInfo;
import com.nsb.enms.restful.model.adapter.Q3Address;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-07-29T17:16:31.406+08:00")
public class NesApiServiceImpl extends NesApiService {
	private final static Logger log = LogManager.getLogger(NesApiServiceImpl.class);
	private NesDbMgr nesDbMgr = new NesDbMgr();

	@Override
	public Response addNe(Ne body, SecurityContext securityContext) throws NotFoundException {
		String location = "";
		NeExtraInfo extInfo = body.getExtraInfo();
		location = extInfo.getLocationName();

		log.debug("location = ", location);
		if (StringUtils.isEmpty(location)) {
			location = "CD";
		}

		NeEntity entity = null;
		try {
			Addresses address = body.getAddresses();
			entity = CreateNe.createNe(body.getVersion(), body.getNeType(), body.getUserLabel(), location,
					address.getQ3Address().getAddress());
		} catch (AdapterException e) {
			log.error("create ne occur error", e);
			return Response.serverError().entity(e).build();
		}

		if (null == entity) {
			return Response.serverError()
					.entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "failed to create ne")).build();
		}

		log.debug(entity);

		String moi = entity.getMoi();
		String groupId = moi.split("/")[0].replaceAll("neGroupId=", StringUtils.EMPTY);
		String neId = moi.split("/")[1].replaceAll("networkElementId=", StringUtils.EMPTY);

		Ne ne = constructNe(entity, groupId, neId);

		try {
			ne = nesDbMgr.addNe(ne);
		} catch (Exception e) {
			log.error("addNe", e);
			return Response.serverError().entity(e).build();
		}

		// new thread
		new SyncTpThread(Integer.valueOf(groupId), Integer.valueOf(neId), ne.getId()).start();

		log.debug("adapter----------------addNe----------end");

		return Response.ok().entity(ne).build();
	}

	private Ne constructNe(NeEntity entity, String groupId, String neId) {
		log.debug("groupId = " + groupId + ", neId = " + neId);

		Ne ne = new Ne();
		ne.setAid(entity.getMoi());
		ne.setUserLabel(entity.getUserLabel());
		// ne.setNativeName(entity.getUserLabel());
		ne.setVersion(entity.getNeRelease());

		Addresses address = new Addresses();
		Q3Address q3Address = new Q3Address();
		q3Address.setAddress(entity.getNetworkAddress());
		address.setQ3Address(q3Address);
		address.setTl1Address(new ArrayList<String>());
		address.setSnmpAddress(null);
		ne.setAddresses(address);

		ne.setNeType(entity.getNeType());
		ne.setOperationState("disable");
		ne.setAdminState(entity.getAdministrativeState());
		NeExtraInfo neExtraInfo = new NeExtraInfo();
		neExtraInfo.setMoi(entity.getMoc());
		ne.setExtraInfo(neExtraInfo);
		return ne;
	}

	@Override
	public Response deleteNe(String neid, SecurityContext securityContext) throws NotFoundException {
		log.debug("adapter------deleteNE");
		// DeleteNe deleteNe = new DeleteNe();
		try {
			Ne ne = nesDbMgr.getNeById(neid);
			log.debug("ne = " + ne);

			String moi = StringUtils.EMPTY;
			/*
			 * List<com.nsb.enms.restful.db.client.model.NEExtraInfo> extInfos =
			 * ne.getExtraInfo(); for
			 * (com.nsb.enms.restful.db.client.model.NEExtraInfo extInfo :
			 * extInfos) { if ("moi".equalsIgnoreCase(extInfo.getKey())) { moi =
			 * extInfo.getValue(); break; } }
			 */
			NeExtraInfo extraInfo = ne.getExtraInfo();
			moi = extraInfo.getMoi();

			if (StringUtils.isEmpty(moi)) {
				return Response.serverError().build();
			}
			String groupId = moi.split("/")[0].replaceAll("neGroupId=", StringUtils.EMPTY);
			String neId = moi.split("/")[1].replaceAll("networkElementId=", StringUtils.EMPTY);

			log.debug("groupId = " + groupId + ", neId = " + neId);

			DeleteNe.deleteNe(Integer.valueOf(groupId), Integer.valueOf(neId));

			// delete db record, contains ne and tp
			nesDbMgr.deleteNe(neid);

			XcsDbMgr xcsDbMgr = new XcsDbMgr();
			xcsDbMgr.deleteXcsByNeId(neId);

			TpsDbMgr tpsDbMgr = new TpsDbMgr();
			tpsDbMgr.deleteTpsbyNeId(neId);

			EquipmentsDbMgr equipmentsDbMgr = new EquipmentsDbMgr();
			equipmentsDbMgr.deleteEquipmentsByNeId(neId);
		} catch (Exception e) {
			log.error("deleteNE", e);
			return Response.serverError().entity(e).build();
		}

		return Response.ok().build();
	}

	@Override
	public Response getNeById(String neid, SecurityContext securityContext) throws NotFoundException {
		Ne ne = new Ne();
		try {
			ne = nesDbMgr.getNeById(neid);
		} catch (Exception e) {
			log.error("getNeById", e);
			return Response.serverError().entity(e).build();
		}
		return Response.ok().entity(ne).build();
	}

	@Override
	public Response updateNe(Ne body, SecurityContext securityContext) throws NotFoundException {
		return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
	}

	@Override
	public Response findNesByType(String netype, SecurityContext securityContext) throws NotFoundException {
		List<Ne> nes = new ArrayList<Ne>();
		try {
			nes = nesDbMgr.findNesByType(netype);
		} catch (Exception e) {
			log.error("getNeById", e);
			return Response.serverError().entity(e).build();
		}
		return Response.ok().entity(nes).build();
	}

	@Override
	public Response findNeByTypeVersion(String netype, String neversion, SecurityContext securityContext)
			throws NotFoundException {
		List<Ne> nes = new ArrayList<Ne>();
		try {
			nes = nesDbMgr.findNeByTypeVersion(netype, neversion);
		} catch (Exception e) {
			log.error("findNeByTypeVersion", e);
			return Response.serverError().entity(e).build();
		}
		return Response.ok().entity(nes).build();
	}

	@Override
	public Response findNesByVersion(String neversion, SecurityContext securityContext) throws NotFoundException {
		List<Ne> nes = new ArrayList<Ne>();
		try {
			nes = nesDbMgr.findNesByVersion(neversion);
		} catch (Exception e) {
			log.error("findNesByVersion", e);
			return Response.serverError().entity(e).build();
		}
		return Response.ok().entity(nes).build();
	}

	@Override
	public Response getNes(SecurityContext securityContext) throws NotFoundException {
		log.debug("adapter-------nesGet");
		List<Ne> nes = new ArrayList<Ne>();
		try {
			nes = nesDbMgr.getNes();
		} catch (Exception e) {
			log.error("nesGet", e);
			return Response.serverError().entity(e).build();
		}
		return Response.ok().entity(nes).build();
	}
}
