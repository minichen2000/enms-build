package com.nsb.enms.adapter.server.db.mongodb.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.nsb.enms.restful.model.adapter.Host;

public class NeAssignmentBean {
	private Host adapter = null;
	private String assignTime = null;
	private String status = null;
	private List<String> neList = new ArrayList<String>();
	private String remark = null;

	public Host getAdapter() {
		return adapter;
	}

	public void setAdapter(Host adapter) {
		this.adapter = adapter;
	}

	public String getAssignTime() {
		return assignTime;
	}

	public void setAssignTime(String assignTime) {
		this.assignTime = assignTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<String> getNeList() {
		return neList;
	}

	public void setNeList(List<String> neList) {
		this.neList = neList;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		NeAssignmentBean NEASSIGNMENT = (NeAssignmentBean) o;
		return Objects.equals(adapter, NEASSIGNMENT.adapter) && Objects.equals(assignTime, NEASSIGNMENT.assignTime)
				&& Objects.equals(status, NEASSIGNMENT.status) && Objects.equals(neList, NEASSIGNMENT.neList)
				&& Objects.equals(remark, NEASSIGNMENT.remark);
	}

	@Override
	public int hashCode() {
		return Objects.hash(adapter, assignTime, status, neList, remark);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class NeAssignment {\n");

		sb.append("    adapter: ").append(toIndentedString(adapter)).append("\n");
		sb.append("    assignTime: ").append(toIndentedString(assignTime)).append("\n");
		sb.append("    status: ").append(toIndentedString(status)).append("\n");
		sb.append("    neList: ").append(toIndentedString(neList)).append("\n");
		sb.append("    remark: ").append(toIndentedString(remark)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}
