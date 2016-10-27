package com.nsb.enms.adapter.server.business.sync;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.action.entity.TpEntity;
import com.nsb.enms.adapter.server.action.method.tp.GetCtp;
import com.nsb.enms.adapter.server.action.method.tp.GetTtp;
import com.nsb.enms.adapter.server.action.method.tp.TerminateTug3ToTu12;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.utils.GenerateKeyOnNeUtil;
import com.nsb.enms.adapter.server.common.utils.GenerateUserLabelUtil;
import com.nsb.enms.adapter.server.db.mgr.AdpTpsDbMgr;
import com.nsb.enms.common.EntityType;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.common.LayerRate;
import com.nsb.enms.restful.model.adapter.AdpTp;

public class TerminateTpMgr {
	private final static Logger log = LogManager.getLogger(TerminateTpMgr.class);
	private AdpTpsDbMgr tpsDbMgr = new AdpTpsDbMgr();

	public TerminateTpMgr() {

	}

//	public List<AdpTp> run(String groupId, String neId, String au4CtpId, String neDbId) {
//		// TODO 创建交叉前，应先检查交叉是否已经存在
//		String vc4TTPId = "";// createXcVc4();
//		log.debug("vc4TTPId = {}", vc4TTPId);
//		if (StringUtils.isEmpty(vc4TTPId)) {
//			log.error("vc4TTPId is null or empty");
//			return new ArrayList<AdpTp>();
//		}
//
//		try {
//			terminateTp(groupId, neId, vc4TTPId);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		List<AdpTp> tpList = null;
//		try {
//			tpList = syncTu12Ctp(groupId, neId, vc4TTPId, au4CtpId, neDbId);
//		} catch (AdapterException e) {
//			e.printStackTrace();
//		}
//
//		log.debug("terminate tp end");
//
//		return tpList;
//	}

	public void terminateTp(String groupId, String neId, String vc4TTPId) throws AdapterException {
		TerminateTug3ToTu12.terminateTug3ToTu12(groupId, neId, vc4TTPId);
	}

	public void terminateTp(String groupId, String neId, String vc4TTPId, String tug3Id) throws AdapterException {
		TerminateTug3ToTu12.terminateTug3ToTu12(groupId, neId, vc4TTPId, tug3Id);
	}

	
}
