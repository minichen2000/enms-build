package com.nsb.enms.adapter.server.wdm.business.tp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.business.itf.ObjectIdGenerator;
import com.nsb.enms.adapter.server.common.db.mgr.AdpEqusDbMgr;
import com.nsb.enms.adapter.server.common.db.mgr.AdpTpsDbMgr;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.utils.Object2IntegerUtil;
import com.nsb.enms.adapter.server.wdm.action.entity.SnmpTpEntity;
import com.nsb.enms.adapter.server.wdm.business.tp.ctp.oa.GetAHPHGCTPs;
import com.nsb.enms.adapter.server.wdm.business.tp.ctp.ot.Get130SCX10CTPs;
import com.nsb.enms.adapter.server.wdm.business.tp.ctp.sfd.GetSFD44CTPs;
import com.nsb.enms.adapter.server.wdm.business.xc.AdpSnmpXcsMgr;
import com.nsb.enms.adapter.server.wdm.constants.SnmpDirection;
import com.nsb.enms.adapter.server.wdm.constants.SnmpEquType;
import com.nsb.enms.adapter.server.wdm.constants.SnmpTpType;
import com.nsb.enms.adapter.server.wdm.factory.AdpSnmpClientFactory;
import com.nsb.enms.adapter.server.wdm.utils.AdpTpWrapperUtil;
import com.nsb.enms.adapter.server.wdm.utils.PSSBoardUtil;
import com.nsb.enms.adapter.server.wdm.utils.SnmpTpUserLabelUtil;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.common.LayerRate;
import com.nsb.enms.common.TpType;
import com.nsb.enms.common.utils.Pair;
import com.nsb.enms.common.utils.snmp.SnmpClient;
import com.nsb.enms.mib.pss.def.M_tnAccessPortTable;
import com.nsb.enms.mib.pss.def.M_tnIfTable;
import com.nsb.enms.mib.pss.def.M_tnSfpPortInfoTable;
import com.nsb.enms.mib.pss.enums.E_ifAdminStatus;
import com.nsb.enms.mib.pss.enums.E_ifOperStatus;
import com.nsb.enms.mib.pss.enums.E_ifType;
import com.nsb.enms.restful.model.adapter.AdpEquipment;
import com.nsb.enms.restful.model.adapter.AdpKVPair;
import com.nsb.enms.restful.model.adapter.AdpTp;

public class AdpSnmpTpsMgr {
	private final static Logger log = LogManager.getLogger(AdpSnmpTpsMgr.class);

	private SnmpClient client;
	private AdpTpsDbMgr tpsMgr = new AdpTpsDbMgr();
	private AdpEqusDbMgr equsMgr = new AdpEqusDbMgr();
	private AdpSnmpXcsMgr xcsMgr;
	private String neId;
	private List<AdpTp> _130SCX10ClientPTPList = new ArrayList<AdpTp>();
	private List<AdpTp> _130SCX10LinePTPList = new ArrayList<AdpTp>();
	private List<AdpTp> sfd44ClientPTPList = new ArrayList<AdpTp>();
	private List<AdpTp> sfd44LinePTPList = new ArrayList<AdpTp>();
	private Map<String, Integer> usedSFD44LineCTPList = new HashMap<String, Integer>();
	private List<AdpTp> ahphgSigPTPList = new ArrayList<AdpTp>();
	private List<AdpTp> ahphgLinePTPList = new ArrayList<AdpTp>();

	private ObjectIdGenerator objectIdGenerator;

