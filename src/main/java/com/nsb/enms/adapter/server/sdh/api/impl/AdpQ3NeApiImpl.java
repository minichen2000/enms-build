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
		// TODO Auto-generated method stub

	}

	@Override
	public void assign() throws AdapterException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete() throws AdapterException {
		// TODO Auto-generated method stub

	}

	@Override
	public void update() throws AdapterException {
		// TODO Auto-generated method stub

	}

	@Override
	public void supervise() throws AdapterException {
		// TODO Auto-generated method stub

	}

	@Override
	public void synchronize() throws AdapterException {
		// TODO Auto-generated method stub

	}

	@Override
	public AdpNe getData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AdpTp> getTps() throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AdpTp> getTpsByLayerrate(Integer layerrate) throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AdpTp getTp(Integer tpid) throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AdpTp> getTpsByType(Integer tptype) throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}
}
