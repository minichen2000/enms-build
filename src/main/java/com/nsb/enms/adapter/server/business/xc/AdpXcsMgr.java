package com.nsb.enms.adapter.server.business.xc;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.action.entity.XcEntity;
import com.nsb.enms.adapter.server.action.entity.param.XcParamBean;
import com.nsb.enms.adapter.server.action.method.xc.CreateXc;
import com.nsb.enms.adapter.server.business.tp.AdpTpsMgr;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.utils.GenerateKeyOnNeUtil;
import com.nsb.enms.adapter.server.db.mgr.AdpNesDbMgr;
import com.nsb.enms.adapter.server.db.mgr.AdpTpsDbMgr;
import com.nsb.enms.adapter.server.db.mgr.AdpXcsDbMgr;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.common.LayerRate;
import com.nsb.enms.restful.model.adapter.AdpNe;
import com.nsb.enms.restful.model.adapter.AdpTp;
import com.nsb.enms.restful.model.adapter.AdpXc;

public class AdpXcsMgr {
	private final static Logger log = LogManager.getLogger(AdpXcsMgr.class);
	private AdpTpsDbMgr tpsDbMgr = new AdpTpsDbMgr();
	private AdpXcsDbMgr xcsDbMgr = new AdpXcsDbMgr();
	private AdpTpsMgr tpsMgr = new AdpTpsMgr();
	private AdpNesDbMgr nesDbMgr = new AdpNesDbMgr();

	public AdpXcsMgr() {
	}

	public AdpXc createXcByTu12AndVc12(String neDbId, String atpId, String ztpId) throws AdapterException {
		log.debug("atpId = {}", atpId);
		XcParamBean atpBean = getParam(atpId);

		log.debug("ztpId = {}", ztpId);
		XcParamBean ztpBean = getParam(ztpId);

		XcParamBean tpBean = new XcParamBean();
		if (atpBean.isSdhTp()) {
			tpBean = atpBean;
			tpBean.setVc12TtpId(ztpBean.getVc12TtpId());
		} else {
			tpBean = ztpBean;
			tpBean.setVc12TtpId(atpBean.getVc12TtpId());
		}

		XcEntity xcEntity = CreateXc.createXcVc12(tpBean.getGroupId(), tpBean.getNeId(), tpBean.getVc4TtpId(),
				tpBean.getTug3Id(), tpBean.getTug2Id(), tpBean.getTu12CtpId(), tpBean.getVc12TtpId());
		AdpXc xc = insertXc2DbByTu12AndVc12(xcEntity.getMoi(), neDbId, atpId, ztpId);
		return xc;
	}

