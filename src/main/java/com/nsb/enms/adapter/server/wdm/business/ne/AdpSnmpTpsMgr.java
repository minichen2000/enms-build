package com.nsb.enms.adapter.server.wdm.business.ne;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.db.mgr.AdpSeqDbMgr;
import com.nsb.enms.adapter.server.common.db.mgr.AdpTpsDbMgr;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.wdm.action.entity.SnmpTpEntity;
import com.nsb.enms.adapter.server.wdm.utils.SnmpTpUserLabelUtil;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.common.utils.Pair;
import com.nsb.enms.common.utils.snmpclient.SnmpClient;
import com.nsb.enms.restful.model.adapter.AdpTp;

public class AdpSnmpTpsMgr {
	private final static Logger log = LogManager.getLogger(AdpSnmpNesMgr.class);
	private final static String ADP_ADDRESS = "135.251.99.58";
	// ConfLoader.getInstance().getConf(ConfigKey.ADP_ADDRESS,"");

	private SnmpClient client;
	private AdpTpsDbMgr tpsDbMgr = new AdpTpsDbMgr();

	public AdpSnmpTpsMgr() {
	}

	public AdpSnmpTpsMgr(SnmpClient client) {
		this.client = client;
	}

	public SnmpClient getClient() {
		return client;
	}

	public void setClient(SnmpClient client) {
		this.client = client;
	}

	private List<SnmpTpEntity> getIfTable() {
		List<String> oids = new ArrayList<String>();
		// oids.add("1.3.6.1.2.1.2.2.1.1"); // ifIndex
		oids.add("1.3.6.1.2.1.2.2.1.3"); // ifType
		oids.add("1.3.6.1.2.1.2.2.1.7"); // ifAdminStatus
		oids.add("1.3.6.1.2.1.2.2.1.8"); // ifOperStatus

		List<SnmpTpEntity> entityList = new ArrayList<SnmpTpEntity>();
		String oid, value;
		SnmpTpEntity entity = new SnmpTpEntity();
		for (List<Pair<String, String>> row : getTableValues(oids)) {
			for (Pair<String, String> pair : row) {
				oid = pair.getFirst();
				value = pair.getSecond();
				if ("index".equals(oid)) {
					entity.setIndex(value);
				} else if ("1.3.6.1.2.1.2.2.1.3".equals(oid)) {
					entity.setTpType(value);
				} else if ("1.3.6.1.2.1.2.2.1.7".equals(oid)) {
					entity.setAdminStatus(Integer.valueOf(value));
				} else if ("1.3.6.1.2.1.2.2.1.8".equals(oid)) {
					entity.setOperStatus(Integer.valueOf(value));
				}
			}
			entityList.add(entity);
			System.out.println("************");
		}
		return entityList;
	}

	private List<SnmpTpEntity> getTnIfTable() {
		List<String> oids = new ArrayList<String>();
		oids.add("1.3.6.1.4.1.7483.2.2.4.1.2.2.1.1"); // tnIfPhysicalLocation
		oids.add("1.3.6.1.4.1.7483.2.2.4.1.2.2.1.2"); // tnIfType
		oids.add("1.3.6.1.4.1.7483.2.2.4.1.2.2.1.3"); // tnIfSupportedTypes

		List<SnmpTpEntity> entityList = new ArrayList<SnmpTpEntity>();
		String oid, value;
		SnmpTpEntity entity = new SnmpTpEntity();
		for (List<Pair<String, String>> row : getTableValues(oids)) {
			for (Pair<String, String> pair : row) {
				oid = pair.getFirst();
				value = pair.getSecond();
				if ("1.3.6.1.4.1.7483.2.2.4.1.2.2.1.1".equals(oid)) {
					entity.setIndex(value);
				} else if ("1.3.6.1.4.1.7483.2.2.4.1.2.2.1.2".equals(oid)) {
					entity.setInternalType(value);
				} else if ("1.3.6.1.4.1.7483.2.2.4.1.2.2.1.3".equals(oid)) {
					List<String> supportedTypes = new ArrayList<String>();
					supportedTypes.add(value);
					entity.setSupportedTypes(supportedTypes);
				}
			}
			entityList.add(entity);
			System.out.println("************");
		}
		return entityList;
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
		List<AdpTp> tpList = new ArrayList<AdpTp>();
		List<SnmpTpEntity> entityList1 = getIfTable();
		List<SnmpTpEntity> entityList2 = getTnIfTable();
		for (SnmpTpEntity entity1 : entityList1) {
			for (SnmpTpEntity entity2 : entityList2) {
				if (entity1.getIndex().equals(entity2.getIndex())) {
					entity1.setInternalType(entity2.getInternalType());
					entity1.setSupportedTypes(entity2.getSupportedTypes());
					break;
				}
			}
			constructTp(entity1, 0, 0, 0);
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
		adpTp.setUserLabel(SnmpTpUserLabelUtil.generate(tp.getUserLabel()));
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
		SnmpClient client = new SnmpClient("135.251.96.5", 161, "admin_snmp");
		client.start();
		AdpSnmpTpsMgr mgr = new AdpSnmpTpsMgr(client);
		try {
			mgr.getIfTable();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
