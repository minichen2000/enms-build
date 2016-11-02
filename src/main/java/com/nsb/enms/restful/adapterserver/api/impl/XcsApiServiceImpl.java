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

			List<String> atpTimeSlots = body.getAtpTimeslots();
			List<String> ztpTimeSlots = body.getZtpTimeslots();
			boolean isAtpEmpty = isTimeSlotsEmpty(atpTimeSlots);
			boolean isZtpEmpty = isTimeSlotsEmpty(ztpTimeSlots);

			boolean isValid = false;
			String au4CtpId, timeSlot = StringUtils.EMPTY;
			if (isAtpEmpty && isZtpEmpty) {
				log.error("atpTimeSlots and ztpTimeSlots are null or empty");
				throw new AdapterException(ErrorCode.FAIL_CREATE_XC_BY_INVALID_PARAM);
			} else if (isAtpEmpty) {
				isValid = isTimeSlotValid(ztpTimeSlots) && isAu4Ctp(ztps) && isPdhTtp(atps);
				au4CtpId = ztps.get(0);
				timeSlot = ztpTimeSlots.get(0);
			} else if (isZtpEmpty) {
				isValid = isTimeSlotValid(atpTimeSlots) && isAu4Ctp(atps) && isPdhTtp(ztps);
				au4CtpId = atps.get(0);
				timeSlot = atpTimeSlots.get(0);
			} else {
				// TODO 需要同时打散两端的tp
				isValid = isTimeSlotValid(atpTimeSlots) && isAu4Ctp(atps) && isTimeSlotValid(ztpTimeSlots)
						&& isAu4Ctp(ztps);
				au4CtpId = atps.get(0);
			}

			log.error("xxxxxx===============1=================");

			if (!isValid) {
				log.error("one of parameter that contains tp and timeSlot may be not a valid value");
				throw new AdapterException(ErrorCode.FAIL_CREATE_XC_BY_INVALID_PARAM);
			}
			log.error("xxxxxx===============2=================");
			if (isTpExisted(timeSlot)) {
				log.error("xxxxxx===============4=================");
				if (isTpUsedByXc(timeSlot)) {
					log.error("xxxxxx===============5=================");
					throw new AdapterException(ErrorCode.FAIL_CREATE_XC_BY_TP_NOT_FREE);
				}
			} else {
				boolean isVc4TtpExisted = isVc4TtpExisted(au4CtpId);
				if (!isVc4TtpExisted) {
					log.debug("start to createXcVc4 by ctpId = {}", au4CtpId);
					String vc4TTPId = adpXcMgr.createXcByAu4AndVc4(au4CtpId);
					log.debug("successed to createXcVc4 by ctpId = {}", au4CtpId);

					String tug3Id = timeSlot.split("-")[0];
					terminateTp(LayerRate.LR_TUVC12, au4CtpId, vc4TTPId, tug3Id);

				} else {
					// TODO 判断传入的时隙是否空闲
				}
			}

			log.error("xxxxxx===============3=================");

			// TODO 根据时隙传入正确的LayerRate
			// xc = createXc(LayerRate.LR_TUVC12, body.getNeId(), atpId, ztpId);
			AdpXc xc = createXc(LayerRate.LR_TUVC12, body.getNeId(), "", "");
			log.error("xxxxxx===============6=================");
			return Response.ok().entity(xc).build();
		} catch (AdapterException e) {
			return ErrorWrapperUtils.adapterException(e);
		}
	}

	// private Response invalidParam() {
	// return ErrorWrapperUtils.adapterException(new
	// AdapterException(ErrorCode.FAIL_CREATE_XC_BY_INVALID_PARAM));
	// }

	private boolean isTimeSlotsEmpty(List<String> tpTimeSlots) {
		if (null == tpTimeSlots || tpTimeSlots.isEmpty()) {
			log.error("tpTimeSlots is null or empty");
			return true;
		}

		return false;
	}

	private AdpXc createXc(LayerRate layerRate, String neDbId, String atpId, String ztpId) throws AdapterException {
		AdpXc xc = null;
		if (LayerRate.LR_TUVC3 == layerRate) {
			xc = adpXcMgr.createXcByTu3AndVc3(neDbId, atpId, ztpId);
		} else if (LayerRate.LR_TUVC12 == layerRate) {
			xc = adpXcMgr.createXcByTu12AndVc12(neDbId, atpId, ztpId);
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
		return false;
	}

	private boolean isPdhTtp(List<String> tpIds) throws AdapterException {
		String tpId = tpIds.get(0);
		try {
			AdpTp tp = tpsDbMgr.getTpById(tpId);
			if ("vc3TTPBidirectionalR1".equalsIgnoreCase(tp.getTpType())
					|| "vc12PathTraceTTPBidirectional".equalsIgnoreCase(tp.getTpType())) {
				return true;
			}
		} catch (Exception e) {
			log.error("getTpById", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
		return false;
	}

	private boolean isVc4TtpExisted(String au4CtpId) throws AdapterException {
		List<AdpXc> xcList = getXcsByTpId(au4CtpId);
		if (null == xcList || xcList.isEmpty()) {
			log.error("xcList is null or empty");
			return false;
		}

		return true;
	}

	private boolean isTpExisted(String timeSlot) throws AdapterException {
		AdpTp tp;
		try {
			tp = tpsDbMgr.getTpById(timeSlot);
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

	// TODO unimplement
	private boolean isTpUsedByXc(String timeSlot) throws AdapterException {
		String[] str = timeSlot.split("-");
		String tpId = "";
		List<AdpXc> xcList = getXcsByTpId(tpId);
		if (null == xcList || xcList.isEmpty()) {
			log.error("can not find xc by " + tpId);
			return false;
		}
		return true;
	}

	private boolean isTimeSlotValid(List<String> timeSlots) {
		String timeSlot = timeSlots.get(0);
		String regex1 = "^[1-3]-[1-7]-[1-3]$";
		Pattern pattern1 = Pattern.compile(regex1);
		Matcher matcher1 = pattern1.matcher(timeSlot);
		boolean result = matcher1.matches();

		if (!result) {
			String regex2 = "^[1-3]-1$";
			Pattern pattern2 = Pattern.compile(regex2);
			Matcher matcher2 = pattern2.matcher(timeSlot);
			result = matcher2.matches();
		}
		return result;
	}

	@Override
	public Response deleteXc(String xcid, SecurityContext securityContext) throws NotFoundException {
		try {
			xcsDbMgr.deleteXc(xcid);
		} catch (Exception e) {
			log.error("deleteXC", e);
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
	public Response deleteXcsByNeId(String arg0, SecurityContext arg1) throws NotFoundException {
		return null;
	}

	@Override
	public Response getXcsByNeId(String arg0, SecurityContext arg1) throws NotFoundException {
		return null;
	}
}
