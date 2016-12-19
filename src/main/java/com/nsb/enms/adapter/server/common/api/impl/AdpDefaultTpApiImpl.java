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
	private Logger log = LogManager.getLogger(AdpDefaultNeApiImpl.class);
	protected AdpNe data;
	private AdpTpsDbMgr tpsDbMgr = new AdpTpsDbMgr();
	private Integer id;

	public AdpDefaultTpApiImpl(AdpNe ne) {
		data = ne;
		id = data.getId();
	}

	public AdpNe getData() {
		return data;
	}

	protected Integer getId() {
		return id;
	}

	@Override
	public List<AdpTp> getChildrenTps(Integer tpId) throws AdapterException {
		try {
			List<AdpTp> tpList = tpsDbMgr.getChildrenTps(getId(), tpId);
			return tpList;
		} catch (Exception e) {
			log.error("getChildrenTps", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
	}

	@Override
	public List<AdpTp> getNeTps() throws AdapterException {
		try {
			List<AdpTp> tpList = tpsDbMgr.getTpsByNeId(getId());
			return tpList;
		} catch (Exception e) {
			log.error("getTpsByNeId", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
	}

	@Override
	public AdpTp getTpById(Integer tpid) throws AdapterException {
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
