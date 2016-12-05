package com.nsb.enms.restful.adapterserver.api.impl;

import java.util.ArrayList;
import java.util.Date;
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
import com.nsb.enms.adapter.server.business.sync.SyncThread;
import com.nsb.enms.adapter.server.business.xc.AdpXcsMgr;
import com.nsb.enms.adapter.server.common.MethodOperator;
import com.nsb.enms.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.adapter.server.common.conf.ConfigKey;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.utils.ErrorWrapperUtils;
import com.nsb.enms.adapter.server.common.utils.KeyValuePairUtil;
import com.nsb.enms.adapter.server.db.mgr.AdpEqusDbMgr;
import com.nsb.enms.adapter.server.db.mgr.AdpNesDbMgr;
import com.nsb.enms.adapter.server.db.mgr.AdpTpsDbMgr;
import com.nsb.enms.adapter.server.db.mgr.AdpXcsDbMgr;
import com.nsb.enms.adapter.server.notification.NotificationSender;
import com.nsb.enms.adapter.server.statemachine.app.NeStateMachineApp;
import com.nsb.enms.alarm.AlarmNEMisalignment;
import com.nsb.enms.alarm.AlarmNENotSupervised;
import com.nsb.enms.common.EntityType;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.common.utils.ValidationUtil;
import com.nsb.enms.restful.adapterserver.api.NesApiService;
import com.nsb.enms.restful.adapterserver.api.NotFoundException;
import com.nsb.enms.restful.model.adapter.AdpAddresses;
import com.nsb.enms.restful.model.adapter.AdpEquipment;
import com.nsb.enms.restful.model.adapter.AdpKVPair;
import com.nsb.enms.restful.model.adapter.AdpNe;
import com.nsb.enms.restful.model.adapter.AdpQ3Address;
import com.nsb.enms.restful.model.adapter.AdpTp;
import com.nsb.enms.restful.model.adapter.AdpXc;
import com.nsb.enms.state.AlignmentState;
import com.nsb.enms.state.CommunicationState;
import com.nsb.enms.state.OperationalState;
import com.nsb.enms.state.SupervisionState;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-07-29T17:16:31.406+08:00")
public class NesApiServiceImpl extends NesApiService {
	private final static Logger log = LogManager.getLogger(NesApiServiceImpl.class);

