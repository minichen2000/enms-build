package com.nsb.enms.adapter.server.sdh.business.xc;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.db.mgr.AdpTpsDbMgr;
import com.nsb.enms.adapter.server.common.db.mgr.AdpXcsDbMgr;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.utils.LayerRatesUtil;
import com.nsb.enms.adapter.server.sdh.business.tp.AdpQ3TpsMgr;
import com.nsb.enms.adapter.server.sdh.business.tp.TerminateTpMgr;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.common.LayerRate;
import com.nsb.enms.common.ManagedObjectType;
import com.nsb.enms.common.utils.HexDecConvertUtil;
import com.nsb.enms.restful.model.adapter.AdpTp;
import com.nsb.enms.restful.model.adapter.AdpXc;

public class AdpCreateXcsMgr {
	private final static Logger log = LogManager.getLogger(AdpCreateXcsMgr.class);
	private AdpXcsDbMgr xcsDbMgr = new AdpXcsDbMgr();
	private AdpQ3XcsMgr adpXcMgr = new AdpQ3XcsMgr();
	private AdpTpsDbMgr tpsDbMgr = new AdpTpsDbMgr();

	public AdpXc createXc(Integer neId, AdpXc body) throws AdapterException {
		List<Integer> atps = body.getAEndPoints();
		List<Integer> ztps = body.getZEndPoints();
		isTpsValid(atps, ztps);
		List<String> layerrates = body.getLayerrate();
		isLayerRateValid(layerrates);

		List<Integer> atpTimeSlots = body.getAtpTimeslots();
		List<Integer> ztpTimeSlots = body.getZtpTimeslots();
		boolean isAtpEmpty = isTimeSlotsEmpty(atpTimeSlots);
		boolean isZtpEmpty = isTimeSlotsEmpty(ztpTimeSlots);

		LayerRate layerRate = convertLayerRate(layerrates);

		AdpXc xc = null;
		if (isAtpEmpty && isZtpEmpty) {
			log.error("atpTimeSlots and ztpTimeSlots are null or empty");
			throw new AdapterException(ErrorCode.FAIL_CREATE_XC_BY_INVALID_PARAM);
		} else if (isAtpEmpty) {
			xc = createXcByZtpIsSdh(ztpTimeSlots, atps, ztps, neId, layerRate);
		} else if (isZtpEmpty) {
			xc = createXcByAtpIsSdh(atpTimeSlots, atps, ztps, neId, layerRate);
		} else {
			xc = createXcByBothTpIsSdh(atpTimeSlots, ztpTimeSlots, atps, ztps, neId, layerRate);
		}

		log.debug("create xc ok!");
		return xc;
	}

	private AdpTp getTimeSlotTpByVc4Tp(Integer neId, Integer ptpId, AdpTp au4Ctp, Integer[] timeSlots,
			LayerRate layerRate) throws AdapterException {
		log.debug("===============getTimeSlotTpByVc4Tp=================");
		String vc4TTPId = getVc4TtpIdFromXcs(au4Ctp.getId());
		if (StringUtils.isEmpty(vc4TTPId)) {
			return handleVc4TpIdIsEmpty(neId, au4Ctp, ptpId, timeSlots, layerRate);
		} else {
			return handleVc4TpIdExisted(neId, timeSlots, layerRate, vc4TTPId);
		}
	}

	private AdpXc createXcBySdhAndPdh(Integer neId, Integer sdhTpId, AdpTp au4Ctp, Integer[] timeSlots, AdpTp tpId,
			boolean isAtp, LayerRate layerRate) throws AdapterException {
		log.debug("===============createXcBySdhAndPdh=================");
		AdpTp timeSlotTp = getTimeSlotTpByVc4Tp(neId, sdhTpId, au4Ctp, timeSlots, layerRate);
		return createXcByLayerRate(layerRate, neId, timeSlotTp, tpId, isAtp);
	}

	private AdpXc createXcByBothSdh(Integer neId, AdpTp atp, AdpTp ztp, LayerRate layerRate) throws AdapterException {
		log.debug("===============createXcByBothSdh=================");
		return createXcByLayerRate(layerRate, neId, atp, ztp);
	}

