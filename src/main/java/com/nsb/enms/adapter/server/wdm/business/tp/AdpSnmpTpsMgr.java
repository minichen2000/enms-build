package com.nsb.enms.adapter.server.wdm.business.tp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.business.itf.ObjectIdGenerator;
import com.nsb.enms.adapter.server.common.db.mgr.AdpEqusDbMgr;
import com.nsb.enms.adapter.server.common.db.mgr.AdpSeqDbMgr;
import com.nsb.enms.adapter.server.common.db.mgr.AdpTpsDbMgr;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.utils.Object2IntegerUtil;
import com.nsb.enms.adapter.server.wdm.action.entity.SnmpTpEntity;
import com.nsb.enms.adapter.server.wdm.business.objectIdGenerator.WdmObjectIdGenerator;
import com.nsb.enms.adapter.server.wdm.business.tp.ctp.oa.GetAHPHGCtps;
import com.nsb.enms.adapter.server.wdm.business.tp.ctp.ot.Get130SCX10Ctps;
import com.nsb.enms.adapter.server.wdm.business.tp.ctp.sfd.GetSFD44Ctps;
import com.nsb.enms.adapter.server.wdm.business.xc.AdpSnmpXcsMgr;
import com.nsb.enms.adapter.server.wdm.constants.SnmpDirection;
import com.nsb.enms.adapter.server.wdm.constants.SnmpEquType;
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
	private AdpSnmpXcsMgr xcsMgr;
	private Integer neId;
	private List<AdpTp> _130Scx10ClientPtpList = new ArrayList<AdpTp>();
	private List<AdpTp> _130Scx10LinePtpList = new ArrayList<AdpTp>();
	private List<AdpTp> sfd44ClientPtpList = new ArrayList<AdpTp>();
	private List<AdpTp> sfd44LinePtpList = new ArrayList<AdpTp>();
	private List<AdpTp> ahphgClientPtpList = new ArrayList<AdpTp>();
	private List<AdpTp> ahphgLinePtpList = new ArrayList<AdpTp>();

	private ObjectIdGenerator objectIdGenerator;

	public AdpSnmpTpsMgr(Integer neId, ObjectIdGenerator objectIdGenerator) {
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

	public void syncTps() throws AdapterException {
		List<AdpTp> ptpList = syncPtps();
		syncCtps(ptpList);
		createXc();
	}

	private void createXc() throws AdapterException {
		if (!_130Scx10ClientPtpList.isEmpty() && !_130Scx10LinePtpList.isEmpty()) {
			for (AdpTp tp : _130Scx10ClientPtpList) {
				System.out.println(tp.getNativeName());
				create130SCX10FixedXc(tp);
			}
		}

		if (!sfd44ClientPtpList.isEmpty() && !sfd44LinePtpList.isEmpty()) {
			int i = 0;
			for (AdpTp tp : sfd44ClientPtpList) {
				System.out.println(tp.getNativeName());
				createSFD44FixedXc(tp, ++i);
			}
		}
	}

	private void create130SCX10FixedXc(AdpTp tp) throws AdapterException {
		String userLabel = tp.getNativeName();
		int index = userLabel.indexOf("-C");
		try {
			String clientCtpkeyOnNe = "/odu2=1_" + tp.getKeyOnNe();
			AdpTp ctp1 = tpsMgr.getTpByKeyOnNe(neId, clientCtpkeyOnNe);
			if (!isTpValid(ctp1)) {
				log.error("ctp1 is invalid");
				return;
			}
			String userLabel_L1 = userLabel.substring(0, index) + "-L1";
			String portNumber = userLabel.substring(index + 2);
			String lineCtpkeyOnNe = "/odu4=1/odu2=" + portNumber + "_" + find130SCX10L1PtpIndex(userLabel_L1);
			AdpTp ctp2 = tpsMgr.getTpByKeyOnNe(neId, lineCtpkeyOnNe);
			if (!isTpValid(ctp2)) {
				log.error("ctp2 is invalid");
				return;
			}

			Integer ctpId1 = ctp1.getId();
			Integer ctpId2 = ctp2.getId();
			if (xcsMgr.isXcExisted(ctpId1) || xcsMgr.isXcExisted(ctpId2)) {
				return;
			}

			List<Integer> atps = new ArrayList<Integer>();
			atps.add(ctpId1);
			List<Integer> ztps = new ArrayList<Integer>();
			ztps.add(ctpId2);
			xcsMgr.createXc(neId, atps, ztps);
		} catch (AdapterException e) {
			throw e;
		} catch (Exception e) {
			log.error("create130SCX10FixedXc", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
	}

	private void createSFD44FixedXc(AdpTp tp, int portNumber) throws AdapterException {
		String userLabel = tp.getNativeName();
		try {
			int index = userLabel.lastIndexOf("-");
			String tpRate = userLabel.substring(index + 1);
			String clientCtpkeyOnNe = "/odu2=1_" + tp.getKeyOnNe();
			AdpTp ctp1 = tpsMgr.getTpByKeyOnNe(neId, clientCtpkeyOnNe);
			if (!isTpValid(ctp1)) {
				log.error("ctp1 is invalid");
				return;
			}
			String userLabel_L1 = userLabel.substring(0, index) + "-OMD";
			String lineCtpkeyOnNe = userLabel_L1 + "_" + portNumber;
			AdpTp ctp2 = tpsMgr.getTpByKeyOnNe(neId, lineCtpkeyOnNe);
			if (!isTpValid(ctp2)) {
				log.error("ctp2 is invalid");
				return;
			}

			// update ctp2 userlabel

			Integer ctpId1 = ctp1.getId();
			Integer ctpId2 = ctp2.getId();
			if (xcsMgr.isXcExisted(ctpId1) || xcsMgr.isXcExisted(ctpId2)) {
				return;
			}

			List<Integer> atps = new ArrayList<Integer>();
			atps.add(ctpId1);
			List<Integer> ztps = new ArrayList<Integer>();
			ztps.add(ctpId2);
			xcsMgr.createXc(neId, atps, ztps);
		} catch (AdapterException e) {
			throw e;
		} catch (Exception e) {
			log.error("createSFD44FixedXc", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
	}

	private String find130SCX10L1PtpIndex(String userLabel) {
		for (AdpTp tp : _130Scx10LinePtpList) {
			if (tp.getNativeName().equalsIgnoreCase(userLabel)) {
				return tp.getKeyOnNe();
			}
		}
		return StringUtils.EMPTY;
	}

	private String findSFD44L1PtpIndex(String userLabel) {
		for (AdpTp tp : sfd44LinePtpList) {
			if (tp.getNativeName().equalsIgnoreCase(userLabel)) {
				return tp.getKeyOnNe();
			}
		}
		return StringUtils.EMPTY;
	}

	private List<AdpTp> syncPtps() throws AdapterException {
		List<String> oids = setOidParams();
		List<List<Pair<String, String>>> values = getTableValues(oids);
		List<AdpTp> tpList = constructTpList(values);
		updatePtpConnectedIfIndex(tpList);
		return tpList;
	}

	private void syncCtps(List<AdpTp> ptpList) throws AdapterException {
		Get130SCX10Ctps _130SCX10Ctps = new Get130SCX10Ctps();
		GetSFD44Ctps sfd44Ctps = new GetSFD44Ctps();
		GetAHPHGCtps ahphgCtps = new GetAHPHGCtps();
		for (AdpTp tp : ptpList) {
			String keyOnNe = tp.getKeyOnNe();
			String equType = getEquType(keyOnNe);
			if (isExpectedEqu(SnmpEquType._130SCX10.getEquType(), equType)) {
				_130SCX10Ctps.syncCtps(tp);
				save130Scx10Ptp2List(tp);
			} else if (isExpectedEqu(SnmpEquType.SFD44.getEquType(), equType)) {
				sfd44Ctps.syncCtps(tp);
				saveSFD44Ptp2List(tp);
			} else if (isExpectedEqu(SnmpEquType.AHPHG.getEquType(), equType)) {
				ahphgCtps.syncCtps(tp);
			}
		}
	}

	private void save130Scx10Ptp2List(AdpTp tp) {
		String userLabel = tp.getNativeName();
		if (StringUtils.isEmpty(userLabel)) {
			return;
		}
		int index = userLabel.indexOf("-C");
		if (index == -1) {
			if (!_130Scx10LinePtpList.contains(tp)) {
				_130Scx10LinePtpList.add(tp);
			}
		} else {
			if (!_130Scx10ClientPtpList.contains(tp)) {
				_130Scx10ClientPtpList.add(tp);
			}
		}
	}

	private void saveSFD44Ptp2List(AdpTp tp) {
		String userLabel = tp.getNativeName();
		if (StringUtils.isEmpty(userLabel)) {
			return;
		}
		if (userLabel.endsWith("-OMD")) {
			if (!sfd44LinePtpList.contains(tp)) {
				sfd44LinePtpList.add(tp);
			}
		} else {
			if (!sfd44ClientPtpList.contains(tp)) {
				sfd44ClientPtpList.add(tp);
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

			String equType = getEquType(index);
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
			setTpEntityConnectedTo(entity, endType, ifIndex, address);
			entity.setDirection(row.get(10).getSecond());
			address = row.get(11).getSecond();
			ifIndex = row.get(12).getSecond();
			endType = Object2IntegerUtil.toInt(row.get(13).getSecond());
			setTpEntityConnectedFrom(entity, endType, ifIndex, address);

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
		boolean ret = true;
		try {

			tpsMgr.deleteTpByKeyOnNe(tp.getNeId(), tp.getKeyOnNe());
			tpsMgr.addTp(tp);
			//ret = tpsMgr.updateTp(tp);
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
		Integer tpId = getAdpTpId(tpType, maxTpId);
		adpTp.setId(tpId);
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
			adpTp.setPtpID(tpId);
			adpTp.setParentTpID(parentTpId);
		}
		adpTp.setDirection(SnmpDirection.getDirection(tp.getDirection()));

		if (TpType.PTP == tpType)
			constructPtpParameters(adpTp, tp, tpType, equType);

		return adpTp;
	}

	private String getEquType(String index) throws AdapterException {
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

	private boolean isExpectedEqu(String expectedType, String actualType) throws AdapterException {
		if (StringUtils.isEmpty(expectedType) || StringUtils.isEmpty(actualType)) {
			return false;
		}
		if (actualType.equalsIgnoreCase(expectedType)) {
			return true;
		}
		return false;
	}

	private void constructPtpParameters(AdpTp adpTp, SnmpTpEntity tp, TpType tpType, String equType) {
		setPtpParameter(adpTp, "adminState", String.valueOf(tp.getAdminStatus()));
		setPtpParameter(adpTp, "operStatus", String.valueOf(tp.getOperStatus()));
		setPtpParameter(adpTp, "secondaryState", String.valueOf(tp.getSecondaryState()));
		setPtpParameter(adpTp, "supportedLayers", String.valueOf(tp.getSupportedTypes()));
		setPtpParameter(adpTp, "signalRate", tp.getInternalType());
		setPtpParameter(adpTp, "connectedTo", tp.getConnectedTo());
		setPtpParameter(adpTp, "connectedFrom", tp.getConnectedFrom());
		// Vendor
		setPtpParameter(adpTp, "sfpPortModuleVendor", tp.getSfpPortModuleVendor());
		// Module Type
		setPtpParameter(adpTp, "sfpPortModuleType", tp.getSfpPortModuleType());
		// CLEI
		setPtpParameter(adpTp, "sfpPortCLEI", tp.getSfpPortCLEI());
		// Unit Part Number
		setPtpParameter(adpTp, "sfpPortUnitPartNum", tp.getSfpPortUnitPartNum());
		// Factory ID
		setPtpParameter(adpTp, "sfpPortFactoryID", tp.getSfpPortFactoryID());
		// Software Part Number
		setPtpParameter(adpTp, "sfpPortSWPartNum", tp.getSfpPortSWPartNum());
		// Serial Number
		setPtpParameter(adpTp, "sfpPortModuleVendorSerNo", tp.getSfpPortModuleVendorSerNo());
		// Date
		setPtpParameter(adpTp, "sfpPortDate", tp.getSfpPortDate());
		// Extra Data
		setPtpParameter(adpTp, "sfpPortExtraData", tp.getSfpPortExtraData());

		// 130SCX10 Client PTP specail Param
		if (TpType.PTP == tpType && true == is130SCX10ClientPtp(adpTp, equType))
			setPtpParameter(adpTp, "supportedContainer", "ODU2");
	}

	private void setPtpParameter(AdpTp adpTp, String key, String value) {
		AdpKVPair pair = new AdpKVPair();
		pair.setKey(key);
		pair.setValue(value);
		adpTp.addParamsItem(pair);
	}

	private void setTpEntityConnectedTo(SnmpTpEntity entity, int endType, String ifIndex, String address) {
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
	}

	private void setTpEntityConnectedFrom(SnmpTpEntity entity, int endType, String ifIndex, String address) {
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
				if (false == key.equals("connectedTo") && false == key.equals("connectedFrom"))
					continue;
				String value = pair.getValue();
				if (isIfIndex(value)) {
					String ifIndex = value.substring(8);
					String tpId = queryPtpId(ifIndex);
					if (tpId != null) {
						pair.setValue(tpId);
						bUpdate = true;
						log.info("update key " + key + ", set the ifindex/" + ifIndex + " to id " + tpId);
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

	private boolean is130SCX10ClientPtp(AdpTp adpTp, String equType) {
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

	private Integer getAdpTpId(TpType tpType, Object obj) {
		Integer adpTpId;
		if (TpType.PTP == tpType)
			adpTpId = generatePtpId(obj);
		else
			adpTpId = generatePtpId(obj);
		return adpTpId;
	}

	public Integer generatePtpId(Object obj) {
		Integer ptpId;
		ptpId = new Integer(Integer.valueOf(obj.toString()));
		return ptpId;
	}

	private boolean isTpValid(AdpTp tp) {
		if (null == tp) {
			return false;
		}
		if (null == tp.getId()) {
			return false;
		}
		if (tp.getId() < 0) {
			return false;
		}
		return true;
	}

	public static void main(String args[]) {
		SnmpClient client = new SnmpClient("135.251.96.5", 161, "admin_snmp");
		AdpSnmpClientFactory.getInstance().add("135.251.96.5:161", client);
		AdpSnmpTpsMgr mgr = new AdpSnmpTpsMgr(5, new WdmObjectIdGenerator());
		try {
			mgr.syncTps();
		} catch (AdapterException e) {
			e.printStackTrace();
		}
	}
}
