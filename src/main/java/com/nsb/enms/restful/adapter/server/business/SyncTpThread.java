package com.nsb.enms.restful.adapter.server.business;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.restful.adapter.server.action.entity.TpEntity;
import com.nsb.enms.restful.adapter.server.action.method.ne.StartSuppervision;
import com.nsb.enms.restful.adapter.server.action.method.tp.GetCtp;
import com.nsb.enms.restful.adapter.server.action.method.tp.GetTp;
import com.nsb.enms.restful.adapter.server.common.exception.AdapterException;
import com.nsb.enms.restful.adapter.server.common.util.GenerateUserLabelUtils;
import com.nsb.enms.restful.adapter.server.common.util.LayerRateConst;
import com.nsb.enms.restful.db.client.ApiException;
import com.nsb.enms.restful.db.client.api.NesApi;
import com.nsb.enms.restful.db.client.api.TpsApi;
import com.nsb.enms.restful.db.client.model.TP;

public class SyncTpThread extends Thread {
	private final static Logger log = LogManager.getLogger(SyncTpThread.class);
	private TpsApi tpsApi = new TpsApi();
	private int groupId, neId;
	private String id;

	public SyncTpThread(int groupId, int neId, String id) {
		this.groupId = groupId;
		this.neId = neId;
		this.id = id;
	}

	@Override
	public void run() {
		//StartSuppervision start = new StartSuppervision();
		boolean isSuccess;
		try {
			log.debug("before startSuppervision");
			isSuccess = StartSuppervision.startSuppervision(groupId, neId);
			log.debug("isSuccess = " + isSuccess);
			if (!isSuccess) {
				return;
			}
		} catch (AdapterException e) {
			e.printStackTrace();
			return;
		}

		syncTp();

		// update the value of alignmentStatus for ne to true
		updateNeAttr(id);

		log.debug("sync tp end");
	}

	private void syncTp() {
		//GetTp getTp = new GetTp();
		try {
			List<TpEntity> tpList = GetTp.getTp(groupId, neId);
			log.debug("tpList = " + tpList.size() + ", neId = " + neId);

			for (TpEntity tp : tpList) {
				List<TP> tps = new ArrayList<TP>();
				log.debug("tp = " + tp);
				TP newTp = new TP();
				newTp.setNeId(id);
				String moi = tp.getMoi();
				newTp.setAid(moi);
				String userLabel = GenerateUserLabelUtils.generateTpUserLabel( tp );
				newTp.setUserLabel(userLabel);
				newTp.setNativeName(userLabel);
				newTp.setTpType(tp.getMoc());

				// TODO 读取映射文件获取层速率
				int layerRate = getLayerRate(tp);
				newTp.setLayerRate( String.valueOf( layerRate ) );
				tps.add(newTp);

				tps = tpsApi.addTPs(tps);
				String tpId = moi.split("/")[2];
				log.debug("tpId = " + tpId);
				String ptpId = tpId.split("=")[1];
				log.debug("ptpId = " + ptpId);
				String ptpDbId = tps.get(0).getId();
				if (tpId.startsWith("opticalSPI")) {
					syncSdhCtp(moi, ptpId, ptpDbId);
				} else if (tpId.startsWith("pPI")) {
					syncPdhCtp(moi, ptpId, ptpDbId);
				} else {
					log.error("tpId is not valid:" + tpId);
					return;
				}
			}

		} catch (Exception e) {
			log.error("syncTp", e);
		}
	}

	private void syncSdhCtp(String moi, String ptpId, String ptpDbId) throws AdapterException {
		List<TpEntity> ctpList = GetCtp.getSdhCtp(groupId, neId, ptpId);
		if (null == ctpList || ctpList.isEmpty()) {
			log.error("ctpList is null or empty");
			return;
		}

		List<TP> tps = new ArrayList<TP>();
		for (TpEntity ctp : ctpList) {
			TP newCtp = new TP();
			newCtp.setNeId(id);
			String ctpMoi = ctp.getMoi();
			newCtp.setAid(ctpMoi);
			String userLabel = GenerateUserLabelUtils.generateTpUserLabel( ctp );
			newCtp.setUserLabel(userLabel);
			newCtp.setNativeName(userLabel);
			newCtp.setTpType(ctp.getMoc());
			newCtp.setParentTpId(ptpDbId);

			// TODO 读取映射文件获取层速率
			newCtp.setLayerRate(String.valueOf(LayerRateConst.LR_STS3c_and_AU4_VC4));
			tps.add(newCtp);
		}

		try {
			tpsApi.addTPs(tps);
		} catch (ApiException e) {
			log.error("syncCtp", e);
		}
	}

	private void syncPdhCtp(String moi, String ptpId, String ptpDbId) throws AdapterException {
		List<TpEntity> ctpList = GetCtp.getPdhCtp(groupId, neId, ptpId);
		if (null == ctpList || ctpList.isEmpty()) {
			log.error("ctpList is null or empty");
			return;
		}

		List<TP> tps = new ArrayList<TP>();
		for (TpEntity ctp : ctpList) {
			TP newCtp = new TP();
			newCtp.setNeId(id);
			String ctpMoi = ctp.getMoi();
			newCtp.setAid(ctpMoi);
			String userLabel = GenerateUserLabelUtils.generateTpUserLabel( ctp );
			newCtp.setUserLabel(userLabel);
			newCtp.setNativeName(userLabel);
			newCtp.setTpType(ctp.getMoc());
			newCtp.setParentTpId(ptpDbId);

			// TODO 读取映射文件获取层速率
			newCtp.setLayerRate( String.valueOf( LayerRateConst.LR_VT2_and_TU12_VC12 ) );
			tps.add(newCtp);
		}

		try {
			tpsApi.addTPs(tps);
		} catch (ApiException e) {
			log.error("syncCtp", e);
		}
	}

	/**
	 * update the value of alignmentStatus for ne to true
	 */
	private void updateNeAttr(String id) {
		NesApi nesApi = new NesApi();
		com.nsb.enms.restful.db.client.model.NE ne = new com.nsb.enms.restful.db.client.model.NE();
		ne.setId(id);
		ne.setAlignmentStatus("true");
		try {
			nesApi.updateNE(ne);
		} catch (ApiException e) {
			log.error("updateNeAttr", e);
		}
	}
	
	private int getLayerRate(TpEntity tp)
	{
	    String moc = tp.getMoc();
	    if (moc.contains( "OpticalSPITTP" ))
        {
            int stmLevel = tp.getStmLevel();
            switch( stmLevel )
            {
                case 1:
                    return LayerRateConst.LR_PHYSICAL_OPTICAL_MS_STM1;
                case 4:
                    return LayerRateConst.LR_PHYSICAL_OPTICAL_MS_STM4;
                case 16:
                    return LayerRateConst.LR_PHYSICAL_OPTICAL_MS_STM16;
                case 64:
                    return LayerRateConst.LR_PHYSICAL_OPTICAL_MS_STM64;
                case 256:
                    return LayerRateConst.LR_PHYSICAL_OPTICAL_MS_STM256;
                default:
                    return LayerRateConst.LR_UNDEFINE;
            }
        } else if (moc.contains( "pPITTP" ))
        {
            return LayerRateConst.LR_PHYSICAL_ELECTRICAL_DSR_E1_2M;
        }
	    return LayerRateConst.LR_UNDEFINE;
	}
}
