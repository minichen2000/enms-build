package com.nsb.enms.adapter.server.common.utils;

import org.apache.commons.lang3.StringUtils;

import com.nsb.enms.common.EntityType;

public class GenerateKeyOnNeUtil {
	public static String generateKeyOnNe(EntityType type, String moc, String moi) {
		switch (type) {
		case NE:
			return moc + ":" + moi;
		case TP:
		case XC:
		case BOARD:
			return moc + ":" + moi.replaceAll("neGroupId=\\d+/networkElementId=\\d+/", StringUtils.EMPTY);
		default:
			return StringUtils.EMPTY;
		}
	}

	public static String getMoi(String keyOnNe) {
		return keyOnNe.split(":")[1];
	}
}