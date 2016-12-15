package com.nsb.enms.adapter.server.common.api.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.MethodOperator;
import com.nsb.enms.adapter.server.common.api.itf.AdpNeApiItf;
import com.nsb.enms.adapter.server.common.db.mgr.AdpNesDbMgr;
import com.nsb.enms.adapter.server.common.db.mgr.AdpTpsDbMgr;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.common.utils.ValidationUtil;
import com.nsb.enms.restful.model.adapter.AdpNe;
import com.nsb.enms.restful.model.adapter.AdpTp;

public abstract class AdpDefaultNeApiImpl implements AdpNeApiItf {
	private Logger log = LogManager.getLogger(AdpDefaultNeApiImpl.class);
	protected AdpNe data;
	private AdpNesDbMgr nesDbMgr = new AdpNesDbMgr();
	private AdpTpsDbMgr tpsDbMgr = new AdpTpsDbMgr();
	private Integer id;

	public AdpDefaultNeApiImpl(AdpNe ne) {
		data = ne;
		id = data.getId();
	}

	public AdpNe getData() {
		return data;
	}

	protected Integer getId() {
		return id;
	}

	protected void commonValidate(AdpNe body, MethodOperator operate) throws AdapterException {
		Integer id = body.getId();
		if (null == id || id < 0) {
			throw new AdapterException(ErrorCode.FAIL_INVALID_ID);
		}

		String userLabel = body.getUserLabel();
		if (!ValidationUtil.isValidUserLabel(userLabel)) {
			throw new AdapterException(ErrorCode.FAIL_INVALID_USER_LABEL);
		}

		if (MethodOperator.ADD == operate) {
			boolean isExisted = false;
			try {
				isExisted = nesDbMgr.isUserLabelExisted(id, userLabel, operate);
			} catch (Exception e) {
				log.error("isUserLabelExisted", e);
				throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
			}
			if (isExisted) {
				throw new AdapterException(ErrorCode.FAIL_USER_LABEL_EXISTED);
			}
		}
	}

	@Override
	public AdpNe getNeById() throws AdapterException {
		AdpNe ne = new AdpNe();
		try {
			ne = nesDbMgr.getNeById(data.getId());
			if (ne.getId() < 0) {
				throw new AdapterException(ErrorCode.FAIL_OBJ_NOT_EXIST);
			}
		} catch (Exception e) {
			log.error("getNeById", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
		return ne;
	}

	@Override
	public List<AdpTp> getChildrenTps(Integer tpId) throws AdapterException {
		List<AdpTp> tpList = new ArrayList<AdpTp>();
		try {
			tpList = tpsDbMgr.getChildrenTps(getId(), tpId);
		} catch (Exception e) {
			log.error("getChildrenTps", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
		return tpList;
	}

	@Override
	public List<AdpTp> getNeTps() throws AdapterException {
		List<AdpTp> tpList = new ArrayList<AdpTp>();
		try {
			tpList = tpsDbMgr.getTpsByNeId(getId());
		} catch (Exception e) {
			log.error("getTpsByNeId", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
		return tpList;
	}
}
