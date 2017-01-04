package com.nsb.enms.adapter.server.wdm.business.tp.ctp.ot;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.wdm.utils.AdpTpWrapperUtil;
import com.nsb.enms.common.LayerRate;
import com.nsb.enms.restful.model.adapter.AdpKVPair;
import com.nsb.enms.restful.model.adapter.AdpTp;

public class Get130SCX10CTPs {
	private final static Logger log = LogManager.getLogger(Get130SCX10CTPs.class);

	public Get130SCX10CTPs() {
	}

	public static void main(String[] args) {

	}

	private AdpTp getDSRCTP(String neId, AdpTp ptp) throws AdapterException {
		String primaryLayerRate = LayerRate.DSR.name();
		List<String> layerRates = new ArrayList<String>();
		layerRates.add(primaryLayerRate);
		return constructCTP(neId, "/dsr=1", layerRates, ptp, primaryLayerRate, null);
	}

	private AdpTp constructCTP(String neId, String nativeName, List<String> layerRates, AdpTp ptp,
			String primaryLayerRate, List<AdpKVPair> params) throws AdapterException {
		return AdpTpWrapperUtil.constructCTP(neId, nativeName, layerRates, ptp, primaryLayerRate, params);
	}

	private AdpTp getODUjCTP(String userLabel, String neId, AdpTp ptp) throws AdapterException {
		List<String> layerRates = new ArrayList<String>();
		String primaryLayerRate = LayerRate.ODU2.name();
		layerRates.add(primaryLayerRate);
		return constructCTP(neId, userLabel, layerRates, ptp, primaryLayerRate, null);
	}

	private void getClientCTPs(String neId, AdpTp ptp) throws AdapterException {
		getDSRCTP(neId, ptp);
		getODUjCTP("/odu2=1", neId, ptp);
	}

	private void getLineCTPs(String neId, AdpTp ptp) throws AdapterException {
		for (int i = 1; i < 11; i++) {
			getODUjCTP("/odu4=1/odu2=" + i, neId, ptp);
		}
		getODUkCTP(neId, ptp);
		getOTUkCTPs(neId, ptp);
		AdpTpWrapperUtil.getOCHCTP(neId, ptp);
	}

	private AdpTp getODUkCTP(String neId, AdpTp ptp) throws AdapterException {
		String primaryLayerRate = LayerRate.ODU4.name();
		List<String> layerRates = new ArrayList<String>();
		layerRates.add(primaryLayerRate);
		String userLabel = "/odu4=1";
		return constructCTP(neId, userLabel, layerRates, ptp, primaryLayerRate, null);

	}

	private AdpTp getOTUkCTPs(String neId, AdpTp ptp) throws AdapterException {
		String primaryLayerRate = LayerRate.OTU4.name();
		List<String> layerRates = new ArrayList<String>();
		layerRates.add(primaryLayerRate);
		String nativeName = "/out4=1";
		return constructCTP(neId, nativeName, layerRates, ptp, primaryLayerRate, null);
	}

	public void syncCTPs(AdpTp ptp) throws AdapterException {
		String nativeName = ptp.getNativeName();
		String neId = ptp.getNeId();
		if (nativeName.indexOf("-C") != -1) {
			getClientCTPs(neId, ptp);
		} else if (nativeName.indexOf("-L1") != -1) {
			getLineCTPs(neId, ptp);
		}
	}
}
