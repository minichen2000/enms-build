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
import com.nsb.enms.restful.adapterserver.api.ApiResponseMessage;
import com.nsb.enms.restful.adapterserver.api.NesApiService;
import com.nsb.enms.restful.adapterserver.api.NotFoundException;
import com.nsb.enms.restful.dbclient.ApiException;
import com.nsb.enms.restful.dbclient.api.DbEquipmentsApi;
import com.nsb.enms.restful.dbclient.api.DbNesApi;
import com.nsb.enms.restful.dbclient.api.DbTpsApi;
import com.nsb.enms.restful.dbclient.api.DbXcsApi;
import com.nsb.enms.restful.model.Addresses;
import com.nsb.enms.restful.model.Ne;
import com.nsb.enms.restful.model.NeExtraInfo;
import com.nsb.enms.restful.model.Q3Address;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-07-29T17:16:31.406+08:00")
public class NesApiServiceImpl extends NesApiService {
	private final static Logger log = LogManager.getLogger(NesApiServiceImpl.class);
	private DbNesApi dbNesApi = new DbNesApi();

	@Override
	public Response addNe(Ne body, SecurityContext securityContext) throws NotFoundException {
		// CreateNe create = new CreateNe();
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

		Ne ne = constructNe(entity, groupId, neId);

		try {
			ne = dbNesApi.addNe(ne);
		} catch (ApiException e) {
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
		//ne.setNativeName(entity.getUserLabel());
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
		ne.setAdminState( entity.getAdministrativeState() );
		//ne.setAlignmentStatus("false");
		//List<NeExtraInfo> extInfoList = new ArrayList<NeExtraInfo>();
		NeExtraInfo neExtraInfo = new NeExtraInfo();
		neExtraInfo.setMoi( entity.getMoc() );
		ne.setExtraInfo( neExtraInfo );
		return ne;
	}

	@Override
	public Response deleteNe(String neid, SecurityContext securityContext) throws NotFoundException {
		log.debug("adapter------deleteNE");
		// DeleteNe deleteNe = new DeleteNe();
		try {
			Ne ne = dbNesApi.getNeById(neid);
			log.debug("ne = " + ne);

			String moi = StringUtils.EMPTY;
			/*List<com.nsb.enms.restful.db.client.model.NEExtraInfo> extInfos = ne.getExtraInfo();
			for (com.nsb.enms.restful.db.client.model.NEExtraInfo extInfo : extInfos) {
				if ("moi".equalsIgnoreCase(extInfo.getKey())) {
					moi = extInfo.getValue();
					break;
				}
			}*/
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
			dbNesApi.deleteNe(neid);
			DbXcsApi dbXcsApi = new DbXcsApi();
			dbXcsApi.deleteXcsByNeId(neId);
			DbTpsApi dbTpsApi = new DbTpsApi();
			dbTpsApi.deleteTpsbyNeId(neId);
			DbEquipmentsApi dbEquipmentsApi = new DbEquipmentsApi();
			dbEquipmentsApi.deleteEquipmentsByNeId(neId);
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
			ne = dbNesApi.getNeById(neid);
		} catch (ApiException e) {
			e.printStackTrace();
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
			nes = dbNesApi.findNesByType(netype);
		} catch (ApiException e) {
			e.printStackTrace();
			return Response.serverError().entity(e).build();
		}
		return Response.ok().entity(nes).build();
	}

	@Override
	public Response findNeByTypeVersion(String netype, String neversion, SecurityContext securityContext)
			throws NotFoundException {
		List<Ne> nes = new ArrayList<Ne>();
		try {
			nes = dbNesApi.findNeByTypeVersion(netype, neversion);
		} catch (ApiException e) {
			e.printStackTrace();
			return Response.serverError().entity(e).build();
		}
		return Response.ok().entity(nes).build();
	}

	@Override
	public Response findNesByVersion(String neversion, SecurityContext securityContext) throws NotFoundException {
		List<Ne> nes = new ArrayList<Ne>();
		try {
			nes = dbNesApi.findNesByVersion(neversion);
		} catch (ApiException e) {
			e.printStackTrace();
			return Response.serverError().entity(e).build();
		}
		return Response.ok().entity(nes).build();
	}

	@Override
	public Response getNes(SecurityContext securityContext) throws NotFoundException {
		log.debug("adapter-------nesGet");
		List<Ne> nes = new ArrayList<Ne>();
		try {
			nes = dbNesApi.getNes();
		} catch (ApiException e) {
			log.error("nesGet", e);
			return Response.serverError().entity(e).build();
		}
		return Response.ok().entity(nes).build();
	}
}
