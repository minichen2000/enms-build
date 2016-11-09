package com.nsb.enms.restful.adapterserver.api.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.business.tp.AdpTpsMgr;
import com.nsb.enms.adapter.server.business.tp.TerminateTpMgr;
import com.nsb.enms.adapter.server.business.xc.AdpXcsMgr;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.utils.ErrorWrapperUtils;
import com.nsb.enms.adapter.server.db.mgr.AdpTpsDbMgr;
import com.nsb.enms.adapter.server.db.mgr.AdpXcsDbMgr;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.common.LayerRate;
import com.nsb.enms.common.utils.HexDecConvertUtil;
import com.nsb.enms.restful.adapterserver.api.NotFoundException;
import com.nsb.enms.restful.adapterserver.api.XcsApiService;
import com.nsb.enms.restful.model.adapter.AdpTp;
import com.nsb.enms.restful.model.adapter.AdpXc;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-08-31T16:19:02.183+08:00")
public class XcsApiServiceImpl extends XcsApiService {
	private final static Logger log = LogManager.getLogger(XcsApiServiceImpl.class);
	private AdpXcsDbMgr xcsDbMgr = new AdpXcsDbMgr();
	private AdpXcsMgr adpXcMgr = new AdpXcsMgr();
	private AdpTpsDbMgr tpsDbMgr = new AdpTpsDbMgr();

	@Override
	public Response createXc(AdpXc body, SecurityContext securityContext) throws NotFoundException {
		try {
			// TODO 还需要判断atps和时隙之间是否匹配
			List<String> atps = body.getAtps();
			List<String> ztps = body.getZtps();
			isTpsValid(atps, ztps);
			String layerrate = body.getLayerrate();
			isLayerRateValid(layerrate);

			List<Integer> atpTimeSlots = body.getAtpTimeslots();
			List<Integer> ztpTimeSlots = body.getZtpTimeslots();
			boolean isAtpEmpty = isTimeSlotsEmpty(atpTimeSlots);
			boolean isZtpEmpty = isTimeSlotsEmpty(ztpTimeSlots);

			LayerRate layerRate = convertLayerRate(layerrate);

			String neId = body.getNeId();
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

			return Response.ok().entity(xc).build();
		} catch (AdapterException e) {
			return ErrorWrapperUtils.adapterException(e);
		}
	}

	private AdpXc handleVc4Tp(String neId, String au4CtpId, Integer[] timeSlots, String tpId, boolean isAtp,
			LayerRate layerRate) throws AdapterException {
		log.error("xxxxxx===============2=================");
		// LayerRate layerRate = getLayerRateByTimeSlot(timeSlot);
		String vc4TTPId = getVc4TtpIdFromXcs(au4CtpId);
		if (StringUtils.isEmpty(vc4TTPId)) {
			return handleVc4TpIdIsEmpty(neId, au4CtpId, timeSlots, tpId, isAtp, layerRate);
		} else {
			return handleVc4TpIdExisted(neId, timeSlots, tpId, isAtp, layerRate, vc4TTPId);
		}
	}

	private LayerRate convertLayerRate(String layerRate) {
		if ("LR_TUVC12".equalsIgnoreCase(layerRate)) {
			return LayerRate.LR_TUVC12;
		}
		if ("LR_TUVC3".equalsIgnoreCase(layerRate)) {
			return LayerRate.LR_TUVC3;
		}
		return null;
	}

	private AdpXc handleVc4TpIdExisted(String neId, Integer[] timeSlots, String tpId, boolean isAtp,
			LayerRate layerRate, String vc4TTPId) throws AdapterException {
		// TODO 判断传入的时隙是否空闲
		String timeSlotTpId = getTpIdByTimeSlot(layerRate, timeSlots, neId, vc4TTPId);
		if (!isTpExisted(timeSlotTpId)) {
			log.error("tp is not existed," + timeSlotTpId);
			throw new AdapterException(ErrorCode.FAIL_CREATE_XC_BY_TP_NOT_EXISTED);
		}

		// 判断时隙所在的TUG3中的其他TP是否有创建了交叉业务的
		if (isTpUsedByXc(timeSlotTpId)) {
			log.error("tp was used by XC," + timeSlotTpId);
			throw new AdapterException(ErrorCode.FAIL_CREATE_XC_BY_TP_NOT_FREE);
		}

		log.error("xxxxxx===============3=================");

		return createXcByLayerRate(layerRate, neId, timeSlotTpId, tpId, isAtp);
	}

