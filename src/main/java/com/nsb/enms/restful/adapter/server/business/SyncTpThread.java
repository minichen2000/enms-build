package com.nsb.enms.restful.adapter.server.business;

import java.util.ArrayList;
import java.util.List;

import com.nsb.enms.restful.adapter.server.action.entity.TpEntity;
import com.nsb.enms.restful.adapter.server.action.method.ne.StartSuppervision;
import com.nsb.enms.restful.adapter.server.action.method.tp.GetTp;
import com.nsb.enms.restful.adapter.server.common.exception.AdapterException;
import com.nsb.enms.restful.db.client.ApiException;
import com.nsb.enms.restful.db.client.api.TpsApi;
import com.nsb.enms.restful.db.client.model.TP;

public class SyncTpThread extends Thread {

	private int groupId, neId;

	public SyncTpThread(int groupId, int neId) {
		this.groupId = groupId;
		this.neId = neId;
	}

	@Override
	public void run() {
		StartSuppervision start = new StartSuppervision();
		boolean isSuccess;
		try {
			System.out.println("before startSuppervision");
			isSuccess = start.startSuppervision(groupId, neId);
			System.out.println("isSuccess = " + isSuccess);
			if (!isSuccess) {
				return;
			}
		} catch (AdapterException e) {
			e.printStackTrace();
			return;
		}

		GetTp getTp = new GetTp();
		try {
			List<TpEntity> tpList = getTp.getTp(groupId, neId);
			System.out.println("tpList = " + tpList.size());
			System.out.println("neId = " + neId);
			List<TP> tps = new ArrayList<TP>();
			TpsApi tpsApi = new TpsApi();
			for (TpEntity tp : tpList) {
				System.out.println("tp = " + tp);
				TP newTp = new TP();
				newTp.setNeId(String.valueOf(neId));
				newTp.setUserLabel(tp.getUserLabel());
				tps.add(newTp);
			}
			tpsApi.addTPs(tps);
		} catch (AdapterException e) {
			e.printStackTrace();
		} catch (ApiException e) {
			e.printStackTrace();
		}
	}
}
