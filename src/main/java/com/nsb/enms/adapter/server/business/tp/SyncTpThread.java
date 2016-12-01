
package com.nsb.enms.adapter.server.business.tp;

import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.notification.NotificationSender;
import com.nsb.enms.adapter.server.statemachine.app.NeStateMachineApp;
import com.nsb.enms.common.EntityType;
import com.nsb.enms.state.OperationalState;

public class SyncTpThread implements Callable<Object> {
	private final static Logger log = LogManager.getLogger(SyncTpThread.class);

	private String groupId, neId;
	private Integer id;

	public SyncTpThread(String groupId, String neId, Integer id) {
		this.groupId = groupId;
		this.neId = neId;
		this.id = id;
	}

	@Override
	public Object call() throws Exception {
		//int valueType = ValueType.ENUM.getCode();
		NotificationSender.instance().sendAvcNotif(EntityType.NE, id, "operationalState",
				OperationalState.DOING.name(), OperationalState.IDLE.name());
		new AdpTpsMgr().syncTp(groupId, neId, id);
		NeStateMachineApp.instance().afterSynchData(id);
		NotificationSender.instance().sendAvcNotif(EntityType.NE, id, "operationalState",
				OperationalState.IDLE.name(), OperationalState.DOING.name());

		log.debug("sync tp end");
		return null;
	}
}
