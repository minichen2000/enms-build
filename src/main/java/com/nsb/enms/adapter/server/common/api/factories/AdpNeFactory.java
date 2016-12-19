package com.nsb.enms.adapter.server.common.api.factories;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.api.builder.AdpNeBuilder;
import com.nsb.enms.adapter.server.common.api.itf.AdpNeApiItf;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.restful.model.adapter.AdpNe;

public class AdpNeFactory {
	private Logger log = LogManager.getLogger(AdpNeFactory.class);
	private static final AdpNeFactory INSTANCE = new AdpNeFactory();

	private AdpNeFactory() {
	}

	public static AdpNeFactory getInstance() {
		return INSTANCE;
	}

	public AdpNeApiItf getNeApi(AdpNe ne) throws AdapterException {
		return AdpNeBuilder.getInstance().getNeInstance(ne);
	}

	public AdpNeApiItf getNeApi(Integer neid) throws AdapterException {
		return AdpNeBuilder.getInstance().getNeInstance(neid);
	}
}
