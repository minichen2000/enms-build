package com.nsb.enms.adapter.server.sdh.business.ne;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.FutureTask;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.adapter.server.common.constants.ConfigKey;
import com.nsb.enms.adapter.server.common.constants.MethodOperator;
import com.nsb.enms.adapter.server.common.db.mgr.AdpEqusDbMgr;
import com.nsb.enms.adapter.server.common.db.mgr.AdpNesDbMgr;
import com.nsb.enms.adapter.server.common.db.mgr.AdpTpsDbMgr;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.notification.NotificationSender;
import com.nsb.enms.adapter.server.common.utils.KeyValuePairUtil;
import com.nsb.enms.adapter.server.sdh.action.entity.NeEntity;
import com.nsb.enms.adapter.server.sdh.action.method.ne.CreateNe;
import com.nsb.enms.adapter.server.sdh.action.method.ne.DeleteNe;
import com.nsb.enms.adapter.server.sdh.action.method.ne.SetManagerAddress;
import com.nsb.enms.adapter.server.sdh.action.method.ne.StartSupervision;
import com.nsb.enms.adapter.server.sdh.business.sync.SyncThread;
import com.nsb.enms.adapter.server.sdh.business.xc.AdpQ3XcsMgr;
import com.nsb.enms.adapter.server.statemachine.app.NeStateMachineApp;
import com.nsb.enms.alarm.AlarmNEMisalignment;
import com.nsb.enms.alarm.AlarmNENotSupervised;
import com.nsb.enms.common.AlignmentState;
import com.nsb.enms.common.CommunicationState;
import com.nsb.enms.common.EntityType;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.common.OperationState;
import com.nsb.enms.common.SupervisionState;
import com.nsb.enms.common.utils.ValidationUtil;
import com.nsb.enms.restful.model.adapter.AdpAddresses;
import com.nsb.enms.restful.model.adapter.AdpKVPair;
import com.nsb.enms.restful.model.adapter.AdpNe;
import com.nsb.enms.restful.model.adapter.AdpQ3Address;

public class AdpQ3NesMgr {
	private final static Logger log = LogManager.getLogger(AdpQ3NesMgr.class);
	private final static String NAMESERVERFILE_URL = ConfLoader.getInstance().getConf("NAMESERVERFILE_URL", "");
	private final static String ADP_ADDRESS = ConfLoader.getInstance().getConf(ConfigKey.ADP_ADDRESS, "");

	private AdpNesDbMgr nesDbMgr = new AdpNesDbMgr();

	public AdpQ3NesMgr() {
	}

	public AdpNe addNe(AdpNe body) throws AdapterException {
		validateParam(body, MethodOperator.ADD);

		AdpAddresses addresses = body.getAddresses();
		String osMain = addresses.getQ3Address().getOsMain();
		if (!ADP_ADDRESS.equals(osMain)) {
			String location = getLocationName();
			NeEntity entity = null;
			String q3Address = addresses.getQ3Address().getAddress().trim();
			entity = CreateNe.createNe(body.getId(), body.getNeRelease(), body.getNeType(), body.getUserLabel(),
					location, q3Address);

			if (null == entity) {
				throw new AdapterException(ErrorCode.FAIL_CREATE_NE_BY_EMLIM);
			}

			String moi = entity.getMoi();
			String groupId = moi.split("/")[0].split("=")[1];
			String neId = moi.split("/")[1].split("=")[1];
			boolean isSuccess = false;
			try {
				isSuccess = StartSupervision.startSupervision(groupId, neId);
			} catch (AdapterException e) {
				log.error("failed to supervision ne", e);
				throw e;
			}

			if (!isSuccess) {
				throw new AdapterException(ErrorCode.FAIL_SUPERVISE_NE_BY_EMLIM);
			}
			if (StringUtils.isNotEmpty(osMain)) {
				SetManagerAddress.setMainOSAddress(groupId, neId, osMain.trim());
			}

			String spareOSAddress = addresses.getQ3Address().getOsSpare();
			if (StringUtils.isNotEmpty(spareOSAddress)) {
				SetManagerAddress.setSpareOSAddress(groupId, neId, spareOSAddress.trim());
			}

			DeleteNe.deleteNe(groupId, neId);
			return null;
		} else {
			String location = getLocationName();
			NeEntity entity = null;
			String q3Address = addresses.getQ3Address().getAddress();
			entity = CreateNe.createNe(body.getId(), body.getNeRelease(), body.getNeType(), body.getUserLabel(),
					location, q3Address.trim());

			if (null == entity) {
				throw new AdapterException(ErrorCode.FAIL_CREATE_NE_BY_EMLIM);
			}

			log.debug(entity);

			AdpNe ne = constructNe(entity, body.getId(), body.getNeType());

			try {
				ne = nesDbMgr.addNe(ne);
			} catch (Exception e) {
				log.error("addNe", e);
				throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
			}

			String id = ne.getId();
			NotificationSender.instance().sendOcNotif(EntityType.NE, id);
			NotificationSender.instance().sendAlarm(new AlarmNENotSupervised(id));
			NotificationSender.instance().sendAlarm(new AlarmNEMisalignment(id));

			log.debug("adapter----------------addNe----------end");

			return ne;
		}
	}

	private void validateParam(AdpNe body, MethodOperator operate) throws AdapterException {
		String locationName = getLocationName();
		if (!ValidationUtil.isValidLocationName(locationName)) {
			throw new AdapterException(ErrorCode.FAIL_INVALID_LOCATION_NAME);
		}

		validateAddress(body.getAddresses(), operate);

		// TODO 其他校验条件
	}

	private String getLocationName() {
		String id = ConfLoader.getInstance().getConf(ConfigKey.ADP_ID, "adapter_" + System.currentTimeMillis());
		return id;
	}

