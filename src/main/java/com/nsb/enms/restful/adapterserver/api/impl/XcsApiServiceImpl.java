package com.nsb.enms.restful.adapterserver.api.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.action.entity.XcEntity;
import com.nsb.enms.adapter.server.action.entity.param.XcParamBean;
import com.nsb.enms.adapter.server.action.method.xc.CreateXc;
import com.nsb.enms.adapter.server.db.mgr.TpsDbMgr;
import com.nsb.enms.adapter.server.db.mgr.XcsDbMgr;
import com.nsb.enms.restful.adapterserver.api.NotFoundException;
import com.nsb.enms.restful.adapterserver.api.XcsApiService;
import com.nsb.enms.restful.model.adapter.AdpTp;
import com.nsb.enms.restful.model.adapter.AdpXc;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-08-31T16:19:02.183+08:00")
public class XcsApiServiceImpl extends XcsApiService {
	private final static Logger log = LogManager.getLogger(XcsApiServiceImpl.class);
	private XcsDbMgr xcsDbMgr = new XcsDbMgr();
	private TpsDbMgr tpsDbMgr = new TpsDbMgr();

	@Override
	public Response createXc(AdpXc body, SecurityContext securityContext) throws NotFoundException {
		List<String> atps = body.getAtps();
		if (null == atps || atps.isEmpty()) {
			log.error("atps is null or empty");
			return Response.serverError().entity("atps is null or empty").build();
		}

		List<String> ztps = body.getZtps();
		if (null == ztps || ztps.isEmpty()) {
			log.error("ztps is null or empty");
			return Response.serverError().entity("ztps is null or empty").build();
		}

		String atpId = atps.get(0);
		log.debug("atpId = {}", atpId);
		XcParamBean atpBean = getParam(atpId);

		String ztpId = ztps.get(0);
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

		AdpXc xc = new AdpXc();
		try {
			XcEntity xcEntity = CreateXc.createXcVc12(tpBean.getGroupId(), tpBean.getNeId(), tpBean.getVc4TtpId(),
					tpBean.getTug3Id(), tpBean.getTug2Id(), tpBean.getTu12CtpId(), tpBean.getVc12TtpId());
			String neDbId = body.getNeId();
			xc = insertXc2Db(xcEntity, neDbId, atpId, ztpId);
		} catch (Exception e) {
			log.error("createXcVc12", e);
			return Response.serverError().entity(e).build();
		}
		return Response.ok().entity(xc).build();
	}

	private AdpXc insertXc2Db(XcEntity xcEntity, String neDbId, String atpDbId, String ztpDbId) throws Exception {
	    AdpXc xc = new AdpXc();
		xc.setAid(xcEntity.getMoi());
		xc.setImplStatus("");
		xc.setNeId(neDbId);
		//xc.setUsedByConnection("0");
		// TODO 修改该值
		xc.setLayerrate("5");

		List<String> atps = new ArrayList<String>();
		atps.add(atpDbId);
		xc.setAtps(atps);

		List<String> ztps = new ArrayList<String>();
		ztps.add(ztpDbId);
		xc.setZtps(ztps);

		xc = xcsDbMgr.createXc(xc);
		return xc;
	}

	private XcParamBean getParam(String tpId) {
		String groupId = StringUtils.EMPTY;
		String neId = StringUtils.EMPTY;
		String vc4TtpId = StringUtils.EMPTY;
		String tug3Id = StringUtils.EMPTY;
		String tug2Id = StringUtils.EMPTY;
		String tu12CtpId = StringUtils.EMPTY;
		String vc12TtpId = StringUtils.EMPTY;
		XcParamBean bean = new XcParamBean();
		try {
			AdpTp tp = tpsDbMgr.getTpById(tpId);
			String tpType = tp.getTpType();
			String moi = tp.getAid();
			if ("tu12CTPBidirectionalR1".equalsIgnoreCase(tpType)) {
				bean.setSdhTp(true);
				groupId = moi.split("/")[0].replaceAll("neGroupId=", StringUtils.EMPTY);
				neId = moi.split("/")[1].replaceAll("networkElementId=", StringUtils.EMPTY);
				vc4TtpId = moi.split("/")[2].replaceAll("vc4TTPId=", StringUtils.EMPTY);
				bean.setVc4TtpId(vc4TtpId);
				tug3Id = moi.split("/")[3].replaceAll("tug3Id=", StringUtils.EMPTY);
				bean.setTug3Id(tug3Id);
				tug2Id = moi.split("/")[4].replaceAll("tug2Id=", StringUtils.EMPTY);
				bean.setTug2Id(tug2Id);
				tu12CtpId = moi.split("/")[5].replaceAll("tu12CTPId=", StringUtils.EMPTY);
				bean.setTu12CtpId(tu12CtpId);
			} else if ("vc12PathTraceTTPBidirectional".equalsIgnoreCase(tpType)) {
				bean.setSdhTp(false);
				groupId = moi.split("/")[0].replaceAll("neGroupId=", StringUtils.EMPTY);
				neId = moi.split("/")[1].replaceAll("networkElementId=", StringUtils.EMPTY);
				vc12TtpId = moi.split("/")[2].replaceAll("vc12TTPId=", StringUtils.EMPTY);
				bean.setVc12TtpId(vc12TtpId);
			}
			bean.setGroupId(groupId);
			bean.setNeId(neId);
		} catch (Exception e) {
			log.error("getTPById", e);
			return null;
		}
		return bean;
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
