package com.nsb.enms.adapter.server.sdh.api.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.MethodOperator;
import com.nsb.enms.adapter.server.common.api.impl.AdpDefaultNeApiImpl;
import com.nsb.enms.adapter.server.common.db.mgr.AdpXcsDbMgr;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.sdh.business.ne.AdpQ3NesMgr;
import com.nsb.enms.adapter.server.sdh.business.xc.AdpQ3XcsMgr;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.restful.model.adapter.AdpNe;
import com.nsb.enms.restful.model.adapter.AdpXc;

public class AdpQ3NeApiImpl extends AdpDefaultNeApiImpl {
	private Logger log = LogManager.getLogger(AdpQ3NeApiImpl.class);
	private AdpQ3NesMgr nesMgr = new AdpQ3NesMgr();
	private AdpQ3XcsMgr adpXcMgr = new AdpQ3XcsMgr();
	private AdpXcsDbMgr xcsDbMgr = new AdpXcsDbMgr();

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

	@Override
	public AdpXc createXc(AdpXc xc) throws AdapterException {
		return adpXcMgr.createXc(getId(), xc);
	}

	@Override
	public List<AdpXc> getXcsByNeId() throws AdapterException {
		try {
			return xcsDbMgr.getXcsByNeId(getId());
		} catch (Exception e) {
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
	}

	@Override
	public void deleteXcsByNeId() throws AdapterException {
		adpXcMgr.deleteXcsByNeId(getId());
	}

	@Override
	public void deleteXcById(Integer xcid) throws AdapterException {
		adpXcMgr.deleteXcById(getId(), xcid);
	}

	@Override
	public AdpXc getXcById(Integer xcId) throws AdapterException {
		try {
			return xcsDbMgr.getXcById(getId(), xcId);
		} catch (Exception e) {
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
	}
}
