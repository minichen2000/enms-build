package com.nsb.enms.adapter.server.common.api.builder;

import java.util.HashMap;
import java.util.Map;

import com.nsb.enms.adapter.server.common.api.factories.Constants;
import com.nsb.enms.adapter.server.common.api.itf.AdpNeApiItf;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.sdh.factories.builder.AdpQ3NeBuilder;
import com.nsb.enms.adapter.server.wdm.api.builder.AdpSnmpNeBuilder;
import com.nsb.enms.restful.model.adapter.AdpNe;

public class AdpNeBuilder {
	private static final AdpNeBuilder INSTANCE = new AdpNeBuilder();
	private Map<String, AdpNeBuilder> builderMap;

	protected AdpNeBuilder() {
		builderMap = new HashMap<String, AdpNeBuilder>();
		builderMap.put(Constants.ALUQ3_PROTOCOL, AdpQ3NeBuilder.getInstance());
		// builderMap.put(Constants.ALUtl1_PROTOCOL,
		// Tl1NeBuilder.getInstance());
		builderMap.put(Constants.SNMP_PROTOCOL, AdpSnmpNeBuilder.getInstance());
	}

	public AdpNeApiItf getNeInstance(AdpNe ne) throws AdapterException {
		return getNeBuilderInstance(ne).getNeInstance(ne);
	}

	public static AdpNeBuilder getInstance() {
		return INSTANCE;
	}

	private AdpNeBuilder getNeBuilderInstance(AdpNe ne) throws AdapterException {
		// List<SupportedNe> list =
		// NeParameterInfo.instance().getSupportedNeTypes();
		// for (SupportedNe detail : list) {
		// if (ne.getNeType().equals(detail.getNeType())) {
		// for (String protocol : builderMap.keySet()) {
		// if (detail.getProtocols().contains(protocol)) {
		// return builderMap.get(protocol);
		// }
		// }
		// }
		// }
		return builderMap.get(Constants.SNMP_PROTOCOL);
	}
}