	private LayerRate convertLayerRate(List<String> layerRates) {
		String layerRate = layerRates.get(0);
		if (LayerRate.VC12.name().equalsIgnoreCase(layerRate)) {
			return LayerRate.VC12;
		}
		if (LayerRate.VC3.name().equalsIgnoreCase(layerRate)) {
			return LayerRate.VC3;
		}
		if (LayerRate.TU12.name().equalsIgnoreCase(layerRate)) {
			return LayerRate.TU12;
		}
		if (LayerRate.TU3.name().equalsIgnoreCase(layerRate)) {
			return LayerRate.TU3;
		}
		return null;
	}

	private AdpTp handleVc4TpIdExisted(Integer neId, Integer[] timeSlots, LayerRate layerRate, String vc4TTPId)
			throws AdapterException {
		// TODO 判断传入的时隙是否空闲
		String timeSlotTpKeyOnNe = constructTpKeyOnNeByTimeSlot(layerRate, timeSlots, neId, vc4TTPId);

		AdpTp tp = isTpExisted(neId, timeSlotTpKeyOnNe);
		if (null == tp) {
			log.error("tp is not existed," + timeSlotTpKeyOnNe);
			throw new AdapterException(ErrorCode.FAIL_CREATE_XC_BY_TP_NOT_EXISTED);
		}

		// 判断时隙所在的TUG3中的其他TP是否有创建了交叉业务的
		if (isTpUsedByXc(tp.getId())) {
			log.error("tp was used by XC," + timeSlotTpKeyOnNe);
			throw new AdapterException(ErrorCode.FAIL_CREATE_XC_BY_TP_NOT_FREE);
		}

		log.debug("timeSlotTpKeyOnNe = " + timeSlotTpKeyOnNe);

		return tp;
	}

	private AdpTp handleVc4TpIdIsEmpty(Integer neId, AdpTp au4Ctp, Integer ptpId, Integer[] timeSlots,
			LayerRate layerRate) throws AdapterException {
		log.debug("start to createXcVc4 by ctpId = {}", au4Ctp.getId());
		String vc4TTPId = adpXcMgr.createXcByAu4AndVc4(neId, au4Ctp, ptpId);
		log.debug("successed to createXcVc4 by ctpId = {}", au4Ctp.getId());

		String tug3Id = String.valueOf(timeSlots[1]);
		terminateTp(neId, layerRate, au4Ctp, vc4TTPId, tug3Id, ptpId);
		String keyOnNe = constructTpKeyOnNeByTimeSlot(layerRate, timeSlots, neId, vc4TTPId);
		AdpTp tp = isTpExisted(neId, keyOnNe);
		if (null == tp) {
			log.error("tp is not existed," + keyOnNe);
			throw new AdapterException(ErrorCode.FAIL_CREATE_XC_BY_TP_NOT_EXISTED);
		}
		return tp;
	}

	private AdpXc createXcByAtpIsSdh(List<Integer> atpTimeSlots, List<Integer> atps, List<Integer> ztps, Integer neId,
			LayerRate layerRate) throws AdapterException {
		Integer sdhTpId = atps.get(0);
		AdpTp sdhTp = getTpById(sdhTpId);
		boolean isValid = isTimeSlotValid(atpTimeSlots, layerRate) && isSdhTtp(sdhTp) && isPdhTtp(ztps);
		if (!isValid) {
			log.error("one of parameter that contains tp and timeSlot may be not a valid value");
			throw new AdapterException(ErrorCode.FAIL_CREATE_XC_BY_INVALID_PARAM);
		}

		Integer[] timeSlots = HexDecConvertUtil.hex2Int(atpTimeSlots.get(0));
		AdpTp au4Ctp = adpXcMgr.getAu4Tp(neId, sdhTp.getKeyOnNe(), timeSlots[0]);
		log.debug("au4CtpId = " + au4Ctp.getId());
		AdpTp ztp = adpXcMgr.getPdhSubTp(ztps.get(0), layerRate);
		Integer ztpId = ztp.getId();
		log.debug("ztpId = " + ztpId);

		if (null == ztpId || ztpId < 0) {
			log.error("can not find pdh sub tp by tpId = " + ztps.get(0) + " and layerRate = " + layerRate);
			throw new AdapterException(ErrorCode.FAIL_CREATE_XC_BY_TP_NOT_EXISTED);
		}

		if (isTpUsedByXc(ztpId)) {
			log.error("tp was used by XC," + ztpId);
			throw new AdapterException(ErrorCode.FAIL_CREATE_XC_BY_TP_NOT_FREE);
		}

		return createXcBySdhAndPdh(neId, sdhTpId, au4Ctp, timeSlots, ztp, false, layerRate);
	}

