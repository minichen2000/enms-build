package com.nsb.enms.adapter.server.sdh.api.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.api.impl.AdpDefaultEquApiImpl;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.restful.model.adapter.AdpEquipment;
import com.nsb.enms.restful.model.adapter.AdpNe;

public class AdpQ3EquApiImpl extends AdpDefaultEquApiImpl {
	private Logger log = LogManager.getLogger(AdpQ3EquApiImpl.class);

	public AdpQ3EquApiImpl(AdpNe ne) {
		super(ne);
	}

	@Override
	public AdpEquipment addEquipment(AdpEquipment equipment) throws AdapterException {
		return null;
	}

}
