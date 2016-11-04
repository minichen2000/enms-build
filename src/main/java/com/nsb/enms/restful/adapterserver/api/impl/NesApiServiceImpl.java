package com.nsb.enms.restful.adapterserver.api.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.FutureTask;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.action.entity.NeEntity;
import com.nsb.enms.adapter.server.action.method.ne.CreateNe;
import com.nsb.enms.adapter.server.action.method.ne.DeleteNe;
import com.nsb.enms.adapter.server.action.method.ne.SetManagerAddress;
import com.nsb.enms.adapter.server.action.method.ne.StartSupervision;
import com.nsb.enms.adapter.server.business.tp.SyncTpThread;
import com.nsb.enms.adapter.server.business.xc.AdpXcsMgr;
import com.nsb.enms.adapter.server.common.MethodOperator;
import com.nsb.enms.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.adapter.server.common.conf.ConfigKey;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.utils.ErrorWrapperUtils;
import com.nsb.enms.adapter.server.common.utils.GenerateKeyOnNeUtil;
import com.nsb.enms.adapter.server.common.utils.TimeUtil;
import com.nsb.enms.adapter.server.db.mgr.AdpEqusDbMgr;
import com.nsb.enms.adapter.server.db.mgr.AdpNesDbMgr;
import com.nsb.enms.adapter.server.db.mgr.AdpTpsDbMgr;
import com.nsb.enms.adapter.server.db.mgr.AdpXcsDbMgr;
import com.nsb.enms.adapter.server.notification.NotificationSender;
import com.nsb.enms.adapter.server.statemachine.app.NeStateMachineApp;
import com.nsb.enms.common.AlarmSeverity;
import com.nsb.enms.common.AlarmType;
import com.nsb.enms.common.EntityType;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.common.utils.ValidationUtil;
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

	private final static String ADP_ADDRESS = ConfLoader.getInstance().getConf(ConfigKey.ADP_ADDRESS, "");

	@Override
	public Response addNe(AdpNe body, SecurityContext securityContext) throws NotFoundException {
		Response response = validateParam(body, MethodOperator.ADD);
		if (null != response) {
			return response;
		}

		AdpAddresses addresses = body.getAddresses();
		String osMain = addresses.getQ3Address().getOsMain();
		if (!ADP_ADDRESS.equals(osMain)) {
			String location = body.getLocationName();
			NeEntity entity = null;
			try {
				String id = addresses.getQ3Address().getAddress().trim();
				entity = CreateNe.createNe(body.getVersion(), body.getNeType(), body.getUserLabel(), location, id);
			} catch (AdapterException e) {
				log.error("create ne occur error", e);
				return ErrorWrapperUtils.adapterException(e);
			}

			if (null == entity) {
				return failCreateNeByEMLIM();
			}
			String moi = entity.getMoi();
			String groupId = moi.split("/")[0].split("=")[1];
			String neId = moi.split("/")[1].split("=")[1];
			boolean isSuccess = false;
			try {
				isSuccess = StartSupervision.startSupervision(groupId, neId);
			} catch (AdapterException e) {
				log.error("failed to supervision ne", e);
			}

			if (!isSuccess) {
				return failSuperviseNeByEMLIM();
			}
			try {
				if (StringUtils.isNotEmpty(osMain)) {
					SetManagerAddress.setMainOSAddress(groupId, neId, osMain.trim());
				}

				String spareOSAddress = addresses.getQ3Address().getOsSpare();
				if (StringUtils.isNotEmpty(spareOSAddress)) {
					SetManagerAddress.setSpareOSAddress(groupId, neId, spareOSAddress.trim());
				}
			} catch (AdapterException e) {
				log.error("setAddress", e);
				return failSetManagerAddress();
			}

			try {
				DeleteNe.deleteNe(groupId, neId);
			} catch (AdapterException e) {
				log.error("deleteNe", e);
				return ErrorWrapperUtils.adapterException(e);
			}
			return Response.ok().build();
		} else {
			String location = body.getLocationName();
			NeEntity entity = null;
			String id = "";
			try {
				id = addresses.getQ3Address().getAddress();
				entity = CreateNe.createNe(body.getVersion(), body.getNeType(), body.getUserLabel(), location,
						id.trim());
			} catch (AdapterException e) {
				log.error("create ne occur error", e);
				return ErrorWrapperUtils.adapterException(e);
			}

			if (null == entity) {
				return failCreateNeByEMLIM();
			}

			log.debug(entity);

			AdpNe ne = constructNe(entity, id);

			try {
				ne = nesDbMgr.addNe(ne);
			} catch (Exception e) {
				log.error("addNe", e);
				return failDbOperation();
			}

			NotificationSender.instance().sendOcNotif(EntityType.NE, ne.getId());

			String eventTime = TimeUtil.getLocalTmfTime();
			String occureTime = eventTime;
			NotificationSender.instance().sendAlarm(ErrorCode.ALM_NE_NOT_SUPERVISED, AlarmType.ALM_COMMUNICATION,
					AlarmSeverity.MAJOR, eventTime, occureTime, "", "", EntityType.NE, id, "", "",
					ErrorCode.ALM_NE_NOT_SUPERVISED.getMessage());
			NotificationSender.instance().sendAlarm(ErrorCode.ALM_NE_MISALIGNMENT, AlarmType.ALM_COMMUNICATION,
					AlarmSeverity.MAJOR, eventTime, occureTime, "", "", EntityType.NE, id, "", "",
					ErrorCode.ALM_NE_MISALIGNMENT.getMessage());

			log.debug("adapter----------------addNe----------end");

			return Response.ok().entity(ne).build();
		}
	}

	private Response failCreateNeByEMLIM() {
		return constructErrorInfo(ErrorCode.FAIL_CREATE_NE_BY_EMLIM);
	}

	private Response failDbOperation() {
		return ErrorWrapperUtils.failDbOperation();
	}

	private Response constructErrorInfo(ErrorCode errorCode) {
		return ErrorWrapperUtils.constructErrorInfo(errorCode);
	}

	private Response validateParam(AdpNe body, MethodOperator operate) {
		String userLabel = body.getUserLabel();
		if (!ValidationUtil.isValidUserLabel(userLabel)) {
			return constructErrorInfo(ErrorCode.FAIL_INVALID_USER_LABEL);
		}

		try {
			if (MethodOperator.ADD == operate) {
				boolean isExisted = nesDbMgr.isUserLabelExisted(body.getId(), userLabel, operate);
				if (isExisted) {
					return constructErrorInfo(ErrorCode.FAIL_USER_LABEL_EXISTED);
				}
			}
		} catch (Exception e) {
			return failDbOperation();
		}

		String locationName = body.getLocationName();
		if (!ValidationUtil.isValidLocationName(locationName)) {
			return constructErrorInfo(ErrorCode.FAIL_INVALID_LOCATION_NAME);
		}

		Response response = validateAddress(body.getAddresses(), operate);
		if (null != response) {
			return response;
		}

		// TODO 其他校验条件

		return null;

	}

	private Response validateAddress(AdpAddresses addresses, MethodOperator operate) {
		if (null == addresses) {
			if (MethodOperator.ADD == operate) {
				return constructErrorInfo(ErrorCode.FAIL_INVALID_Q3_ADDRESS);
			}
			return null;
		}
		AdpQ3Address q3Address = addresses.getQ3Address();
		if (!ValidationUtil.isValidQ3Address(q3Address.getAddress())) {
			return constructErrorInfo(ErrorCode.FAIL_INVALID_Q3_ADDRESS);
		}

		if (!ValidationUtil.isValidQ3Address(q3Address.getAddress())) {
			return constructErrorInfo(ErrorCode.FAIL_INVALID_Q3_MAIN_ADDRESS);
		}

		if (!ValidationUtil.isValidQ3Address(q3Address.getAddress())) {
			return constructErrorInfo(ErrorCode.FAIL_INVALID_Q3_SPARE_ADDRESS);
		}

		return null;
	}

	private AdpNe constructNe(NeEntity entity, String id) {
		AdpNe ne = new AdpNe();
		ne.setId(id);
		ne.setKeyOnNe(GenerateKeyOnNeUtil.generateKeyOnNe(EntityType.NE, entity.getMoc(), entity.getMoi()));
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

		boolean hasBusiness = checkBusiness();
		if (hasBusiness) {
			// TODO 确定错误码是否正确
			return constructErrorInfo(ErrorCode.FAIL_NOT_OPERABLE);
		}

		AdpNe ne = null;
		try {
			ne = isNeExisted(neid);
		} catch (AdapterException e) {
			return ErrorWrapperUtils.adapterException(e);
		}

		String moi = GenerateKeyOnNeUtil.getMoi(ne.getKeyOnNe());
		if (StringUtils.isEmpty(moi)) {
			return failObjNotExist();
		}

		String groupId = moi.split("/")[0].replaceAll("neGroupId=", StringUtils.EMPTY);
		String neId = moi.split("/")[1].replaceAll("networkElementId=", StringUtils.EMPTY);

		log.debug("groupId = " + groupId + ", neId = " + neId);

		try {
			DeleteNe.deleteNe(groupId, neId);
			NotificationSender.instance().sendOdNotif(EntityType.NE, neid);
		} catch (AdapterException e) {
			log.error("deleteNe", e);
			return ErrorWrapperUtils.adapterException(e);
		}

		// delete db record, contains ne and tp
		try {
			nesDbMgr.deleteNe(neid);
			AdpXcsMgr xcsMgr = new AdpXcsMgr();
			xcsMgr.deleteXcsByNeId(neId);

			AdpTpsDbMgr tpsDbMgr = new AdpTpsDbMgr();
			tpsDbMgr.deleteTpsbyNeId(neId);

			AdpEqusDbMgr equipmentsDbMgr = new AdpEqusDbMgr();
			equipmentsDbMgr.deleteEquipmentsByNeId(neId);
		} catch (AdapterException e) {
			return ErrorWrapperUtils.adapterException(e);
		} catch (Exception e) {
			return failDbOperation();
		}

		return Response.ok().build();
	}

	private AdpNe isNeExisted(String neid) throws AdapterException {
		AdpNe ne;
		try {
			ne = nesDbMgr.getNeById(neid);
			log.debug("ne = " + ne);
			if (null == ne || StringUtils.isEmpty(ne.getId())) {
				throw new AdapterException(ErrorCode.FAIL_OBJ_NOT_EXIST);
			}
		} catch (Exception e) {
			log.error("getNeById", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}

		return ne;
	}

	private Response failDeleteNeByEMLIM() {
		return constructErrorInfo(ErrorCode.FAIL_DELETE_NE_BY_EMLIM);
	}

	private Response failSetManagerAddress() {
		return constructErrorInfo(ErrorCode.FAIL_SET_MANAGER_ADDRESS_BY_EMLIM);
	}

	private Response failObjNotExist() {
		return constructErrorInfo(ErrorCode.FAIL_OBJ_NOT_EXIST);
	}

	private boolean checkBusiness() {
		return false;
	}

	@Override
	public Response getNeById(String neid, SecurityContext securityContext) throws NotFoundException {
		AdpNe ne = new AdpNe();
		try {
			ne = nesDbMgr.getNeById(neid);
			if (StringUtils.isEmpty(ne.getId())) {
				return failObjNotExist();
			}
		} catch (Exception e) {
			log.error("getNeById", e);
			return failDbOperation();
		}
		return Response.ok().entity(ne).build();
	}

	@Override
	public Response updateNe(AdpNe body, SecurityContext securityContext) throws NotFoundException {
		Response response = validateParam(body, MethodOperator.UPDATE);
		if (null != response) {
			return response;
		}

		AdpNe ne = null;
		String id = body.getId();
		try {
			ne = isNeExisted(id);
		} catch (AdapterException e) {
			return ErrorWrapperUtils.adapterException(e);
		}

		String moi = GenerateKeyOnNeUtil.getMoi(ne.getKeyOnNe());
		String groupId = moi.split("/")[0].replaceAll("neGroupId=", StringUtils.EMPTY);
		String neId = moi.split("/")[1].replaceAll("networkElementId=", StringUtils.EMPTY);

		try {
			OperationalStateEnum operationalState = body.getOperationalState();
			if (null == operationalState) {
				return updateNe(body);
			}
			switch (operationalState) {
			case SUPERVISING:
				return supervising(body, id, groupId, neId);
			case SYNCHRONIZING:
				return synchronizing(body, groupId, neId);
			default:
				return updateNe(body);
			}
		} catch (AdapterException e) {
			return ErrorWrapperUtils.adapterException(e);
		}
	}

	private Response updateNe(AdpNe body) throws AdapterException {
		try {
			nesDbMgr.updateNe(body);
		} catch (Exception e) {
			log.error("updateNe", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}

		return Response.ok().build();
	}

	/**
	 * 监管网元
	 * 
	 * @param body
	 * @param id
	 * @param groupId
	 * @param neId
	 * @return
	 */
	private Response supervising(AdpNe body, String id, String groupId, String neId) throws AdapterException {
		updateNe(body);

		boolean isSuccess = false;
		try {
			NotificationSender.instance().sendAvcNotif(EntityType.NE, body.getId(), "operationalState", "enum",
					OperationalStateEnum.SUPERVISING.name(), OperationalStateEnum.IDLE.name());
			isSuccess = StartSupervision.startSupervision(groupId, neId);
		} catch (Exception e) {
			log.error("failed to supervision ne", e);
			body.setOperationalState(OperationalStateEnum.IDLE);
			updateNe(body);
		}
		log.debug("isSuccess = " + isSuccess);
		if (!isSuccess) {
			throw new AdapterException(ErrorCode.FAIL_SUPERVISE_NE_BY_EMLIM);
		}
		NeStateMachineApp.instance().afterSuperviseNe(id);

		NotificationSender.instance().sendAvcNotif(EntityType.NE, id, "supervsionState", "enum",
				SupervisionStateEnum.SUPERVISED.name(), SupervisionStateEnum.UNSUPERVISED.name());
		NotificationSender.instance().sendAvcNotif(EntityType.NE, id, "communicationState", "enum",
				CommunicationStateEnum.UNREACHABLE.name(), CommunicationStateEnum.REACHABLE.name());
		NotificationSender.instance().sendAvcNotif(EntityType.NE, id, "operationalState", "enum",
				OperationalStateEnum.IDLE.name(), OperationalStateEnum.SUPERVISING.name());
		return Response.ok().build();
	}

	/**
	 * 同步TP
	 * 
	 * @param body
	 * @param groupId
	 * @param neId
	 * @return
	 */
	private Response synchronizing(AdpNe body, String groupId, String neId) throws AdapterException {
		updateNe(body);

		try {
			SyncTpThread thread = new SyncTpThread(groupId, neId, body.getId());
			FutureTask<Object> ft = new FutureTask<Object>(thread);
			new Thread(ft).start();
		} catch (Exception e) {
			log.error("failed to sync TP", e);
			if (e instanceof AdapterException) {
				AdapterException adpExp = (AdapterException) e;
				throw adpExp;
			}
			return Response.serverError().entity(e).build();
		}

		return Response.ok().build();
	}

	private Response failSuperviseNeByEMLIM() {
		return constructErrorInfo(ErrorCode.FAIL_SUPERVISE_NE_BY_EMLIM);
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
}
