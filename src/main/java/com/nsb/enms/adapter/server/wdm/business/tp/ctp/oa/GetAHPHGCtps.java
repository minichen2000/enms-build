package com.nsb.enms.adapter.server.wdm.business.tp.ctp.oa;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.db.mgr.AdpSeqDbMgr;
import com.nsb.enms.adapter.server.common.db.mgr.AdpTpsDbMgr;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.common.Direction;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.common.LayerRate;
import com.nsb.enms.common.TpType;
import com.nsb.enms.restful.model.adapter.AdpTp;

public class GetAHPHGCtps {
	private final static Logger log = LogManager.getLogger(GetAHPHGCtps.class);
	private AdpTpsDbMgr tpDbMgr = new AdpTpsDbMgr();

	public static void main(String[] args) {

	}

	private AdpTp constructCtp(Integer neId, String userLabel, List<String> layerRates, Integer ptpId, String ptpIndex,
			Integer primaryLayerRate) throws AdapterException {
		AdpTp tp = new AdpTp();
		tp.setId(getMaxId(neId));
		tp.setNeId(neId);
		tp.setNativeName(userLabel);
		tp.setUserLabel(userLabel);
		tp.setLayerRates(layerRates);
		String keyOnNe = userLabel + "_" + ptpIndex;
		tp.setKeyOnNe(keyOnNe);
		tp.setDirection(Direction.BI.name());
		tp.setFreeResources(null);
		tp.setTpType(TpType.CTP.name());
		tp.setPrimaryLayerRate(primaryLayerRate);
		tp.setPtpID(ptpId);
		tp.setParentTpID(ptpId);
		tp.setParams(null);
		tp.setObjectType(primaryLayerRate);
		tp.setAlarmState(null);
		return addCtp2Db(tp);
	}

	private Integer getMaxId(Integer neId) throws AdapterException {
		Integer maxTpId;
		try {
			maxTpId = AdpSeqDbMgr.getMaxTpId(neId);
		} catch (Exception e) {
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
		return maxTpId;
	}

	private void getClientCtps(Integer neId, Integer ptpId, String ptpIndex) throws AdapterException {
		getOchCtp(neId, ptpId, ptpIndex);
	}

	private void getLineCtps(Integer neId, Integer ptpId, String ptpIndex) throws AdapterException {
		getOchCtp(neId, ptpId, ptpIndex);
		getOmsCtps(neId, ptpId, ptpIndex);
	}

	private AdpTp getOchCtp(Integer neId, Integer ptpId, String ptpIndex) throws AdapterException {
		List<String> layerRates = new ArrayList<String>();
		layerRates.add(LayerRate.OCH.name());
		int primaryLayerRate = LayerRate.OCH.ordinal();
		String userLabel = "";
		return constructCtp(neId, userLabel, layerRates, ptpId, ptpIndex, primaryLayerRate);
	}

	private AdpTp getOmsCtps(Integer neId, Integer ptpId, String ptpIndex) throws AdapterException {
		List<String> layerRates = new ArrayList<String>();
		layerRates.add(LayerRate.OTU4.name());
		int primaryLayerRate = LayerRate.OTU4.ordinal();
		String userLabel = "/out4=1";
		return constructCtp(neId, userLabel, layerRates, ptpId, ptpIndex, primaryLayerRate);
	}

	public void syncCtps(AdpTp tp) throws AdapterException {
		String userLabel = tp.getNativeName();
		System.out.println("=========" + userLabel);
		Integer neId = tp.getNeId();
		Integer ptpId = tp.getPtpID();
		String ptpIndex = tp.getKeyOnNe();
		if (userLabel.indexOf("-C") != -1) {
			getClientCtps(neId, ptpId, ptpIndex);
		} else if (userLabel.indexOf("-L1") != -1) {
			System.out.println("xxxxxxxxxxxxxx");
			getLineCtps(neId, ptpId, ptpIndex);
		}
	}

	private AdpTp addCtp2Db(AdpTp ctp) throws AdapterException {
		AdpTp ctpFromDb = isCtpExisted(ctp.getNeId(), ctp.getKeyOnNe());
		if (null != ctpFromDb) {
			// TODO 替换已有值
			return ctpFromDb;
		}
		try {
			ctpFromDb = tpDbMgr.addTp(ctp);
		} catch (Exception e) {
			log.error("addTps", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
		return ctpFromDb;
	}

	private AdpTp isCtpExisted(Integer neId, String keyOnNe) throws AdapterException {
		try {
			AdpTp tpFromDb = tpDbMgr.getTpByKeyOnNe(neId, keyOnNe);
			if (null != tpFromDb && null != tpFromDb.getId() && -1 < tpFromDb.getId()) {
				return tpFromDb;
			}
		} catch (Exception e) {
			log.error("getTpByKeyOnNe", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
		return null;
	}
}
