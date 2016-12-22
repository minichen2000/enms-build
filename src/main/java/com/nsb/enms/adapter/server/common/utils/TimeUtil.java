package com.nsb.enms.adapter.server.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeUtil {
	// private static final DateFormat LOCAL_TMF_FORMAT = new
	// SimpleDateFormat("yyyyMMddHHmmss.0Z");

	private static final DateFormat UTC_TMF_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss.0'Z'");

	private static final DateFormat LABEL_TMF_FORMAT = new SimpleDateFormat("yyyyMMddHHmm");

	private static Pattern pattern = Pattern.compile("(\\d+)\\s*days,\\s*(\\d+):(\\d+):(\\d+)\\.(\\d*)");

	static {
		UTC_TMF_FORMAT.setTimeZone(TimeZone.getTimeZone("Etc/Greenwich"));
	}

	// public static String getUtcTime(boolean utc) {
	// return utc ? getUtcTmfTime() : getLocalTmfTime();
	// }

	// public static String getLocalTmfTime() {
	// return LOCAL_TMF_FORMAT.format(new Date());
	// }

	public static Long getLocalTmfTime() {
		return new Date().getTime();
	}

	public static String getUtcTmfTime() {
		return UTC_TMF_FORMAT.format(new Date());
	}

	public static String getUtcTmfTime(Date date) {
		return UTC_TMF_FORMAT.format(date);
	}

	public static String getLabelTmfTime() {
		return LABEL_TMF_FORMAT.format(new Date());
	}

	public static String getLabelTmfTime(Date date) {
		return LABEL_TMF_FORMAT.format(date);
	}

	public static Long getTime(String time) {
		Date date = null;
		try {
			date = LABEL_TMF_FORMAT.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (null == date) {
			return new Date().getTime();
		}
		return date.getTime();
	}

	public static Long getSysUpTime(String time) {
		long sysUpTime = 0;
		Matcher matcher = pattern.matcher(time);
		if (matcher.matches()) {
			int day = Integer.valueOf(matcher.group(1));
			int hour = Integer.valueOf(matcher.group(2));
			int min = Integer.valueOf(matcher.group(3));
			int sec = Integer.valueOf(matcher.group(4));
			int hsec = Integer.valueOf(matcher.group(5));
			sysUpTime = day * 24 * 3600 * 100 + hour * 3600 * 100 + min * 60 * 100 + sec * 100 + hsec;
		}

		return sysUpTime;
	}
}
