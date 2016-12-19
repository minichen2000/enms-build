package com.nsb.enms.adapter.server.sdh.q3gateway.entity;

import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.constants.AdapterExceptionType;

public class Moi {
	private String moi;

	public Moi(String moi) {
		this.moi = moi;
	}

	public Moi() {

	}

	public String getLayerValue(int layer) throws AdapterException {
		String[] layerValues = moi.split("/");
		if (layer < layerValues.length) {
			return layerValues[layer];
		}
		throw new AdapterException(AdapterExceptionType.EXCPT_INTERNAL_ERROR, "Input layer is invalid!!!");
	}

	public String getMoi() {
		return moi;
	}

	public void setMoi(String moi) {
		this.moi = moi;
	}

	@Override
	public String toString() {
		return moi;
	}
}