	private AdpNesDbMgr nesDbMgr = new AdpNesDbMgr();
	private AdpTpsDbMgr tpsDbMgr = new AdpTpsDbMgr();
	private AdpEqusDbMgr equsDbMgr = new AdpEqusDbMgr();
	private AdpXcsDbMgr xcsDbMgr = new AdpXcsDbMgr();
	private AdpXcsMgr adpXcMgr = new AdpXcsMgr();

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
			String location = getLocationName();
			NeEntity entity = null;
			try {
				String q3Address = addresses.getQ3Address().getAddress().trim();
				entity = CreateNe.createNe(body.getNeRelease(), body.getNeType(), body.getUserLabel(), location,
						q3Address);
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
			String location = getLocationName();
			NeEntity entity = null;
			try {
				String q3Address = addresses.getQ3Address().getAddress();
				entity = CreateNe.createNe(body.getNeRelease(), body.getNeType(), body.getUserLabel(), location,
						q3Address.trim());
			} catch (AdapterException e) {
				log.error("create ne occur error", e);
				return ErrorWrapperUtils.adapterException(e);
			}

			if (null == entity) {
				return failCreateNeByEMLIM();
			}

			log.debug(entity);

			AdpNe ne = constructNe(entity, body.getId(), body.getNeType());

			try {
				ne = nesDbMgr.addNe(ne);
			} catch (Exception e) {
				log.error("addNe", e);
				return failDbOperation();
			}

			NotificationSender.instance().sendOcNotif(EntityType.NE, ne.getId());

			// Long eventTime = TimeUtil.getLocalTmfTime();
			// Long occureTime = eventTime;
			// NotificationSender.instance().sendAlarm(AlarmCode.ALM_NE_NOT_SUPERVISED,
			// AlarmType.COMMUNICATION,
			// AlarmSeverity.MAJOR, eventTime, occureTime, null, "",
			// EntityType.NE, ne.getId(), null, null,
			// AlarmCode.ALM_NE_NOT_SUPERVISED.getDescription());
			// NotificationSender.instance().sendAlarm(AlarmCode.ALM_NE_MISALIGNMENT,
			// AlarmType.COMMUNICATION,
			// AlarmSeverity.MAJOR, eventTime, occureTime, null, "",
			// EntityType.NE, ne.getId(), null, null,
			// AlarmCode.ALM_NE_MISALIGNMENT.getDescription());
			NotificationSender.instance().sendAlarm(new AlarmNENotSupervised(body.getId()));
			NotificationSender.instance().sendAlarm(new AlarmNEMisalignment(body.getId()));

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

		String locationName = getLocationName();
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

	private AdpNe constructNe(NeEntity entity, Integer id, String neType) {
		AdpNe ne = new AdpNe();
		ne.setId(id);
		ne.setUserLabel(entity.getUserLabel());
		ne.setNeRelease(entity.getNeRelease());

		AdpAddresses address = new AdpAddresses();
		AdpQ3Address q3Address = new AdpQ3Address();
		q3Address.setAddress(entity.getNetworkAddress());
		address.setQ3Address(q3Address);
		address.setTl1Address(new ArrayList<String>());
		address.setSnmpAddress(null);
		ne.setAddresses(address);

		ne.setNeType(neType);
		ne.setOperationalState(OperationalState.IDLE.name());
		ne.setCommunicationState(CommunicationState.DISCONNECTED.name());
		ne.setAlignmentState(AlignmentState.MISALIGNED.name());
		ne.setSupervisionState(SupervisionState.NOSUPERVISED.name());

		List<AdpKVPair> params = constructParams(entity);
		ne.setParams(params);
		return ne;
	}

	private List<AdpKVPair> constructParams(NeEntity entity) {
		String[] groupNeId = entity.getMoi().split("/");

		List<AdpKVPair> params = new ArrayList<AdpKVPair>();
		AdpKVPair neTypePair = new AdpKVPair();
		neTypePair.setKey("NE_TYPE");
		neTypePair.setValue(entity.getNeType());
		params.add(neTypePair);

		AdpKVPair neReleasePair = new AdpKVPair();
		neReleasePair.setKey("NE_RELEASE");
		neReleasePair.setValue(entity.getNeRelease());
		params.add(neReleasePair);

		AdpKVPair groupIdPair = new AdpKVPair();
		groupIdPair.setKey("GROUP_ID");
		groupIdPair.setValue(groupNeId[0].split("=")[1]);
		params.add(groupIdPair);

		AdpKVPair neIdPair = new AdpKVPair();
		neIdPair.setKey("NE_ID");
		neIdPair.setValue(groupNeId[1].split("=")[1]);
		params.add(neIdPair);

		AdpKVPair nameServerUrlPair = new AdpKVPair();
		nameServerUrlPair.setKey("NAMESERVERFILE_URL");
		nameServerUrlPair.setValue(NAMESERVERFILE_URL);
		params.add(nameServerUrlPair);

		return params;
	}

	@Override
	public Response deleteNe(String neid, SecurityContext securityContext) throws NotFoundException {
		log.debug("adapter------deleteNE");

		Integer id = Integer.valueOf(neid);
		boolean hasBusiness = checkBusiness();
		if (hasBusiness) {
			// TODO 确定错误码是否正确
			return constructErrorInfo(ErrorCode.FAIL_NOT_OPERABLE);
		}

		AdpNe ne = null;
		try {
			ne = isNeExisted(id);
		} catch (AdapterException e) {
			return ErrorWrapperUtils.adapterException(e);
		}

		List<AdpKVPair> pairs = ne.getParams();
		String[] groupAndNeId = KeyValuePairUtil.getGroupAndNeId(pairs);
		String groupId = groupAndNeId[0];
		String neId = groupAndNeId[1];

		log.debug("groupId = " + groupId + ", neId = " + neId);

		try {
			DeleteNe.deleteNe(groupId, neId);
			NotificationSender.instance().sendOdNotif(EntityType.NE, id);
		} catch (AdapterException e) {
			log.error("deleteNe", e);
			return ErrorWrapperUtils.adapterException(e);
		}

		// delete db record, contains ne and tp
		try {
			nesDbMgr.deleteNe(id);

			AdpXcsMgr xcsMgr = new AdpXcsMgr();
			xcsMgr.deleteXcsByNeId(Integer.valueOf(neId));

			AdpTpsDbMgr tpsDbMgr = new AdpTpsDbMgr();
			tpsDbMgr.deleteTpsbyNeId(Integer.valueOf(neId));

			AdpEqusDbMgr equipmentsDbMgr = new AdpEqusDbMgr();
			equipmentsDbMgr.deleteEquipmentsByNeId(Integer.valueOf(neId));
		} catch (AdapterException e) {
			if (ErrorCode.FAIL_OBJ_NOT_EXIST != e.errorCode) {
				return ErrorWrapperUtils.adapterException(e);
			}
		} catch (Exception e) {
			return failDbOperation();
		}

		return Response.ok().build();
	}

	private AdpNe isNeExisted(Integer neid) throws AdapterException {
		AdpNe ne;
		try {
			ne = nesDbMgr.getNeById(neid);
			log.debug("ne = " + ne);
			if (null == ne || ne.getId() < 0) {
				throw new AdapterException(ErrorCode.FAIL_OBJ_NOT_EXIST);
			}
		} catch (Exception e) {
			log.error("getNeById", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}

		return ne;
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
			ne = nesDbMgr.getNeById(Integer.valueOf(neid));
			if (ne.getId() < 0) {
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
		// Response response = validateParam(body, MethodOperator.UPDATE);
		// if (null != response) {
		// return response;
		// }
		//
		// AdpNe ne = null;
		// Integer id = body.getId();
		// try {
		// ne = isNeExisted(id);
		// } catch (AdapterException e) {
		// return ErrorWrapperUtils.adapterException(e);
		// }
		//
		// List<AdpKVPair> pairs = ne.getParams();
		// String[] groupAndNeId = KeyValuePairUtil.getGroupAndNeId(pairs);
		// String groupId = groupAndNeId[0];
		// String neId = groupAndNeId[1];
		//
		// try {
		// Integer operationalState = body.getOperationalState();
		// if (null == operationalState) {
		// return updateNe(body);
		// }
		// if (body.getSupervisionState()) {
		// }
		// switch (operationalState) {
		// case SUPERVISING:
		// return supervising(body, groupId, neId);
		// case SYNCHRONIZING:
		// return synchronizing(body, groupId, neId);
		// default:
		// return updateNe(body);
		// }
		// } catch (AdapterException e) {
		// return ErrorWrapperUtils.adapterException(e);
		// }
		return null;
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
	 * @param groupId
	 * @param neId
	 * @return
	 */
	private Response supervising(AdpNe body, String groupId, String neId) throws AdapterException {
		updateNe(body);

		boolean isSuccess = false;
		try {
			NotificationSender.instance().sendAvcNotif(EntityType.NE, body.getId(), "operationalState",
					OperationalState.DOING.name(), OperationalState.IDLE.name());
			isSuccess = StartSupervision.startSupervision(groupId, neId);
		} catch (Exception e) {
			log.error("failed to supervision ne", e);
			body.setOperationalState(OperationalState.IDLE.name());
			updateNe(body);
		}
		log.debug("isSuccess = " + isSuccess);
		if (!isSuccess) {
			throw new AdapterException(ErrorCode.FAIL_SUPERVISE_NE_BY_EMLIM);
		}
		Integer id = body.getId();
		NeStateMachineApp.instance().afterSuperviseNe(id);

		NotificationSender.instance().sendAvcNotif(EntityType.NE, id, "supervsionState",
				SupervisionState.SUPERVISED.name(), SupervisionState.NOSUPERVISED.name());
		NotificationSender.instance().sendAvcNotif(EntityType.NE, id, "communicationState",
				CommunicationState.DISCONNECTED.name(), CommunicationState.CONNECTED.name());
		NotificationSender.instance().sendAvcNotif(EntityType.NE, id, "operationalState", OperationalState.IDLE.name(),
				OperationalState.DOING.name());
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
			SyncThread thread = new SyncThread(groupId, neId, body.getId());
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

	@Override
	public Response addTps(String neid, List<AdpTp> tps, SecurityContext securityContext) throws NotFoundException {
		return null;
	}

	@Override
	public Response getChildrenTps(String neid, String tpid, SecurityContext securityContext) throws NotFoundException {
		List<AdpTp> tpList = new ArrayList<AdpTp>();
		try {
			tpList = tpsDbMgr.getChildrenTps(Integer.valueOf(neid), Integer.valueOf(tpid));
		} catch (Exception e) {
			log.error("getChildrenTps", e);
			return Response.serverError().entity(e).build();
		}
		return Response.ok().entity(tpList).build();
	}

	@Override
	public Response getNeTps(String neid, SecurityContext securityContext) throws NotFoundException {
		System.out.println("getNeTps, neId = " + neid);
		List<AdpTp> tpList = new ArrayList<AdpTp>();
		try {
			tpList = tpsDbMgr.getTpsByNeId(Integer.valueOf(neid));
		} catch (Exception e) {
			log.error("getTPByNEId", e);
			return Response.serverError().entity(e).build();
		}
		return Response.ok().entity(tpList).build();
	}

	@Override
	public Response getTpById(String neid, String tpid, SecurityContext securityContext) throws NotFoundException {
		System.out.println("getTpById, neId = " + neid);
		AdpTp tp;
		try {
			tp = tpsDbMgr.getTpById(Integer.valueOf(neid), Integer.valueOf(tpid));
		} catch (Exception e) {
			log.error("getTPByNEId", e);
			return Response.serverError().entity(e).build();
		}
		return Response.ok().entity(tp).build();
	}

	@Override
	public Response getTpsByLayerRate(String neid, String layerrate, SecurityContext securityContext)
			throws NotFoundException {
		List<AdpTp> tpList = new ArrayList<AdpTp>();
		try {
			tpList = tpsDbMgr.getTpsByLayerRate(Integer.valueOf(neid), layerrate);
		} catch (Exception e) {
			log.error("getTPsByLayerrate", e);
			return Response.serverError().entity(e).build();
		}
		return Response.ok().entity(tpList).build();
	}

	@Override
	public Response getTpsByType(String neid, String tptype, SecurityContext securityContext) throws NotFoundException {
		Date begin = new Date();
		List<AdpTp> tpList = new ArrayList<AdpTp>();
		try {
			tpList = tpsDbMgr.getTpsByType(Integer.valueOf(neid), tptype);
		} catch (Exception e) {
			log.error("getTPsByType", e);
			return Response.serverError().entity(e).build();
		}

		Date end = new Date();
		log.debug("adapter.getTPsByType cost time = " + (end.getTime() - begin.getTime()));

		return Response.ok().entity(tpList).build();
	}

	@Override
	public Response updateTp(String neid, AdpTp tp, SecurityContext securityContext) throws NotFoundException {
		return null;
	}

	private String getLocationName() {
		String id = ConfLoader.getInstance().getConf(ConfigKey.ADP_ID, "adapter_" + System.currentTimeMillis());
		return id;
	}

	@Override
	public Response deleteXc(String neid, String xcid, SecurityContext securityContext) throws NotFoundException {
		try {
			adpXcMgr.deleteXcById(Integer.valueOf(neid), Integer.valueOf(xcid));
		} catch (AdapterException e) {
			log.error("deleteXC", e);
			return ErrorWrapperUtils.adapterException(e);
		}
		return Response.ok().build();
	}

	@Override
	public Response getEquipmentById(String neid, String eqid, SecurityContext securityContext)
			throws NotFoundException {
		return null;
	}

	@Override
	public Response getXcById(String neid, String xcid, SecurityContext securityContext) throws NotFoundException {
		AdpXc xc = new AdpXc();
		try {
			xc = xcsDbMgr.getXcById(Integer.valueOf(neid), Integer.valueOf(xcid));
		} catch (Exception e) {
			log.error("getXCById", e);
		}
		return Response.ok().entity(xc).build();
	}

	@Override
	public Response startAlignment(String neid, SecurityContext securityContext) throws NotFoundException {
		AdpNe ne;
		try {
			ne = isNeExisted(Integer.valueOf(neid));
		} catch (AdapterException e) {
			return ErrorWrapperUtils.adapterException(e);
		}

		List<AdpKVPair> pairs = ne.getParams();
		String[] groupAndNeId = KeyValuePairUtil.getGroupAndNeId(pairs);
		String groupId = groupAndNeId[0];
		String neId = groupAndNeId[1];
		try {
			return synchronizing(ne, groupId, neId);
		} catch (AdapterException e) {
			return ErrorWrapperUtils.adapterException(e);
		}
	}

	@Override
	public Response startSupervision(String neid, SecurityContext securityContext) throws NotFoundException {
		AdpNe ne;
		try {
			ne = isNeExisted(Integer.valueOf(neid));
		} catch (AdapterException e) {
			return ErrorWrapperUtils.adapterException(e);
		}

		List<AdpKVPair> pairs = ne.getParams();
		String[] groupAndNeId = KeyValuePairUtil.getGroupAndNeId(pairs);
		String groupId = groupAndNeId[0];
		String neId = groupAndNeId[1];
		try {
			return supervising(ne, groupId, neId);
		} catch (AdapterException e) {
			return ErrorWrapperUtils.adapterException(e);
		}
	}

	@Override
	public Response createXc(String neid, AdpXc xc, SecurityContext securityContext) throws NotFoundException {
		AdpXc newXc = null;
		try {
			newXc = adpXcMgr.createXc(Integer.valueOf(neid), xc);
		} catch (AdapterException e) {
			log.error("createXc", e);
			return ErrorWrapperUtils.adapterException(e);
		}
		return Response.ok().entity(newXc).build();
	}

	@Override
	public Response deleteXcsByNeId(String neid, SecurityContext securityContext) throws NotFoundException {
		try {
			adpXcMgr.deleteXcsByNeId(Integer.valueOf(neid));
		} catch (AdapterException e) {
			log.error("deleteXcsByNeId", e);
			return ErrorWrapperUtils.adapterException(e);
		}
		return Response.ok().build();
	}

	@Override
	public Response getEquipmentsByNeId(String neid, SecurityContext securityContext) throws NotFoundException {
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
	public Response getXcsByNeId(String neid, SecurityContext securityContext) throws NotFoundException {
		// TODO Auto-generated method stub
		return null;
	}
}
