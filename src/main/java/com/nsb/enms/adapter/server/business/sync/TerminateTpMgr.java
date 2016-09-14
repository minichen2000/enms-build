package com.nsb.enms.adapter.server.business.sync;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.action.entity.TpEntity;
import com.nsb.enms.adapter.server.action.entity.XcEntity;
import com.nsb.enms.adapter.server.action.method.tp.GetCtp;
import com.nsb.enms.adapter.server.action.method.tp.GetTtp;
import com.nsb.enms.adapter.server.action.method.tp.TerminateTug3ToTu12;
import com.nsb.enms.adapter.server.action.method.xc.CreateXc;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.util.GenerateUserLabelUtils;
import com.nsb.enms.adapter.server.common.util.LayerRateConst;
import com.nsb.enms.restful.db.client.ApiException;
import com.nsb.enms.restful.db.client.api.TpsApi;
import com.nsb.enms.restful.db.client.api.XcsApi;
import com.nsb.enms.restful.db.client.model.TP;
import com.nsb.enms.restful.db.client.model.XC;

public class TerminateTpMgr {
	private final static Logger log = LogManager.getLogger(TerminateTpMgr.class);
	private TpsApi tpsApi = new TpsApi();
	private String groupId, neId;
	private String au4CtpId;
	private String neDbId;

	public TerminateTpMgr(String au4CtpId) {
		this.au4CtpId = au4CtpId;
	}

	public List<TP> run() {
		// TODO 创建交叉前，应先检查交叉是否已经存在
		String vc4TTPId = createXcVc4();
		log.debug("vc4TTPId = {}", vc4TTPId);
		if (StringUtils.isEmpty(vc4TTPId)) {
			log.error("vc4TTPId is null or empty");
			return new ArrayList<TP>();
		}

		terminateTp(vc4TTPId);

		List<TP> tpList = syncCtp(vc4TTPId);

		log.debug("terminate tp end");

		return tpList;
	}

	private String createXcVc4() {
		try {
			TP tp = tpsApi.getTPById(au4CtpId);
			String moi = tp.getAid();
			groupId = moi.split("/")[0].replaceAll("neGroupId=", StringUtils.EMPTY);
			neId = moi.split("/")[1].replaceAll("networkElementId=", StringUtils.EMPTY);
			String pTTPId = moi.split("/")[2].replaceAll("protectedTTPId=", StringUtils.EMPTY);
			String augId = moi.split("/")[3].replaceAll("augId=", StringUtils.EMPTY);
			String au4CTPId = moi.split("/")[4].replaceAll("au4CTPId=", StringUtils.EMPTY);

			log.debug("before createXcVc4");
			XcEntity xcEntity = CreateXc.createXcVc4(groupId, neId, pTTPId, augId, au4CTPId);
			String toTp = xcEntity.getToTermination();
			String vc4TTPId = toTp.replace("vc4TTPId=", StringUtils.EMPTY);
			log.debug("createXcVc4 ok");

			neDbId = tp.getNeId();

			String vc4TtpDbId = StringUtils.EMPTY;
			List<TP> ttps = syncTtp(vc4TTPId);
			if (null == ttps || ttps.isEmpty()) {
				log.error("ttps is null or empty");
			} else {
				TP ttp = ttps.get(0);
				vc4TtpDbId = ttp.getId();
			}

			insertXc2Db(xcEntity, vc4TtpDbId);

			return vc4TTPId;
		} catch (Exception e) {
			log.error("createXcVc4 occur error", e);
			return null;
		}
	}

	private void insertXc2Db(XcEntity xcEntity, String vc4TtpDbId) throws ApiException {
		XcsApi xcsApi = new XcsApi();
		XC xc = new XC();
		xc.setAid(xcEntity.getMoi());
		xc.setImplStatus("");
		xc.setNeId(neDbId);
		xc.setUsedByConnection("0");
		// TODO 修改该值
		xc.setLayerrate("7");

		List<String> atps = new ArrayList<String>();
		atps.add(au4CtpId);
		xc.setAtps(atps);

		List<String> ztps = new ArrayList<String>();
		ztps.add(vc4TtpDbId);
		xc.setZtps(ztps);

		xcsApi.createXC(xc);
	}

	private void terminateTp(String vc4TTPId) {
		try {
			TerminateTug3ToTu12.terminateTug3ToTu12(groupId, neId, vc4TTPId);
		} catch (AdapterException e) {
			log.error("terminateTp", e);
		}
	}

	private List<TP> syncCtp(String vc4TtpId) {
		List<TP> tps = new ArrayList<TP>();
		try {
			List<TpEntity> tpList = GetCtp.getTu12Ctp(groupId, neId, vc4TtpId);
			log.debug("syncCtp tpList = {}, neId = {}, vc4TtpId = {}", tpList.size(), neId, vc4TtpId);
			TP au4Ctp = tpsApi.getTPById( au4CtpId );
			String au4CtpUserLabel = au4Ctp.getUserLabel();
			for (TpEntity tp : tpList) {
				log.debug("syncCtp tp = " + tp);
				TP ctp = new TP();
				ctp.setNeId(neDbId);
				String moi = tp.getMoi();
				ctp.setAid(moi);
				String tu12CtpUserLabel = au4CtpUserLabel + GenerateUserLabelUtils.generateTpUserLabel( tp );
				ctp.setUserLabel(tu12CtpUserLabel);
				ctp.setNativeName(tu12CtpUserLabel);
				ctp.setTpType(tp.getMoc());
				ctp.setParentTpId(au4CtpId);

				// TODO 读取映射文件获取层速率
				ctp.setLayerRate( String.valueOf( LayerRateConst.LR_VT2_and_TU12_VC12 ) );
				tps.add(ctp);
			}
			tps = tpsApi.addTPs(tps);

		} catch (Exception e) {
			log.error("syncCtp", e);
		}
		return tps;
	}

	private List<TP> syncTtp(String vc4TtpId) {
		List<TP> tps = new ArrayList<TP>();
		try {
			List<TpEntity> tpList = GetTtp.getVc4Ttp(groupId, neId);
			log.debug("syncTtp tpList = {}, groupId = {}, neId = {}", tpList.size(), groupId, neId);

			for (TpEntity tp : tpList) {
				log.debug("syncTtp tp = " + tp);
				String moi = tp.getMoi();
				if (moi.endsWith(vc4TtpId)) {
					TP ttp = new TP();
					ttp.setNeId(neDbId);
					ttp.setAid(moi);
					String userLabel = GenerateUserLabelUtils.generateTpUserLabel( tp );
					ttp.setUserLabel(userLabel);
					ttp.setNativeName(userLabel);
					ttp.setTpType(tp.getMoc());

					// TODO 读取映射文件获取层速率
					ttp.setLayerRate(String.valueOf( LayerRateConst.LR_STS3c_and_AU4_VC4 ));
					tps.add(ttp);
					break;
				}
			}
			tps = tpsApi.addTPs(tps);

		} catch (Exception e) {
			log.error("syncTtp", e);
		}
		return tps;
	}
}
