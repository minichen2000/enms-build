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
import com.nsb.enms.adapter.server.common.utils.GenerateKeyOnNeUtil;
import com.nsb.enms.adapter.server.common.utils.GenerateUserLabelUtil;
import com.nsb.enms.adapter.server.db.mgr.AdpNesDbMgr;
import com.nsb.enms.adapter.server.db.mgr.AdpTpsDbMgr;
import com.nsb.enms.adapter.server.db.mgr.AdpXcsDbMgr;
import com.nsb.enms.common.LayerRate;
import com.nsb.enms.common.util.ObjectType;
import com.nsb.enms.restful.model.adapter.AdpNe;
import com.nsb.enms.restful.model.adapter.AdpTp;
import com.nsb.enms.restful.model.adapter.AdpXc;

public class TerminateTpMgr {
	private final static Logger log = LogManager.getLogger(TerminateTpMgr.class);
	private AdpTpsDbMgr tpsDbMgr = new AdpTpsDbMgr();
	private String groupId, neId;
	private String au4CtpId;
	private String neDbId;

	public TerminateTpMgr(String au4CtpId) {
		this.au4CtpId = au4CtpId;
	}

	public List<AdpTp> run() {
		// TODO 创建交叉前，应先检查交叉是否已经存在
		String vc4TTPId = createXcVc4();
		log.debug("vc4TTPId = {}", vc4TTPId);
		if (StringUtils.isEmpty(vc4TTPId)) {
			log.error("vc4TTPId is null or empty");
			return new ArrayList<AdpTp>();
		}

		terminateTp(vc4TTPId);

		List<AdpTp> tpList = syncCtp(vc4TTPId);

		log.debug("terminate tp end");

		return tpList;
	}

	private String createXcVc4() {
		try {
		    AdpTp tp = tpsDbMgr.getTpById(au4CtpId);
		    neDbId = tp.getNeId();
		    AdpNe ne = new AdpNesDbMgr().getNeById( neDbId );
		    String neMoi = GenerateKeyOnNeUtil.getMoi( ne.getKeyOnNe() );
			String moi = GenerateKeyOnNeUtil.getMoi(tp.getKeyOnNe());
			groupId = neMoi.split("/")[0].replaceAll("neGroupId=", StringUtils.EMPTY);
			neId = neMoi.split("/")[1].replaceAll("networkElementId=", StringUtils.EMPTY);
			String pTTPId = moi.split("/")[0].replaceAll("protectedTTPId=", StringUtils.EMPTY);
			String augId = moi.split("/")[1].replaceAll("augId=", StringUtils.EMPTY);
			String au4CTPId = moi.split("/")[2].replaceAll("au4CTPId=", StringUtils.EMPTY);

			log.debug("before createXcVc4");
			XcEntity xcEntity = CreateXc.createXcVc4(groupId, neId, pTTPId, augId, au4CTPId);
			String toTp = xcEntity.getToTermination();
			String vc4TTPId = toTp.replace("vc4TTPId=", StringUtils.EMPTY);
			log.debug("createXcVc4 ok");

			neDbId = tp.getNeId();

			String vc4TtpDbId = StringUtils.EMPTY;
			List<AdpTp> ttps = syncTtp(vc4TTPId);
			if (null == ttps || ttps.isEmpty()) {
				log.error("ttps is null or empty");
			} else {
			    AdpTp ttp = ttps.get(0);
				vc4TtpDbId = ttp.getId();
			}

			insertXc2Db(xcEntity, vc4TtpDbId);

			return vc4TTPId;
		} catch (Exception e) {
			log.error("createXcVc4 occur error", e);
			return null;
		}
	}

	private void insertXc2Db(XcEntity xcEntity, String vc4TtpDbId) throws Exception {
		AdpXcsDbMgr xcsDbMgr = new AdpXcsDbMgr();
		AdpXc xc = new AdpXc();
		xc.setAid(xcEntity.getMoi());
		xc.setImplStatus("");
		xc.setNeId(neDbId);
		// TODO 修改该值
		xc.setLayerrate("7");

		List<String> atps = new ArrayList<String>();
		atps.add(au4CtpId);
		xc.setAtps(atps);

		List<String> ztps = new ArrayList<String>();
		ztps.add(vc4TtpDbId);
		xc.setZtps(ztps);

		xcsDbMgr.createXc(xc);
	}

