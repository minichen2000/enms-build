package com.nsb.enms.adapter.server.common.api.builder;

import java.util.HashMap;
import java.util.Map;

import com.nsb.enms.adapter.server.common.constants.Protocols;
import com.nsb.enms.adapter.server.common.api.itf.AdpNeApiItf;
import com.nsb.enms.adapter.server.common.db.mgr.AdpNesDbMgr;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.sdh.api.builder.AdpQ3NeBuilder;
import com.nsb.enms.adapter.server.wdm.api.builder.AdpSnmpNeBuilder;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.restful.model.adapter.AdpNe;

public class AdpNeBuilder {
	private static final AdpNeBuilder INSTANCE = new AdpNeBuilder();
	private Map<String, AdpNeBuilder> builderMap;
	private AdpNesDbMgr nesDbMgr = new AdpNesDbMgr();

	protected AdpNeBuilder() {
		builderMap = new HashMap<String, AdpNeBuilder>();
		builderMap.put(Protocols.ALUQ3_PROTOCOL, AdpQ3NeBuilder.getInstance());
		builderMap.put(Protocols.SNMP_PROTOCOL, AdpSnmpNeBuilder.getInstance());
	}

	public AdpNeApiItf getNeInstance(AdpNe ne) throws AdapterException {
		return getNeBuilderInstance(ne).getNeInstance(ne);
	}

	public AdpNeApiItf getNeInstance(Integer neId) throws AdapterException {
		AdpNe ne = new AdpNe();
		try {
			ne = nesDbMgr.getNeById(neId);
		} catch (Exception e) {
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
		return getNeInstance(ne);
	}

	public static AdpNeBuilder getInstance() {
		return INSTANCE;
	}

	private AdpNeBuilder getNeBuilderInstance(AdpNe ne) throws AdapterException {
		return builderMap.get(Protocols.SNMP_PROTOCOL);
	}
}
