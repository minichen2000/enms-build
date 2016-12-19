package com.nsb.enms.adapter.server.wdm.api.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.api.impl.AdpDefaultEquApiImpl;
import com.nsb.enms.restful.model.adapter.AdpNe;

public class AdpSnmpEquApiImpl extends AdpDefaultEquApiImpl {
	private Logger log = LogManager.getLogger(AdpSnmpEquApiImpl.class);

	public AdpSnmpEquApiImpl(AdpNe ne) {
		super(ne);
	}
}