	private AdpXc createXcByZtpIsSdh(List<Integer> ztpTimeSlots, List<Integer> atps, List<Integer> ztps, Integer neId,
			LayerRate layerRate) throws AdapterException {
		Integer sdhTpId = ztps.get(0);
		AdpTp sdhTp = getTpById(sdhTpId);
		boolean isValid = isTimeSlotValid(ztpTimeSlots, layerRate) && isPdhTtp(atps) && isSdhTtp(sdhTp);
		if (!isValid) {
			log.error("one of parameter that contains tp and timeSlot may be not a valid value");
			throw new AdapterException(ErrorCode.FAIL_CREATE_XC_BY_INVALID_PARAM);
		}

		Integer[] timeSlots = HexDecConvertUtil.hex2Int(ztpTimeSlots.get(0));
		AdpTp au4Ctp = adpXcMgr.getAu4Tp(neId, sdhTp.getKeyOnNe(), timeSlots[0]);
		log.debug("au4CtpId = " + au4Ctp.getId());

		AdpTp atp = adpXcMgr.getPdhSubTp(atps.get(0), layerRate);
		Integer atpId = atp.getId();
		log.debug("atpId = " + atpId);
		if (null == atpId || atpId < 0) {
			log.error("can not find pdh sub tp by tpId = " + atps.get(0) + " and layerRate = " + layerRate);
			throw new AdapterException(ErrorCode.FAIL_CREATE_XC_BY_TP_NOT_EXISTED);
		}

		if (isTpUsedByXc(atpId)) {
			log.error("tp was used by XC," + atpId);
			throw new AdapterException(ErrorCode.FAIL_CREATE_XC_BY_TP_NOT_FREE);
		}
		return createXcBySdhAndPdh(neId, sdhTpId, au4Ctp, timeSlots, atp, true, layerRate);
	}

	private AdpXc createXcByBothTpIsSdh(List<Integer> atpTimeSlots, List<Integer> ztpTimeSlots, List<Integer> atps,
			List<Integer> ztps, Integer neId, LayerRate layerRate) throws AdapterException {
		Integer atpId = atps.get(0);
		AdpTp sdhAtp = getTpById(atpId);
		Integer ztpId = ztps.get(0);
		AdpTp sdhZtp = getTpById(ztpId);

		boolean isValid = isSdhTtp(sdhAtp) && isSdhTtp(sdhZtp) && isTimeSlotValid(atpTimeSlots, layerRate)
				&& isTimeSlotValid(ztpTimeSlots, layerRate);
		if (!isValid) {
			log.error("one of parameter that contains tp and timeSlot may be not a valid value");
			throw new AdapterException(ErrorCode.FAIL_CREATE_XC_BY_INVALID_PARAM);
		}

		Integer[] a_timeSlots = HexDecConvertUtil.hex2Int(atpTimeSlots.get(0));
		AdpTp a_au4Ctp = adpXcMgr.getAu4Tp(neId, sdhAtp.getKeyOnNe(), a_timeSlots[0]);
		log.debug("a_au4CtpId = " + a_au4Ctp.getId());

		AdpTp a_TuCtp = getTimeSlotTpByVc4Tp(neId, atpId, a_au4Ctp, a_timeSlots, layerRate);

		Integer[] z_timeSlots = HexDecConvertUtil.hex2Int(ztpTimeSlots.get(0));
		AdpTp z_au4Ctp = adpXcMgr.getAu4Tp(neId, sdhZtp.getKeyOnNe(), z_timeSlots[0]);
		log.debug("z_au4CtpId = " + z_au4Ctp.getId());

		AdpTp z_TuCtp = getTimeSlotTpByVc4Tp(neId, ztpId, z_au4Ctp, z_timeSlots, layerRate);

		return createXcByBothSdh(neId, a_TuCtp, z_TuCtp, layerRate);
	}

