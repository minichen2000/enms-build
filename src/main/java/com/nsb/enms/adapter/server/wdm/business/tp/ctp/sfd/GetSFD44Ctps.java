package com.nsb.enms.adapter.server.wdm.business.tp.ctp.sfd;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.db.mgr.AdpSeqDbMgr;
import com.nsb.enms.adapter.server.common.db.mgr.AdpTpsDbMgr;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.wdm.utils.SnmpOCHTpNameUtil;
import com.nsb.enms.common.Direction;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.common.LayerRate;
import com.nsb.enms.common.TpType;
import com.nsb.enms.restful.model.adapter.AdpTp;

public class GetSFD44Ctps {
	private final static Logger log = LogManager.getLogger(GetSFD44Ctps.class);
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

	private void getSingleCtps(Integer neId, Integer ptpId, String ptpIndex) throws AdapterException {
		getOchCtp(neId, ptpId, ptpIndex);
	}

	private void getMutilCtps(Integer neId, Integer ptpId, String ptpIndex, String nativeName) throws AdapterException {
		for (int i = 1; i < 45; i++) {
			getOchCtp(neId, ptpId, ptpIndex, nativeName + "_" + i);
		}
		getOmsCtps(neId, ptpId, ptpIndex);
	}

	private AdpTp getOchCtp(Integer neId, Integer ptpId, String ptpIndex) throws AdapterException {
		String nativeName = SnmpOCHTpNameUtil.getNativeName(neId, ptpIndex);
		return getOchCtp(neId, ptpId, ptpIndex, nativeName);
	}

	private AdpTp getOchCtp(Integer neId, Integer ptpId, String ptpIndex, String nativeName) throws AdapterException {
		List<String> layerRates = new ArrayList<String>();
		layerRates.add(LayerRate.OCH.name());
		int primaryLayerRate = LayerRate.OCH.ordinal();
		return constructCtp(neId, nativeName, layerRates, ptpId, ptpIndex, primaryLayerRate);
	}

	private AdpTp getOmsCtps(Integer neId, Integer ptpId, String ptpIndex) throws AdapterException {
		List<String> layerRates = new ArrayList<String>();
		layerRates.add(LayerRate.OMS.name());
		int primaryLayerRate = LayerRate.OMS.ordinal();
		String userLabel = "/oms=1";
		return constructCtp(neId, userLabel, layerRates, ptpId, ptpIndex, primaryLayerRate);
	}

	public void syncCtps(AdpTp tp) throws AdapterException {
		String userLabel = tp.getNativeName();
		Integer neId = tp.getNeId();
		Integer ptpId = tp.getPtpID();
		String ptpIndex = tp.getKeyOnNe();
		if (userLabel.endsWith("-OMD")) {
			getMutilCtps(neId, ptpId, ptpIndex, userLabel);
		} else {
			getSingleCtps(neId, ptpId, ptpIndex);
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
