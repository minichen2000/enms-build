package com.nsb.enms.adapter.server.action.entity.param;

import org.apache.commons.lang3.StringUtils;

public class XcParamBean {
	private String groupId = StringUtils.EMPTY;
	private String neId = StringUtils.EMPTY;
	private String vc4TtpId = StringUtils.EMPTY;
	private String tug3Id = StringUtils.EMPTY;
	private String tug2Id = StringUtils.EMPTY;
	private String tu12CtpId = StringUtils.EMPTY;
	private String vc12TtpId = StringUtils.EMPTY;
	private boolean isSdhTp = false;

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getNeId() {
		return neId;
	}

	public void setNeId(String neId) {
		this.neId = neId;
	}

	public String getVc4TtpId() {
		return vc4TtpId;
	}

	public void setVc4TtpId(String vc4TtpId) {
		this.vc4TtpId = vc4TtpId;
	}

	public String getTug3Id() {
		return tug3Id;
	}

	public void setTug3Id(String tug3Id) {
		this.tug3Id = tug3Id;
	}

	public String getTug2Id() {
		return tug2Id;
	}

	public void setTug2Id(String tug2Id) {
		this.tug2Id = tug2Id;
	}

	public String getTu12CtpId() {
		return tu12CtpId;
	}

	public void setTu12CtpId(String tu12CtpId) {
		this.tu12CtpId = tu12CtpId;
	}

	public String getVc12TtpId() {
		return vc12TtpId;
	}

	public void setVc12TtpId(String vc12TtpId) {
		this.vc12TtpId = vc12TtpId;
	}

	public boolean isSdhTp() {
		return isSdhTp;
	}

	public void setSdhTp(boolean isSdhTp) {
		this.isSdhTp = isSdhTp;
	}

	@Override
	public String toString() {
		return "XcParamBean [groupId=" + groupId + ", neId=" + neId + ", vc4TtpId=" + vc4TtpId + ", tug3Id=" + tug3Id
				+ ", tug2Id=" + tug2Id + ", tu12CtpId=" + tu12CtpId + ", vc12TtpId=" + vc12TtpId + ", isSdhTp="
				+ isSdhTp + "]";
	}
}
