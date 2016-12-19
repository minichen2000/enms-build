package com.nsb.enms.adapter.server.common.api.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.api.itf.AdpEquApiItf;
import com.nsb.enms.restful.model.adapter.AdpNe;

public abstract class AdpDefaultEquApiImpl implements AdpEquApiItf {
	private Logger log = LogManager.getLogger(AdpDefaultEquApiImpl.class);
	protected AdpNe data;
	private Integer id;

	public AdpDefaultEquApiImpl(AdpNe ne) {
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
