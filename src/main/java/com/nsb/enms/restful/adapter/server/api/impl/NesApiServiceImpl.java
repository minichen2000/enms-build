package com.nsb.enms.restful.adapter.server.api.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.lang3.StringUtils;

import com.nsb.enms.restful.adapter.server.action.entity.NeEntity;
import com.nsb.enms.restful.adapter.server.action.method.ne.CreateNe;
import com.nsb.enms.restful.adapter.server.action.method.ne.DeleteNe;
import com.nsb.enms.restful.adapter.server.api.ApiResponseMessage;
import com.nsb.enms.restful.adapter.server.api.NesApiService;
import com.nsb.enms.restful.adapter.server.api.NotFoundException;
import com.nsb.enms.restful.adapter.server.business.SyncTpThread;
import com.nsb.enms.restful.adapter.server.common.exception.AdapterException;
import com.nsb.enms.restful.adapter.server.model.NE;
import com.nsb.enms.restful.adapter.server.model.NEExtraInfo;
import com.nsb.enms.restful.db.client.ApiException;
import com.nsb.enms.restful.db.client.api.NesApi;
import com.nsb.enms.restful.db.client.api.TpsApi;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-07-29T17:16:31.406+08:00")
public class NesApiServiceImpl extends NesApiService {
	private NesApi nesApi = new NesApi();

	@Override
	public Response addNe(NE body, SecurityContext securityContext) throws NotFoundException {
		CreateNe create = new CreateNe();
		String location = "";
		List<NEExtraInfo> extInfos = body.getExtraInfo();
		for (NEExtraInfo extInfo : extInfos) {
			if (extInfo.getKey().equalsIgnoreCase("locationName")) {
				location = extInfo.getValue();
				break;
			}
		}
		NeEntity entity = null;
		try {
			entity = create.createNe(body.getVersion(), body.getNeType(), body.getUserLabel(), location,
					body.getAddress());
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
			e.printStackTrace();
			return Response.serverError().entity(e).build();
		}

		// new thread
		new SyncTpThread(Integer.valueOf(groupId), Integer.valueOf(neId), ne.getId()).start();

		return Response.ok().entity(ne).build();
	}

	private com.nsb.enms.restful.db.client.model.NE constructNe(NeEntity entity, String groupId, String neId) {
		System.out.println("groupId = " + groupId);
		System.out.println("neId = " + neId);
		System.out.println(entity.getNetworkAddress());
		System.out.println(entity.getUserLabel());
		System.out.println(entity.getNeType());

		com.nsb.enms.restful.db.client.model.NE ne = new com.nsb.enms.restful.db.client.model.NE();
		ne.setAid(entity.getMoi());
		ne.setNeId(neId);
		ne.setGroupId(groupId);
		ne.setUserLabel(entity.getUserLabel());
		ne.setNativeName(entity.getUserLabel());
		ne.setVersion(entity.getNeRelease());
		ne.setAddress(entity.getNetworkAddress());
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
		System.out.println("adapter------deleteNE");
		DeleteNe deleteNe = new DeleteNe();
		try {
			com.nsb.enms.restful.db.client.model.NE ne = nesApi.getNeById(neid);
			System.out.println("ne = " + ne);

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

			System.out.println("neGroupId = " + groupId);
			System.out.println("neId = " + neId);

			deleteNe.deleteNe(Integer.valueOf(groupId), Integer.valueOf(neId));

			// delete db record, contains ne and tp
			nesApi.deleteNE(neid);
			TpsApi tpsApi = new TpsApi();
			tpsApi.deleteTpsByNeId(neid);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return Response.serverError().entity(e).build();
		} catch (AdapterException e) {
			e.printStackTrace();
			return Response.serverError().entity(e).build();
		} catch (ApiException e) {
			e.printStackTrace();
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
	public Response nesGet(String netype, String version, SecurityContext securityContext) throws NotFoundException {
		System.out.println("adapter-------nesGet");
		List<com.nsb.enms.restful.db.client.model.NE> nes = new ArrayList<com.nsb.enms.restful.db.client.model.NE>();
		try {
			nes = nesApi.nesGet(netype, version);
		} catch (ApiException e) {
			e.printStackTrace();
			return Response.serverError().entity(e).build();
		}
		return Response.ok().entity(nes).build();
	}

	@Override
	public Response updateNE(NE body, SecurityContext securityContext) throws NotFoundException {
		// do some magic!
		return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
	}
}
