package com.nsb.enms.adapter.server.common.utils;

import org.apache.commons.lang3.StringUtils;

import com.nsb.enms.common.utils.ValidationUtil;

public class Ip2HexUtil {

	public static void main(String args[]) {
		String s = Ip2HexUtil.convert("135.251.99.35", 162);
		System.out.println(s);
	}

	public static String convert(String ip, int port) {
		if (StringUtils.isEmpty(ip) || !ValidationUtil.isValidPort(port)) {
			return StringUtils.EMPTY;
		}

		StringBuffer sb = new StringBuffer();
		String chars[] = ip.split("\\.");
		for (String s : chars) {
			Integer i = Integer.valueOf(s);
			sb.append(Integer.toHexString(i)).append(":");
		}

		sb.append("00:");
		sb.append(Integer.toHexString(Integer.valueOf(port)));
		return sb.toString();
	}
}
