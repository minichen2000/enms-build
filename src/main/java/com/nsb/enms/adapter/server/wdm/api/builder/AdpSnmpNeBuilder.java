package com.nsb.enms.adapter.server.wdm.api.builder;

import com.nsb.enms.adapter.server.common.api.builder.AdpNeBuilder;
import com.nsb.enms.adapter.server.common.api.itf.AdpNeApiItf;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.wdm.api.impl.AdpSnmpNeApiImpl;
import com.nsb.enms.restful.model.adapter.AdpNe;

public class AdpSnmpNeBuilder extends AdpNeBuilder {
	private static final AdpSnmpNeBuilder INSTANCE = new AdpSnmpNeBuilder();

	private AdpSnmpNeBuilder() {
	}

	public static AdpSnmpNeBuilder getInstance() {
		return INSTANCE;
	}

	@Override
	public AdpNeApiItf getNeInstance(AdpNe ne) throws AdapterException {
		return new AdpSnmpNeApiImpl(ne);
	}

	public AdpNeApiItf getTpInstance(AdpNe ne) throws AdapterException {
		return new AdpSnmpNeApiImpl(ne);
	}
}
