package com.nsb.enms.restful.adapter.server.business;

import java.util.List;

import com.nsb.enms.restful.adapter.server.action.entity.TpEntity;
import com.nsb.enms.restful.adapter.server.action.method.ne.StartSuppervision;
import com.nsb.enms.restful.adapter.server.action.method.tp.GetTp;
import com.nsb.enms.restful.adapter.server.common.exception.AdapterException;

public class SyncTpThread extends Thread {

	private String groupId, neId;

	public SyncTpThread(String groupId, String neId) {
		this.groupId = groupId;
		this.neId = neId;
	}

	@Override
	public void run() {
		StartSuppervision start = new StartSuppervision();
		boolean isSuccess;
		try {
			isSuccess = start.startSuppervision(groupId, neId);
			if (isSuccess) {
				GetTp getTp = new GetTp();
				try {
					List<TpEntity> tpList = getTp.getTp(groupId, neId);
					
					// TODO insert to db
				} catch (AdapterException e) {
					e.printStackTrace();
				}
			}
		} catch (AdapterException e1) {
			e1.printStackTrace();
		}
	}
}
