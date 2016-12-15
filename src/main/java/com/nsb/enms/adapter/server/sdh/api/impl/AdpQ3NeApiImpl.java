package com.nsb.enms.adapter.server.sdh.api.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.MethodOperator;
import com.nsb.enms.adapter.server.common.api.impl.AdpDefaultNeApiImpl;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.sdh.business.ne.AdpQ3NesMgr;
import com.nsb.enms.restful.model.adapter.AdpNe;
import com.nsb.enms.restful.model.adapter.AdpTp;

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
	public void create() throws AdapterException {
	}

	@Override
	public void assign() throws AdapterException {
	}

	@Override
	public void delete() throws AdapterException {
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

	@Override
	public AdpNe getData() {
		return null;
	}

	@Override
	public List<AdpTp> getTpsByLayerrate(Integer layerrate) throws AdapterException {
		return null;
	}

	@Override
	public AdpTp getTp(Integer tpid) throws AdapterException {
		return null;
	}

	@Override
	public List<AdpTp> getTpsByType(Integer tptype) throws AdapterException {
		return null;
	}
}
