package com.nsb.enms.adapter.server.sdh.api.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.api.impl.AdpDefaultTpApiImpl;
import com.nsb.enms.adapter.server.common.constants.Protocols;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.restful.model.adapter.AdpNe;
import com.nsb.enms.restful.model.adapter.AdpTp;

public class AdpQ3TpApiImpl extends AdpDefaultTpApiImpl {
	private Logger log = LogManager.getLogger(AdpQ3TpApiImpl.class);

	public AdpQ3TpApiImpl(AdpNe ne) {
		super(ne);
	}

	@Override
	public List<AdpTp> getNeTps() throws AdapterException {
		try {
			return tpsDbMgr.getTpsByNeId(getId(), Protocols.ALUQ3);
		} catch (Exception e) {
			log.error("getTpsByNeId", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
	}
}
