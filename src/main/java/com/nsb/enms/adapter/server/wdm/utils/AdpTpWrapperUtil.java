package com.nsb.enms.adapter.server.wdm.utils;

import java.util.List;

import com.nsb.enms.adapter.server.common.business.itf.ObjectIdGenerator;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.wdm.business.objectIdGenerator.WdmObjectIdGenerator;
import com.nsb.enms.common.Direction;
import com.nsb.enms.common.TpType;
import com.nsb.enms.restful.model.adapter.AdpKVPair;
import com.nsb.enms.restful.model.adapter.AdpTp;

public class AdpTpWrapperUtil {
	private static ObjectIdGenerator objectIdGenerator = new WdmObjectIdGenerator();

	public static AdpTp constructCTP(String neId, String nativeName, List<String> layerRates, AdpTp ptp,
			String primaryLayerRate, List<AdpKVPair> params) throws AdapterException {
		AdpTp tp = new AdpTp();
		tp.setNeId(neId);
		tp.setNativeName(nativeName);
		tp.setUserLabel(nativeName);
		tp.setLayerRates(layerRates);
		String keyOnNe = nativeName + "_" + ptp.getKeyOnNe();
		tp.setKeyOnNe(keyOnNe);
		tp.setDirection(Direction.BI.name());
		tp.setFreeResources(null);
		tp.setTpType(TpType.CTP.name());
		tp.setPrimaryLayerRate(primaryLayerRate);
		tp.setId(objectIdGenerator.generateCTPId(ptp, tp));
		String ptpID = ptp.getId();
		tp.setPtpID(ptpID);
		tp.setParentTpID(ptpID);
		tp.setParams(null);
		tp.setAlarmState(null);
		return tp;
	}

}
