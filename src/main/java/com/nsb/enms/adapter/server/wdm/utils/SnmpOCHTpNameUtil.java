package com.nsb.enms.adapter.server.wdm.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.wdm.constants.SnmpChannel;
import com.nsb.enms.adapter.server.wdm.factory.AdpSnmpClientFactory;
import com.nsb.enms.common.utils.Pair;
import com.nsb.enms.common.utils.snmp.SnmpClient;

public class SnmpOCHTpNameUtil {
	private final static Logger log = LogManager.getLogger(SnmpOCHTpNameUtil.class);

	public static String getNativeName(Integer neId, String index) throws AdapterException {
		SnmpClient client = AdpSnmpClientFactory.getInstance().getByNeId(neId);
		try {
			List<String> oids = new ArrayList<String>();
			oids.add("1.3.6.1.4.1.7483.2.2.4.3.5.46.1.6");
			oids.add("1.3.6.1.4.1.7483.2.2.4.3.4.42.1.3");
			List<Pair<String, String>> values = client.snmpMultiGet(oids, index);
			if (null == values || values.isEmpty()) {
				return StringUtils.EMPTY;
			}

			String waveLength = values.get(0).getSecond();
			if ("1".equals(waveLength)) {
				return "/frequency=tunable-number=1";
			}

			String channel = values.get(1).getSecond();
			String frequency = SnmpChannel.getFrequency(Integer.valueOf(channel));
			return "/frequency=" + frequency;

		} catch (IOException e) {
			log.error("getNativeName", e);
		} catch (NumberFormatException e) {
			log.error("getNativeName", e);
		}
		return StringUtils.EMPTY;
	}

	public static void main(String args[]) {
		SnmpClient client = new SnmpClient("135.251.96.5", 161, "admin_snmp");
		AdpSnmpClientFactory.getInstance().add("135.251.96.5:161", client);
		try {
			getNativeName(5, "51380480");
		} catch (AdapterException e) {
			e.printStackTrace();
		}
	}
}
