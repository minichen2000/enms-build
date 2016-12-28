package com.nsb.enms.adapter.server.common.utils;

public class Object2IntegerUtil {

	public static Integer toInt(Object value) {
		try {
			return Integer.valueOf(String.valueOf(value));
		} catch (NumberFormatException e) {
			return 0;
		}
	}
}
