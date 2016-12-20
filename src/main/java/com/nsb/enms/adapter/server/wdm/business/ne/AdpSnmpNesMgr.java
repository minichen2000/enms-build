package com.nsb.enms.adapter.server.wdm.business.ne;

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
import com.nsb.enms.adapter.server.common.utils.Ip2HexUtil;
import com.nsb.enms.adapter.server.common.utils.KeyValuePairUtil;
import com.nsb.enms.adapter.server.common.utils.String2AsciiUtil;
import com.nsb.enms.adapter.server.sdh.action.entity.NeEntity;
import com.nsb.enms.adapter.server.sdh.action.method.ne.DeleteNe;
import com.nsb.enms.adapter.server.sdh.action.method.ne.StartSupervision;
import com.nsb.enms.adapter.server.sdh.business.sync.SyncThread;
import com.nsb.enms.adapter.server.sdh.business.xc.AdpQ3XcsMgr;
import com.nsb.enms.adapter.server.statemachine.app.NeStateMachineApp;
import com.nsb.enms.common.CommunicationState;
import com.nsb.enms.common.EntityType;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.common.OperationState;
import com.nsb.enms.common.SNMPType;
import com.nsb.enms.common.SupervisionState;
import com.nsb.enms.common.utils.Pair;
import com.nsb.enms.common.utils.ValidationUtil;
import com.nsb.enms.common.utils.snmpclient.SnmpClient;
import com.nsb.enms.restful.model.adapter.AdpAddresses;
import com.nsb.enms.restful.model.adapter.AdpKVPair;
import com.nsb.enms.restful.model.adapter.AdpNe;
import com.nsb.enms.restful.model.adapter.AdpSnmpAddress;

public class AdpSnmpNesMgr {
	private final static Logger log = LogManager.getLogger(AdpSnmpNesMgr.class);
	private final static String ADP_ADDRESS = "135.251.99.58";
	// ConfLoader.getInstance().getConf(ConfigKey.ADP_ADDRESS,"");
	private static final String IP_ASCII = String2AsciiUtil.convert(ADP_ADDRESS);
	private static final int SNMP_PORT = 162;

	private AdpNesDbMgr nesDbMgr = new AdpNesDbMgr();

	public AdpSnmpNesMgr() {
	}