	private String constructTpKeyOnNeByTimeSlot(LayerRate layerRate, Integer[] timeSlots, Integer neId,
			String vc4TpId) {
		String keyOnNe = StringUtils.EMPTY;
		if (LayerRate.VC12 == layerRate || LayerRate.TU12 == layerRate) {
			keyOnNe = "tu12CTPBidirectionalR1:vc4TTPId=" + vc4TpId + "/tug3Id=" + timeSlots[1] + "/tug2Id="
					+ timeSlots[2] + "/tu12CTPId=" + timeSlots[3];
		} else if (LayerRate.VC3 == layerRate || LayerRate.TU3 == layerRate) {
			keyOnNe = "tu3CTPBidirectionalR1:vc4TTPId=" + vc4TpId + "/tug3Id=" + timeSlots[1] + "/tu3CTPId=1";
		}
		return keyOnNe;
	}

	private boolean isTimeSlotsEmpty(List<Integer> tpTimeSlots) {
		if (null == tpTimeSlots || tpTimeSlots.isEmpty()) {
			log.error("tpTimeSlots is null or empty");
			return true;
		}

		return false;
	}

	private AdpXc createXcByLayerRate(LayerRate layerRate, Integer neDbId, AdpTp timeSlotTp, AdpTp tp, boolean isAtp)
			throws AdapterException {
		AdpTp atpId, ztpId;
		if (isAtp) {
			atpId = tp;
			ztpId = timeSlotTp;
		} else {
			atpId = timeSlotTp;
			ztpId = tp;
		}

		return createXcByLayerRate(layerRate, neDbId, atpId, ztpId);
	}

	private AdpXc createXcByLayerRate(LayerRate layerRate, Integer neDbId, AdpTp atp, AdpTp ztp)
			throws AdapterException {
		AdpXc xc = null;
		if (LayerRate.VC3 == layerRate) {
			xc = adpXcMgr.createXcByTu3AndVc3(neDbId, atp, ztp);
		} else if (LayerRate.VC12 == layerRate) {
			xc = adpXcMgr.createXcByTu12AndVc12(neDbId, atp, ztp);
		} else if (LayerRate.TU12 == layerRate) {
			xc = adpXcMgr.createXcByTu12AndTu12(neDbId, atp, ztp);
		} else if (LayerRate.TU3 == layerRate) {
			xc = adpXcMgr.createXcByTu3AndTu3(neDbId, atp, ztp);
		}
		return xc;
	}

	/**
	 * 执行打散操作
	 * 
	 * @param neId
	 * @param layerRate
	 * @param au4Ctp
	 * @param vc4TTPId
	 * @param tug3Id
	 * @param ptpId
	 * @throws AdapterException
	 */
	private void terminateTp(Integer neId, LayerRate layerRate, AdpTp au4Ctp, String vc4TTPId, String tug3Id,
			Integer ptpId) throws AdapterException {
		String groupId = "100";
		TerminateTpMgr terminateMgr = new TerminateTpMgr();
		AdpQ3TpsMgr adpTpsMgr = new AdpQ3TpsMgr();
		if (LayerRate.VC12 == layerRate || LayerRate.TU12 == layerRate) {
			terminateMgr.terminateTug3ToTu12(groupId, String.valueOf(neId), vc4TTPId, tug3Id);
			adpTpsMgr.syncTu12Ctp(groupId, neId, vc4TTPId, au4Ctp, ptpId);
		} else {
			terminateMgr.terminateTug3ToTu3(groupId, String.valueOf(neId), vc4TTPId, tug3Id);
			adpTpsMgr.syncTu3Ctp(groupId, neId, vc4TTPId, au4Ctp, ptpId);
		}
	}

	private boolean isPdhTtp(List<Integer> tpIds) throws AdapterException {
		Integer tpId = tpIds.get(0);
		AdpTp tp = getTpById(tpId);
		if (null == tp) {
			return false;
		}

		boolean isEqual = LayerRatesUtil.isEquals(ManagedObjectType.STM1_ELECTRICAL.getLayerRates(),
				tp.getLayerRates());

		return isEqual;
	}

	private boolean isSdhTtp(AdpTp tp) throws AdapterException {
		if (null == tp) {
			return false;
		}

		List<String> layerRates = tp.getLayerRates();
		boolean isEqual = LayerRatesUtil.isEquals(ManagedObjectType.STM1_OPTICAL.getLayerRates(), layerRates)
				|| LayerRatesUtil.isEquals(ManagedObjectType.STM4_OPTICAL.getLayerRates(), layerRates)
				|| LayerRatesUtil.isEquals(ManagedObjectType.STM16_OPTICAL.getLayerRates(), layerRates)
				|| LayerRatesUtil.isEquals(ManagedObjectType.STM64_OPTICAL.getLayerRates(), layerRates)
				|| LayerRatesUtil.isEquals(ManagedObjectType.STM256_OPTICAL.getLayerRates(), layerRates);

		return isEqual;
	}