	private void terminateTp(String vc4TTPId) {
		try {
			TerminateTug3ToTu12.terminateTug3ToTu12(groupId, neId, vc4TTPId);
		} catch (AdapterException e) {
			log.error("terminateTp", e);
		}
	}

	private List<AdpTp> syncCtp(String vc4TtpId) {
		List<AdpTp> tps = new ArrayList<AdpTp>();
		try {
			List<TpEntity> tpList = GetCtp.getTu12Ctp(groupId, neId, vc4TtpId);
			log.debug("syncCtp tpList = {}, neId = {}, vc4TtpId = {}", tpList.size(), neId, vc4TtpId);
			AdpTp au4Ctp = tpsDbMgr.getTpById(au4CtpId);
			String au4CtpUserLabel = au4Ctp.getUserLabel();
			for (TpEntity tp : tpList) {
				log.debug("syncCtp tp = " + tp);
				AdpTp ctp = new AdpTp();
				ctp.setNeId(neDbId);
				String moi = tp.getMoi();
				String keyOnNe = GenerateKeyOnNeUtil.generateKeyOnNe( ObjectType.TP, tp.getMoc(), moi );
				ctp.setId( neDbId + ":" + keyOnNe );
				ctp.keyOnNe( keyOnNe );
				ctp.setAdminState( tp.getAdministrativeState() );
				ctp.setOperationalState( tp.getOperationalState() );
				String tu12CtpUserLabel = au4CtpUserLabel + GenerateUserLabelUtil.generateTpUserLabel(tp);
				ctp.setUserLabel(tu12CtpUserLabel);
				ctp.setNativeName(tu12CtpUserLabel);
				ctp.setTpType(tp.getMoc());
				ctp.setParentTpId(au4CtpId);

				// TODO 读取映射文件获取层速率
				List<String> layerRates = new ArrayList<String>();
				layerRates.add( String.valueOf(LayerRate.LR_DSR_2M) );
				ctp.setLayerRates(layerRates);
				tps.add(ctp);
			}
			tps = tpsDbMgr.addTps(tps);

		} catch (Exception e) {
			log.error("syncCtp", e);
		}
		return tps;
	}

	private List<AdpTp> syncTtp(String vc4TtpId) {
		List<AdpTp> tps = new ArrayList<AdpTp>();
		try {
			List<TpEntity> tpList = GetTtp.getVc4Ttp(groupId, neId);
			log.debug("syncTtp tpList = {}, groupId = {}, neId = {}", tpList.size(), groupId, neId);

			for (TpEntity tp : tpList) {
				log.debug("syncTtp tp = " + tp);
				String moi = tp.getMoi();
				if (moi.endsWith(vc4TtpId)) {
				    AdpTp ttp = new AdpTp();
					ttp.setNeId(neDbId);
					String keyOnNe = GenerateKeyOnNeUtil.generateKeyOnNe( ObjectType.TP, tp.getMoc(), moi );
					ttp.setId( neDbId + ":" + keyOnNe );
					ttp.setKeyOnNe( keyOnNe );
					ttp.setAdminState( tp.getAdministrativeState() );
					ttp.setOperationalState( tp.getOperationalState() );
					String userLabel = GenerateUserLabelUtil.generateTpUserLabel(tp);
					ttp.setUserLabel(userLabel);
					ttp.setNativeName(userLabel);
					ttp.setTpType(tp.getMoc());

					// TODO 读取映射文件获取层速率
					List<String> layerRates = new ArrayList<String>();
	                layerRates.add( String.valueOf(LayerRate.LR_VC4) );
	                ttp.setLayerRates(layerRates);
					tps.add(ttp);
					break;
				}
			}
			tps = tpsDbMgr.addTps(tps);

		} catch (Exception e) {
			log.error("syncTtp", e);
		}
		return tps;
	}
}
