package com.nsb.enms.adapter.server.wdm.api.builder;

import com.nsb.enms.adapter.server.common.api.builder.AdpNeBuilder;

public class AdpSnmpNeBuilder extends AdpNeBuilder {
	private static final AdpSnmpNeBuilder INSTANCE = new AdpSnmpNeBuilder();

	private AdpSnmpNeBuilder() {
	}

	public static AdpSnmpNeBuilder getInstance() {
		return INSTANCE;
	}
}
