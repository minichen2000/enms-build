package com.nsb.enms.adapter.server.common.utils;

public class String2AsciiUtil {

	public static void main(String[] args) {
		System.out.println(convert("135.251.99.35"));
		System.out.println(asciiToString("49.51.53.46.50.53.49.46.57.57.46.51.53"));
	}

	public static String convert(String value) {
		StringBuffer sb = new StringBuffer();
		char[] chars = value.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (i != chars.length - 1) {
				sb.append((int) chars[i]).append(".");
			} else {
				sb.append((int) chars[i]);
			}
		}
		return sb.toString();
	}

	public static String asciiToString(String value) {
		StringBuffer sb = new StringBuffer();
		String[] chars = value.split("\\.");
		for (int i = 0; i < chars.length; i++) {
			sb.append((char) Integer.parseInt(chars[i]));
		}
		return sb.toString();
	}
}
