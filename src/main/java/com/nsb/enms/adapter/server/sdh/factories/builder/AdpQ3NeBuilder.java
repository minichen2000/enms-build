package com.nsb.enms.adapter.server.sdh.factories.builder;

import com.nsb.enms.adapter.server.common.api.builder.AdpNeBuilder;

public class AdpQ3NeBuilder extends AdpNeBuilder {

	private static final AdpQ3NeBuilder INSTANCE = new AdpQ3NeBuilder();

	private AdpQ3NeBuilder() {

	}

	public static AdpQ3NeBuilder getInstance() {
		return INSTANCE;
	}

}
