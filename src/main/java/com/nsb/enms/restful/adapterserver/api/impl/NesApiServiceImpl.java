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
import com.nsb.enms.adapter.server.common.TYPES;
import com.nsb.enms.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.utils.GenerateKeyOnNeUtil;
import com.nsb.enms.adapter.server.db.mgr.AdpEqusDbMgr;
import com.nsb.enms.adapter.server.db.mgr.AdpNesDbMgr;
import com.nsb.enms.adapter.server.db.mgr.AdpTpsDbMgr;
import com.nsb.enms.adapter.server.db.mgr.AdpXcsDbMgr;
import com.nsb.enms.restful.adapterserver.api.ApiResponseMessage;
import com.nsb.enms.restful.adapterserver.api.NesApiService;
import com.nsb.enms.restful.adapterserver.api.NotFoundException;
import com.nsb.enms.restful.model.adapter.AdpAddresses;
import com.nsb.enms.restful.model.adapter.AdpNe;
import com.nsb.enms.restful.model.adapter.AdpNe.CommunicationStateEnum;
import com.nsb.enms.restful.model.adapter.AdpNe.OperationalStateEnum;
import com.nsb.enms.restful.model.adapter.AdpNe.SupervisionStateEnum;
import com.nsb.enms.restful.model.adapter.AdpNe.SynchStateEnum;
import com.nsb.enms.restful.model.adapter.AdpNeExtraInfo;
import com.nsb.enms.restful.model.adapter.AdpQ3Address;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-07-29T17:16:31.406+08:00")
public class NesApiServiceImpl extends NesApiService {
	private final static Logger log = LogManager.getLogger(NesApiServiceImpl.class);

	private AdpNesDbMgr nesDbMgr = new AdpNesDbMgr();

	private final static String NAMESERVERFILE_URL = ConfLoader.getInstance().getConf("NAMESERVERFILE_URL", "");

	@Override
	public Response addNe(AdpNe body, SecurityContext securityContext) throws NotFoundException {
		String location = body.getLocationName();
		NeEntity entity = null;
		String id = "";
		try {
			AdpAddresses address = body.getAddresses();
			id = address.getQ3Address().getAddress();
			entity = CreateNe.createNe(body.getVersion(), body.getNeType(), body.getUserLabel(), location, id);
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

		AdpNe ne = constructNe(entity, id);

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

	private AdpNe constructNe(NeEntity entity, String id) {
		AdpNe ne = new AdpNe();
		ne.setId(id);
		ne.setKeyOnNe(GenerateKeyOnNeUtil.generateKeyOnNe(TYPES.NE, entity.getMoc(), entity.getMoi()));
		ne.setUserLabel(entity.getUserLabel());
		ne.setVersion(entity.getNeRelease());

		AdpAddresses address = new AdpAddresses();
		AdpQ3Address q3Address = new AdpQ3Address();
		q3Address.setAddress(entity.getNetworkAddress());
		address.setQ3Address(q3Address);
		address.setTl1Address(new ArrayList<String>());
		address.setSnmpAddress(null);
		ne.setAddresses(address);

		ne.setNeType(entity.getNeType());
		ne.setOperationalState(OperationalStateEnum.IDLE);
		ne.setAdminState(false);
		ne.setCommunicationState(CommunicationStateEnum.UNREACHABLE);
		ne.setSynchState(SynchStateEnum.UNSYNCHRONIZED);
		ne.setSupervisionState(SupervisionStateEnum.UNSUPERVISED);
		ne.setLocationName(entity.getLocationName());
		String[] groupNeId = entity.getMoi().split("/");
		StringBuilder neUsmParameter = new StringBuilder();
		neUsmParameter.append("NE_TYPE=").append(entity.getNeType()).append("&NE_RELEASE=")
				.append(entity.getNeRelease()).append("&GROUP_ID=").append(groupNeId[0].split("=")[1]).append("&NE_ID=")
				.append(groupNeId[1].split("=")[1]).append("&NAMESERVERFILE_URL=").append(NAMESERVERFILE_URL);
		AdpNeExtraInfo neExtraInfo = new AdpNeExtraInfo();
		neExtraInfo.setNeUsmParameter(neUsmParameter.toString());
		ne.setExtraInfo(neExtraInfo);
		return ne;
	}

	@Override
	public Response deleteNe(String neid, SecurityContext securityContext) throws NotFoundException {
		log.debug("adapter------deleteNE");

		boolean hasBussiness = checkBussiness();
		if (hasBussiness) {
			return Response.serverError().build();
		}

		try {
			AdpNe ne = nesDbMgr.getNeById(neid);
			log.debug("ne = " + ne);

			String moi = StringUtils.EMPTY;
			moi = GenerateKeyOnNeUtil.getMoi(ne.getKeyOnNe());

			if (StringUtils.isEmpty(moi)) {
				return Response.serverError().build();
			}
			String groupId = moi.split("/")[0].replaceAll("neGroupId=", StringUtils.EMPTY);
			String neId = moi.split("/")[1].replaceAll("networkElementId=", StringUtils.EMPTY);

			log.debug("groupId = " + groupId + ", neId = " + neId);

			DeleteNe.deleteNe(Integer.valueOf(groupId), Integer.valueOf(neId));

			// delete db record, contains ne and tp
			nesDbMgr.deleteNe(neid);

			AdpXcsDbMgr xcsDbMgr = new AdpXcsDbMgr();
			xcsDbMgr.deleteXcsByNeId(neId);

			AdpTpsDbMgr tpsDbMgr = new AdpTpsDbMgr();
			tpsDbMgr.deleteTpsbyNeId(neId);

			AdpEqusDbMgr equipmentsDbMgr = new AdpEqusDbMgr();
			equipmentsDbMgr.deleteEquipmentsByNeId(neId);
		} catch (Exception e) {
			log.error("deleteNE", e);
			return Response.serverError().entity(e).build();
		}

		return Response.ok().build();
	}

	private boolean checkBussiness() {
		return false;
	}

	@Override
	public Response getNeById(String neid, SecurityContext securityContext) throws NotFoundException {
		AdpNe ne = new AdpNe();
		try {
			ne = nesDbMgr.getNeById(neid);
		} catch (Exception e) {
			log.error("getNeById", e);
			return Response.serverError().entity(e).build();
		}
		return Response.ok().entity(ne).build();
	}

	@Override
	public Response updateNe(AdpNe body, SecurityContext securityContext) throws NotFoundException {
		try {
			nesDbMgr.updateNe(body);
		} catch (Exception e) {
			log.error("updateNe", e);
			return Response.serverError().entity(e).build();
		}
		return Response.ok().build();
	}

	@Override
	public Response getNes(SecurityContext securityContext) throws NotFoundException {
		log.debug("adapter-------nesGet");
		List<AdpNe> nes = new ArrayList<AdpNe>();
		try {
			nes = nesDbMgr.getNes();
		} catch (Exception e) {
			log.error("nesGet", e);
			return Response.serverError().entity(e).build();
		}
		return Response.ok().entity(nes).build();
	}
}
