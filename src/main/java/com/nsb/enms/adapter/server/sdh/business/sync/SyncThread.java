package com.nsb.enms.adapter.server.sdh.business.sync;

import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.notification.NotificationSender;
import com.nsb.enms.adapter.server.sdh.business.eq.AdpEqusMgr;
import com.nsb.enms.adapter.server.sdh.business.tp.AdpTpsMgr;
import com.nsb.enms.adapter.server.statemachine.app.NeStateMachineApp;
import com.nsb.enms.common.EntityType;
import com.nsb.enms.common.OperationState;

public class SyncThread implements Callable<Object> {
	private final static Logger log = LogManager.getLogger(SyncThread.class);

	private String groupId, neId;
	private Integer id;

	public SyncThread(String groupId, String neId, Integer id) {
		this.groupId = groupId;
		this.neId = neId;
		this.id = id;
	}

	@Override
	public Object call() throws Exception {
		NotificationSender.instance().sendAvcNotif(EntityType.NE, id, "operationalState", OperationState.SYNCING.name(),
				OperationState.IDLE.name());
		new AdpTpsMgr().syncTp(groupId, neId, id);
		new AdpEqusMgr().syncEquip(groupId, neId, id);
		NeStateMachineApp.instance().afterSynchData(id);
		// NotificationSender.instance().sendAvcNotif(EntityType.NE, id,
		// "alignmentState", AlignmentState.ALIGNED.name(),
		// AlignmentState.MISALIGNED.name());
		// NotificationSender.instance().sendAvcNotif(EntityType.NE, id,
		// "operationalState", OperationState.IDLE.name(),
		// OperationState.SYNCING.name());

		log.debug("------------ sync end -------------");
		return null;
	}
}
