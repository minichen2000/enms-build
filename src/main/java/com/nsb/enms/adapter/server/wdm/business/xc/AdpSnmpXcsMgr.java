package com.nsb.enms.adapter.server.wdm.business.xc;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.business.itf.ObjectIdGenerator;
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

	private ObjectIdGenerator objectIdGenerator;

	private AdpXcsDbMgr xcsMgr = new AdpXcsDbMgr();

	public AdpSnmpXcsMgr(ObjectIdGenerator objectIdGenerator) {
		this.objectIdGenerator = objectIdGenerator;
	}

	public void createXC(String neId, List<String> atps, List<String> ztps) throws AdapterException {
		AdpXc xc = new AdpXc();
		String maxXcId;
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

		addXC2DB(xc);
	}

	private void addXC2DB(AdpXc xc) throws AdapterException {
		try {
			xcsMgr.createXC(xc);
		} catch (Exception e) {
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
	}

	public boolean isXCExisted(String neID, String ctpID) {
		List<AdpXc> xcs = null;
		try {
			xcs = xcsMgr.findXCsByTPID(neID, ctpID);
		} catch (Exception e) {
			log.error("findXCsByTPID", e);
		}
		if (null == xcs || xcs.isEmpty()) {
			return false;
		}
		return true;
	}

	public static void main(String args[]) {
	}
}
