package com.nsb.enms.adapter.server.wdm.api.builder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.api.builder.AdpBuilder;
import com.nsb.enms.adapter.server.common.api.itf.AdpCommonApiItf;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.wdm.api.impl.AdpSnmpEquApiImpl;
import com.nsb.enms.adapter.server.wdm.api.impl.AdpSnmpNeApiImpl;
import com.nsb.enms.adapter.server.wdm.api.impl.AdpSnmpTpApiImpl;
import com.nsb.enms.adapter.server.wdm.api.impl.AdpSnmpXcApiImpl;
import com.nsb.enms.common.EntityType;
import com.nsb.enms.restful.model.adapter.AdpNe;

public class AdpSnmpNeBuilder extends AdpBuilder {
	private Logger log = LogManager.getLogger(AdpSnmpNeBuilder.class);
	private static final AdpSnmpNeBuilder INSTANCE = new AdpSnmpNeBuilder();

	private AdpSnmpNeBuilder() {
	}

	public static AdpSnmpNeBuilder getInstance() {
		return INSTANCE;
	}

	@Override
	public AdpCommonApiItf getApiItf(AdpNe ne, EntityType type) throws AdapterException {
		switch (type) {
		case NE:
			return new AdpSnmpNeApiImpl(ne);
		case TP:
			return new AdpSnmpTpApiImpl(ne);
		case EQUIPMENT:
			return new AdpSnmpEquApiImpl(ne);
		case XC:
			return new AdpSnmpXcApiImpl(ne);
		default:
			log.error("undefind type:" + type);
			return null;
		}
	}
}
