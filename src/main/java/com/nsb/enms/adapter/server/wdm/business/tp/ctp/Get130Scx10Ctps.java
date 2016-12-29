package com.nsb.enms.adapter.server.wdm.business.tp.ctp;

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

public class Get130Scx10Ctps {
	private final static Logger log = LogManager.getLogger(Get130Scx10Ctps.class);
	private AdpTpsDbMgr tpDbMgr = new AdpTpsDbMgr();

	public static void main(String[] args) {

	}

	private AdpTp getDsrCtp(Integer neId, Integer ptpId, String ptpIndex) throws AdapterException {
		List<String> layerRates = new ArrayList<String>();
		layerRates.add(LayerRate.DSR.name());
		int primaryLayerRate = LayerRate.DSR.ordinal();
		return constructCtp(neId, "/dsr=1", layerRates, ptpId, ptpIndex, primaryLayerRate);
	}

	private AdpTp constructCtp(Integer neId, String userLabel, List<String> layerRates, Integer ptpId, String ptpIndex,
			Integer primaryLayerRate) throws AdapterException {
		AdpTp tp = new AdpTp();
		tp.setId(getMaxId(neId));
		tp.setNeId(neId);
		tp.setNativeName(userLabel);
		tp.setUserLabel(userLabel);
		tp.setLayerRates(layerRates);
		String keyOnNe = primaryLayerRate + "_" + ptpIndex;
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
		return tp;
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

	private AdpTp getOdujCtp(String userLabel, Integer neId, Integer ptpId, String ptpIndex) throws AdapterException {
		List<String> layerRates = new ArrayList<String>();
		layerRates.add(LayerRate.ODU2.name());
		int primaryLayerRate = LayerRate.ODU2.ordinal();
		return constructCtp(neId, userLabel, layerRates, ptpId, ptpIndex, primaryLayerRate);
	}

	private void getClientCtps(List<AdpTp> ctpList, Integer neId, Integer ptpId, String ptpIndex)
			throws AdapterException {
		AdpTp dsrCtp = getDsrCtp(neId, ptpId, ptpIndex);
		ctpList.add(dsrCtp);
		AdpTp odujCtp = getOdujCtp("/odu2=1", neId, ptpId, ptpIndex);
		ctpList.add(odujCtp);
	}

	private void getLineCtps(List<AdpTp> ctpList, Integer neId, Integer ptpId, String ptpIndex)
			throws AdapterException {
		for (int i = 1; i <= 10; i++) {
			AdpTp odujCtp = getOdujCtp("/odu4=1/odu2=" + i, neId, ptpId, ptpIndex);
			ctpList.add(odujCtp);
		}
		AdpTp odukCtp = getOdukCtp(neId, ptpId, ptpIndex);
		ctpList.add(odukCtp);
		AdpTp otukCtp = getOtukCtps(neId, ptpId, ptpIndex);
		ctpList.add(otukCtp);
		AdpTp ochjCtp = getOchCtp(neId, ptpId, ptpIndex);
		ctpList.add(ochjCtp);
	}

	private AdpTp getOdukCtp(Integer neId, Integer ptpId, String ptpIndex) throws AdapterException {
		List<String> layerRates = new ArrayList<String>();
		layerRates.add(LayerRate.ODU4.name());
		int primaryLayerRate = LayerRate.ODU4.ordinal();
		String userLabel = "/odu4=1";
		return constructCtp(neId, userLabel, layerRates, ptpId, ptpIndex, primaryLayerRate);

	}

	private AdpTp getOchCtp(Integer neId, Integer ptpId, String ptpIndex) throws AdapterException {
		List<String> layerRates = new ArrayList<String>();
		layerRates.add(LayerRate.OCH.name());
		int primaryLayerRate = LayerRate.OCH.ordinal();
		String userLabel = "";
		return constructCtp(neId, userLabel, layerRates, ptpId, ptpIndex, primaryLayerRate);
	}

	private AdpTp getOtukCtps(Integer neId, Integer ptpId, String ptpIndex) throws AdapterException {
		List<String> layerRates = new ArrayList<String>();
		layerRates.add(LayerRate.OTU4.name());
		int primaryLayerRate = LayerRate.OTU4.ordinal();
		String userLabel = "/out4=1";
		return constructCtp(neId, userLabel, layerRates, ptpId, ptpIndex, primaryLayerRate);
	}

	private LayerRate getOduLayerRate(Integer i) {
		switch (i) {
		case 0:
			return LayerRate.ODU0;
		case 1:
			return LayerRate.ODU1;
		case 2:
			return LayerRate.ODU2;
		case 3:
			return LayerRate.ODU3;
		case 4:
			return LayerRate.ODU4;
		default:
			return null;
		}
	}

	public List<AdpTp> getNotOtnSignalCtps(Integer neId, Integer ptpId, String ptpIndex) throws AdapterException {
		List<AdpTp> ctpList = new ArrayList<AdpTp>();
		getClientCtps(ctpList, neId, ptpId, ptpIndex);
		getLineCtps(ctpList, neId, ptpId, ptpIndex);
		List<AdpTp> newCtpList = new ArrayList<AdpTp>();
		for (AdpTp ctp : ctpList) {
			AdpTp ctpFromDb = isCtpExisted(neId, ctp.getKeyOnNe());
			if (null != ctpFromDb) {
				// TODO 替换已有值
				newCtpList.add(ctpFromDb);
				continue;
			}
			try {
				AdpTp newCtp = tpDbMgr.addTp(ctp);
				newCtpList.add(newCtp);
			} catch (Exception e) {
				log.error("addTps", e);
				throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
			}
		}

		return newCtpList;
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
