package com.nsb.enms.adapter.server.wdm.business.tp.ctp.sfd;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.wdm.utils.AdpTpWrapperUtil;
import com.nsb.enms.common.LayerRate;
import com.nsb.enms.restful.model.adapter.AdpKVPair;
import com.nsb.enms.restful.model.adapter.AdpTp;

public class GetSFD44CTPs {
	private final static Logger log = LogManager.getLogger(GetSFD44CTPs.class);

	public static void main(String[] args) {

	}

	private AdpTp constructCTP(String neId, String nativeName, List<String> layerRates, AdpTp ptp,
			String primaryLayerRate, List<AdpKVPair> params) throws AdapterException {
		return AdpTpWrapperUtil.constructCTP(neId, nativeName, layerRates, ptp, primaryLayerRate, params);
	}

	private void getSingleCTPs(String neId, AdpTp ptp) throws AdapterException {
		AdpTpWrapperUtil.getOCHCTP(neId, ptp);
	}

	private void getMutilCTPs(String neId, AdpTp ptp, String nativeName) throws AdapterException {
		for (int i = 1; i < 45; i++) {
			AdpTpWrapperUtil.getOCHCTP(neId, ptp, nativeName + "_" + i);
		}
		getOMSCTPs(neId, ptp);
	}

	private AdpTp getOMSCTPs(String neId, AdpTp ptp) throws AdapterException {
		String primaryLayerRate = LayerRate.OMS.name();
		List<String> layerRates = new ArrayList<String>();
		layerRates.add(primaryLayerRate);
		String userLabel = "/oms=1";
		return constructCTP(neId, userLabel, layerRates, ptp, primaryLayerRate, null);
	}

	public void syncCTPs(AdpTp ptp) throws AdapterException {
		String nativeName = ptp.getNativeName();
		String neId = ptp.getNeId();
		if (nativeName.endsWith("-OMD")) {
			getMutilCTPs(neId, ptp, nativeName);
		} else {
			getSingleCTPs(neId, ptp);
		}
	}
}
