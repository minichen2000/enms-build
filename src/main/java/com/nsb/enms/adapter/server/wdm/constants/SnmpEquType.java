package com.nsb.enms.adapter.server.wdm.constants;

public enum SnmpEquType {
	_130SCX10("130SCX10"), SFD44("SFD44"), AHPHG("AHPHG");

	private String equType;

	private SnmpEquType(String equType) {
		this.equType = equType;
	}

	public String getEquType() {
		return equType;
	}
}