	private AdpTp getTpById(Integer tpId) throws AdapterException {
		try {
			AdpTp tp = tpsDbMgr.getTpById(tpId);
			if (tp.getId() > -1) {
				return tp;
			}
		} catch (Exception e) {
			log.error("getTpByIdAndLayerRate", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}

		log.error("can not find TP, tpId = " + tpId);
		return null;
	}

	private String getVc4TtpIdFromXcs(Integer au4CtpId) throws AdapterException {
		List<AdpXc> xcList = getXcsByTpId(au4CtpId);
		if (null == xcList || xcList.isEmpty()) {
			log.error("xcList is null or empty");
			return null;
		}

		AdpXc xc = xcList.get(0);
		Integer atpId = xc.getAEndPoints().get(0);
		AdpTp atp = getTpById(atpId);
		if (LayerRatesUtil.isEquals(atp.getLayerRates(), ManagedObjectType.AU4.getLayerRates())) {
			Integer ztpId = xc.getZEndPoints().get(0);
			AdpTp ztp = getTpById(ztpId);
			String vc4TpId = ztp.getKeyOnNe().split("=")[1];
			return vc4TpId;
		} else {
			return atp.getKeyOnNe().split("=")[1];
		}
	}

	private AdpTp isTpExisted(Integer neId, String keyOnNe) throws AdapterException {
		AdpTp tp;
		try {
			tp = tpsDbMgr.getTpByKeyOnNe(neId, keyOnNe);
		} catch (Exception e) {
			log.error("getTpByKeyOnNe", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}

		if (null == tp || tp.getId() < 0) {
			return null;
		}
		return tp;
	}

	private void isTpsValid(List<Integer> atps, List<Integer> ztps) throws AdapterException {
		if (null == atps || atps.isEmpty()) {
			log.error("atps is null or empty");
			throw new AdapterException(ErrorCode.FAIL_CREATE_XC_BY_INVALID_PARAM);
		}

		if (null == ztps || ztps.isEmpty()) {
			log.error("ztps is null or empty");
			throw new AdapterException(ErrorCode.FAIL_CREATE_XC_BY_INVALID_PARAM);
		}
	}

	private void isLayerRateValid(List<String> layerRate) throws AdapterException {
		if (null == layerRate || layerRate.isEmpty()) {
			log.error("layerRate is null or empty");
			throw new AdapterException(ErrorCode.FAIL_CREATE_XC_BY_INVALID_PARAM);
		}
	}

	private boolean isTpUsedByXc(Integer tpId) throws AdapterException {
		List<AdpXc> xcList = getXcsByTpId(tpId);
		if (null == xcList || xcList.isEmpty()) {
			log.error("can not find xc by " + tpId);
			return false;
		}
		return true;
	}

	private boolean isTimeSlotValid(List<Integer> timeSlots, LayerRate layerRate) {
		Integer[] timeSlotList = HexDecConvertUtil.hex2Int(timeSlots.get(0));
		String timeSlotStr = String.valueOf(timeSlotList[1]) + timeSlotList[2] + timeSlotList[3];

		if (LayerRate.VC12 == layerRate || LayerRate.TU12 == layerRate) {
			String regex1 = "^[1-3][1-7][1-3]$";
			Pattern pattern1 = Pattern.compile(regex1);
			Matcher matcher1 = pattern1.matcher(timeSlotStr);
			return matcher1.matches();
		}
		if (LayerRate.VC3 == layerRate || LayerRate.TU3 == layerRate) {
			String regex2 = "^[1-3]10$";
			Pattern pattern2 = Pattern.compile(regex2);
			Matcher matcher2 = pattern2.matcher(timeSlotStr);
			return matcher2.matches();
		}

		log.error("timeSlot is invalid, " + timeSlotStr + ", layerRate = " + layerRate);

		return false;
	}

	private List<AdpXc> getXcsByTpId(Integer tpid) throws AdapterException {
		List<AdpXc> xcList = new ArrayList<AdpXc>();
		try {
			xcList = xcsDbMgr.findXcsByTpId(tpid);
		} catch (Exception e) {
			log.error("findXcsByTpId", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
		return xcList;
	}
}
