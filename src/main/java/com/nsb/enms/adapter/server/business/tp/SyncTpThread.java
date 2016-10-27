package com.nsb.enms.adapter.server.business.tp;

import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.business.tp.AdpTpsMgr;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.utils.ErrorWrapperUtils;
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
		// StartSuppervision start = new StartSuppervision();
		// boolean isSuccess;
		// try
		// {
		log.debug("before startSuppervision");
		// NeStateMachineApp.instance().beforeSuperviseNe(id);
		NotificationSender.instance().sendAvcNotif(EntityType.NE, id, "operationalState", "enum",
				OperationalStateEnum.SYNCHRONIZING.name(), OperationalStateEnum.IDLE.name());
		// isSuccess = StartSuppervision.startSuppervision( groupId, neId );
		// log.debug( "isSuccess = " + isSuccess );
		// if( !isSuccess )
		// {
		// return;
		// }
		// NeStateMachineApp.instance().afterSuperviseNe(id);
		// }
		// catch( AdapterException e )
		// {
		// e.printStackTrace();
		// return;
		// }

		// NeStateMachineApp.instance().beforeSynchData(id);
		// try {
		new AdpTpsMgr().syncTp(groupId, neId, id);
		// } catch (AdapterException e) {
		// log.error("sync tp occur error", e);
		// ErrorWrapperUtils.adapterException(e);
		// }
		// NeStateMachineApp.instance().afterSynchData(id);

		// update the value of alignmentStatus for ne to true
		/*
		 * AdpNe ne = new AdpNe(); ne.setId( id ); ne.setSynchState(
		 * SynchStateEnum.SYNCHRONIZED ); ne.setOperationalState(
		 * OperationalStateEnum.IDLE ); updateNeAttr( ne );
		 */
		NeStateMachineApp.instance().afterSynchData(id);
		NotificationSender.instance().sendAvcNotif(EntityType.NE, id, "synchState", "enum",
				SynchStateEnum.SYNCHRONIZED.name(), SynchStateEnum.UNSYNCHRONIZED.name());
		NotificationSender.instance().sendAvcNotif(EntityType.NE, id, "operationalState", "enum",
				OperationalStateEnum.IDLE.name(), OperationalStateEnum.SYNCHRONIZING.name());

		log.debug("sync tp end");
		return null;
	}

	/**
	 * update the value of alignmentStatus for ne to true
	 */

	/*
	 * private void updateNeAttr( AdpNe ne ) { AdpNesDbMgr nesDbMgr = new
	 * AdpNesDbMgr(); try { nesDbMgr.updateNe( ne ); } catch( Exception e ) {
	 * log.error( "updateNeAttr", e ); } }
	 */
}