	private AdpXc handleVc4TpIdIsEmpty(String neId, String au4CtpId, Integer[] timeSlots, String tpId, boolean isAtp,
			LayerRate layerRate) throws AdapterException {
		log.debug("start to createXcVc4 by ctpId = {}", au4CtpId);
		String vc4TTPId = adpXcMgr.createXcByAu4AndVc4(au4CtpId);
		log.debug("successed to createXcVc4 by ctpId = {}", au4CtpId);

		String tug3Id = String.valueOf(timeSlots[1]);
		terminateTp(layerRate, au4CtpId, vc4TTPId, tug3Id);
		String timeSlotTpId = getTpIdByTimeSlot(layerRate, timeSlots, neId, vc4TTPId);

		return createXcByLayerRate(layerRate, neId, timeSlotTpId, tpId, isAtp);
	}

	private AdpXc createXcByAtpIsSdh(List<Integer> atpTimeSlots, List<String> atps, List<String> ztps, String neId,
			LayerRate layerRate) throws AdapterException {
		boolean isValid = isTimeSlotValid(atpTimeSlots, layerRate) && isPdhTtp(ztps, layerRate);
		if (!isValid) {
			log.error("one of parameter that contains tp and timeSlot may be not a valid value");
			throw new AdapterException(ErrorCode.FAIL_CREATE_XC_BY_INVALID_PARAM);
		}

		String sdhTpId = atps.get(0);
		Integer[] timeSlots = HexDecConvertUtil.hex2Int(atpTimeSlots.get(0));
		String au4CtpId = adpXcMgr.getAu4TpId(neId, sdhTpId, timeSlots[0]);
		log.debug("au4CtpId = " + au4CtpId);
		String ztpId = adpXcMgr.getPdhSubTp(ztps.get(0), layerRate);
		log.debug("ztpId = " + ztpId);
		if (isTpUsedByXc(ztpId)) {
			log.error("tp was used by XC," + ztpId);
			throw new AdapterException(ErrorCode.FAIL_CREATE_XC_BY_TP_NOT_FREE);
		}

		return handleVc4Tp(neId, au4CtpId, timeSlots, ztpId, false, layerRate);
	}

	private AdpXc createXcByZtpIsSdh(List<Integer> ztpTimeSlots, List<String> atps, List<String> ztps, String neId,
			LayerRate layerRate) throws AdapterException {
		boolean isValid = isTimeSlotValid(ztpTimeSlots, layerRate) && isPdhTtp(atps, layerRate);
		if (!isValid) {
			log.error("one of parameter that contains tp and timeSlot may be not a valid value");
			throw new AdapterException(ErrorCode.FAIL_CREATE_XC_BY_INVALID_PARAM);
		}

		String sdhTpId = ztps.get(0);
		Integer[] timeSlots = HexDecConvertUtil.hex2Int(ztpTimeSlots.get(0));
		String au4CtpId = adpXcMgr.getAu4TpId(neId, sdhTpId, timeSlots[0]);
		log.debug("au4CtpId = " + au4CtpId);

		String atpId = adpXcMgr.getPdhSubTp(atps.get(0), layerRate);
		log.debug("atpId = " + atpId);
		if (isTpUsedByXc(atpId)) {
			log.error("tp was used by XC," + atpId);
			throw new AdapterException(ErrorCode.FAIL_CREATE_XC_BY_TP_NOT_FREE);
		}
		return handleVc4Tp(neId, au4CtpId, timeSlots, atpId, true, layerRate);
	}

	private AdpXc createXcByBothTpIsSdh(List<Integer> atpTimeSlots, List<Integer> ztpTimeSlots, List<String> atps,
			List<String> ztps, String neId, LayerRate layerRate) throws AdapterException {
		boolean isValid = isTimeSlotValid(atpTimeSlots, layerRate) && isTimeSlotValid(ztpTimeSlots, layerRate);
		if (!isValid) {
			log.error("one of parameter that contains tp and timeSlot may be not a valid value");
			throw new AdapterException(ErrorCode.FAIL_CREATE_XC_BY_INVALID_PARAM);
		}

		String a_au4CtpId = atps.get(0);
		Integer a_timeSlot = atpTimeSlots.get(0);
		String atpId = getTpIdByTimeSlot(neId, a_au4CtpId, a_timeSlot, layerRate);

		String z_au4CtpId = ztps.get(0);
		Integer z_timeSlot = ztpTimeSlots.get(0);
		String ztpId = getTpIdByTimeSlot(neId, z_au4CtpId, z_timeSlot, layerRate);

		return createXcByLayerRate(layerRate, neId, atpId, ztpId, false);
	}

