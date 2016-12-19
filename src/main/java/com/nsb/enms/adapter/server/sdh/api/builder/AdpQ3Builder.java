package com.nsb.enms.adapter.server.sdh.api.builder;

import com.nsb.enms.adapter.server.common.api.builder.AdpBuilder;

public class AdpQ3Builder extends AdpBuilder {

	private static final AdpQ3Builder INSTANCE = new AdpQ3Builder();

	private AdpQ3Builder() {

	}

	public static AdpQ3Builder getInstance() {
		return INSTANCE;
	}
}
