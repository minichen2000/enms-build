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
import com.nsb.enms.adapter.server.db.mgr.AdpXcsDbMgr;
import com.nsb.enms.common.LayerRate;
import com.nsb.enms.restful.adapterserver.api.NotFoundException;
import com.nsb.enms.restful.adapterserver.api.XcsApiService;
import com.nsb.enms.restful.model.adapter.AdpXc;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-08-31T16:19:02.183+08:00")
public class XcsApiServiceImpl extends XcsApiService {
	private final static Logger log = LogManager.getLogger(XcsApiServiceImpl.class);
	private AdpXcsDbMgr xcsDbMgr = new AdpXcsDbMgr();
	private AdpXcsMgr adpXcMgr = new AdpXcsMgr();

	@Override
	public Response createXc(AdpXc body, SecurityContext securityContext) throws NotFoundException {
		List<String> atps = body.getAtps();
		List<String> ztps = body.getZtps();
		Response response = isTpsValid(atps, ztps);
		if (Response.Status.OK.getStatusCode() != response.getStatus()) {
			return response;
		}

		List<String> atpTimeSlots = body.getAtpTimeslots();
		List<String> ztpTimeSlots = body.getZtpTimeslots();
		if (null == atpTimeSlots || atpTimeSlots.isEmpty()) {
			log.error("atpTimeSlots is null or empty");
			return Response.serverError().entity("atpTimeSlots is null or empty").build();
		}
		if (null == ztpTimeSlots || atpTimeSlots.isEmpty()) {
			log.error("ztpTimeSlots is null or empty");
			return Response.serverError().entity("ztpTimeSlots is null or empty").build();
		}

		// 校验时隙参数是否合法
		String atpTimeSlot = atpTimeSlots.get(0);
		boolean isValid = isValidTimeSlots(atpTimeSlot);
		if (!isValid) {
			log.error("atpTimeSlot is not valid" + atpTimeSlot);
			return Response.serverError().entity("atpTimeSlot is not valid").build();
		}

		String ztpTimeSlot = ztpTimeSlots.get(0);
		isValid = isValidTimeSlots(ztpTimeSlot);
		if (!isValid) {
			log.error("ztpTimeSlot is not valid" + ztpTimeSlot);
			return Response.serverError().entity("ztpTimeSlot is not valid").build();
		}

		String atpId = atps.get(0);
		String ztpId = ztps.get(0);
		boolean isSdhTp4Atp = isSdhTp(atpId);
		boolean isSdhTp4Ztp = isSdhTp(ztpId);

		if (isTpExisted(atpTimeSlot)) {
			// TODO tp是否被占用
		} else {
			// TODO 执行打散操作
		}

		if (isTpExisted(ztpTimeSlot)) {
			// TODO tp是否被占用
		} else {
			// TODO 执行打散操作

			String au4CtpId = "";
			if (isSdhTp4Atp) {
				au4CtpId = atpId;
			} else {
				au4CtpId = ztpId;
			}

			boolean isVc4TtpExisted = false;

			try {
				isVc4TtpExisted = isVc4TtpExisted(au4CtpId);
			} catch (Exception e) {
				log.error("get Vc4Ttp from db occur error", e);
				return ErrorWrapperUtils.failDbOperation();
			}

			if (!isVc4TtpExisted) {
				log.debug("start to createXcVc4 by ctpId = {}", au4CtpId);
				// String vc4TTPId = mgr.createXcVc4();
				String vc4TTPId = StringUtils.EMPTY;
				try {
					vc4TTPId = adpXcMgr.createXcByAu4AndVc4(au4CtpId);
				} catch (AdapterException e) {
					log.error("createXcByAu4AndVc4", e);
					return ErrorWrapperUtils.adapterException(e);
				}
				log.debug("successed to createXcVc4 by ctpId = {}", au4CtpId);

				try {
					String tug3Id = atpTimeSlot.split("-")[0];
					terminateTp(LayerRate.LR_TUVC12, au4CtpId, vc4TTPId, tug3Id);
				} catch (AdapterException e) {
					log.error("terminate tp occur error", e);
					return ErrorWrapperUtils.adapterException(e);
				}

			} else {
				// TODO 判断传入的时隙是否空闲
			}
		}

		AdpXc xc;
		try {
			// TODO 根据时隙传入正确的LayerRate
			xc = createXc(LayerRate.LR_TUVC12, body.getNeId(), atpId, ztpId);
		} catch (AdapterException e) {
			return ErrorWrapperUtils.adapterException(e);
		}
		return Response.ok().entity(xc).build();
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
		TerminateTpMgr terminateMgr = new TerminateTpMgr();
		String[] neInfos = adpXcMgr.getNeInfo(au4CtpId);
		String groupId = neInfos[0];
		String neId = neInfos[1];
		String neDbId = neInfos[2];
		if (LayerRate.LR_TUVC12 == layerRate) {
			terminateMgr.terminateTug3ToTu12(groupId, neId, vc4TTPId, tug3Id);
			new AdpTpsMgr().syncTu12Ctp(groupId, neId, vc4TTPId, au4CtpId, neDbId);
		} else {
			terminateMgr.terminateTug3ToTu3(groupId, neId, vc4TTPId, tug3Id);
			new AdpTpsMgr().syncTu3Ctp(groupId, neId, vc4TTPId, au4CtpId, neDbId);
		}
	}

	// TODO 完成对sdh端口的判断
	private boolean isSdhTp(String tpId) {
		return true;
	}

	private boolean isVc4TtpExisted(String au4CtpId) throws Exception {
		AdpXcsDbMgr xcsDbMgr = new AdpXcsDbMgr();
		List<AdpXc> xcList = xcsDbMgr.findXcsByTpId(au4CtpId);
		if (null == xcList || xcList.isEmpty()) {
			log.error("xcList is null or empty");
			return false;
		}
		return true;
	}

	// TODO 构造错误码
	private Response isTpsValid(List<String> atps, List<String> ztps) {
		if (null == atps || atps.isEmpty()) {
			log.error("atps is null or empty");
			return Response.serverError().entity("atps is null or empty").build();
		}

		if (null == ztps || ztps.isEmpty()) {
			log.error("ztps is null or empty");
			return Response.serverError().entity("ztps is null or empty").build();
		}

		return Response.ok().build();
	}

	// TODO unimplement
	private boolean isTpExisted(String timeSlot) {
		return false;
	}

	private boolean isValidTimeSlots(String timeSlot) {
		String regex1 = "^[1-3]-[1-7]-[1-3]$";
		String regex2 = "^[1-3]-1$";
		Pattern pattern1 = Pattern.compile(regex1);
		Matcher matcher1 = pattern1.matcher(timeSlot);
		boolean result = matcher1.matches();

		if (!result) {
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
		List<AdpXc> xcList = new ArrayList<AdpXc>();
		try {
			xcList = xcsDbMgr.findXcsByTpId(tpid);
		} catch (Exception e) {
			log.error("findXCs", e);
		}
		return Response.ok().entity(xcList).build();
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
