package com.nsb.enms.adapter.server.common.api.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.api.itf.AdpXcApiItf;
import com.nsb.enms.restful.model.adapter.AdpNe;

public abstract class AdpDefaultXcApiImpl implements AdpXcApiItf {
	private Logger log = LogManager.getLogger(AdpDefaultXcApiImpl.class);
	protected AdpNe data;
	private Integer id;

	public AdpDefaultXcApiImpl(AdpNe ne) {
		data = ne;
		id = data.getId();
	}

	public AdpNe getData() {
		return data;
	}

	protected Integer getId() {
		return id;
	}
}
