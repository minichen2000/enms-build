package com.nsb.enms.adapter.server.wdm.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SnmpTpUserLabelUtil {
	private final static Logger log = LogManager.getLogger(SnmpTpUserLabelUtil.class);

	public static void main(String[] args) {
		System.out.println(generate("16843008"));
	}

	public static String generate(String index) {
		Integer[] result = string2Hex(index);
		if (null == result) {
			return StringUtils.EMPTY;
		}
		StringBuilder userLabel = new StringBuilder();
		userLabel.append("/rack=1");
		userLabel.append("/shelf=" + result[0]);
		userLabel.append("/slot=" + result[1]);
		userLabel.append("/port=" + result[2]);

		return userLabel.toString();
	}

	public static Integer[] string2Hex(String index) {
		Integer hex;
		try {
			hex = Integer.valueOf(index);
		} catch (NumberFormatException e) {
			log.error("index is invalid", e);
			return null;
		}
		Integer[] result = new Integer[4];
		result[0] = (hex) >> 24;
		result[1] = (hex & 0x00ff0000) >> 16;
		result[2] = (hex & 0x0000ff00) >> 8;
		result[3] = hex & 0x000000ff;
		for (Integer i : result) {
			System.out.println(i);
		}
		return result;
	}
}
