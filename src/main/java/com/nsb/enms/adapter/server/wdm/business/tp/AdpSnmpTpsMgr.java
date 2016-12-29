package com.nsb.enms.adapter.server.wdm.business.tp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.db.mgr.AdpEqusDbMgr;
import com.nsb.enms.adapter.server.common.db.mgr.AdpSeqDbMgr;
import com.nsb.enms.adapter.server.common.db.mgr.AdpTpsDbMgr;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.utils.Object2IntegerUtil;
import com.nsb.enms.adapter.server.wdm.action.entity.SnmpTpEntity;
import com.nsb.enms.adapter.server.wdm.business.tp.ctp.Get130Scx10Ctps;
import com.nsb.enms.adapter.server.wdm.constants.SnmpDirection;
import com.nsb.enms.adapter.server.wdm.constants.SnmpTpType;
import com.nsb.enms.adapter.server.wdm.factory.AdpSnmpClientFactory;
import com.nsb.enms.adapter.server.wdm.utils.PSSBoardUtil;
import com.nsb.enms.adapter.server.wdm.utils.SnmpTpUserLabelUtil;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.common.LayerRate;
import com.nsb.enms.common.TpType;
import com.nsb.enms.common.utils.Pair;
import com.nsb.enms.common.utils.snmp.SnmpClient;
import com.nsb.enms.restful.model.adapter.AdpEquipment;
import com.nsb.enms.restful.model.adapter.AdpKVPair;
import com.nsb.enms.restful.model.adapter.AdpTp;

public class AdpSnmpTpsMgr {
	private final static Logger log = LogManager.getLogger(AdpSnmpTpsMgr.class);

	private SnmpClient client;
	private AdpTpsDbMgr tpsMgr = new AdpTpsDbMgr();
	private AdpEqusDbMgr equsMgr = new AdpEqusDbMgr();
	private Integer neId;

	public AdpSnmpTpsMgr(Integer neId) {
		this.neId = neId;
		try {
			this.client = AdpSnmpClientFactory.getInstance().getByNeId(neId);
		} catch (AdapterException e) {
			log.error("getByNeId", e);
		}
	}

	private List<List<Pair<String, String>>> getTableValues(List<String> oids) {
		List<List<Pair<String, String>>> values = new ArrayList<List<Pair<String, String>>>();
		try {
			values = client.snmpWalkTableView(oids);
		} catch (IOException e) {
			log.error("snmpWalkTableView", e);
		}
		return values;
	}

	public void syncTps() throws AdapterException {
		List<AdpTp> ptpList = syncPtps();
		syncCtps(ptpList);
	}

	private List<AdpTp> syncPtps() throws AdapterException {
		List<String> oids = setOidParams();
		List<List<Pair<String, String>>> values = getTableValues(oids);
		List<AdpTp> tpList = constructTpList(values);
		updatePtpConnectedIfIndex(tpList);
		return tpList;
	}

	private void syncCtps(List<AdpTp> ptpList) throws AdapterException {
		Get130Scx10Ctps ctps = new Get130Scx10Ctps();
		for (AdpTp tp : ptpList) {
			String keyOnNe = tp.getKeyOnNe();
			if (is130Scx10(keyOnNe)) {
				ctps.syncNotOtnSignalCtpsAndXc(neId, tp.getId(), keyOnNe);
			}
		}
	}

	private List<AdpTp> constructTpList(List<List<Pair<String, String>>> values) throws AdapterException {
		SnmpTpEntity entity = new SnmpTpEntity();
		List<AdpTp> tpList = new ArrayList<AdpTp>();
		for (List<Pair<String, String>> row : values) {
			String index = row.get(0).getSecond();
			AdpTp tp = isTpExisted(index);
			if (null != tp) {
				tpList.add(tp);
				continue;
			}

			String equType = isExpectedEquExisted(index);
			if (StringUtils.isEmpty(equType)) {
				continue;
			}

			constructTpEntity(entity, row, index);
			tp = constructTp(entity, TpType.PTP, equType, null, 0);
			addTp2Db(tp);
			tpList.add(tp);
		}
		return tpList;
	}

