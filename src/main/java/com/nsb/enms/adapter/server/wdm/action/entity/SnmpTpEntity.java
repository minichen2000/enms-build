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

	private String direction;

	private int secondaryState;

	private String connectedTo;

	private String connectedFrom;

	private String sfpPortModuleVendorSerNo;
	
	private String sfpPortModuleVendor;
	
	private String sfpPortModuleType;
	
	private String sfpPortCLEI;
	
	private String sfpPortUnitPartNum;
	
	private String sfpPortSWPartNum;
	
	private String sfpPortFactoryID;
	
	private String sfpPortDate;
	
	private String sfpPortExtraData;

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

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public int getSecondaryState() {
		return this.secondaryState;
	}

	public void setSecondaryState(int secondaryState) {
		this.secondaryState = secondaryState;
	}

	public String getConnectedTo() {
		return this.connectedTo;
	}

	public void setConnectedTo(String connectedTo) {
		this.connectedTo = connectedTo;
	}

	public String getConnectedFrom() {
		return this.connectedFrom;
	}

	public void setConnectedFrom(String connectedFrom) {
		this.connectedFrom = connectedFrom;
	}

	public void setSfpPortModuleVendorSerNo(String sfpPortModuleVendorSerNo) {
		this.sfpPortModuleVendorSerNo = sfpPortModuleVendorSerNo;
	}
	public String getSfpPortModuleVendorSerNo() {
		return this.sfpPortModuleVendorSerNo;
	}
	
	public void setSfpPortModuleVendor(String sfpPortModuleVendor) {
		this.sfpPortModuleVendor = sfpPortModuleVendor;
	}
	public String getSfpPortModuleVendor() {
		return this.sfpPortModuleVendor;
	}
	
	public void setSfpPortModuleType(String sfpPortModuleType) {
		this.sfpPortModuleType = sfpPortModuleType;
	}
	public String getSfpPortModuleType() {
		return this.sfpPortModuleType;
	}
	
	public void setSfpPortCLEI(String sfpPortCLEI) {
		this.sfpPortCLEI = sfpPortCLEI;
	}
	public String getSfpPortCLEI() {
		return this.sfpPortCLEI;
	}
	
	public void setSfpPortUnitPartNum(String sfpPortUnitPartNum) {
		this.sfpPortUnitPartNum = sfpPortUnitPartNum;
	}
	public String getSfpPortUnitPartNum() {
		return this.sfpPortUnitPartNum;
	}
	
	public void setSfpPortSWPartNum(String sfpPortSWPartNum) {
		this.sfpPortSWPartNum = sfpPortSWPartNum;
	}
	public String getSfpPortSWPartNum() {
		return this.sfpPortSWPartNum ;
	}
	
	public void setSfpPortFactoryID(String sfpPortFactoryID) {
		this.sfpPortFactoryID = sfpPortFactoryID;
	}
	public String getSfpPortFactoryID() {
		return this.sfpPortFactoryID;
	}
	
	public void setSfpPortDate(String sfpPortDate) {
		this.sfpPortDate = sfpPortDate;
	}
	public String getSfpPortDate() {
		return this.sfpPortDate;
	}
	
	public void setSfpPortExtraData(String sfpPortExtraData) {
		this.sfpPortExtraData = sfpPortExtraData;
	}
	
	public String getSfpPortExtraData() {
		return this.sfpPortExtraData = sfpPortExtraData;
	}
	
	@Override
	public String toString() {
		return "SnmpTpEntity [index=" + index + ", userLabel=" + userLabel + ", tpType=" + tpType + ", internalType="
				+ internalType + ", adminStatus=" + adminStatus + ", operStatus=" + operStatus + ", supportedTypes="
				+ supportedTypes + ", direction=" + direction + ", secondaryState=" + secondaryState +", connectedTo="
				+ connectedTo +", connectedFrom=" + connectedFrom + ", sfpPortModuleVendorSerNo=" + sfpPortModuleVendorSerNo +
				", sfpPortModuleVendor=" + sfpPortModuleVendor +
				", sfpPortModuleType=" + sfpPortModuleType +
				", sfpPortCLEI=" + sfpPortCLEI +
				", sfpPortUnitPartNum=" + sfpPortUnitPartNum +
				", sfpPortSWPartNum=" + sfpPortSWPartNum +
				", sfpPortFactoryID=" + sfpPortFactoryID +
				", sfpPortDate=" + sfpPortDate +
				", sfpPortExtraData=" + sfpPortExtraData + "]";
	}
}