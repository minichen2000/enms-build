package com.nsb.enms.adapter.server.wdm.api.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.api.impl.AdpDefaultXcApiImpl;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.restful.model.adapter.AdpNe;
import com.nsb.enms.restful.model.adapter.AdpXc;

public class AdpSnmpXcApiImpl extends AdpDefaultXcApiImpl {
	private Logger log = LogManager.getLogger(AdpSnmpXcApiImpl.class);

	public AdpSnmpXcApiImpl(AdpNe ne) {
		super(ne);
	}

	@Override
	public AdpXc createXc(AdpXc xc) throws AdapterException {
		return null;
	}

	@Override
	public List<AdpXc> getXcsByNeId() throws AdapterException {
		return null;
	}

	@Override
	public void deleteXcsByNeId() throws AdapterException {

	}

	@Override
	public void deleteXcById(Integer xcid) throws AdapterException {

	}

	@Override
	public AdpXc getXcById(Integer xcId) throws AdapterException {
		return null;
	}
}
