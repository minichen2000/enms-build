package com.nsb.enms.adapter.server.db.mongodb.bean;

public class AdpCommonBean {
	private String aId;

	private String userLable;

	private String layerRate;

	private String[] addtionalInfo;

	public String getaId() {
		return aId;
	}

	public void setaId(String aId) {
		this.aId = aId;
	}

	public String getUserLable() {
		return userLable;
	}

	public void setUserLable(String userLable) {
		this.userLable = userLable;
	}

	public String getLayerRate() {
		return layerRate;
	}

	public void setLayerRate(String layerRate) {
		this.layerRate = layerRate;
	}

	public String[] getAddtionalInfo() {
		return addtionalInfo;
	}

	public void setAddtionalInfo(String[] addtionalInfo) {
		this.addtionalInfo = addtionalInfo;
	}
}
