package com.nsb.enms.adapter.server.wdm.business.xc;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.db.mgr.AdpSeqDbMgr;
import com.nsb.enms.adapter.server.common.db.mgr.AdpXcsDbMgr;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.common.Direction;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.common.XcRole;
import com.nsb.enms.common.XcType;
import com.nsb.enms.restful.model.adapter.AdpXc;

public class AdpSnmpXcsMgr {
	private final static Logger log = LogManager.getLogger(AdpSnmpXcsMgr.class);

	private AdpXcsDbMgr xcsMgr = new AdpXcsDbMgr();

	public AdpSnmpXcsMgr() {
	}

	public void createXc(Integer neId, List<Integer> atps, List<Integer> ztps) throws AdapterException {
		AdpXc xc = new AdpXc();
		Integer maxXcId;
		try {
			maxXcId = AdpSeqDbMgr.getMaxTpId(neId);
		} catch (Exception e) {
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
		xc.setId(maxXcId);
		xc.setAEndPoints(atps);
		xc.setZEndPoints(ztps);
		xc.setNeId(neId);
		// xc.setKeyOnNe(keyOnNe);
		xc.setType(XcType.SIMPLE.name());
		// xc.setLayerrate(layerrate);
		xc.setXcRole(XcRole.FIXED.name());
		xc.setDirection(Direction.BI.name());

		addXc2Db(xc);
	}

	private void addXc2Db(AdpXc xc) throws AdapterException {
		try {
			xcsMgr.createXc(xc);
		} catch (Exception e) {
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
	}

	public static void main(String args[]) {
	}
}
