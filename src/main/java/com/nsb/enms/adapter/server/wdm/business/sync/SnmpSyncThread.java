package com.nsb.enms.adapter.server.wdm.business.sync;

import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.notification.NotificationSender;
import com.nsb.enms.adapter.server.statemachine.app.NeStateMachineApp;
import com.nsb.enms.adapter.server.wdm.business.equ.AdpSnmpEqusMgr;
import com.nsb.enms.common.EntityType;
import com.nsb.enms.common.OperationState;
import com.nsb.enms.common.utils.snmpclient.SnmpClient;

public class SnmpSyncThread implements Callable<Object> {
	private final static Logger log = LogManager.getLogger(SnmpSyncThread.class);

	private Integer neId;
	private SnmpClient client;

	public SnmpSyncThread(SnmpClient client, Integer neId) {
	    this.client = client;
		this.neId = neId;
	}

	@Override
	public Object call() throws Exception {
		NotificationSender.instance().sendAvcNotif(EntityType.NE, neId, "operationalState", OperationState.SYNCING.name(),
				OperationState.IDLE.name());
		new AdpSnmpEqusMgr().syncEquip(client, neId);
		NeStateMachineApp.instance().afterSynchData(neId);
		log.debug("------------ sync end -------------");
		return null;
	}
}
