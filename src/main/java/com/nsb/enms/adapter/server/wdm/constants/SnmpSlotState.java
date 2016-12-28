package com.nsb.enms.adapter.server.wdm.constants;

public enum SnmpSlotState {
	up(1), down(2), testing(3);
	
	private int code;

	private SnmpSlotState(int code) {
		this.code = code;
	}

	public static String getSlotState(String code) {
		for (SnmpSlotState type : SnmpSlotState.values()) {
			if (type.code == Integer.valueOf(code)) {
				return type.name();
			}
		}
		return null;
	}
}
