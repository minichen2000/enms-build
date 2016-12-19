package com.nsb.enms.adapter.server.sdh.api.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.api.impl.AdpDefaultNeApiImpl;
import com.nsb.enms.adapter.server.common.constants.MethodOperator;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.sdh.business.ne.AdpQ3NesMgr;
import com.nsb.enms.restful.model.adapter.AdpNe;

public class AdpQ3NeApiImpl extends AdpDefaultNeApiImpl {
	private Logger log = LogManager.getLogger(AdpQ3NeApiImpl.class);
	private AdpQ3NesMgr nesMgr = new AdpQ3NesMgr();

	public AdpQ3NeApiImpl(AdpNe ne) {
		super(ne);
	}

	@Override
	public AdpNe addNe() throws AdapterException {
		commonValidate(data, MethodOperator.ADD);
		return nesMgr.addNe(data);
	}

	@Override
	public void deleteNe() throws AdapterException {
		nesMgr.deleteNe(getId());
	}

	@Override
	public void update() throws AdapterException {

	}

	@Override
	public void supervise() throws AdapterException {
		nesMgr.startSupervision(getId());
	}

	@Override
	public void synchronize() throws AdapterException {
		nesMgr.startAlignment(getId());
	}
}
