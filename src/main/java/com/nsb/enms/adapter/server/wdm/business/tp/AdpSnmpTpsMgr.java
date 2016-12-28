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
import com.nsb.enms.adapter.server.wdm.action.entity.SnmpTpEntity;
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

	public List<AdpTp> syncTps() throws AdapterException {
		List<String> oids = setOidParams();
		List<List<Pair<String, String>>> values = getTableValues(oids);
		List<AdpTp> tpList = constructTpList(values);
		return tpList;
	}

	private List<AdpTp> constructTpList(List<List<Pair<String, String>>> values) throws AdapterException {
		SnmpTpEntity entity = new SnmpTpEntity();
		List<AdpTp> tpList = new ArrayList<AdpTp>();
		for (List<Pair<String, String>> row : values) {
			String index = row.get(0).getSecond();
			if (isTpExisted(index)) {
				continue;
			}

			String equType = isExpectedEquExisted(index);
			if (StringUtils.isEmpty(equType)) {
				continue;
			}

			constructTpEntity(entity, row, index);
			AdpTp tp = constructTp(entity, TpType.PTP, equType, null, 0);
			addTp2Db(tp);
			tpList.add(tp);
		}
		return tpList;
	}

	private void constructTpEntity(SnmpTpEntity entity, List<Pair<String, String>> row, String index) {
		entity.setIndex(index);
		entity.setTpType(row.get(1).getSecond());
		entity.setAdminStatus(Integer.valueOf(row.get(2).getSecond()));
		entity.setOperStatus(Integer.valueOf(row.get(3).getSecond()));
		entity.setInternalType(row.get(4).getSecond());
		List<String> supportedTypes = new ArrayList<String>();
		supportedTypes.add(row.get(5).getSecond());
		entity.setSupportedTypes(supportedTypes);
		entity.setSecondaryState(Integer.valueOf(row.get(6).getSecond()));
		int endType = Integer.valueOf(row.get(7).getSecond());
		switch(endType)
		{
		case 1: // notConnected
			entity.setConnectedTo("");break;
		case 2: // internal
			entity.setConnectedTo("ifIndex");break;
		case 3: // external
			entity.setConnectedTo("IP/ifIndex");break;
		}
		entity.setDirection(row.get(8).getSecond());
		endType = Integer.valueOf(row.get(9).getSecond());
		switch(endType)
		{
		case 1: // notConnected
			entity.setConnectedFrom("");break;
		case 2: // internal
			entity.setConnectedFrom("ifIndex");break;
		case 3: // external
			entity.setConnectedFrom("IP/ifIndex");break;
		}
		
	}

	private boolean isTpExisted(String keyOnNe) throws AdapterException {
		try {
			AdpTp tpFromDb = tpsMgr.getTpByKeyOnNe(neId, keyOnNe);
			if (null == tpFromDb || null == tpFromDb.getId()) {
				return false;
			}
		} catch (Exception e) {
			log.error("getTpByKeyOnNe", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
		return true;
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

	private List<String> setOidParams() {
		List<String> oids = new ArrayList<String>();
		oids.add("1.3.6.1.2.1.2.2.1.3"); // ifType
		oids.add("1.3.6.1.2.1.2.2.1.7"); // ifAdminStatus
		oids.add("1.3.6.1.2.1.2.2.1.8"); // ifOperStatus
		oids.add("1.3.6.1.4.1.7483.2.2.4.1.2.2.1.2"); // tnIfType
		oids.add("1.3.6.1.4.1.7483.2.2.4.1.2.2.1.3"); // tnIfSupportedTypes
		oids.add("1.3.6.1.4.1.7483.2.2.4.1.2.1.1.5"); // tnAccessPortStateQualifier
		oids.add("1.3.6.1.4.1.7483.2.2.4.1.2.1.1.8"); // tnAccessPortFarEndType
		oids.add("1.3.6.1.4.1.7483.2.2.4.1.2.1.1.9"); // tnAccessPortDirection
		oids.add("1.3.6.1.4.1.7483.2.2.4.1.2.1.1.17"); //tnAccessPortFarEndTypeConnFrom
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
		if (TpType.PTP == tpType && !actualTpType.equals(SnmpTpType.PHN)) {
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
		if (TpType.PTP == tpType && equType.equals("130SCX10")) {
			AdpKVPair pair = new AdpKVPair();
			pair.setKey("supportedContainer");
			pair.setValue("ODU2");
			List<AdpKVPair> params = new ArrayList<AdpKVPair>();
			params.add(pair);
			adpTp.setParams(params);
		}
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

	public static void main(String args[]) {
	}
}
