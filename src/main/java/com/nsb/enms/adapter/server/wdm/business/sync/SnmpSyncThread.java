package com.nsb.enms.adapter.server.wdm.business.sync;

import java.util.concurrent.Callable;

import com.nsb.enms.adapter.server.common.business.itf.ObjectIdGenerator;
import com.nsb.enms.adapter.server.wdm.business.objectIdGenerator.WdmObjectIdGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.notification.NotificationSender;
import com.nsb.enms.adapter.server.statemachine.app.NeStateMachineApp;
import com.nsb.enms.adapter.server.wdm.business.eq.AdpSnmpEqusMgr;
import com.nsb.enms.adapter.server.wdm.business.tp.AdpSnmpTpsMgr;
import com.nsb.enms.common.EntityType;
import com.nsb.enms.common.OperationState;

public class SnmpSyncThread implements Callable<Object> {
	private final static Logger log = LogManager.getLogger(SnmpSyncThread.class);

	private String neId;

	public SnmpSyncThread(String neId) {
		this.neId = neId;
	}

	@Override
	public Object call() throws Exception {
		NotificationSender.instance().sendAvcNotif(EntityType.NE, neId, "operationalState",
				OperationState.SYNCING.name(), OperationState.IDLE.name());
		ObjectIdGenerator wdmObjectIdGenerator=new WdmObjectIdGenerator();
		new AdpSnmpEqusMgr(wdmObjectIdGenerator).syncEqs(neId);
		new AdpSnmpTpsMgr(neId,wdmObjectIdGenerator).syncTps();
		NeStateMachineApp.instance().afterSynchData(neId);
		log.debug("------------ sync end -------------");
		return null;
	}
}
