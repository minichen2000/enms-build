package com.nsb.enms.adapter.server.wdm.api.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.api.impl.AdpDefaultNeApiImpl;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.restful.model.adapter.AdpNe;
import com.nsb.enms.restful.model.adapter.AdpTp;

public class AdpSnmpNeApiImpl extends AdpDefaultNeApiImpl {
	private Logger log = LogManager.getLogger(AdpSnmpNeApiImpl.class);

	public AdpSnmpNeApiImpl(AdpNe ne) {
		super(ne);
	}

	@Override
	public AdpNe addNe() throws AdapterException {
		// TODO Auto-generated method stub
		return null;
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
