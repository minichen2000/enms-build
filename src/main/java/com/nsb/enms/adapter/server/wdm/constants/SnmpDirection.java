package com.nsb.enms.adapter.server.wdm.constants;

import org.apache.commons.lang3.StringUtils;

import com.nsb.enms.common.Direction;

public enum SnmpDirection {
	bidirectional(1), unidirectionalTx(2), unidirectionalRx(3);

	private int code;

	private SnmpDirection(int code) {
		this.code = code;
	}

	public static String getDirection(String code) {
		if (StringUtils.isEmpty(code)) {
			return Direction.BI.name();
		}
		Integer intCode = Integer.valueOf(code);
		if (intCode == 1) {
			return Direction.BI.name();
		}
		return Direction.UNI.name();
	}
}
