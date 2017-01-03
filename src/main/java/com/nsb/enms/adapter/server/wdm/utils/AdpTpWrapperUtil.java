package com.nsb.enms.adapter.server.wdm.utils;

import java.util.List;

import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.common.Direction;
import com.nsb.enms.common.TpType;
import com.nsb.enms.restful.model.adapter.AdpTp;

public class AdpTpWrapperUtil {

	public static AdpTp constructCtp(String tpId, String neId, String nativeName, List<String> layerRates, String ptpId,
			String ptpIndex, String primaryLayerRate) throws AdapterException {
		AdpTp tp = new AdpTp();
		tp.setId(tpId);
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
		tp.setAlarmState(null);
		return tp;
	}

}
