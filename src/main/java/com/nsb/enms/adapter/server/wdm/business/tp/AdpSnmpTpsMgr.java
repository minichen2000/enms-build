package com.nsb.enms.adapter.server.wdm.business.tp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.db.mgr.AdpSeqDbMgr;
import com.nsb.enms.adapter.server.common.db.mgr.AdpTpsDbMgr;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.wdm.action.entity.SnmpTpEntity;
import com.nsb.enms.adapter.server.wdm.factory.AdpSnmpClientFactory;
import com.nsb.enms.adapter.server.wdm.utils.SnmpTpUserLabelUtil;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.common.utils.Pair;
import com.nsb.enms.common.utils.snmpclient.SnmpClient;
import com.nsb.enms.restful.model.adapter.AdpTp;

public class AdpSnmpTpsMgr {
	private final static Logger log = LogManager.getLogger(AdpSnmpTpsMgr.class);

	private SnmpClient client;
	private AdpTpsDbMgr tpsMgr = new AdpTpsDbMgr();

	public AdpSnmpTpsMgr() {
	}

	public AdpSnmpTpsMgr(Integer neId) {
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

	public List<AdpTp> getTps() throws AdapterException {
		List<String> oids = new ArrayList<String>();
		oids.add("1.3.6.1.2.1.2.2.1.3"); // ifType
		oids.add("1.3.6.1.2.1.2.2.1.7"); // ifAdminStatus
		oids.add("1.3.6.1.2.1.2.2.1.8"); // ifOperStatus
		oids.add("1.3.6.1.4.1.7483.2.2.4.1.2.2.1.2"); // tnIfType
		oids.add("1.3.6.1.4.1.7483.2.2.4.1.2.2.1.3"); // tnIfSupportedTypes

		List<AdpTp> tpList = new ArrayList<AdpTp>();
		SnmpTpEntity entity = new SnmpTpEntity();
		for (List<Pair<String, String>> row : getTableValues(oids)) {
			entity.setIndex(row.get(0).getSecond());
			entity.setTpType(row.get(1).getSecond());
			entity.setAdminStatus(Integer.valueOf(row.get(2).getSecond()));
			entity.setOperStatus(Integer.valueOf(row.get(3).getSecond()));
			entity.setInternalType(row.get(4).getSecond());
			List<String> supportedTypes = new ArrayList<String>();
			supportedTypes.add(row.get(5).getSecond());
			entity.setSupportedTypes(supportedTypes);
			AdpTp tp = constructTp(entity, 0, 0, 0);
			try {
				tpsMgr.addTp(tp);
			} catch (Exception e) {
				log.error("addTp", e);
				throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
			}
			tpList.add(tp);
		}

		return tpList;
	}

	private AdpTp constructTp(SnmpTpEntity tp, Integer neDbId, Integer ptpId, Integer parentTpId)
			throws AdapterException {
		AdpTp adpTp = new AdpTp();
		Integer maxTpId;
		try {
			maxTpId = AdpSeqDbMgr.getMaxTpId();
		} catch (Exception e) {
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
		adpTp.setId(maxTpId);
		adpTp.setNeId(neDbId);
		adpTp.setUserLabel(SnmpTpUserLabelUtil.generate(tp.getIndex()));
		List<String> layerRates = new ArrayList<String>();
		layerRates.add(tp.getInternalType());
		adpTp.setLayerRates(layerRates);
		adpTp.setKeyOnNe("");
		adpTp.setTpType(tp.getTpType());
		adpTp.setPtpID(ptpId);
		adpTp.setParentTpID(parentTpId);
		return adpTp;
	}

	public static void main(String args[]) {
	}
}
