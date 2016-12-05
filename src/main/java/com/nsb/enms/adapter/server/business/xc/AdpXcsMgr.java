package com.nsb.enms.adapter.server.business.xc;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.action.entity.XcEntity;
import com.nsb.enms.adapter.server.action.entity.param.XcParamBean;
import com.nsb.enms.adapter.server.action.method.xc.CreateXc;
import com.nsb.enms.adapter.server.action.method.xc.DeleteXc;
import com.nsb.enms.adapter.server.business.tp.AdpTpsMgr;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.utils.ErrorWrapperUtils;
import com.nsb.enms.adapter.server.common.utils.GenerateKeyOnNeUtil;
import com.nsb.enms.adapter.server.common.utils.LayerRatesUtil;
import com.nsb.enms.adapter.server.db.mgr.AdpNesDbMgr;
import com.nsb.enms.adapter.server.db.mgr.AdpTpsDbMgr;
import com.nsb.enms.adapter.server.db.mgr.AdpXcsDbMgr;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.common.LayerRate;
import com.nsb.enms.common.ManagedObjectType;
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

	public AdpXc createXcByTu12AndVc12(Integer neDbId, AdpTp atp, AdpTp ztp) throws AdapterException {
		log.debug("atpId = {}", atp);
		XcParamBean atpBean = getParam(neDbId, atp);

		log.debug("ztpId = {}", ztp);
		XcParamBean ztpBean = getParam(neDbId, ztp);

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
		log.debug("createXcByTu12AndVc12 ok");
		return insertXc2Db(neDbId, atp.getId(), ztp.getId(), ManagedObjectType.VC12.getLayerRates());
	}

	public AdpXc createXcByTu3AndVc3(Integer neDbId, AdpTp atp, AdpTp ztp) throws AdapterException {
		log.debug("atpId = {}", atp);
		XcParamBean atpBean = getParam(neDbId, atp);

		log.debug("ztpId = {}", ztp);
		XcParamBean ztpBean = getParam(neDbId, ztp);

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
		log.debug("createXcByTu3AndVc3 ok");
		return insertXc2Db(neDbId, atp.getId(), ztp.getId(), ManagedObjectType.VC3.getLayerRates());
	}

	public String createXcByAu4AndVc4(Integer neId, AdpTp au4Ctp, Integer ptpId) throws AdapterException {
		String moi = GenerateKeyOnNeUtil.getMoi(au4Ctp.getKeyOnNe());
		String groupId = "100";
		String pTTPId = moi.split("/")[0].replaceAll("protectedTTPId=", StringUtils.EMPTY);
		String augId = moi.split("/")[1].replaceAll("augId=", StringUtils.EMPTY);
		String au4CTPId = moi.split("/")[2].replaceAll("au4CTPId=", StringUtils.EMPTY);

		log.debug("before createXcVc4");
		XcEntity xcEntity = CreateXc.createXcVc4(groupId, String.valueOf(neId), pTTPId, augId, au4CTPId);
		String toTp = xcEntity.getToTermination();
		if (StringUtils.isEmpty(toTp)) {
			log.error("failed to createXcVc4, toTp is null or empty");
			throw new AdapterException(ErrorCode.FAIL_CREATE_XC_BY_EMLIM);
		}
		String vc4TTPId = toTp.replace("vc4TTPId=", StringUtils.EMPTY);
		log.debug("createXcVc4 ok");

		Integer vc4TtpDbId = null;
		List<AdpTp> ttps = tpsMgr.syncTtp(groupId, neId, vc4TTPId, ptpId);
		if (null == ttps || ttps.isEmpty()) {
			log.error("ttps is null or empty");
		} else {
			AdpTp ttp = ttps.get(0);
			vc4TtpDbId = ttp.getId();
		}

		insertXc2Db(neId, au4Ctp.getId(), vc4TtpDbId, ManagedObjectType.VC4.getLayerRates());

		return vc4TTPId;
	}

	private AdpNe getNeById(Integer neDbId) throws AdapterException {
		AdpNe ne = null;
		try {
			ne = nesDbMgr.getNeById(neDbId);
		} catch (Exception e) {
			log.error("getNeById", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}

		if (null == ne || ne.getId() < 0) {
			log.error("can not find ne by id:" + neDbId);
			throw new AdapterException(ErrorCode.FAIL_OBJ_NOT_EXIST);
		}

		return ne;
	}

	private XcParamBean getParam(Integer neId, AdpTp tp) throws AdapterException {
		List<String> layerRates = tp.getLayerRates();
		String moi = GenerateKeyOnNeUtil.getMoi(tp.getKeyOnNe());
		log.debug("moi = " + moi);
		XcParamBean bean = new XcParamBean();

		if (LayerRatesUtil.isEquals(ManagedObjectType.TU12.getLayerRates(), layerRates)) {
			constructTu12CtpBean(moi, bean);
		} else if (LayerRatesUtil.isEquals(ManagedObjectType.VC12.getLayerRates(), layerRates)) {
			constructVc12TtpBean(moi, bean);
		} else if (LayerRatesUtil.isEquals(ManagedObjectType.TU3.getLayerRates(), layerRates)) {
			constructTu3CtpBean(moi, bean);
		} else if (LayerRatesUtil.isEquals(ManagedObjectType.VC3.getLayerRates(), layerRates)) {
			constructVc3TtpBean(moi, bean);
		} else {
			log.error("there is not a valid tp layerRates:" + layerRates);
		}

		String groupId = "100";
		bean.setGroupId(groupId);
		bean.setNeId(String.valueOf(neId));
		return bean;
	}

	private void constructVc12TtpBean(String moi, XcParamBean bean) {
		bean.setSdhTp(false);
		String vc12TtpId = moi.split("/")[0].replaceAll("vc12TTPId=", StringUtils.EMPTY);
		bean.setVc12TtpId(vc12TtpId);
	}

	private void constructTu12CtpBean(String moi, XcParamBean bean) {
		String vc4TtpId = moi.split("/")[0].replaceAll("vc4TTPId=", StringUtils.EMPTY);
		bean.setVc4TtpId(vc4TtpId);
		String tug3Id = moi.split("/")[1].replaceAll("tug3Id=", StringUtils.EMPTY);
		bean.setTug3Id(tug3Id);
		String tug2Id = moi.split("/")[2].replaceAll("tug2Id=", StringUtils.EMPTY);
		bean.setTug2Id(tug2Id);
		String tu12CtpId = moi.split("/")[3].replaceAll("tu12CTPId=", StringUtils.EMPTY);
		bean.setTu12CtpId(tu12CtpId);
		bean.setSdhTp(true);
	}

	private void constructTu3CtpBean(String moi, XcParamBean bean) {
		String vc4TtpId = moi.split("/")[0].replaceAll("vc4TTPId=", StringUtils.EMPTY);
		bean.setVc4TtpId(vc4TtpId);
		String tug3Id = moi.split("/")[1].replaceAll("tug3Id=", StringUtils.EMPTY);
		bean.setTug3Id(tug3Id);
		String tu3CtpId = moi.split("/")[2].replaceAll("tu3CTPId=", StringUtils.EMPTY);
		bean.setTu3CtpId(tu3CtpId);
		bean.setSdhTp(true);
	}

	private void constructVc3TtpBean(String moi, XcParamBean bean) {
		bean.setSdhTp(false);
		String vc3TtpId = moi.split("/")[0].replaceAll("vc3TTPId=", StringUtils.EMPTY);
		bean.setVc3TtpId(vc3TtpId);
	}

	// public String[] getNeInfo(String tpId) throws AdapterException {
	// String groupId = StringUtils.EMPTY;
	// String neId = StringUtils.EMPTY;
	//
	// AdpTp tp = getTpById(tpId);
	//
	// String neDbId = tp.getNeId();
	// AdpNe ne = getNeById(neDbId);
	//
	// String neMoi = GenerateKeyOnNeUtil.getMoi(ne.getKeyOnNe());
	// groupId = neMoi.split("/")[0].replaceAll("neGroupId=",
	// StringUtils.EMPTY);
	// neId = neMoi.split("/")[1].replaceAll("networkElementId=",
	// StringUtils.EMPTY);
	// return new String[] { groupId, neId, neDbId };
	// }

	// private AdpTp getTpById(Integer tpId) throws AdapterException {
	// AdpTp tp = null;
	// try {
	// tp = tpsDbMgr.getTpById(tpId);
	// } catch (Exception e) {
	// log.error("getTpById", e);
	// throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
	// }
	// return tp;
	// }

	public AdpTp getPdhSubTp(Integer ptpId, LayerRate layerRate) throws AdapterException {
		AdpTp ctp = null;
		List<String> subTpLR = null;
		try {
			if (LayerRate.VC12 == layerRate) {
				subTpLR = ManagedObjectType.E1_PHYSICAL.getLayerRates();
			} else if (LayerRate.VC3 == layerRate) {
				subTpLR = ManagedObjectType.E3_PHYSICAL.getLayerRates();
			}

			ctp = tpsDbMgr.getTpByParentIdAndLayerRate(ptpId, subTpLR);
		} catch (Exception e) {
			log.error("getPdhSubTp", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
		return ctp;
	}

	public AdpTp getAu4Tp(Integer neId, String keyOnNe, Integer au4TpTimeSlot) throws AdapterException {
		String protectedTTPId = keyOnNe.split("=")[1];
		String au4TpKeyOnNe = "au4CTPBidirectionalR1:protectedTTPId=" + protectedTTPId + "/augId=" + au4TpTimeSlot
				+ "/au4CTPId=1";
		AdpTp tp = null;
		try {
			tp = tpsDbMgr.getTpByKeyOnNe(neId, au4TpKeyOnNe);
		} catch (Exception e) {
			log.error("getAu4TpById", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}

		if (null == tp || tp.getId() < 0) {
			throw new AdapterException(ErrorCode.FAIL_OBJ_NOT_EXIST);
		}
		return tp;
	}

	public void deleteXcById(String xcId) throws AdapterException {
		AdpXc xc;
		try {
			xc = xcsDbMgr.getXcById(xcId);
		} catch (Exception e) {
			log.error("getXcById", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}

		if (null == xc || xc.getId() < 0) {
			log.error("can not find xc by id:" + xcId);
			throw new AdapterException(ErrorCode.FAIL_OBJ_NOT_EXIST);
		}
		deleteXc(xc);
	}

	public void deleteXcById(Integer neId, Integer xcId) throws AdapterException {
		AdpXc xc;
		try {
			xc = xcsDbMgr.getXcById(neId, xcId);
		} catch (Exception e) {
			log.error("getXcById", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}

		if (null == xc || xc.getId() < 0) {
			log.error("can not find xc by id:" + xcId);
			throw new AdapterException(ErrorCode.FAIL_OBJ_NOT_EXIST);
		}
		deleteXc(xc);
	}

	private void deleteXc(AdpXc xc) throws AdapterException {
		String moi = GenerateKeyOnNeUtil.getMoi(xc.getKeyOnNe());
		String groupId = moi.split("/")[0].replaceAll("neGroupId=", StringUtils.EMPTY);
		String neId = moi.split("/")[1].replaceAll("networkElementId=", StringUtils.EMPTY);
		String corssConnectionId = moi.split("/")[3].replaceAll("crossConnectionId=", StringUtils.EMPTY);
		DeleteXc.deleteXc(groupId, neId, corssConnectionId);

		try {
			xcsDbMgr.deleteXc(xc.getId());
		} catch (Exception e) {
			log.error("deleteXc", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
	}

	public void deleteXcsByNeId(Integer neId) throws AdapterException {
		List<AdpXc> xcList;
		try {
			xcList = xcsDbMgr.getXcsByNeId(neId);
		} catch (Exception e) {
			log.error("getXcsByNeId", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}

		if (null == xcList || xcList.isEmpty()) {
			throw new AdapterException(ErrorCode.FAIL_OBJ_NOT_EXIST);
		}

		for (AdpXc xc : xcList) {
			deleteXc(xc);
		}
	}

	public AdpXc createXcByTu12AndTu12(Integer neDbId, AdpTp atp, AdpTp ztp) throws AdapterException {
		log.debug("atpId = {}", atp);
		XcParamBean atpBean = getParam(neDbId, atp);

		log.debug("ztpId = {}", ztp);
		XcParamBean ztpBean = getParam(neDbId, ztp);

		XcEntity xcEntity = CreateXc.createXcTu12(atpBean.getGroupId(), atpBean.getNeId(), atpBean.getVc4TtpId(),
				atpBean.getTug3Id(), atpBean.getTug2Id(), atpBean.getTu12CtpId(), ztpBean.getVc4TtpId(),
				ztpBean.getTug3Id(), ztpBean.getTug2Id(), ztpBean.getTu12CtpId());
		log.debug("createXcByTu12AndTu12 ok");
		return insertXc2Db(neDbId, atp.getId(), ztp.getId(), ManagedObjectType.TU12.getLayerRates());
	}

	public AdpXc createXcByTu3AndTu3(Integer neDbId, AdpTp atp, AdpTp ztp) throws AdapterException {
		log.debug("atpId = {}", atp);
		XcParamBean atpBean = getParam(neDbId, atp);

		log.debug("ztpId = {}", ztp);
		XcParamBean ztpBean = getParam(neDbId, ztp);

		XcEntity xcEntity = CreateXc.createXcTu3(atpBean.getGroupId(), atpBean.getNeId(), atpBean.getVc4TtpId(),
				atpBean.getTug3Id(), atpBean.getTu3CtpId(), ztpBean.getVc4TtpId(), ztpBean.getTug3Id(),
				ztpBean.getTu3CtpId());
		log.debug("createXcByTu3AndTu3 ok");
		return insertXc2Db(neDbId, atp.getId(), ztp.getId(), ManagedObjectType.TU3.getLayerRates());
	}

	private AdpXc insertXc2Db(Integer neDbId, Integer atpDbId, Integer ztpDbId, List<String> layerRates)
			throws AdapterException {
		AdpXc xc = new AdpXc();
		xc.setNeId(neDbId);
		xc.setLayerrate(layerRates);

		List<Integer> atps = new ArrayList<Integer>();
		atps.add(atpDbId);
		xc.setAEndPoints(atps);

		List<Integer> ztps = new ArrayList<Integer>();
		ztps.add(ztpDbId);
		xc.setZEndPoints(ztps);

		try {
			xc = xcsDbMgr.createXc(xc);
		} catch (Exception e) {
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
		return xc;
	}

	public AdpXc createXc(Integer neId, AdpXc body) throws AdapterException {
		return new AdpCreateXcsMgr().createXc(neId, body);
	}
}
