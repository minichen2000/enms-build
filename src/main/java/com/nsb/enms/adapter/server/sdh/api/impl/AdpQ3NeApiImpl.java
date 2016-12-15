package com.nsb.enms.adapter.server.sdh.api.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.api.itf.AdpNeApiItf;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.restful.model.adapter.AdpNe;
import com.nsb.enms.restful.model.adapter.AdpTp;

public class AdpQ3NeApiImpl implements AdpNeApiItf {
	private Logger log = LogManager.getLogger(AdpQ3NeApiImpl.class);

	@Override
	public AdpNe addNe(AdpNe ne) throws AdapterException {
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
