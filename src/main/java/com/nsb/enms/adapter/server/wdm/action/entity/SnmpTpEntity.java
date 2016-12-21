package com.nsb.enms.adapter.server.wdm.action.entity;

import java.util.List;

public class SnmpTpEntity {
	private String index;

	private String userLabel;

	private String tpType;

	private String internalType;

	private int adminStatus;

	private int operStatus;

	private List<String> supportedTypes;

	public String getUserLabel() {
		return userLabel;
	}

	public void setUserLabel(String userLabel) {
		this.userLabel = userLabel;
	}

	public List<String> getSupportedTypes() {
		return supportedTypes;
	}

	public void setSupportedTypes(List<String> supportedTypes) {
		this.supportedTypes = supportedTypes;
	}

	public String getTpType() {
		return tpType;
	}

	public void setTpType(String tpType) {
		this.tpType = tpType;
	}

	public String getInternalType() {
		return internalType;
	}

	public void setInternalType(String internalType) {
		this.internalType = internalType;
	}

	public int getAdminStatus() {
		return adminStatus;
	}

	public void setAdminStatus(int adminStatus) {
		this.adminStatus = adminStatus;
	}

	public int getOperStatus() {
		return operStatus;
	}

	public void setOperStatus(int operStatus) {
		this.operStatus = operStatus;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	@Override
	public String toString() {
		return "SnmpTpEntity [index=" + index + ", userLabel=" + userLabel + ", tpType=" + tpType + ", internalType="
				+ internalType + ", adminStatus=" + adminStatus + ", operStatus=" + operStatus + ", supportedTypes="
				+ supportedTypes + "]";
	}
}