	public AdpSnmpTpsMgr(String neId, ObjectIdGenerator objectIdGenerator) {
		this.neId = neId;
		this.objectIdGenerator = objectIdGenerator;
		xcsMgr = new AdpSnmpXcsMgr(objectIdGenerator);
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

	public void syncTPs() throws AdapterException {
		List<AdpTp> ptpList = syncPTPs();
		syncCTPs(ptpList);
		createXC();
	}

	private void createXC() throws AdapterException {
		if (!_130SCX10ClientPTPList.isEmpty() && !_130SCX10LinePTPList.isEmpty()) {
			for (AdpTp tp : _130SCX10ClientPTPList) {
				create130SCX10FixedXC(tp);
			}
		}

		if (!sfd44ClientPTPList.isEmpty() && !sfd44LinePTPList.isEmpty()) {
			for (AdpTp tp : sfd44ClientPTPList) {
				createSFD44FixedXC(tp);
			}
		}

		if (!ahphgSigPTPList.isEmpty() && !ahphgLinePTPList.isEmpty()) {
			for (AdpTp tp : ahphgSigPTPList) {
				createAHPHGFixedXC(tp);
			}
		}
	}

	private void create130SCX10FixedXC(AdpTp tp) throws AdapterException {
		String clientCTPkeyOnNe = tp.getKeyOnNe() + "_/odu2=1";
		String userLabel = tp.getNativeName();
		int index = userLabel.indexOf("-C");
		String userLabel_L1 = userLabel.substring(0, index) + "-L1";
		String portNumber = userLabel.substring(index + 2);
		String lineCTPKeyOnNe = find130SCX10L1PTPIndex(userLabel_L1) + "_/odu4=1/odu2=" + portNumber;
		createXC(clientCTPkeyOnNe, lineCTPKeyOnNe, LayerRate.ODU2.name());
	}

	private void createSFD44FixedXC(AdpTp ptp) throws AdapterException {
		String keyOnNe = ptp.getKeyOnNe();
		String clientCTPKeyOnNe = keyOnNe + "_/och=1";
		String nativeName = ptp.getNativeName();
		int index = nativeName.lastIndexOf("-");
		String userLabel_Line = nativeName.substring(0, index) + "-OMD";
		String lineCTPKeyOnNe = findSFD44LineCTPKeyOnNe(userLabel_Line);
		createXC(clientCTPKeyOnNe, lineCTPKeyOnNe, LayerRate.OCH.name());
	}

	private void createAHPHGFixedXC(AdpTp ptp) throws AdapterException {
		String keyOnNe = ptp.getKeyOnNe();
		String sigCTPKeyOnNe = keyOnNe + "_/oms=1";
		String nativeName = ptp.getNativeName();
		String nativeName_Line = nativeName.replace("-SIG", "-LINE");
		String lineCTPKeyOnNe = findAHPHGLinePTPIndex(nativeName_Line) + "_/oms=1";
		createXC(sigCTPKeyOnNe, lineCTPKeyOnNe, LayerRate.OMS.name());
	}

	private void createXC(String atpName, String ztpName, String layerRate) throws AdapterException {
		try {
			AdpTp ctp1 = tpsMgr.getTpByKeyOnNe(neId, atpName);
			if (!isTPValid(ctp1)) {
				log.error("ctp1 is invalid, " + atpName);
				return;
			}
			AdpTp ctp2 = tpsMgr.getTpByKeyOnNe(neId, ztpName);
			if (!isTPValid(ctp2)) {
				log.error("ctp2 is invalid, " + ztpName);
				return;
			}

			String ctpId1 = ctp1.getId();
			String ctpId2 = ctp2.getId();
			if (xcsMgr.isXCExisted(neId, ctpId1) || xcsMgr.isXCExisted(neId, ctpId2)) {
				return;
			}

			List<String> atps = new ArrayList<String>();
			atps.add(ctpId1);
			List<String> ztps = new ArrayList<String>();
			ztps.add(ctpId2);
			List<String> layerRates = new ArrayList<String>();
			layerRates.add(layerRate);
			xcsMgr.createXC(neId, atps, ztps, layerRates);
		} catch (Exception e) {
			log.error("createXC", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
	}

	private String find130SCX10L1PTPIndex(String nativeName) {
		for (AdpTp tp : _130SCX10LinePTPList) {
			if (tp.getNativeName().equalsIgnoreCase(nativeName)) {
				return tp.getKeyOnNe();
			}
		}
		return StringUtils.EMPTY;
	}

	private String findSFD44LineCTPKeyOnNe(String ptpNativeName) {
		for (AdpTp tp : sfd44LinePTPList) {
			if (!tp.getNativeName().equalsIgnoreCase(ptpNativeName)) {
				continue;
			}
			String keyOnNe = tp.getKeyOnNe();
			Integer used = usedSFD44LineCTPList.get(keyOnNe);
			if (used == null || used < 0) {
				used = 0;
			}
			used++;
			usedSFD44LineCTPList.put(keyOnNe, used);
			return keyOnNe + "_/och=" + used;
		}
		return StringUtils.EMPTY;
	}

	private String findAHPHGLinePTPIndex(String nativeName) {
		for (AdpTp tp : ahphgLinePTPList) {
			if (tp.getNativeName().equalsIgnoreCase(nativeName)) {
				return tp.getKeyOnNe();
			}
		}
		return StringUtils.EMPTY;
	}

	private List<AdpTp> syncPTPs() throws AdapterException {
		List<String> oids = setOidParams();
		List<List<Pair<String, String>>> values = getTableValues(oids);
		List<AdpTp> tpList = constructPTPList(values);
		updatePTPConnectedIfIndex(tpList);
		return tpList;
	}

	private void syncCTPs(List<AdpTp> ptpList) throws AdapterException {
		Get130SCX10CTPs _130SCX10CTPs = new Get130SCX10CTPs();
		GetSFD44CTPs sfd44Ctps = new GetSFD44CTPs();
		GetAHPHGCTPs ahphgCtps = new GetAHPHGCTPs();
		for (AdpTp ptp : ptpList) {
			String keyOnNe = ptp.getKeyOnNe();
			String equType = getEquType(keyOnNe);
			if (isExpectedEqu(SnmpEquType._130SCX10.getEquType(), equType)) {
				_130SCX10CTPs.syncCTPs(ptp);
				save130Scx10PTP2List(ptp);
			} else if (isExpectedEqu(SnmpEquType.SFD44.getEquType(), equType)) {
				sfd44Ctps.syncCTPs(ptp);
				saveSFD44PTP2List(ptp);
			} else if (isExpectedEqu(SnmpEquType.AHPHG.getEquType(), equType)) {
				ahphgCtps.syncCTPs(ptp);
				saveAHPHGPTP2List(ptp);
			}
		}
	}

	private void save130Scx10PTP2List(AdpTp tp) {
		String userLabel = tp.getNativeName();
		if (StringUtils.isEmpty(userLabel)) {
			return;
		}
		int index = userLabel.indexOf("-C");
		if (index == -1) {
			if (!_130SCX10LinePTPList.contains(tp)) {
				_130SCX10LinePTPList.add(tp);
			}
		} else {
			if (!_130SCX10ClientPTPList.contains(tp)) {
				_130SCX10ClientPTPList.add(tp);
			}
		}
	}

	private void saveSFD44PTP2List(AdpTp tp) {
		String userLabel = tp.getNativeName();
		if (StringUtils.isEmpty(userLabel)) {
			return;
		}
		if (userLabel.endsWith("-OMD")) {
			if (!sfd44LinePTPList.contains(tp)) {
				sfd44LinePTPList.add(tp);
			}
		} else {
			if (!sfd44ClientPTPList.contains(tp)) {
				sfd44ClientPTPList.add(tp);
			}
		}
	}

	private void saveAHPHGPTP2List(AdpTp tp) {
		String nativeName = tp.getNativeName();
		if (StringUtils.isEmpty(nativeName)) {
			return;
		}
		if (nativeName.endsWith("-SIG")) {
			if (!ahphgSigPTPList.contains(tp)) {
				ahphgSigPTPList.add(tp);
			}
		} else if (nativeName.endsWith("-LINE")) {
			if (!ahphgLinePTPList.contains(tp)) {
				ahphgLinePTPList.add(tp);
			}
		}
	}

	private List<AdpTp> constructPTPList(List<List<Pair<String, String>>> values) throws AdapterException {
		SnmpTpEntity entity = new SnmpTpEntity();
		List<AdpTp> tpList = new ArrayList<AdpTp>();
		for (List<Pair<String, String>> row : values) {
			String index = row.get(0).getSecond();
			String equType = getEquType(index);
			if (StringUtils.isEmpty(equType)) {
				continue;
			}

			constructPTPEntity(entity, row, index);
			AdpTp tp = constructPTP(entity, equType);
			AdpTpWrapperUtil.addTP2DB(tp);
			tpList.add(tp);
		}
		return tpList;
	}

	private void constructPTPEntity(SnmpTpEntity entity, List<Pair<String, String>> row, String index) {
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
			setPTPEntityConnectedTo(entity, endType, ifIndex, address);
			entity.setDirection(row.get(10).getSecond());
			address = row.get(11).getSecond();
			ifIndex = row.get(12).getSecond();
			endType = Object2IntegerUtil.toInt(row.get(13).getSecond());
			setPTPEntityConnectedFrom(entity, endType, ifIndex, address);

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

	private List<String> setOidParams() {
		List<String> oids = new ArrayList<String>();
		oids.add(E_ifType.oid);
		oids.add(E_ifAdminStatus.oid);
		oids.add(E_ifOperStatus.oid);
		oids.add(M_tnIfTable.tnIfType);
		oids.add(M_tnIfTable.tnIfSupportedTypes); // tnIfSupportedTypes
		oids.add(M_tnAccessPortTable.tnAccessPortStateQualifier); // tnAccessPortStateQualifier
		oids.add(M_tnAccessPortTable.tnAccessPortFarEndAddress); // tnAccessPortFarEndAddress
		oids.add(M_tnAccessPortTable.tnAccessPortFarEndIfIndex); // tnAccessPortFarEndIfIndex
		oids.add(M_tnAccessPortTable.tnAccessPortFarEndType); // tnAccessPortFarEndType
		oids.add(M_tnAccessPortTable.tnAccessPortDirection); // tnAccessPortDirection
		oids.add(M_tnAccessPortTable.tnAccessPortFarEndAddressConnFrom); // tnAccessPortFarEndAddressConnFrom
		oids.add(M_tnAccessPortTable.tnAccessPortFarEndIfIndexConnFrom); // tnAccessPortFarEndIfIndexConnFrom
		oids.add(M_tnAccessPortTable.tnAccessPortFarEndTypeConnFrom); // tnAccessPortFarEndTypeConnFrom

		oids.add(M_tnSfpPortInfoTable.tnSfpPortModuleVendorSerNo); // tnSfpPortModuleVendorSerNo
		oids.add(M_tnSfpPortInfoTable.tnSfpPortModuleVendor); // tnSfpPortModuleVendor
		oids.add(M_tnSfpPortInfoTable.tnSfpPortModuleType); // tnSfpPortModuleType
		oids.add(M_tnSfpPortInfoTable.tnSfpPortCLEI); // tnSfpPortCLEI
		oids.add(M_tnSfpPortInfoTable.tnSfpPortUnitPartNum); // tnSfpPortUnitPartNum
		oids.add(M_tnSfpPortInfoTable.tnSfpPortSWPartNum); // tnSfpPortSWPartNum
		oids.add(M_tnSfpPortInfoTable.tnSfpPortFactoryID); // tnSfpPortFactoryID
		oids.add(M_tnSfpPortInfoTable.tnSfpPortDate); // tnSfpPortDate
		oids.add(M_tnSfpPortInfoTable.tnSfpPortExtraData); // tnSfpPortExtraData

		return oids;
	}

	private AdpTp constructPTP(SnmpTpEntity tp, String equType) throws AdapterException {
		AdpTp adpTp = new AdpTp();
		adpTp.setNeId(neId);
		String userLabel = setUserLabel(tp.getIndex(), equType);
		adpTp.setUserLabel(userLabel);
		adpTp.setNativeName(userLabel);
		List<String> layerRates = new ArrayList<String>();
		String actualTpType = SnmpTpType.getTpType(tp.getInternalType());
		layerRates.add(actualTpType);
		if (!actualTpType.equalsIgnoreCase(SnmpTpType.PHN.name())) {
			layerRates.add(LayerRate.PHYSICAL.name());
		}
		adpTp.setLayerRates(layerRates);
		adpTp.setKeyOnNe(tp.getIndex());
		adpTp.setTpType(TpType.PTP.name());

		adpTp.setDirection(SnmpDirection.getDirection(tp.getDirection()));
		String tpId = objectIdGenerator.generatePTPId(adpTp);
		adpTp.setId(tpId);
		constructPTPParameters(adpTp, tp, equType);
		return adpTp;
	}

	private String getEquType(String index) throws AdapterException {
		Integer[] position = SnmpTpUserLabelUtil.string2Hex(index);
		String equKeyOnNe = position[0] + "." + position[1];
		AdpEquipment equ = null;
		try {
			equ = equsMgr.getEquipmentByKeyOnNe(neId, equKeyOnNe);
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

	private boolean isExpectedEqu(String expectedType, String actualType) throws AdapterException {
		if (StringUtils.isEmpty(expectedType) || StringUtils.isEmpty(actualType)) {
			return false;
		}
		if (actualType.equalsIgnoreCase(expectedType)) {
			return true;
		}
		return false;
	}

	private void constructPTPParameters(AdpTp adpTp, SnmpTpEntity tp, String equType) {
		setPTPParameter(adpTp, "adminState", String.valueOf(tp.getAdminStatus()));
		setPTPParameter(adpTp, "operStatus", String.valueOf(tp.getOperStatus()));
		setPTPParameter(adpTp, "secondaryState", String.valueOf(tp.getSecondaryState()));
		setPTPParameter(adpTp, "supportedLayers", String.valueOf(tp.getSupportedTypes()));
		setPTPParameter(adpTp, "signalRate", tp.getInternalType());
		setPTPParameter(adpTp, "connectedTo", tp.getConnectedTo());
		setPTPParameter(adpTp, "connectedFrom", tp.getConnectedFrom());
		// Vendor
		setPTPParameter(adpTp, "sfpPortModuleVendor", tp.getSfpPortModuleVendor());
		// Module Type
		setPTPParameter(adpTp, "sfpPortModuleType", tp.getSfpPortModuleType());
		// CLEI
		setPTPParameter(adpTp, "sfpPortCLEI", tp.getSfpPortCLEI());
		// Unit Part Number
		setPTPParameter(adpTp, "sfpPortUnitPartNum", tp.getSfpPortUnitPartNum());
		// Factory ID
		setPTPParameter(adpTp, "sfpPortFactoryID", tp.getSfpPortFactoryID());
		// Software Part Number
		setPTPParameter(adpTp, "sfpPortSWPartNum", tp.getSfpPortSWPartNum());
		// Serial Number
		setPTPParameter(adpTp, "sfpPortModuleVendorSerNo", tp.getSfpPortModuleVendorSerNo());
		// Date
		setPTPParameter(adpTp, "sfpPortDate", tp.getSfpPortDate());
		// Extra Data
		setPTPParameter(adpTp, "sfpPortExtraData", tp.getSfpPortExtraData());

		// 130SCX10 Client PTP specail Param
		if (true == is130SCX10ClientPTP(adpTp, equType))
			setPTPParameter(adpTp, "supportedContainer", "ODU2");
	}

	private void setPTPParameter(AdpTp adpTp, String key, String value) {
		AdpKVPair pair = new AdpKVPair();
		pair.setKey(key);
		pair.setValue(value);
		adpTp.addParamsItem(pair);
	}

	private void setPTPEntityConnectedTo(SnmpTpEntity entity, int endType, String ifIndex, String address) {
		switch (endType) {
		case 1: // notConnected
			entity.setConnectedTo("");
			break;
		case 2: // internal
			String tpId = queryPTPId(ifIndex);
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
	}

	private void setPTPEntityConnectedFrom(SnmpTpEntity entity, int endType, String ifIndex, String address) {
		switch (endType) {
		case 1: // notConnected
			entity.setConnectedFrom("");
			break;
		case 2: // internal
			String tpId = queryPTPId(ifIndex);
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
	}

	private String queryPTPId(String keyOnNe) {
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

	private void updatePTPConnectedIfIndex(List<AdpTp> tpList) throws AdapterException {
		boolean bUpdate;
		for (AdpTp tp : tpList) {
			bUpdate = false;
			List<AdpKVPair> params = tp.getParams();
			for (AdpKVPair pair : params) {
				String key = pair.getKey();
				if (false == key.equals("connectedTo") && false == key.equals("connectedFrom"))
					continue;
				String value = pair.getValue();
				if (isIfIndex(value)) {
					String ifIndex = value.substring(8);
					String tpId = queryPTPId(ifIndex);
					if (tpId != null) {
						pair.setValue(tpId);
						bUpdate = true;
						log.info("update key " + key + ", set the ifindex/" + ifIndex + " to id " + tpId);
					}
				}
			}
			if (bUpdate)
				AdpTpWrapperUtil.updateTP2DB(tp);
		}
	}

	private boolean isIfIndex(String value) {
		return value != null && value.startsWith("ifIndex/");
	}

	private boolean is130SCX10ClientPTP(AdpTp adpTp, String equType) {
		boolean ret = false;
		if (equType.equals("130SCX10")) {
			String userLabel = adpTp.getNativeName();
			String[] labels = userLabel.split("-");
			if (labels.length == 4) {
				String portName = labels[labels.length - 1];
				if (portName.startsWith("C")) { // Client PTP
					ret = true;
				}
			}
		}
		return ret;
	}

	private boolean isTPValid(AdpTp tp) {
		if (null == tp) {
			return false;
		}
		if (StringUtils.isEmpty(tp.getId())) {
			return false;
		}
		return true;
	}

	public void setTpAttribute() {

	}

	public static void main(String args[]) {
		// SnmpClient client = new SnmpClient("135.251.96.13", 161,
		// "admin_snmp");
		// AdpSnmpClientFactory.getInstance().add("135.251.96.13:161", client);
		// AdpSnmpTpsMgr mgr = new AdpSnmpTpsMgr("1", new
		// WdmObjectIdGenerator());
		// try {
		// mgr.syncTPs();
		// } catch (AdapterException e) {
		// e.printStackTrace();
		// }

		Map<String, Integer> map = new HashMap<String, Integer>();
		Integer x = map.get("xx");
		System.out.println(x);
	}
}
