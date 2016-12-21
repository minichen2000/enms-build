package com.nsb.enms.adapter.server.wdm.api.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.api.impl.AdpDefaultTpApiImpl;
import com.nsb.enms.adapter.server.wdm.business.ne.AdpSnmpTpsMgr;
import com.nsb.enms.restful.model.adapter.AdpNe;

public class AdpSnmpTpApiImpl extends AdpDefaultTpApiImpl {
	private Logger log = LogManager.getLogger(AdpSnmpTpApiImpl.class);
	private AdpSnmpTpsMgr mgr = new AdpSnmpTpsMgr();

	public AdpSnmpTpApiImpl(AdpNe ne) {
		super(ne);
	}
}