	private String getTpIdByTimeSlot(String neId, String au4CtpId, Integer timeSlot, LayerRate layerRate)
			throws AdapterException {
		Integer[] timeSlots = HexDecConvertUtil.hex2Int(timeSlot);
		String vc4TTPId = getVc4TtpIdFromXcs(au4CtpId);
		if (StringUtils.isEmpty(vc4TTPId)) {
			vc4TTPId = adpXcMgr.createXcByAu4AndVc4(au4CtpId);
			log.debug("successed to createXcVc4 by ctpId = {}", au4CtpId);

			String tug3Id = String.valueOf(timeSlots[1]);
			terminateTp(layerRate, au4CtpId, vc4TTPId, tug3Id);
			return getTpIdByTimeSlot(layerRate, timeSlots, neId, vc4TTPId);
		} else {
			String timeSlotTpId = getTpIdByTimeSlot(layerRate, timeSlots, neId, vc4TTPId);
			if (!isTpExisted(timeSlotTpId)) {
				log.error("tp is not existed," + timeSlotTpId);
				throw new AdapterException(ErrorCode.FAIL_CREATE_XC_BY_TP_NOT_EXISTED);
			}

			// 判断时隙所在的TUG3中的其他TP是否有创建了交叉业务的
			if (isTpUsedByXc(timeSlotTpId)) {
				log.error("tp was used by XC," + timeSlotTpId);
				throw new AdapterException(ErrorCode.FAIL_CREATE_XC_BY_TP_NOT_FREE);
			}

			return timeSlotTpId;
		}
	}

	private String getTpIdByTimeSlot(LayerRate layerRate, Integer[] timeSlots, String neId, String vc4TpId) {
		String tpId = StringUtils.EMPTY;
		if (LayerRate.LR_TUVC12 == layerRate) {
			tpId = neId + ":tu12CTPBidirectionalR1" + ":vc4TTPId=" + vc4TpId + "/tug3Id=" + timeSlots[1] + "/tug2Id="
					+ timeSlots[2] + "/tu12CTPId=" + timeSlots[3];
		} else if (LayerRate.LR_TUVC3 == layerRate) {
			tpId = neId + ":tu3CTPBidirectionalR1" + ":vc4TTPId=" + vc4TpId + "/tug3Id=" + timeSlots[1] + "/tu3CTPId=1";
		}
		return tpId;
	}

	private boolean isTimeSlotsEmpty(List<Integer> tpTimeSlots) {
		if (null == tpTimeSlots || tpTimeSlots.isEmpty()) {
			log.error("tpTimeSlots is null or empty");
			return true;
		}

		return false;
	}

	private AdpXc createXcByLayerRate(LayerRate layerRate, String neDbId, String timeSlotTpId, String tpId,
			boolean isAtp) throws AdapterException {
		String atpId = StringUtils.EMPTY;
		String ztpId = StringUtils.EMPTY;
		if (isAtp) {
			atpId = tpId;
			ztpId = timeSlotTpId;
		} else {
			atpId = timeSlotTpId;
			ztpId = tpId;
		}
		AdpXc xc = null;
		if (LayerRate.LR_TUVC3 == layerRate) {
			xc = adpXcMgr.createXcByTu3AndVc3(neDbId, atpId, ztpId);
		} else if (LayerRate.LR_TUVC12 == layerRate) {
			xc = adpXcMgr.createXcByTu12AndVc12(neDbId, atpId, ztpId);
		} else if (LayerRate.LR_TUVC3 == layerRate) { // TODO 修改layerRate
		} else if (LayerRate.LR_TUVC3 == layerRate) {
		}
		return xc;
	}

	/**
	 * 执行打散操作
	 * 
	 * @param layerRate
	 * @param au4CtpId
	 * @param vc4TTPId
	 * @param tug3Id
	 * @throws AdapterException
	 */
	private void terminateTp(LayerRate layerRate, String au4CtpId, String vc4TTPId, String tug3Id)
			throws AdapterException {
		String[] neInfos = adpXcMgr.getNeInfo(au4CtpId);
		String groupId = neInfos[0];
		String neId = neInfos[1];
		String neDbId = neInfos[2];
		TerminateTpMgr terminateMgr = new TerminateTpMgr();
		AdpTpsMgr adpTpsMgr = new AdpTpsMgr();
		if (LayerRate.LR_TUVC12 == layerRate) {
			terminateMgr.terminateTug3ToTu12(groupId, neId, vc4TTPId, tug3Id);
			adpTpsMgr.syncTu12Ctp(groupId, neId, vc4TTPId, au4CtpId, neDbId);
		} else {
			terminateMgr.terminateTug3ToTu3(groupId, neId, vc4TTPId, tug3Id);
			adpTpsMgr.syncTu3Ctp(groupId, neId, vc4TTPId, au4CtpId, neDbId);
		}
	}

