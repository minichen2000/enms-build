package com.nsb.enms.adapter.server.wdm.business.tp.ctp.oa;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.wdm.utils.AdpTpWrapperUtil;
import com.nsb.enms.common.LayerRate;
import com.nsb.enms.restful.model.adapter.AdpKVPair;
import com.nsb.enms.restful.model.adapter.AdpTp;

public class GetAHPHGCTPs {
	private final static Logger log = LogManager.getLogger(GetAHPHGCTPs.class);

	public static void main(String[] args) {

	}

	private AdpTp constructCTP(String neId, String nativeName, List<String> layerRates, AdpTp ptp,
			String primaryLayerRate, List<AdpKVPair> params) throws AdapterException {
		return AdpTpWrapperUtil.constructCTP(neId, nativeName, layerRates, ptp, primaryLayerRate, params);
	}

	private void getSIGCTPs(String neId, AdpTp ptp) throws AdapterException {
		getOMSCTPs(neId, ptp);
	}

	private void getLineCTPs(String neId, AdpTp ptp) throws AdapterException {
		getOMSCTPs(neId, ptp);
	}

	private AdpTp getOMSCTPs(String neId, AdpTp ptp) throws AdapterException {
		String primaryLayerRate = LayerRate.OMS.name();
		List<String> layerRates = new ArrayList<String>();
		layerRates.add(primaryLayerRate);
		String nativeName = "/oms=1";
		return constructCTP(neId, nativeName, layerRates, ptp, primaryLayerRate, null);
	}

	public void syncCTPs(AdpTp ptp) throws AdapterException {
		String nativeName = ptp.getNativeName();
		String neId = ptp.getNeId();
		if (nativeName.endsWith("-SIG")) {
			getSIGCTPs(neId, ptp);
		} else if (nativeName.endsWith("-LINE")) {
			getLineCTPs(neId, ptp);
		}
	}
}
