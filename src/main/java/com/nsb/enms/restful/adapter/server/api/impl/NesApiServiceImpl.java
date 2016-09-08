package com.nsb.enms.restful.adapter.server.api.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.restful.adapter.server.action.entity.NeEntity;
import com.nsb.enms.restful.adapter.server.action.method.ne.CreateNe;
import com.nsb.enms.restful.adapter.server.action.method.ne.DeleteNe;
import com.nsb.enms.restful.adapter.server.api.ApiResponseMessage;
import com.nsb.enms.restful.adapter.server.api.NesApiService;
import com.nsb.enms.restful.adapter.server.api.NotFoundException;
import com.nsb.enms.restful.adapter.server.business.SyncTpThread;
import com.nsb.enms.restful.adapter.server.common.exception.AdapterException;
import com.nsb.enms.restful.adapter.server.model.Addresses;
import com.nsb.enms.restful.adapter.server.model.NE;
import com.nsb.enms.restful.adapter.server.model.NEExtraInfo;
import com.nsb.enms.restful.db.client.ApiException;
import com.nsb.enms.restful.db.client.api.NesApi;
import com.nsb.enms.restful.db.client.api.TpsApi;
import com.nsb.enms.restful.db.client.model.Q3Address;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-07-29T17:16:31.406+08:00")
public class NesApiServiceImpl extends NesApiService {
	private final static Logger log = LogManager.getLogger(NesApiServiceImpl.class);
	private NesApi nesApi = new NesApi();

