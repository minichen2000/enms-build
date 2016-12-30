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

	private AdpTp constructCtp(Integer neId, String nativeName, List<String> layerRates, Integer ptpId, String ptpIndex,
			Integer primaryLayerRate) throws AdapterException {
		AdpTp tp = new AdpTp();
		tp.setId(getMaxId(neId));
		tp.setNeId(neId);
		tp.setNativeName(nativeName);
		tp.setUserLabel(nativeName);
		tp.setLayerRates(layerRates);
		String keyOnNe = nativeName + "_" + ptpIndex;
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

	private void getSigCtps(Integer neId, Integer ptpId, String ptpIndex) throws AdapterException {
		getOmsCtps(neId, ptpId, ptpIndex);
	}

	private void getLineCtps(Integer neId, Integer ptpId, String ptpIndex) throws AdapterException {
		getOmsCtps(neId, ptpId, ptpIndex);
	}

	private AdpTp getOmsCtps(Integer neId, Integer ptpId, String ptpIndex) throws AdapterException {
		List<String> layerRates = new ArrayList<String>();
		layerRates.add(LayerRate.OMS.name());
		int primaryLayerRate = LayerRate.OMS.ordinal();
		String nativeName = "/oms=1";
		return constructCtp(neId, nativeName, layerRates, ptpId, ptpIndex, primaryLayerRate);
	}

	public void syncCtps(AdpTp tp) throws AdapterException {
		String nativeName = tp.getNativeName();
		Integer neId = tp.getNeId();
		Integer ptpId = tp.getPtpID();
		String ptpIndex = tp.getKeyOnNe();
		if (nativeName.endsWith("-SIG")) {
			getSigCtps(neId, ptpId, ptpIndex);
		} else if (nativeName.endsWith("-LINE")) {
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
