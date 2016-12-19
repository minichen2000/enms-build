package com.nsb.enms.adapter.server.sdh.api.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.api.impl.AdpDefaultTpApiImpl;
import com.nsb.enms.restful.model.adapter.AdpNe;

public class AdpQ3TpApiImpl extends AdpDefaultTpApiImpl {
	private Logger log = LogManager.getLogger(AdpQ3TpApiImpl.class);

	public AdpQ3TpApiImpl(AdpNe ne) {
		super(ne);
	}
}
