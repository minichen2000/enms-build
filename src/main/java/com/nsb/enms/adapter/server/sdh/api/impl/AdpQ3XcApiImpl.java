package com.nsb.enms.adapter.server.sdh.api.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.api.impl.AdpDefaultXcApiImpl;
import com.nsb.enms.adapter.server.common.db.mgr.AdpXcsDbMgr;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.sdh.business.xc.AdpQ3XcsMgr;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.restful.model.adapter.AdpNe;
import com.nsb.enms.restful.model.adapter.AdpXc;

public class AdpQ3XcApiImpl extends AdpDefaultXcApiImpl {
	private Logger log = LogManager.getLogger(AdpQ3XcApiImpl.class);
	private AdpQ3XcsMgr adpXcMgr = new AdpQ3XcsMgr();
	private AdpXcsDbMgr xcsDbMgr = new AdpXcsDbMgr();

	public AdpQ3XcApiImpl(AdpNe ne) {
		super(ne);
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
			log.error("getXcById", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
	}
}
