package com.nsb.enms.adapter.server.common.api.builder;

import java.util.HashMap;
import java.util.Map;

import com.nsb.enms.adapter.server.common.api.itf.AdpCommonApiItf;
import com.nsb.enms.adapter.server.common.constants.Protocols;
import com.nsb.enms.adapter.server.common.db.mgr.AdpNesDbMgr;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.sdh.api.builder.AdpQ3Builder;
import com.nsb.enms.adapter.server.wdm.api.builder.AdpSnmpNeBuilder;
import com.nsb.enms.common.EntityType;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.restful.model.adapter.AdpNe;

public class AdpBuilder {
	private static final AdpBuilder INSTANCE = new AdpBuilder();
	private Map<String, AdpBuilder> builderMap;
	private AdpNesDbMgr nesDbMgr = new AdpNesDbMgr();

	protected AdpBuilder() {
		builderMap = new HashMap<String, AdpBuilder>();
		builderMap.put(Protocols.ALUQ3_PROTOCOL, AdpQ3Builder.getInstance());
		builderMap.put(Protocols.SNMP_PROTOCOL, AdpSnmpNeBuilder.getInstance());
	}

	public AdpCommonApiItf getApiItf(AdpNe ne, EntityType type) throws AdapterException {
		return getBuilderInstance(ne).getApiItf(ne, type);
	}

	public AdpCommonApiItf getApiItf(Integer neId, EntityType type) throws AdapterException {
		AdpNe ne = new AdpNe();
		try {
			ne = nesDbMgr.getNeById(neId);
		} catch (Exception e) {
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
		return getApiItf(ne, type);
	}

	public static AdpBuilder getInstance() {
		return INSTANCE;
	}

	private AdpBuilder getBuilderInstance(AdpNe ne) throws AdapterException {
		return builderMap.get(Protocols.SNMP_PROTOCOL);
	}
}
