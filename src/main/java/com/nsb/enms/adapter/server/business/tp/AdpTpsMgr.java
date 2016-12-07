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
import com.nsb.enms.common.ManagedObjectType;
import com.nsb.enms.common.TpType;
import com.nsb.enms.common.utils.Pair;
import com.nsb.enms.restful.model.adapter.AdpTp;

public class AdpTpsMgr {
	private final static Logger log = LogManager.getLogger(AdpTpsMgr.class);
	private AdpTpsDbMgr tpsDbMgr = new AdpTpsDbMgr();

	public AdpTpsMgr() {
	}

	public List<AdpTp> syncTu12Ctp(String groupId, Integer neId, String vc4TtpId, AdpTp au4Ctp, Integer ptpDbId)
			throws AdapterException {
		List<AdpTp> tps = new ArrayList<AdpTp>();
		List<TpEntity> tpList = GetCtp.getTu12Ctps(groupId, String.valueOf(neId), vc4TtpId);
		log.debug("syncTu12Ctp tpList = {}, neId = {}, vc4TtpId = {}", tpList.size(), neId, vc4TtpId);

		String au4CtpUserLabel = au4Ctp.getUserLabel();
		for (TpEntity tp : tpList) {
			log.debug("syncTu12Ctp tp = " + tp);
			String tu12CtpUserLabel = au4CtpUserLabel + GenerateUserLabelUtil.generateTpUserLabel(tp);
			AdpTp ctp = constructTp(tp, neId, tu12CtpUserLabel, ManagedObjectType.TU12.getLayerRates(),
					TpType.CTP.name(), ptpDbId, au4Ctp.getId());

			tps.add(ctp);
		}

		tps = addTps(tps);

		return tps;
	}

	public List<AdpTp> syncTu3Ctp(String groupId, Integer neId, String vc4TtpId, AdpTp au4Ctp, Integer ptpDbId)
			throws AdapterException {
		List<AdpTp> tps = new ArrayList<AdpTp>();
		List<TpEntity> tpList = GetCtp.getTu3Ctps(groupId, String.valueOf(neId), vc4TtpId);
		log.debug("syncCtp tpList = {}, neId = {}, vc4TtpId = {}", tpList.size(), neId, vc4TtpId);

		String au4CtpUserLabel = au4Ctp.getUserLabel();
		for (TpEntity tp : tpList) {
			log.debug("syncTu3Ctp tp = " + tp);
			String tu3CtpUserLabel = au4CtpUserLabel + GenerateUserLabelUtil.generateTpUserLabel(tp);
			AdpTp ctp = constructTp(tp, neId, tu3CtpUserLabel, ManagedObjectType.TU3.getLayerRates(), TpType.CTP.name(),
					ptpDbId, au4Ctp.getId());

			tps.add(ctp);
		}

		tps = addTps(tps);

		return tps;
	}

	private List<AdpTp> addTps(List<AdpTp> tps) throws AdapterException {
		try {
			for (AdpTp tp : tps) {
				AdpTp newTp = tpsDbMgr.getTpById(tp.getId());
				if (null == newTp || newTp.getId() == null) {
					tpsDbMgr.addTp(tp);
				}
			}
		} catch (Exception e) {
			log.error("addTps", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
		return tps;
	}

	public List<AdpTp> syncTtp(String groupId, Integer neId, String vc4TtpId, Integer ptpDbId) throws AdapterException {
		List<AdpTp> tps = new ArrayList<AdpTp>();
		List<TpEntity> tpList = GetTtp.getVc4Ttps(groupId, String.valueOf(neId));
		log.debug("syncTtp tpList = {}, groupId = {}, neId = {}", tpList.size(), groupId, neId);

		for (TpEntity tp : tpList) {
			log.debug("syncTtp tp = " + tp);
			String moi = tp.getMoi();
			if (moi.endsWith(vc4TtpId)) {
				String userLabel = GenerateUserLabelUtil.generateTpUserLabel(tp);
				AdpTp ttp = constructTp(tp, neId, userLabel, ManagedObjectType.VC4.getLayerRates(), TpType.CTP.name(),
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
	private AdpTp constructTp(TpEntity tp, Integer neDbId, String userLabel, List<String> layerRates, String tpType,
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

			List<String> layerRates = getLayerRates(tp);
			if (null == layerRates || layerRates.isEmpty()) {
				log.error("tp's layerRate is null, ignore this tp", tp);
				continue;
			}

			AdpTp newTp = constructTp(tp, neDbId, userLabel, layerRates, TpType.PTP.name(), null, null);
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
			AdpTp newCtp = constructTp(ctp, neDbId, userLabel, ManagedObjectType.AU4.getLayerRates(), TpType.CTP.name(),
					ptpDbId, ptpDbId);
			tps.add(newCtp);
		}

		addTps(tps);
	}

	private void syncPdhCtp(String groupId, String neId, String moi, String ptpId, Integer ptpDbId, Integer neDbId)
			throws AdapterException {
		Pair<List<String>, List<TpEntity>> pair = GetCtp.getPdhCtp(groupId, neId, ptpId);
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
		List<String> layerRates = pair.getFirst();
		for (TpEntity ctp : ctpList) {
			String userLabel = GenerateUserLabelUtil.generateTpUserLabel(ctp);
			AdpTp newCtp = constructTp(ctp, neDbId, userLabel, layerRates, TpType.CTP.name(), ptpDbId, ptpDbId);
			tps.add(newCtp);
		}

		AdpTp pdhPTP = new AdpTp();
		pdhPTP.setId(ptpDbId);
		pdhPTP.setLayerRates(layerRates);
		addTps(tps);
		updateTps(pdhPTP);

		try {
			tpsDbMgr.updateTpLayerRate(ptpDbId, layerRates);
		} catch (Exception e) {
			log.error("getTpById", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
	}

	private void updateTps(AdpTp tp) throws AdapterException {
		try {
			tpsDbMgr.updateTp(tp);
		} catch (Exception e) {
			log.error("updateTps", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
	}

	private List<String> getLayerRates(TpEntity tp) {
		String moc = tp.getMoc();
		if (moc.contains("OpticalSPITTP")) {
			int stmLevel = tp.getStmLevel();
			switch (stmLevel) {
			case 1:
				return ManagedObjectType.STM1_OPTICAL.getLayerRates();
			case 4:
				return ManagedObjectType.STM4_OPTICAL.getLayerRates();
			case 16:
				return ManagedObjectType.STM16_OPTICAL.getLayerRates();
			case 64:
				return ManagedObjectType.STM64_OPTICAL.getLayerRates();
			case 256:
				return ManagedObjectType.STM256_OPTICAL.getLayerRates();
			default:
				return null;
			}
		} else if (moc.contains("pPITTP")) {
			return ManagedObjectType.STM1_ELECTRICAL.getLayerRates();
		}
		return null;
	}
}
