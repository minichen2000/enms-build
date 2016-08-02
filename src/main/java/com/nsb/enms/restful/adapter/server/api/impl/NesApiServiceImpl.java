package com.nsb.enms.restful.adapter.server.api.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.lang3.StringUtils;

import com.nsb.enms.restful.adapter.server.action.entity.NeEntity;
import com.nsb.enms.restful.adapter.server.action.method.ne.CreateNe;
import com.nsb.enms.restful.adapter.server.api.ApiResponseMessage;
import com.nsb.enms.restful.adapter.server.api.NesApiService;
import com.nsb.enms.restful.adapter.server.api.NotFoundException;
import com.nsb.enms.restful.adapter.server.business.SyncTpThread;
import com.nsb.enms.restful.adapter.server.common.exception.AdapterException;
import com.nsb.enms.restful.adapter.server.model.NE;
import com.nsb.enms.restful.adapter.server.model.NEExtraInfo;
import com.nsb.enms.restful.db.client.api.NesApi;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-07-29T17:16:31.406+08:00")
public class NesApiServiceImpl extends NesApiService {
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

		com.nsb.enms.restful.db.client.model.NE ne = new com.nsb.enms.restful.db.client.model.NE();
		ne.setAddress(entity.getNetworkAddress());
		System.out.println(entity.getNetworkAddress());
		ne.setUserLabel(entity.getUserLabel());
		System.out.println(entity.getUserLabel());
		ne.setNeType(entity.getNeType());
		System.out.println(entity.getNeType());
		NesApi nesApi = new NesApi();
		try {
			nesApi.addNe(ne);
		} catch (com.nsb.enms.restful.db.client.ApiException e) {
			e.printStackTrace();
			return Response.serverError().entity(e).build();
		}

		// new thread
		String moi = entity.getMoi();
		String groupId = moi.split("/")[0].replaceAll("neGroupId=", StringUtils.EMPTY);
		String neId = moi.split("/")[1].replaceAll("networkElementId=", StringUtils.EMPTY);
		new SyncTpThread(groupId, neId).start();

		return Response.ok().entity(ne).build();
	}

	@Override
	public Response deleteNE(String neid, SecurityContext securityContext) throws NotFoundException {
		// do some magic!
		return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
	}

	@Override
	public Response getNeById(String neid, SecurityContext securityContext) throws NotFoundException {
		// do some magic!
		return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
	}

	@Override
	public Response nesGet(String netype, String version, SecurityContext securityContext) throws NotFoundException {
		List<NE> nes = new ArrayList<NE>();
		NE ne = new NE();
		ne.setAddress("11111");
		nes.add(ne);
		return Response.ok().entity(nes).build();
	}

	@Override
	public Response updateNE(NE body, SecurityContext securityContext) throws NotFoundException {
		// do some magic!
		return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
	}
}
