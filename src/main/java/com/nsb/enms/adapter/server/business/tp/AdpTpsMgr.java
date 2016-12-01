package com.nsb.enms.adapter.server.business.tp;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.action.entity.TpEntity;
import com.nsb.enms.adapter.server.action.method.tp.GetCtp;
import com.nsb.enms.adapter.server.action.method.tp.GetTp;
import com.nsb.enms.adapter.server.action.method.tp.GetTtp;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.utils.GenerateKeyOnNeUtil;
import com.nsb.enms.adapter.server.common.utils.GenerateUserLabelUtil;
import com.nsb.enms.adapter.server.db.mgr.AdpSeqDbMgr;
import com.nsb.enms.adapter.server.db.mgr.AdpTpsDbMgr;
import com.nsb.enms.common.EntityType;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.common.LayerRate;
import com.nsb.enms.common.TpType;
import com.nsb.enms.common.utils.Pair;
import com.nsb.enms.restful.model.adapter.AdpTp;

public class AdpTpsMgr {
	private final static Logger log = LogManager.getLogger(AdpTpsMgr.class);
	private AdpTpsDbMgr tpsDbMgr = new AdpTpsDbMgr();

	public AdpTpsMgr() {
	}

	public List<AdpTp> syncTu12Ctp(String groupId, String neId, Integer vc4TtpId, Integer au4CtpId, Integer neDbId,
			Integer ptpDbId) throws AdapterException {
		List<AdpTp> tps = new ArrayList<AdpTp>();
		List<TpEntity> tpList = GetCtp.getTu12Ctps(groupId, neId, String.valueOf(vc4TtpId));
		log.debug("syncCtp tpList = {}, neId = {}, vc4TtpId = {}", tpList.size(), neId, vc4TtpId);

		AdpTp au4Ctp = null;
		try {
			au4Ctp = tpsDbMgr.getTpById(au4CtpId);
		} catch (Exception e) {
			log.error("getTpById", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}

		String au4CtpUserLabel = au4Ctp.getUserLabel();
		for (TpEntity tp : tpList) {
			log.debug("syncCtp tp = " + tp);
			String tu12CtpUserLabel = au4CtpUserLabel + GenerateUserLabelUtil.generateTpUserLabel(tp);

			// TODO 读取映射文件获取层速率
			AdpTp ctp = constructTp(tp, neDbId, tu12CtpUserLabel, LayerRate.DSR_2M.getCode(), TpType.CTP.getCode(),
					ptpDbId, au4CtpId);

			tps.add(ctp);
		}

		tps = addTps(tps);

		return tps;
	}

	public List<AdpTp> syncTu3Ctp(String groupId, String neId, Integer vc4TtpId, Integer au4CtpId, Integer neDbId,
			Integer ptpDbId) throws AdapterException {
		List<AdpTp> tps = new ArrayList<AdpTp>();
		List<TpEntity> tpList = GetCtp.getTu3Ctps(groupId, neId, String.valueOf(vc4TtpId));
		log.debug("syncCtp tpList = {}, neId = {}, vc4TtpId = {}", tpList.size(), neId, vc4TtpId);

		AdpTp au4Ctp = null;
		try {
			au4Ctp = tpsDbMgr.getTpById(au4CtpId);
		} catch (Exception e) {
			log.error("getTpById", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}

		String au4CtpUserLabel = au4Ctp.getUserLabel();
		for (TpEntity tp : tpList) {
			log.debug("syncCtp tp = " + tp);
			String tu3CtpUserLabel = au4CtpUserLabel + GenerateUserLabelUtil.generateTpUserLabel(tp);

			// TODO 读取映射文件获取层速率
			AdpTp ctp = constructTp(tp, neDbId, tu3CtpUserLabel, LayerRate.DSR_34M.getCode(), TpType.CTP.getCode(),
					ptpDbId, au4CtpId);

			tps.add(ctp);
		}

		tps = addTps(tps);

		return tps;
	}

	private List<AdpTp> addTps(List<AdpTp> tps) throws AdapterException {
		try {
			for (AdpTp tp : tps) {
				AdpTp newTp = tpsDbMgr.getTpById(tp.getId());
				if (null == newTp || newTp.getId() < 0) {
					tpsDbMgr.addTp(tp);
				}
			}
		} catch (Exception e) {
			log.error("addTps", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
		return tps;
	}

	public List<AdpTp> syncTtp(String groupId, String neId, String vc4TtpId, Integer neDbId, Integer ptpDbId)
			throws AdapterException {
		List<AdpTp> tps = new ArrayList<AdpTp>();
		List<TpEntity> tpList = GetTtp.getVc4Ttps(groupId, neId);
		log.debug("syncTtp tpList = {}, groupId = {}, neId = {}", tpList.size(), groupId, neId);

		for (TpEntity tp : tpList) {
			log.debug("syncTtp tp = " + tp);
			String moi = tp.getMoi();
			if (moi.endsWith(vc4TtpId)) {
				String userLabel = GenerateUserLabelUtil.generateTpUserLabel(tp);

				// TODO 读取映射文件获取层速率
				AdpTp ttp = constructTp(tp, neDbId, userLabel, LayerRate.DSR_34M.getCode(), TpType.CTP.getCode(),
						ptpDbId, ptpDbId);
				tps.add(ttp);
				break;
			}
		}

		tps = addTps(tps);

		return tps;
	}

	/**
	 * 
	 * @param tp
	 * @param neDbId
	 * @param userLabel
	 * @param layerRate
	 * @param tpType
	 * @param ptpId
	 * @param parentTpId
	 * @return
	 * @throws AdapterException
	 */
	private AdpTp constructTp(TpEntity tp, Integer neDbId, String userLabel, Integer layerRate, Integer tpType,
			Integer ptpId, Integer parentTpId) throws AdapterException {
		AdpTp adpTp = new AdpTp();
		Integer maxTpId;
		try {
			maxTpId = AdpSeqDbMgr.getMaxTpId();
		} catch (Exception e) {
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
		adpTp.setId(maxTpId);
		adpTp.setNeId(neDbId);
		adpTp.setUserLabel(userLabel);
		List<Integer> layerRates = new ArrayList<Integer>();
		layerRates.add(layerRate);
		adpTp.setLayerRates(layerRates);
		String moc = tp.getMoc();
		String keyOnNe = GenerateKeyOnNeUtil.generateKeyOnNe(EntityType.TP, moc, tp.getMoi());
		adpTp.setKeyOnNe(keyOnNe);
		adpTp.setTpType(tpType);
		adpTp.setPtpID(ptpId);
		adpTp.setParentTpID(parentTpId);
		return adpTp;
	}

	public void syncTp(String groupId, String neId, Integer neDbId) throws AdapterException {
		List<TpEntity> tpList = GetTp.getTps(groupId, neId);
		log.debug("tpList = " + tpList.size() + ", neId = " + neId);

		for (TpEntity tp : tpList) {
			List<AdpTp> tps = new ArrayList<AdpTp>();
			log.debug("tp = " + tp.toString());
			String userLabel = GenerateUserLabelUtil.generateTpUserLabel(tp);

			// TODO 读取映射文件获取层速率
			LayerRate layerRate = getLayerRate(tp);
			if (null == layerRate) {
				log.error("tp's layerRate is null, ignore this tp", tp);
				continue;
			}

			AdpTp newTp = constructTp(tp, neDbId, userLabel, layerRate.getCode(), TpType.PTP.getCode(), null, null);
			tps.add(newTp);
			tps = addTps(tps);

			String moi = tp.getMoi();
			String tpId = moi.split("/")[2];
			log.debug("tpId = " + tpId);
			String ptpId = tpId.split("=")[1];
			log.debug("ptpId = " + ptpId);
			Integer ptpDbId = tps.get(0).getId();
			if (tpId.startsWith("opticalSPI")) {
				syncSdhCtp(groupId, neId, moi, ptpId, ptpDbId, neDbId);
			} else if (tpId.startsWith("pPI")) {
				syncPdhCtp(groupId, neId, moi, ptpId, ptpDbId, neDbId);
			} else {
				log.error("tpId is not valid:" + tpId);
				return;
			}
		}
		log.debug("sync tp end");
	}

	private void syncSdhCtp(String groupId, String neId, String moi, String ptpId, Integer ptpDbId, Integer neDbId)
			throws AdapterException {
		List<TpEntity> ctpList = GetCtp.getSdhCtps(groupId, neId, ptpId);

		if (null == ctpList || ctpList.isEmpty()) {
			log.error("ctpList is null or empty");
			return;
		}

		List<AdpTp> tps = new ArrayList<AdpTp>();
		for (TpEntity ctp : ctpList) {
			String userLabel = GenerateUserLabelUtil.generateTpUserLabel(ctp);

			// TODO 读取映射文件获取层速率
			AdpTp newCtp = constructTp(ctp, neDbId, userLabel, LayerRate.AU4.getCode(), TpType.CTP.getCode(), ptpDbId,
					ptpDbId);
			tps.add(newCtp);
		}

		addTps(tps);
	}

	private void syncPdhCtp(String groupId, String neId, String moi, String ptpId, Integer ptpDbId, Integer neDbId)
			throws AdapterException {
		Pair<Integer, List<TpEntity>> pair = GetCtp.getPdhCtp(groupId, neId, ptpId);
		if (null == pair) {
			log.error("pair is null");
			return;
		}
		List<TpEntity> ctpList = pair.getSecond();
		if (null == ctpList || ctpList.isEmpty()) {
			log.error("ctpList is null or empty");
			return;
		}
		List<AdpTp> tps = new ArrayList<AdpTp>();
		for (TpEntity ctp : ctpList) {
			String userLabel = GenerateUserLabelUtil.generateTpUserLabel(ctp);
			AdpTp newCtp = constructTp(ctp, neDbId, userLabel, pair.getFirst(), TpType.CTP.getCode(), ptpDbId, ptpDbId);
			tps.add(newCtp);
		}

		AdpTp pdhPTP = new AdpTp();
		pdhPTP.setId(ptpDbId);
		int layerRate = pair.getFirst();
		List<Integer> layerRates = new ArrayList<Integer>();
		layerRates.add(layerRate);
		pdhPTP.setLayerRates(layerRates);
		addTps(tps);
		updateTps(pdhPTP);

		// 读取映射文件获取层速率
		try {
			tpsDbMgr.updateTpLayerRate(ptpDbId, layerRate);
		} catch (Exception e) {
			log.error("getTpById", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
	}

	private void updateTps(AdpTp tp) throws AdapterException {
		try {
			tpsDbMgr.updateTp(tp);
		} catch (Exception e) {
			log.error("syncCtp", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
	}

	private LayerRate getLayerRate(TpEntity tp) {
		String moc = tp.getMoc();
		if (moc.contains("OpticalSPITTP")) {
			int stmLevel = tp.getStmLevel();
			switch (stmLevel) {
			case 1:
				return LayerRate.STM1;
			case 4:
				return LayerRate.STM4;
			case 16:
				return LayerRate.STM16;
			case 64:
				return LayerRate.STM64;
			case 256:
				return LayerRate.STM256;
			default:
				return null;
			}
		} else if (moc.contains("pPITTP")) {
			return LayerRate.ELECTRICAL;
		}
		return null;
	}
}