	private boolean isAu4Ctp(List<String> tpIds) throws AdapterException {
		String tpId = tpIds.get(0);
		try {
			AdpTp tp = tpsDbMgr.getTpById(tpId);
			if ("au4CTPBidirectionalR1".equalsIgnoreCase(tp.getTpType())) {
				return true;
			}
		} catch (Exception e) {
			log.error("getTpById", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}

		log.error("tpId is not a au4 TP, " + tpId);
		return false;
	}

	private boolean isPdhTtp(List<String> tpIds, LayerRate layerRate) throws AdapterException {
		String tpId = tpIds.get(0);
		try {
			AdpTp tp = tpsDbMgr.getTpById(tpId);
			if ("pPITTPBidirectionalR1".equalsIgnoreCase(tp.getTpType())) {
				return true;
			}
		} catch (Exception e) {
			log.error("getTpById", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}

		log.error("tpId is not a Pdh TP, " + tpId);
		return false;
	}

	private String getVc4TtpIdFromXcs(String au4CtpId) throws AdapterException {
		List<AdpXc> xcList = getXcsByTpId(au4CtpId);
		if (null == xcList || xcList.isEmpty()) {
			log.error("xcList is null or empty");
			return null;
		}

		AdpXc xc = xcList.get(0);
		String atpId = xc.getAtps().get(0);
		if (atpId.contains("au4CTPBidirectionalR1")) {
			String ztpId = xc.getZtps().get(0);
			String vc4TpId = ztpId.split("=")[1];
			return vc4TpId;
		} else {
			return atpId.split("=")[1];
		}
	}

	private boolean isTpExisted(String tpId) throws AdapterException {
		AdpTp tp;
		try {
			tp = tpsDbMgr.getTpById(tpId);
		} catch (Exception e) {
			log.error("getTpById", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}

		if (null == tp || StringUtils.isEmpty(tp.getId())) {
			return false;
		}
		return true;
	}

	private void isTpsValid(List<String> atps, List<String> ztps) throws AdapterException {
		if (null == atps || atps.isEmpty()) {
			log.error("atps is null or empty");
			throw new AdapterException(ErrorCode.FAIL_CREATE_XC_BY_INVALID_PARAM);
		}

		if (null == ztps || ztps.isEmpty()) {
			log.error("ztps is null or empty");
			throw new AdapterException(ErrorCode.FAIL_CREATE_XC_BY_INVALID_PARAM);
		}
	}

	private void isLayerRateValid(String layerRate) throws AdapterException {
		if (StringUtils.isEmpty(layerRate)) {
			log.error("layerRate is null or empty");
			throw new AdapterException(ErrorCode.FAIL_CREATE_XC_BY_INVALID_PARAM);
		}
	}

	private boolean isTpUsedByXc(String tpId) throws AdapterException {
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

		if (LayerRate.LR_TUVC12 == layerRate) {
			String regex1 = "^[1-3][1-7][1-3]$";
			Pattern pattern1 = Pattern.compile(regex1);
			Matcher matcher1 = pattern1.matcher(timeSlotStr);
			return matcher1.matches();
		}
		if (LayerRate.LR_TUVC3 == layerRate) {
			String regex2 = "^[1-3]10$";
			Pattern pattern2 = Pattern.compile(regex2);
			Matcher matcher2 = pattern2.matcher(timeSlotStr);
			return matcher2.matches();
		}

		log.error("timeSlot is invalid, " + timeSlotStr);

		return false;
	}

	@Override
	public Response deleteXc(String xcid, SecurityContext securityContext) throws NotFoundException {
		try {
			adpXcMgr.deleteXcById(xcid);
		} catch (AdapterException e) {
			log.error("deleteXC", e);
			return ErrorWrapperUtils.adapterException(e);
		}
		return Response.ok().build();
	}

	@Override
	public Response findXcs(String tpid, SecurityContext securityContext) throws NotFoundException {
		List<AdpXc> xcList;
		try {
			xcList = getXcsByTpId(tpid);
		} catch (AdapterException e) {
			log.error("getXcsByTpId", e);
			return ErrorWrapperUtils.adapterException(e);
		}
		return Response.ok().entity(xcList).build();
	}

	private List<AdpXc> getXcsByTpId(String tpid) throws AdapterException {
		List<AdpXc> xcList = new ArrayList<AdpXc>();
		try {
			xcList = xcsDbMgr.findXcsByTpId(tpid);
		} catch (Exception e) {
			log.error("findXcsByTpId", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
		return xcList;
	}

	@Override
	public Response getXcById(String xcid, SecurityContext securityContext) throws NotFoundException {
		AdpXc xc = new AdpXc();
		try {
			xc = xcsDbMgr.getXcById(xcid);
		} catch (Exception e) {
			log.error("getXCById", e);
		}
		return Response.ok().entity(xc).build();
	}

	@Override
	public Response deleteXcsByNeId(String neId, SecurityContext arg1) throws NotFoundException {
		try {
			adpXcMgr.deleteXcsByNeId(neId);
		} catch (AdapterException e) {
			log.error("deleteXcsByNeId", e);
			return ErrorWrapperUtils.adapterException(e);
		}
		return Response.ok().build();
	}

	@Override
	public Response getXcsByNeId(String arg0, SecurityContext arg1) throws NotFoundException {
		return null;
	}
}