	private void validateAddress(AdpAddresses addresses, MethodOperator operate) throws AdapterException {
		if (null == addresses) {
			if (MethodOperator.ADD == operate) {
				throw new AdapterException(ErrorCode.FAIL_INVALID_Q3_ADDRESS);
			}
		}
		AdpQ3Address q3Address = addresses.getQ3Address();
		if (!ValidationUtil.isValidQ3Address(q3Address.getAddress())) {
			throw new AdapterException(ErrorCode.FAIL_INVALID_Q3_ADDRESS);
		}

		if (!ValidationUtil.isValidQ3Address(q3Address.getAddress())) {
			throw new AdapterException(ErrorCode.FAIL_INVALID_Q3_MAIN_ADDRESS);
		}

		if (!ValidationUtil.isValidQ3Address(q3Address.getAddress())) {
			throw new AdapterException(ErrorCode.FAIL_INVALID_Q3_SPARE_ADDRESS);
		}
	}

	private AdpNe constructNe(NeEntity entity, String id, String neType) {
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
		ne.setOperationalState(OperationState.IDLE.name());
		ne.setCommunicationState(CommunicationState.DISCONNECTED.name());
		ne.setAlignmentState(AlignmentState.MISALIGNED.name());
		ne.setSupervisionState(SupervisionState.NOT_SUPERVISED.name());

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

	public void updateNe(AdpNe body) throws AdapterException {
		try {
			nesDbMgr.updateNe(body);
		} catch (Exception e) {
			log.error("updateNe", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
	}

	public void deleteNe(String id) throws AdapterException {
		boolean hasBusiness = checkBusiness();
		if (hasBusiness) {
			// TODO 确定错误码是否正确
			throw new AdapterException(ErrorCode.FAIL_NOT_OPERABLE);
		}

		AdpNe ne = isNeExisted(id);
		List<AdpKVPair> pairs = ne.getParams();
		String[] groupAndNeId = KeyValuePairUtil.getGroupAndNeId(pairs);
		String groupId = groupAndNeId[0];
		String neId = groupAndNeId[1];

		log.debug("groupId = " + groupId + ", neId = " + neId);

		DeleteNe.deleteNe(groupId, neId);
		NotificationSender.instance().sendOdNotif(EntityType.NE, id);

		// delete db record, contains ne and tp
		try {
			nesDbMgr.deleteNe(id);

			AdpQ3XcsMgr xcsMgr = new AdpQ3XcsMgr();
			xcsMgr.deleteXcsByNeId(neId);

			AdpTpsDbMgr tpsDbMgr = new AdpTpsDbMgr();
			tpsDbMgr.deleteTpsbyNeId(neId);

			AdpEqusDbMgr equipmentsDbMgr = new AdpEqusDbMgr();
			equipmentsDbMgr.deleteEquipmentsByNeId(neId);
		} catch (AdapterException e) {
			if (ErrorCode.FAIL_OBJ_NOT_EXIST != e.errorCode) {
				throw e;
			}
		} catch (Exception e) {
			log.error("deleteNe", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
	}

	private boolean checkBusiness() {
		return false;
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

	public void startSupervision(String neid) throws AdapterException {
		AdpNe ne = isNeExisted(neid);
		List<AdpKVPair> pairs = ne.getParams();
		String[] groupAndNeId = KeyValuePairUtil.getGroupAndNeId(pairs);
		String groupId = groupAndNeId[0];
		String neId = groupAndNeId[1];
		supervising(ne, groupId, neId);
	}

	/**
	 * 监管网元
	 * 
	 * @param body
	 * @param groupId
	 * @param neId
	 * @return
	 */
	private void supervising(AdpNe body, String groupId, String neId) throws AdapterException {
		updateNe(body);
		boolean isSuccess = false;
		try {
			NotificationSender.instance().sendAvcNotif(EntityType.NE, body.getId(), "operationalState",
					OperationState.SUPERVISING.name(), OperationState.IDLE.name());
			isSuccess = StartSupervision.startSupervision(groupId, neId);
		} catch (Exception e) {
			log.error("failed to supervision ne", e);
			body.setOperationalState(OperationState.IDLE.name());
			updateNe(body);
		}
		log.debug("isSuccess = " + isSuccess);
		if (!isSuccess) {
			throw new AdapterException(ErrorCode.FAIL_SUPERVISE_NE_BY_EMLIM);
		}
		String id = body.getId();
		NeStateMachineApp.instance().afterSuperviseNe(id);

		NotificationSender.instance().sendAvcNotif(EntityType.NE, id, "supervisionState",
				SupervisionState.SUPERVISED.name(), SupervisionState.NOT_SUPERVISED.name());
		NotificationSender.instance().sendAvcNotif(EntityType.NE, id, "communicationState",
				CommunicationState.DISCONNECTED.name(), CommunicationState.CONNECTED.name());
		NotificationSender.instance().sendAvcNotif(EntityType.NE, id, "operationalState", OperationState.IDLE.name(),
				OperationState.SYNCING.name());
	}

	public void startAlignment(String neid) throws AdapterException {
		AdpNe ne = isNeExisted(neid);
		List<AdpKVPair> pairs = ne.getParams();
		String[] groupAndNeId = KeyValuePairUtil.getGroupAndNeId(pairs);
		String groupId = groupAndNeId[0];
		String neId = groupAndNeId[1];
		synchronizing(ne, groupId, neId);
	}

	/**
	 * 同步TP
	 * 
	 * @param body
	 * @param groupId
	 * @param neId
	 * @return
	 */
	private void synchronizing(AdpNe body, String groupId, String neId) throws AdapterException {
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
		}
	}
}