	private AdpXc insertXc2DbByTu12AndVc12(String moi, String neDbId, String atpDbId, String ztpDbId)
			throws AdapterException {
		AdpXc xc = new AdpXc();
		xc.setAid(moi);
		xc.setImplStatus("");
		xc.setNeId(neDbId);
		// xc.setUsedByConnection("0");
		// TODO 修改该值
		xc.setLayerrate(String.valueOf(LayerRate.LR_TUVC12.toInt()));

		List<String> atps = new ArrayList<String>();
		atps.add(atpDbId);
		xc.setAtps(atps);

		List<String> ztps = new ArrayList<String>();
		ztps.add(ztpDbId);
		xc.setZtps(ztps);

		try {
			xc = xcsDbMgr.createXc(xc);
		} catch (Exception e) {
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
		return xc;
	}

	public AdpXc createXcByTu3AndVc3(String neDbId, String atpId, String ztpId) throws AdapterException {
		log.debug("atpId = {}", atpId);
		XcParamBean atpBean = getParam(atpId);

		log.debug("ztpId = {}", ztpId);
		XcParamBean ztpBean = getParam(ztpId);

		XcParamBean tpBean = new XcParamBean();
		if (atpBean.isSdhTp()) {
			tpBean = atpBean;
			tpBean.setVc3TtpId(ztpBean.getVc3TtpId());
		} else {
			tpBean = ztpBean;
			tpBean.setVc3TtpId(atpBean.getVc3TtpId());
		}

		XcEntity xcEntity = CreateXc.createXcVc3(tpBean.getGroupId(), tpBean.getNeId(), tpBean.getVc4TtpId(),
				tpBean.getTug3Id(), tpBean.getTu3CtpId(), tpBean.getVc3TtpId());
		AdpXc xc = insertXc2DbByTu3AndVc3(xcEntity.getMoi(), neDbId, atpId, ztpId);
		return xc;
	}

	private AdpXc insertXc2DbByTu3AndVc3(String moi, String neDbId, String atpDbId, String ztpDbId)
			throws AdapterException {
		AdpXc xc = new AdpXc();
		xc.setAid(moi);
		xc.setImplStatus("");
		xc.setNeId(neDbId);
		// xc.setUsedByConnection("0");
		// TODO 修改该值
		xc.setLayerrate(String.valueOf(LayerRate.LR_TUVC12.toInt()));

		List<String> atps = new ArrayList<String>();
		atps.add(atpDbId);
		xc.setAtps(atps);

		List<String> ztps = new ArrayList<String>();
		ztps.add(ztpDbId);
		xc.setZtps(ztps);

		try {
			xc = xcsDbMgr.createXc(xc);
		} catch (Exception e) {
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
		return xc;
	}

	public String createXcByAu4AndVc4(String au4CtpId) throws AdapterException {
		AdpTp tp = getTpById(au4CtpId);

		String neDbId = tp.getNeId();
		AdpNe ne = getNeById(neDbId);

		String neMoi = GenerateKeyOnNeUtil.getMoi(ne.getKeyOnNe());
		String moi = GenerateKeyOnNeUtil.getMoi(tp.getKeyOnNe());
		String groupId = neMoi.split("/")[0].replaceAll("neGroupId=", StringUtils.EMPTY);
		String neId = neMoi.split("/")[1].replaceAll("networkElementId=", StringUtils.EMPTY);
		String pTTPId = moi.split("/")[0].replaceAll("protectedTTPId=", StringUtils.EMPTY);
		String augId = moi.split("/")[1].replaceAll("augId=", StringUtils.EMPTY);
		String au4CTPId = moi.split("/")[2].replaceAll("au4CTPId=", StringUtils.EMPTY);

		log.debug("before createXcVc4");
		XcEntity xcEntity = CreateXc.createXcVc4(groupId, neId, pTTPId, augId, au4CTPId);
		String toTp = xcEntity.getToTermination();
		if (StringUtils.isEmpty(toTp)) {
			log.error("createXcVc4 ok");
			throw new AdapterException(ErrorCode.FAIL_CREATE_XC_BY_EMLIM);
		}
		String vc4TTPId = toTp.replace("vc4TTPId=", StringUtils.EMPTY);
		log.debug("createXcVc4 ok");

		String vc4TtpDbId = StringUtils.EMPTY;
		List<AdpTp> ttps = tpsMgr.syncTtp(groupId, neId, vc4TTPId, neDbId);
		if (null == ttps || ttps.isEmpty()) {
			log.error("ttps is null or empty");
		} else {
			AdpTp ttp = ttps.get(0);
			vc4TtpDbId = ttp.getId();
		}

		insertXc2DbByAu4AndVc4(xcEntity.getMoi(), neDbId, vc4TtpDbId, au4CtpId);

		return vc4TTPId;
	}

	private AdpNe getNeById(String neDbId) throws AdapterException {
		AdpNe ne = null;
		try {
			ne = nesDbMgr.getNeById(neDbId);
		} catch (Exception e) {
			log.error("", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
		return ne;
	}

	private void insertXc2DbByAu4AndVc4(String moi, String neDbId, String vc4TtpDbId, String au4CtpId)
			throws AdapterException {
		AdpXc xc = new AdpXc();
		xc.setAid(moi);
		xc.setImplStatus("");
		xc.setNeId(neDbId);
		// TODO 修改该值
		xc.setLayerrate(String.valueOf(LayerRate.LR_VC4.toInt()));

		List<String> atps = new ArrayList<String>();
		atps.add(au4CtpId);
		xc.setAtps(atps);

		List<String> ztps = new ArrayList<String>();
		ztps.add(vc4TtpDbId);
		xc.setZtps(ztps);

		try {
			xcsDbMgr.createXc(xc);
		} catch (Exception e) {
			log.error("createXc", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
	}

	private XcParamBean getParam(String tpId) throws AdapterException {
		AdpTp tp = getTpById(tpId);

		String neid = tp.getNeId();
		AdpNe ne = null;
		try {
			ne = nesDbMgr.getNeById(neid);
		} catch (Exception e) {
			log.error("getNeById", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}

		String tpType = tp.getTpType();
		String neMoi = GenerateKeyOnNeUtil.getMoi(ne.getKeyOnNe());
		String moi = GenerateKeyOnNeUtil.getMoi(tp.getKeyOnNe());
		XcParamBean bean = new XcParamBean();

		if ("tu12CTPBidirectionalR1".equalsIgnoreCase(tpType)) {
			constructTu12CtpBean(moi, bean);
		} else if ("vc12PathTraceTTPBidirectional".equalsIgnoreCase(tpType)) {
			constructVc12TtpBean(moi, bean);
		} else if ("tu3CTPBidirectionalR1".equalsIgnoreCase(tpType)) {
			constructTu3CtpBean(moi, bean);
		} else if ("vc3TTPBidirectionalR1".equalsIgnoreCase(tpType)) {
			constructVc3TtpBean(moi, bean);
		} else {
			log.error("there is not a valid tp type:" + tpType);
		}

		String groupId = neMoi.split("/")[0].replaceAll("neGroupId=", StringUtils.EMPTY);
		String neId = neMoi.split("/")[1].replaceAll("networkElementId=", StringUtils.EMPTY);
		bean.setGroupId(groupId);
		bean.setNeId(neId);
		return bean;
	}

	private void constructVc12TtpBean(String moi, XcParamBean bean) {
		bean.setSdhTp(false);
		String vc12TtpId = moi.split("/")[2].replaceAll("vc12TTPId=", StringUtils.EMPTY);
		bean.setVc12TtpId(vc12TtpId);
	}

	private void constructTu12CtpBean(String moi, XcParamBean bean) {
		String vc4TtpId = moi.split("/")[2].replaceAll("vc4TTPId=", StringUtils.EMPTY);
		bean.setVc4TtpId(vc4TtpId);
		String tug3Id = moi.split("/")[3].replaceAll("tug3Id=", StringUtils.EMPTY);
		bean.setTug3Id(tug3Id);
		String tug2Id = moi.split("/")[4].replaceAll("tug2Id=", StringUtils.EMPTY);
		bean.setTug2Id(tug2Id);
		String tu12CtpId = moi.split("/")[5].replaceAll("tu12CTPId=", StringUtils.EMPTY);
		bean.setTu12CtpId(tu12CtpId);
		bean.setSdhTp(true);
	}

	private void constructTu3CtpBean(String moi, XcParamBean bean) {
		String vc4TtpId = moi.split("/")[2].replaceAll("vc4TTPId=", StringUtils.EMPTY);
		bean.setVc4TtpId(vc4TtpId);
		String tug3Id = moi.split("/")[3].replaceAll("tug3Id=", StringUtils.EMPTY);
		bean.setTug3Id(tug3Id);
		String tu3CtpId = moi.split("/")[4].replaceAll("tu3CTPId=", StringUtils.EMPTY);
		bean.setTu3CtpId(tu3CtpId);
		bean.setSdhTp(true);
	}

	private void constructVc3TtpBean(String moi, XcParamBean bean) {
		bean.setSdhTp(false);
		String vc3TtpId = moi.split("/")[2].replaceAll("vc3TTPId=", StringUtils.EMPTY);
		bean.setVc3TtpId(vc3TtpId);
	}

	public String[] getNeInfo(String tpId) throws AdapterException {
		String groupId = StringUtils.EMPTY;
		String neId = StringUtils.EMPTY;

		AdpTp tp = getTpById(tpId);

		String neDbId = tp.getNeId();
		AdpNe ne = getNeById(neDbId);

		String neMoi = GenerateKeyOnNeUtil.getMoi(ne.getKeyOnNe());
		groupId = neMoi.split("/")[0].replaceAll("neGroupId=", StringUtils.EMPTY);
		neId = neMoi.split("/")[1].replaceAll("networkElementId=", StringUtils.EMPTY);
		return new String[] { groupId, neId, neDbId };
	}

	private AdpTp getTpById(String tpId) throws AdapterException {
		AdpTp tp = null;
		try {
			tp = tpsDbMgr.getTpById(tpId);
		} catch (Exception e) {
			log.error("getTpById", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
		return tp;
	}
}
