package com.nsb.enms.adapter.server.common.api.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.MethodOperator;
import com.nsb.enms.adapter.server.common.api.itf.AdpNeApiItf;
import com.nsb.enms.adapter.server.common.db.mgr.AdpNesDbMgr;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.common.utils.ValidationUtil;
import com.nsb.enms.restful.model.adapter.AdpNe;

public abstract class AdpDefaultNeApiImpl implements AdpNeApiItf {
	private Logger log = LogManager.getLogger(AdpDefaultNeApiImpl.class);
	protected AdpNe data;
	private AdpNesDbMgr nesDbMgr = new AdpNesDbMgr();

	public AdpDefaultNeApiImpl(AdpNe ne) {
		data = ne;
	}

	public AdpNe getData() {
		return data;
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
}
