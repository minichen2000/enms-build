package com.nsb.enms.adapter.server.common.api.factories;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.api.builder.AdpBuilder;
import com.nsb.enms.adapter.server.common.api.itf.AdpEquApiItf;
import com.nsb.enms.adapter.server.common.api.itf.AdpNeApiItf;
import com.nsb.enms.adapter.server.common.api.itf.AdpTpApiItf;
import com.nsb.enms.adapter.server.common.api.itf.AdpXcApiItf;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.common.EntityType;
import com.nsb.enms.restful.model.adapter.AdpNe;

public class AdpFactory {
	private Logger log = LogManager.getLogger(AdpFactory.class);
	private static final AdpFactory INSTANCE = new AdpFactory();

	private AdpFactory() {
	}

	public static AdpFactory getInstance() {
		return INSTANCE;
	}

	public AdpNeApiItf getNeApi(AdpNe ne) throws AdapterException {
		return (AdpNeApiItf) AdpBuilder.getInstance().getApiItf(ne, EntityType.NE);
	}

	public AdpNeApiItf getNeApi(String neid) throws AdapterException {
		return (AdpNeApiItf) AdpBuilder.getInstance().getApiItf(neid, EntityType.NE);
	}

	public AdpTpApiItf getTpApi(AdpNe ne) throws AdapterException {
		return (AdpTpApiItf) AdpBuilder.getInstance().getApiItf(ne, EntityType.TP);
	}

	public AdpTpApiItf getTpApi(String neid) throws AdapterException {
		return (AdpTpApiItf) AdpBuilder.getInstance().getApiItf(neid, EntityType.TP);
	}

	public AdpXcApiItf getXcApi(AdpNe ne) throws AdapterException {
		return (AdpXcApiItf) AdpBuilder.getInstance().getApiItf(ne, EntityType.XC);
	}

	public AdpXcApiItf getXcApi(String neid) throws AdapterException {
		return (AdpXcApiItf) AdpBuilder.getInstance().getApiItf(neid, EntityType.XC);
	}

	public AdpEquApiItf getEquApi(AdpNe ne) throws AdapterException {
		return (AdpEquApiItf) AdpBuilder.getInstance().getApiItf(ne, EntityType.EQUIPMENT);
	}

	public AdpEquApiItf getEquApi(String neid) throws AdapterException {
		return (AdpEquApiItf) AdpBuilder.getInstance().getApiItf(neid, EntityType.EQUIPMENT);
	}
}
