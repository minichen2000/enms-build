package com.nsb.enms.adapter.server.wdm.business.tp.ctp;

import java.util.ArrayList;
import java.util.List;

import com.nsb.enms.adapter.server.common.db.mgr.AdpSeqDbMgr;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.common.Direction;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.common.LayerRate;
import com.nsb.enms.common.TpType;
import com.nsb.enms.restful.model.adapter.AdpTp;

public class Get130Scx10Ctps {

	public static void main(String[] args) {

	}

	private AdpTp getDsrCtps(Integer neId, Integer ptpId, String ptpIndex) throws AdapterException {
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
		tp.setKeyOnNe(ptpIndex);
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

	private AdpTp getOdujCtps(String userLabel, Integer neId, Integer ptpId, String ptpIndex) throws AdapterException {
		List<String> layerRates = new ArrayList<String>();
		layerRates.add(LayerRate.ODU2.name());
		int primaryLayerRate = LayerRate.ODU2.ordinal();
		return constructCtp(neId, userLabel, layerRates, ptpId, ptpIndex, primaryLayerRate);
	}

	private void getClientCtps(Integer neId, Integer ptpId, String ptpIndex) throws AdapterException {
		getDsrCtps(neId, ptpId, ptpIndex);
		getOdujCtps("/odu2=1", neId, ptpId, ptpIndex);
	}

	private void getLineCtps(Integer neId, Integer ptpId, String ptpIndex) throws AdapterException {
		for (int i = 1; i <= 10; i++) {
			getOdujCtps("/odu4=1/odu2=" + i, neId, ptpId, ptpIndex);
		}
		getOdukCtps(neId, ptpId, ptpIndex);
		getOtukCtps(neId, ptpId, ptpIndex);
		getOchCtps(neId, ptpId, ptpIndex);
	}

	private AdpTp getOdukCtps(Integer neId, Integer ptpId, String ptpIndex) throws AdapterException {
		List<String> layerRates = new ArrayList<String>();
		layerRates.add(LayerRate.ODU4.name());
		int primaryLayerRate = LayerRate.ODU4.ordinal();
		String userLabel = "/odu4=1";
		return constructCtp(neId, userLabel, layerRates, ptpId, ptpIndex, primaryLayerRate);

	}

	private AdpTp getOchCtps(Integer neId, Integer ptpId, String ptpIndex) throws AdapterException {
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

	public void getNotOtnSignalCtp(Integer neId, Integer ptpId, String ptpIndex) throws AdapterException {
		getClientCtps(neId, ptpId, ptpIndex);
		getLineCtps(neId, ptpId, ptpIndex);
	}
}
