package com.nsb.enms.adapter.server.business.tp;

import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.notification.NotificationSender;
import com.nsb.enms.adapter.server.statemachine.app.NeStateMachineApp;
import com.nsb.enms.common.EntityType;
import com.nsb.enms.restful.model.adapter.AdpNe.OperationalStateEnum;
import com.nsb.enms.restful.model.adapter.AdpNe.SynchStateEnum;

public class SyncTpThread implements Callable<Object> {
	private final static Logger log = LogManager.getLogger(SyncTpThread.class);

	private String groupId, neId, id;

	public SyncTpThread(String groupId, String neId, String id) {
		this.groupId = groupId;
		this.neId = neId;
		this.id = id;
	}

	@Override
	public Object call() throws Exception {
		NotificationSender.instance().sendAvcNotif(EntityType.NE, id, "operationalState", "enum",
				OperationalStateEnum.SYNCHRONIZING.name(), OperationalStateEnum.IDLE.name());
		new AdpTpsMgr().syncTp(groupId, neId, id);
		NeStateMachineApp.instance().afterSynchData(id);
		NotificationSender.instance().sendAvcNotif(EntityType.NE, id, "synchState", "enum",
				SynchStateEnum.SYNCHRONIZED.name(), SynchStateEnum.UNSYNCHRONIZED.name());
		NotificationSender.instance().sendAvcNotif(EntityType.NE, id, "operationalState", "enum",
				OperationalStateEnum.IDLE.name(), OperationalStateEnum.SYNCHRONIZING.name());

		log.debug("sync tp end");
		return null;
	}
}