	private void constructTpEntity(SnmpTpEntity entity, List<Pair<String, String>> row, String index) {
		try {
			entity.setIndex(index);
			entity.setTpType(row.get(1).getSecond());
			entity.setAdminStatus(Object2IntegerUtil.toInt(row.get(2).getSecond()));
			entity.setOperStatus(Object2IntegerUtil.toInt(row.get(3).getSecond()));
			entity.setInternalType(row.get(4).getSecond());
			List<String> supportedTypes = new ArrayList<String>();
			supportedTypes.add(row.get(5).getSecond());
			entity.setSupportedTypes(supportedTypes);
			entity.setSecondaryState(Object2IntegerUtil.toInt(row.get(6).getSecond()));
			String address = row.get(7).getSecond();
			String ifIndex = row.get(8).getSecond();
			int endType = Object2IntegerUtil.toInt(row.get(9).getSecond());
			switch (endType) {
			case 1: // notConnected
				entity.setConnectedTo("");
				break;
			case 2: // internal
				String tpId = queryPtpId(ifIndex);
				if (tpId == null)
					entity.setConnectedTo("ifIndex/" + ifIndex);
				else
					entity.setConnectedTo(tpId);
				break;
			case 3: // external
				entity.setConnectedTo(address + "/" + ifIndex);
				break;
			}
			log.debug("connected to = " + entity.getConnectedTo());
			entity.setDirection(row.get(10).getSecond());
			address = row.get(11).getSecond();
			ifIndex = row.get(12).getSecond();
			endType = Object2IntegerUtil.toInt(row.get(13).getSecond());
			switch (endType) {
			case 1: // notConnected
				entity.setConnectedFrom("");
				break;
			case 2: // internal
				String tpId = queryPtpId(ifIndex);
				if (tpId == null)
					entity.setConnectedFrom("ifIndex/" + ifIndex);
				else
					entity.setConnectedFrom(tpId);
				break;
			case 3: // external
				entity.setConnectedFrom(address + "/" + ifIndex);
				break;
			}
			log.debug("connected to = " + entity.getConnectedFrom());

			entity.setSfpPortModuleVendorSerNo(row.get(14).getSecond());
			entity.setSfpPortModuleVendor(row.get(15).getSecond());
			entity.setSfpPortModuleType(row.get(16).getSecond());
			entity.setSfpPortCLEI(row.get(17).getSecond());
			entity.setSfpPortUnitPartNum(row.get(18).getSecond());
			entity.setSfpPortSWPartNum(row.get(19).getSecond());
			entity.setSfpPortFactoryID(row.get(20).getSecond());
			entity.setSfpPortDate(row.get(21).getSecond());
			entity.setSfpPortExtraData(row.get(22).getSecond());

		} catch (Exception e) {
			log.error("constructTpEntity", e);
		}
	}

