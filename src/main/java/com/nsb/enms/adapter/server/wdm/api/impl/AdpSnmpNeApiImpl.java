package com.nsb.enms.adapter.server.wdm.api.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.api.impl.AdpDefaultNeApiImpl;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.wdm.business.ne.AdpSnmpNesMgr;
import com.nsb.enms.restful.model.adapter.AdpNe;

public class AdpSnmpNeApiImpl extends AdpDefaultNeApiImpl {
	private Logger log = LogManager.getLogger(AdpSnmpNeApiImpl.class);

	private AdpSnmpNesMgr mgr = new AdpSnmpNesMgr();

	public AdpSnmpNeApiImpl(AdpNe ne) {
		super(ne);
	}

	@Override
	public AdpNe addNe() throws AdapterException {
		return mgr.addNe(data);
	}

	@Override
	public void update() throws AdapterException {
	}

	@Override
	public void supervise() throws AdapterException {
		mgr.startSupervision(getId());
	}

	@Override
	public void synchronize() throws AdapterException {
		mgr.startAlignment(getId());
	}

	@Override
	public void deleteNe() throws AdapterException {

	}
}
