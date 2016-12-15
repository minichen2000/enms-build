package com.nsb.enms.adapter.server.sdh.api.builder;

import com.nsb.enms.adapter.server.common.api.builder.AdpNeBuilder;
import com.nsb.enms.adapter.server.common.api.itf.AdpNeApiItf;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.sdh.api.impl.AdpQ3NeApiImpl;
import com.nsb.enms.restful.model.adapter.AdpNe;

public class AdpQ3NeBuilder extends AdpNeBuilder {

	private static final AdpQ3NeBuilder INSTANCE = new AdpQ3NeBuilder();

	private AdpQ3NeBuilder() {

	}

	public static AdpQ3NeBuilder getInstance() {
		return INSTANCE;
	}

	@Override
	public AdpNeApiItf getNeInstance(AdpNe ne) throws AdapterException {
		return new AdpQ3NeApiImpl(ne);
	}
}