	public AdpNe addNe(AdpNe body) throws AdapterException {
		validateParam(body, MethodOperator.ADD);

		String snmpAgent = body.getAddresses().getSnmpAddress().getSnmpAgent();
		String agent[] = snmpAgent.split(":");
		SnmpClient client = new SnmpClient(agent[0], Integer.valueOf(agent[1]), "admin_snmp");
		client.start();
		delRegistedTrap(client);
		registTrap(client);
		activeTrap(client);
		setUserLabel(client, body.getUserLabel());
		String neRelease = getNeRelease(client);

		AdpNe ne = body;
		ne.setNeRelease(neRelease);

		try {
			ne = nesDbMgr.addNe(ne);
		} catch (Exception e) {
			log.error("addNe", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}

		Integer id = ne.getId();
		NotificationSender.instance().sendOcNotif(EntityType.NE, id);
		// NotificationSender.instance().sendAlarm(new
		// AlarmNENotSupervised(id));
		// NotificationSender.instance().sendAlarm(new AlarmNEMisalignment(id));

		log.debug("adapter----------------addNe----------end");

		return ne;
	}

	private void delRegistedTrap(SnmpClient client) throws AdapterException {
		try {
			boolean isOk = client.snmpSet("1.3.6.1.6.3.12.1.2.1.9." + IP_ASCII, SNMPType.SNMP_INTEGER, 6);
			if (!isOk) {
				log.error("failed to delRegistedTrap");
			}
		} catch (Exception e) {
			log.error("delRegistedTrap", e);
		}
	}

	private void registTrap(SnmpClient client) throws AdapterException {
		List<String> oids = getCommonOids();
		List<Pair<SNMPType, Object>> values = getCommonValues(5);

		try {
			boolean isOk = client.snmpMultiSet(oids, values);
			if (!isOk) {
				log.error("failed to registTrap");
			}
		} catch (Exception e) {
			log.error("registTrap", e);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<Pair<SNMPType, Object>> getCommonValues(int value) {
		List<Pair<SNMPType, Object>> values = new ArrayList<Pair<SNMPType, Object>>();
		values.add(new Pair(SNMPType.SNMP_STRING, "1.3.6.1.6.1.1"));
		values.add(new Pair(SNMPType.SNMP_STRING, Ip2HexUtil.convert(ADP_ADDRESS, SNMP_PORT)));
		values.add(new Pair(SNMPType.SNMP_INTEGER, 0));
		values.add(new Pair(SNMPType.SNMP_INTEGER, 0));
		values.add(new Pair(SNMPType.SNMP_INTEGER, 3));
		values.add(new Pair(SNMPType.SNMP_STRING, "Na_v2cPrams"));
		values.add(new Pair(SNMPType.SNMP_UINTEGER, 0));
		values.add(new Pair(SNMPType.SNMP_STRING, " v1v2_trap_dest"));
		values.add(new Pair(SNMPType.SNMP_INTEGER, value));
		return values;
	}

	private List<String> getCommonOids() {
		List<String> oids = new ArrayList<String>();
		oids.add("1.3.6.1.6.3.12.1.2.1.2." + IP_ASCII);
		oids.add("1.3.6.1.6.3.12.1.2.1.3." + IP_ASCII);
		oids.add("1.3.6.1.6.3.12.1.2.1.4." + IP_ASCII);
		oids.add("1.3.6.1.6.3.12.1.2.1.5." + IP_ASCII);
		oids.add("1.3.6.1.6.3.12.1.2.1.8." + IP_ASCII);
		oids.add("1.3.6.1.6.3.12.1.2.1.7." + IP_ASCII);
		oids.add("1.3.6.1.4.1.7483.2.1.1.2.9.1.1.1." + IP_ASCII);
		oids.add("1.3.6.1.6.3.12.1.2.1.6." + IP_ASCII);
		oids.add("1.3.6.1.6.3.12.1.2.1.9." + IP_ASCII);
		return oids;
	}

	private void activeTrap(SnmpClient client) {
		List<String> oids = getCommonOids();
		List<Pair<SNMPType, Object>> values = getCommonValues(1);
		try {
			boolean isOk = client.snmpMultiSet(oids, values);
			if (!isOk) {
				log.error("failed to activeTrap");
			}
		} catch (Exception e) {
			log.error("activeTrap", e);
		}
	}

	private void setUserLabel(SnmpClient client, String userLabel) {
		try {
			boolean isOk = client.snmpSet("1.3.6.1.4.1.7483.2.1.1.2.1.18", SNMPType.SNMP_STRING, userLabel);
			if (!isOk) {
				log.error("failed to setUserLabel");
			}
		} catch (Exception e) {
			log.error("setUserLabel", e);
		}
	}

	private String getNeRelease(SnmpClient client) {
		try {
			Pair<String, String> pair = client.snmpGet("1.3.6.1.4.1.7483.2.1.5.2.1.1.19");
			if (null != pair) {
				return pair.getSecond();
			} else {
				log.error("failed to getNeRelease");
			}
		} catch (Exception e) {
			log.error("setUserLabel", e);
		}
		return StringUtils.EMPTY;
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
		return ConfLoader.getInstance().getConf(ConfigKey.ADP_ID, "adapter_" + System.currentTimeMillis());
	}

	private void validateAddress(AdpAddresses addresses, MethodOperator operate) throws AdapterException {
		if (null == addresses) {
			if (MethodOperator.ADD == operate) {
				throw new AdapterException(ErrorCode.FAIL_INVALID_SNMP_ADDRESS);
			}
		}

		AdpSnmpAddress snmpAddress = addresses.getSnmpAddress();
		if (null == snmpAddress) {
			throw new AdapterException(ErrorCode.FAIL_INVALID_SNMP_ADDRESS);
		}

		String snmpAgent = snmpAddress.getSnmpAgent();
		if (StringUtils.isEmpty(snmpAgent)) {
			throw new AdapterException(ErrorCode.FAIL_INVALID_SNMP_ADDRESS);
		}

		String agent[] = snmpAgent.split(":");
		if (!ValidationUtil.isValidIpAddress(agent[0])) {
			throw new AdapterException(ErrorCode.FAIL_INVALID_SNMP_ADDRESS);
		}

		if (!ValidationUtil.isValidPort(agent[1])) {
			throw new AdapterException(ErrorCode.FAIL_INVALID_PORT);
		}
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

	public void deleteNe(Integer id) throws AdapterException {
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
			xcsMgr.deleteXcsByNeId(Integer.valueOf(neId));

			AdpTpsDbMgr tpsDbMgr = new AdpTpsDbMgr();
			tpsDbMgr.deleteTpsbyNeId(Integer.valueOf(neId));

			AdpEqusDbMgr equipmentsDbMgr = new AdpEqusDbMgr();
			equipmentsDbMgr.deleteEquipmentsByNeId(Integer.valueOf(neId));
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

	public void startSupervision(Integer neid) throws AdapterException {
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
		Integer id = body.getId();
		NeStateMachineApp.instance().afterSuperviseNe(id);

		NotificationSender.instance().sendAvcNotif(EntityType.NE, id, "supervsionState",
				SupervisionState.SUPERVISED.name(), SupervisionState.NOT_SUPERVISED.name());
		NotificationSender.instance().sendAvcNotif(EntityType.NE, id, "communicationState",
				CommunicationState.DISCONNECTED.name(), CommunicationState.CONNECTED.name());
		NotificationSender.instance().sendAvcNotif(EntityType.NE, id, "operationalState", OperationState.IDLE.name(),
				OperationState.SYNCING.name());
	}

	public void startAlignment(Integer neid) throws AdapterException {
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

	public static void main(String args[]) {
		AdpSnmpNesMgr mgr = new AdpSnmpNesMgr();
		AdpNe body = new AdpNe();
		AdpSnmpAddress snmpAddr = new AdpSnmpAddress();
		snmpAddr.setSnmpAgent("135.251.96.5:161");
		AdpAddresses addresses = new AdpAddresses();
		addresses.setSnmpAddress(snmpAddr);
		body.setAddresses(addresses);
		try {
			mgr.addNe(body);
		} catch (AdapterException e) {
			e.printStackTrace();
		}
	}
}