	private AdpTp isTpExisted(String keyOnNe) throws AdapterException {
		try {
			AdpTp tpFromDb = tpsMgr.getTpByKeyOnNe(neId, keyOnNe);
			if (null != tpFromDb && null != tpFromDb.getId() && -1 < tpFromDb.getId()) {
				return tpFromDb;
			}
		} catch (Exception e) {
			log.error("getTpByKeyOnNe", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
		return null;
	}

	private AdpTp addTp2Db(AdpTp tp) throws AdapterException {
		try {
			tp = tpsMgr.addTp(tp);
		} catch (Exception e) {
			log.error("addTp", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
		return tp;
	}

	private boolean updateTp2Db(AdpTp tp) throws AdapterException {
		boolean ret = false;
		try {
			ret = tpsMgr.updateTp(tp);
		} catch (Exception e) {
			log.error("updateTp", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
		return ret;
	}

	private List<String> setOidParams() {
		List<String> oids = new ArrayList<String>();
		oids.add("1.3.6.1.2.1.2.2.1.3"); // ifType
		oids.add("1.3.6.1.2.1.2.2.1.7"); // ifAdminStatus
		oids.add("1.3.6.1.2.1.2.2.1.8"); // ifOperStatus
		oids.add("1.3.6.1.4.1.7483.2.2.4.1.2.2.1.2"); // tnIfType
		oids.add("1.3.6.1.4.1.7483.2.2.4.1.2.2.1.3"); // tnIfSupportedTypes
		oids.add("1.3.6.1.4.1.7483.2.2.4.1.2.1.1.5"); // tnAccessPortStateQualifier
		oids.add("1.3.6.1.4.1.7483.2.2.4.1.2.1.1.6"); // tnAccessPortFarEndAddress
		oids.add("1.3.6.1.4.1.7483.2.2.4.1.2.1.1.7"); // tnAccessPortFarEndIfIndex
		oids.add("1.3.6.1.4.1.7483.2.2.4.1.2.1.1.8"); // tnAccessPortFarEndType
		oids.add("1.3.6.1.4.1.7483.2.2.4.1.2.1.1.9"); // tnAccessPortDirection
		oids.add("1.3.6.1.4.1.7483.2.2.4.1.2.1.1.15"); // tnAccessPortFarEndAddressConnFrom
		oids.add("1.3.6.1.4.1.7483.2.2.4.1.2.1.1.16"); // tnAccessPortFarEndIfIndexConnFrom
		oids.add("1.3.6.1.4.1.7483.2.2.4.1.2.1.1.17"); // tnAccessPortFarEndTypeConnFrom

		oids.add("1.3.6.1.4.1.7483.2.2.4.3.5.46.1.2"); // tnSfpPortModuleVendorSerNo
		oids.add("1.3.6.1.4.1.7483.2.2.4.3.5.46.1.4"); // tnSfpPortModuleVendor
		oids.add("1.3.6.1.4.1.7483.2.2.4.3.5.46.1.7"); // tnSfpPortModuleType
		oids.add("1.3.6.1.4.1.7483.2.2.4.3.5.46.1.8"); // tnSfpPortCLEI
		oids.add("1.3.6.1.4.1.7483.2.2.4.3.5.46.1.9"); // tnSfpPortUnitPartNum
		oids.add("1.3.6.1.4.1.7483.2.2.4.3.5.46.1.10"); // tnSfpPortSWPartNum
		oids.add("1.3.6.1.4.1.7483.2.2.4.3.5.46.1.11"); // tnSfpPortFactoryID
		oids.add("1.3.6.1.4.1.7483.2.2.4.3.5.46.1.12"); // tnSfpPortDate
		oids.add("1.3.6.1.4.1.7483.2.2.4.3.5.46.1.13"); // tnSfpPortExtraData

		return oids;
	}

	private AdpTp constructTp(SnmpTpEntity tp, TpType tpType, String equType, Integer ptpId, Integer parentTpId)
			throws AdapterException {
		AdpTp adpTp = new AdpTp();
		Integer maxTpId;
		try {
			maxTpId = AdpSeqDbMgr.getMaxTpId(neId);
		} catch (Exception e) {
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
		adpTp.setId(maxTpId);
		adpTp.setNeId(neId);
		String userLabel = setUserLabel(tp.getIndex(), equType);
		adpTp.setUserLabel(userLabel);
		adpTp.setNativeName(userLabel);
		List<String> layerRates = new ArrayList<String>();
		String actualTpType = SnmpTpType.getTpType(tp.getInternalType());
		layerRates.add(actualTpType);
		if (TpType.PTP == tpType && !actualTpType.equalsIgnoreCase(SnmpTpType.PHN.name())) {
			layerRates.add(LayerRate.PHYSICAL.name());
		}
		adpTp.setLayerRates(layerRates);
		adpTp.setKeyOnNe(tp.getIndex());
		adpTp.setTpType(tpType.name());
		if (TpType.PTP != tpType) {
			adpTp.setPtpID(maxTpId);
			adpTp.setParentTpID(parentTpId);
		}
		adpTp.setDirection(SnmpDirection.getDirection(tp.getDirection()));

		if (TpType.PTP == tpType)
			constructPtpParameters(adpTp, tp, tpType, equType);

		return adpTp;
	}

	private String isExpectedEquExisted(String index) throws AdapterException {
		Integer[] position = SnmpTpUserLabelUtil.string2Hex(index);
		String equKeyOnNe = "1/" + position[0] + "/" + position[1];
		AdpEquipment equ = null;
		try {
			equ = equsMgr.getEquByKeyOnNe(neId, equKeyOnNe);
		} catch (Exception e) {
			log.error("getEquByKeyOnNe", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}

		if (null == equ) {
			log.error("equ is null,queried by " + equKeyOnNe);
			return null;
		}
		String expectedType = equ.getExpectedType();
		if (StringUtils.isEmpty(expectedType) || StringUtils.equals("Empty", expectedType)) {
			log.error("there is no expectedType of equ, queried by " + index);
			return null;
		}
		return expectedType;
	}

	private String setUserLabel(String index, String equType) {
		Integer[] position = SnmpTpUserLabelUtil.string2Hex(index);
		String portName = PSSBoardUtil.getPSSPortDesc(equType, position[2]);
		String userLabel = equType + "-" + position[0] + "-" + position[1] + "-" + portName;
		return userLabel;
	}

	private boolean is130Scx10(String index) throws AdapterException {
		String ExpectedType = isExpectedEquExisted(index);
		if ("130SCX10".equalsIgnoreCase(ExpectedType)) {
			return true;
		}
		return false;
	}

	private void constructPtpParameters(AdpTp adpTp, SnmpTpEntity tp, TpType tpType, String equType) {
		AdpKVPair pair = new AdpKVPair();
		pair.setKey("adminState");
		pair.setValue(String.valueOf(tp.getAdminStatus()));
		adpTp.addParamsItem(pair);

		pair = new AdpKVPair();
		pair.setKey("operStatus");
		pair.setValue(String.valueOf(tp.getOperStatus()));
		adpTp.addParamsItem(pair);

		pair = new AdpKVPair();
		pair.setKey("secondaryState");
		pair.setValue(String.valueOf(tp.getSecondaryState()));
		adpTp.addParamsItem(pair);

		pair = new AdpKVPair();
		pair.setKey("supportedLayers");
		pair.setValue(String.valueOf(tp.getSupportedTypes()));
		adpTp.addParamsItem(pair);

		pair = new AdpKVPair();
		pair.setKey("signalRate");
		pair.setValue(tp.getInternalType());
		adpTp.addParamsItem(pair);

		pair = new AdpKVPair();
		pair.setKey("signalRate");
		pair.setValue(tp.getInternalType());
		adpTp.addParamsItem(pair);

		pair = new AdpKVPair(); // Vendor
		pair.setKey("sfpPortModuleVendor");
		pair.setValue(tp.getSfpPortModuleVendor());
		adpTp.addParamsItem(pair);

		pair = new AdpKVPair();
		pair.setKey("sfpPortModuleType"); // Module Type
		pair.setValue(tp.getSfpPortModuleType());
		adpTp.addParamsItem(pair);

		pair = new AdpKVPair();
		pair.setKey("sfpPortCLEI"); // CLEI
		pair.setValue(tp.getSfpPortCLEI());
		adpTp.addParamsItem(pair);

		pair = new AdpKVPair();
		pair.setKey("sfpPortUnitPartNum"); // Unit Part Number
		pair.setValue(tp.getSfpPortUnitPartNum());
		adpTp.addParamsItem(pair);
		pair = new AdpKVPair();
		pair.setKey("sfpPortFactoryID"); // Factory ID
		pair.setValue(tp.getSfpPortFactoryID());
		adpTp.addParamsItem(pair);

		pair = new AdpKVPair();
		pair.setKey("sfpPortSWPartNum"); // Software Part Number
		pair.setValue(tp.getSfpPortSWPartNum());
		adpTp.addParamsItem(pair);
		pair = new AdpKVPair();
		pair.setKey("sfpPortModuleVendorSerNo"); // Serial Number
		pair.setValue(tp.getSfpPortModuleVendorSerNo());
		adpTp.addParamsItem(pair);

		pair = new AdpKVPair();
		pair.setKey("sfpPortDate"); // Date
		pair.setValue(tp.getSfpPortDate());

		adpTp.addParamsItem(pair);
		pair = new AdpKVPair();
		pair.setKey("sfpPortExtraData"); // Extra Data
		pair.setValue(tp.getSfpPortExtraData());
		adpTp.addParamsItem(pair);

		pair = new AdpKVPair();
		pair.setKey("connectedTo");
		pair.setValue(tp.getConnectedTo());
		adpTp.addParamsItem(pair);

		pair = new AdpKVPair();
		pair.setKey("connectedFrom");
		pair.setValue(tp.getConnectedFrom());
		adpTp.addParamsItem(pair);

		// 130SCX10 Client PTP specail Param
		if (TpType.PTP == tpType && equType.equals("130SCX10")) {
			String userLabel = adpTp.getUserLabel();
			String[] labels = userLabel.split("-");
			if (labels.length == 4) {
				String portName = labels[labels.length - 1];
				if (portName.substring(0, 1).equals("C")) { // Client PTP
					pair = new AdpKVPair();
					pair.setKey("supportedContainer");
					pair.setValue("ODU2");
					adpTp.addParamsItem(pair);
				}
			}
		}
	}

	private String queryPtpId(String keyOnNe) {
		try {
			AdpTp tpFromDb = tpsMgr.getTpByKeyOnNe(neId, keyOnNe);
			if (null == tpFromDb || null == tpFromDb.getId()) {
				log.info("tpFromDb is null, " + "keyOnNe is " + keyOnNe);
				return null;
			}
			return String.valueOf(tpFromDb.getId());
		} catch (Exception e) {
			log.error("getTpByKeyOnNe", e);
			return null;
		}
	}

	private void updatePtpConnectedIfIndex(List<AdpTp> tpList) throws AdapterException {
		boolean bUpdate;
		for (AdpTp tp : tpList) {
			bUpdate = false;
			List<AdpKVPair> params = tp.getParams();
			for (AdpKVPair pair : params) {
				String key = pair.getKey();
				if (!key.equals("connectedTo") && key.equals("connectedFrom"))
					continue;
				String value = pair.getValue();
				if (isIfIndex(value)) {
					String ifIndex = value.substring(8);
					String tpId = queryPtpId(ifIndex);
					if (tpId != null) {
						pair.setKey(tpId);
						bUpdate = true;
					}
				}
			}
			if (bUpdate)
				updateTp2Db(tp);
		}
	}

	private boolean isIfIndex(String value) {
		return value != null && value.startsWith("ifIndex/");
	}

	public static void main(String args[]) {
		SnmpClient client = new SnmpClient("135.251.96.5", 161, "admin_snmp");
		AdpSnmpClientFactory.getInstance().add("135.251.96.5:161", client);
		AdpSnmpTpsMgr mgr = new AdpSnmpTpsMgr(5);
		try {
			mgr.syncTps();
		} catch (AdapterException e) {
			e.printStackTrace();
		}

	}
}
