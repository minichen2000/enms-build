package com.nsb.enms.adapter.server.business.sync;

import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.business.eq.AdpEqusMgr;
import com.nsb.enms.adapter.server.business.tp.AdpTpsMgr;
import com.nsb.enms.adapter.server.notification.NotificationSender;
import com.nsb.enms.adapter.server.statemachine.app.NeStateMachineApp;
import com.nsb.enms.common.EntityType;
import com.nsb.enms.common.ValueType;
import com.nsb.enms.restful.model.adapter.AdpNe.OperationalStateEnum;
import com.nsb.enms.restful.model.adapter.AdpNe.SynchStateEnum;

public class SyncThread implements Callable<Object> {
	private final static Logger log = LogManager.getLogger(SyncThread.class);

	private String groupId, neId, id;

	public SyncThread(String groupId, String neId, String id) {
		this.groupId = groupId;
		this.neId = neId;
		this.id = id;
	}

	@Override
	public Object call() throws Exception {
		int valueType = ValueType.ENUM.getCode();
		Integer int_id = Integer.valueOf(id);
		NotificationSender.instance().sendAvcNotif(EntityType.NE, int_id, "operationalState", valueType,
				OperationalStateEnum.SYNCHRONIZING.name(), OperationalStateEnum.IDLE.name());
		new AdpTpsMgr().syncTp(groupId, neId, id);
		new AdpEqusMgr().syncEquip(groupId, neId, id);
		NeStateMachineApp.instance().afterSynchData(id);
		NotificationSender.instance().sendAvcNotif(EntityType.NE, int_id, "synchState", valueType,
				SynchStateEnum.SYNCHRONIZED.name(), SynchStateEnum.UNSYNCHRONIZED.name());
		NotificationSender.instance().sendAvcNotif(EntityType.NE, int_id, "operationalState", valueType,
				OperationalStateEnum.IDLE.name(), OperationalStateEnum.SYNCHRONIZING.name());

		log.debug("------------ sync end -------------");
		return null;
	}
}
