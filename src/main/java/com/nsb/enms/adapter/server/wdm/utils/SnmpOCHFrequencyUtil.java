package com.nsb.enms.adapter.server.wdm.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.wdm.constants.SnmpChannel;
import com.nsb.enms.adapter.server.wdm.factory.AdpSnmpClientFactory;
import com.nsb.enms.common.utils.Pair;
import com.nsb.enms.common.utils.snmp.SnmpClient;
import com.nsb.enms.mib.pss.def.M_tnNetworkPortConfigTable;
import com.nsb.enms.mib.pss.def.M_tnSfpPortInfoTable;

public class SnmpOCHFrequencyUtil {
	private final static Logger log = LogManager.getLogger(SnmpOCHFrequencyUtil.class);

	public static String getFrequency(String neId, String index) throws AdapterException {
		SnmpClient client = AdpSnmpClientFactory.getInstance().getByNeId(neId);
		String unknown = "/frequency=unknown";
		try {
			List<String> oids = new ArrayList<String>();
			oids.add(M_tnSfpPortInfoTable.tnSfpPortWavelength);
			oids.add(M_tnNetworkPortConfigTable.tnNwPortProgrammedChannel);
			List<Pair<String, String>> values = client.snmpMultiGet(oids, index);
			if (null == values || values.isEmpty()) {
				return unknown;
			}

			String waveLength = values.get(0).getSecond();
			if ("1".equals(waveLength)) {
				return "/frequency=tunable-number=1";
			}

			String channel = values.get(1).getSecond();
			if ("noSuchInstance".equalsIgnoreCase(channel)) {
				return unknown;
			}
			String frequency = SnmpChannel.getFrequency(Integer.valueOf(channel));
			return "/frequency=" + frequency;
		} catch (IOException e) {
			log.error("getFrequency", e);
		} catch (NumberFormatException e) {
			log.error("getFrequency", e);
		}
		return unknown;
	}

	public static void main(String args[]) {
		SnmpClient client = new SnmpClient("135.251.96.13", 161, "admin_snmp");
		AdpSnmpClientFactory.getInstance().add("135.251.96.13:161", client);
		try {
			getFrequency("13", "51380480");
		} catch (AdapterException e) {
			e.printStackTrace();
		}
	}
}
