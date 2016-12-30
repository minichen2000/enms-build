package com.nsb.enms.adapter.server.common.api.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.api.itf.AdpNeApiItf;
import com.nsb.enms.adapter.server.common.constants.MethodOperator;
import com.nsb.enms.adapter.server.common.db.mgr.AdpNesDbMgr;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.common.utils.ValidationUtil;
import com.nsb.enms.restful.model.adapter.AdpNe;

public abstract class AdpDefaultNeApiImpl implements AdpNeApiItf {
	private Logger log = LogManager.getLogger(AdpDefaultTpApiImpl.class);
	protected AdpNe data;
	private AdpNesDbMgr nesDbMgr = new AdpNesDbMgr();
	private String id;

	public AdpDefaultNeApiImpl(AdpNe ne) {
		data = ne;
		id = data.getId();
	}

	public AdpNe getData() {
		return data;
	}

	protected String getId() {
		return id;
	}

	protected void commonValidate(AdpNe body, MethodOperator operate) throws AdapterException {
		String id = body.getId();
		if (StringUtils.isEmpty(id)) {
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
		try {
			AdpNe ne = nesDbMgr.getNeById(data.getId());
			if (StringUtils.isEmpty(ne.getId())) {
				throw new AdapterException(ErrorCode.FAIL_OBJ_NOT_EXIST);
			}
			return ne;
		} catch (Exception e) {
			log.error("getNeById", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
	}
}
