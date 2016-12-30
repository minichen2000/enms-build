package com.nsb.enms.adapter.server.common.api.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.api.itf.AdpTpApiItf;
import com.nsb.enms.adapter.server.common.db.mgr.AdpTpsDbMgr;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.restful.model.adapter.AdpNe;
import com.nsb.enms.restful.model.adapter.AdpTp;

public abstract class AdpDefaultTpApiImpl implements AdpTpApiItf {
	private Logger log = LogManager.getLogger(AdpDefaultTpApiImpl.class);
	protected AdpNe data;
	protected AdpTpsDbMgr tpsDbMgr = new AdpTpsDbMgr();
	private String id;

	public AdpDefaultTpApiImpl(AdpNe ne) {
		data = ne;
		id = data.getId();
	}

	public AdpNe getData() {
		return data;
	}

	protected String getId() {
		return id;
	}

	@Override
	public List<AdpTp> getChildrenTps(String tpId) throws AdapterException {
		try {
			List<AdpTp> tpList = tpsDbMgr.getChildrenTps(getId(), tpId);
			return tpList;
		} catch (Exception e) {
			log.error("getChildrenTps", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
	}

	@Override
	public AdpTp getTpById(String tpid) throws AdapterException {
		try {
			AdpTp tp = tpsDbMgr.getTpById(getId(), tpid);
			return tp;
		} catch (Exception e) {
			log.error("getTpById", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
	}

	@Override
	public List<AdpTp> getTpsByLayerrate(String layerrate) throws AdapterException {
		try {
			List<AdpTp> tpList = tpsDbMgr.getTpsByLayerRate(getId(), layerrate);
			return tpList;
		} catch (Exception e) {
			log.error("getTpsByLayerrate", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
	}

	@Override
	public List<AdpTp> getTpsByType(String tpType) throws AdapterException {
		try {
			List<AdpTp> tpList = tpsDbMgr.getTpsByType(getId(), tpType);
			return tpList;
		} catch (Exception e) {
			log.error("getTpsByType", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
	}
}