	@Override
	public Response addNe(NE body, SecurityContext securityContext) throws NotFoundException {
		// CreateNe create = new CreateNe();
		String location = "";
		List<NEExtraInfo> extInfos = body.getExtraInfo();
		for (NEExtraInfo extInfo : extInfos) {
			if (extInfo.getKey().equalsIgnoreCase("locationName")) {
				location = extInfo.getValue();
				break;
			}
		}

		log.debug("location = ", location);
		if (StringUtils.isEmpty(location)) {
			location = "CD";
		}

		NeEntity entity = null;
		try {
			Addresses address = body.getAddresses();
			entity = CreateNe.createNe(body.getVersion(), body.getNeType(), body.getUserLabel(), location,
					address.getQ3Address().getQ3address());
		} catch (AdapterException e) {
			e.printStackTrace();
			return Response.serverError().entity(e).build();
		}

		if (null == entity) {
			return Response.serverError()
					.entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "failed to create ne")).build();
		}

		System.out.println(entity);

		String moi = entity.getMoi();
		String groupId = moi.split("/")[0].replaceAll("neGroupId=", StringUtils.EMPTY);
		String neId = moi.split("/")[1].replaceAll("networkElementId=", StringUtils.EMPTY);

		com.nsb.enms.restful.db.client.model.NE ne = constructNe(entity, groupId, neId);

		try {
			ne = nesApi.addNe(ne);
		} catch (com.nsb.enms.restful.db.client.ApiException e) {
			log.error("addNe", e);
			return Response.serverError().entity(e).build();
		}

		// new thread
		new SyncTpThread(Integer.valueOf(groupId), Integer.valueOf(neId), ne.getId()).start();

		log.debug("adapter----------------addNe----------end");

		return Response.ok().entity(ne).build();
	}

	private com.nsb.enms.restful.db.client.model.NE constructNe(NeEntity entity, String groupId, String neId) {
		log.debug("groupId = " + groupId + ", neId = " + neId);

		com.nsb.enms.restful.db.client.model.NE ne = new com.nsb.enms.restful.db.client.model.NE();
		ne.setAid(entity.getMoi());
		ne.setUserLabel(entity.getUserLabel());
		ne.setNativeName(entity.getUserLabel());
		ne.setVersion(entity.getNeRelease());

		com.nsb.enms.restful.db.client.model.Addresses address = new com.nsb.enms.restful.db.client.model.Addresses();
		Q3Address q3Address = new Q3Address();
		q3Address.setQ3address(entity.getNetworkAddress());
		address.setQ3Address(q3Address);
		address.setTl1Address(new ArrayList<String>());
		address.setSnmpAddress(null);
		ne.setAddresses(address);

		ne.setNeType(entity.getNeType());
		ne.setOperationStatus(entity.getAdministrativeState());
		ne.setAlignmentStatus("false");
		List<com.nsb.enms.restful.db.client.model.NEExtraInfo> extInfoList = new ArrayList<com.nsb.enms.restful.db.client.model.NEExtraInfo>();
		com.nsb.enms.restful.db.client.model.NEExtraInfo mocExtInfo = new com.nsb.enms.restful.db.client.model.NEExtraInfo();
		mocExtInfo.setKey("moc");
		mocExtInfo.setValue(entity.getMoc());
		extInfoList.add(mocExtInfo);
		com.nsb.enms.restful.db.client.model.NEExtraInfo moiExtInfo = new com.nsb.enms.restful.db.client.model.NEExtraInfo();
		moiExtInfo.setKey("moi");
		moiExtInfo.setValue(entity.getMoi());
		extInfoList.add(moiExtInfo);
		ne.setExtraInfo(extInfoList);
		return ne;
	}

	@Override
	public Response deleteNE(String neid, SecurityContext securityContext) throws NotFoundException {
		log.debug("adapter------deleteNE");
		// DeleteNe deleteNe = new DeleteNe();
		try {
			com.nsb.enms.restful.db.client.model.NE ne = nesApi.getNeById(neid);
			log.debug("ne = " + ne);

			String moi = StringUtils.EMPTY;
			List<com.nsb.enms.restful.db.client.model.NEExtraInfo> extInfos = ne.getExtraInfo();
			for (com.nsb.enms.restful.db.client.model.NEExtraInfo extInfo : extInfos) {
				if ("moi".equalsIgnoreCase(extInfo.getKey())) {
					moi = extInfo.getValue();
					break;
				}
			}

			if (StringUtils.isEmpty(moi)) {
				return Response.serverError().build();
			}
			String groupId = moi.split("/")[0].replaceAll("neGroupId=", StringUtils.EMPTY);
			String neId = moi.split("/")[1].replaceAll("networkElementId=", StringUtils.EMPTY);

			log.debug("groupId = " + groupId + ", neId = " + neId);

			DeleteNe.deleteNe(Integer.valueOf(groupId), Integer.valueOf(neId));

			// delete db record, contains ne and tp
			nesApi.deleteNE(neid);
			TpsApi tpsApi = new TpsApi();
			tpsApi.deleteTpsbyNeId(neId);
		} catch (Exception e) {
			log.error("deleteNE", e);
			return Response.serverError().entity(e).build();
		}

		return Response.ok().build();
	}

	@Override
	public Response getNeById(String neid, SecurityContext securityContext) throws NotFoundException {
		com.nsb.enms.restful.db.client.model.NE ne = new com.nsb.enms.restful.db.client.model.NE();
		try {
			ne = nesApi.getNeById(neid);
		} catch (ApiException e) {
			e.printStackTrace();
			return Response.serverError().entity(e).build();
		}
		return Response.ok().entity(ne).build();
	}

	@Override
	public Response updateNE(NE body, SecurityContext securityContext) throws NotFoundException {
		return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
	}

	@Override
	public Response findNeByType(String netype, SecurityContext securityContext) throws NotFoundException {
		List<com.nsb.enms.restful.db.client.model.NE> nes = new ArrayList<com.nsb.enms.restful.db.client.model.NE>();
		try {
			nes = nesApi.findNeByType(netype);
		} catch (ApiException e) {
			e.printStackTrace();
			return Response.serverError().entity(e).build();
		}
		return Response.ok().entity(nes).build();
	}

	@Override
	public Response findNeByTypeVersion(String netype, String neversion, SecurityContext securityContext)
			throws NotFoundException {
		List<com.nsb.enms.restful.db.client.model.NE> nes = new ArrayList<com.nsb.enms.restful.db.client.model.NE>();
		try {
			nes = nesApi.findNeByTypeVersion(netype, neversion);
		} catch (ApiException e) {
			e.printStackTrace();
			return Response.serverError().entity(e).build();
		}
		return Response.ok().entity(nes).build();
	}

	@Override
	public Response findNeByVersion(String neversion, SecurityContext securityContext) throws NotFoundException {
		List<com.nsb.enms.restful.db.client.model.NE> nes = new ArrayList<com.nsb.enms.restful.db.client.model.NE>();
		try {
			nes = nesApi.findNeByVersion(neversion);
		} catch (ApiException e) {
			e.printStackTrace();
			return Response.serverError().entity(e).build();
		}
		return Response.ok().entity(nes).build();
	}

	@Override
	public Response nesGet(SecurityContext securityContext) throws NotFoundException {
		log.debug("adapter-------nesGet");
		List<com.nsb.enms.restful.db.client.model.NE> nes = new ArrayList<com.nsb.enms.restful.db.client.model.NE>();
		try {
			nes = nesApi.nesGet();
		} catch (ApiException e) {
			log.error("nesGet", e);
			return Response.serverError().entity(e).build();
		}
		return Response.ok().entity(nes).build();
	}
}
