package com.nsb.enms.adapter.server.sdh.q3manager;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.alarm.AlarmNEOutOfMngt;
import com.nsb.enms.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.adapter.server.common.conf.ConfigKey;
import com.nsb.enms.adapter.server.common.db.mgr.AdpEqusDbMgr;
import com.nsb.enms.adapter.server.common.db.mgr.AdpNesDbMgr;
import com.nsb.enms.adapter.server.common.db.mgr.AdpTpsDbMgr;
import com.nsb.enms.adapter.server.common.db.mgr.AdpXcsDbMgr;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.notification.NotificationSender;
import com.nsb.enms.adapter.server.sdh.action.method.ne.DeleteNe;
import com.nsb.enms.adapter.server.statemachine.app.NeStateMachineApp;
import com.nsb.enms.adapter.server.statemachine.ne.model.NeEvent;
import com.nsb.enms.adapter.server.statemachine.ne.model.NeStateCallBack;
import com.nsb.enms.common.CommunicationState;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.restful.model.adapter.AdpNe;

public class Q3EmlImMgr {
	private static final Logger log = LogManager.getLogger(Q3EmlImMgr.class);

	private static List<Integer> neIdList = new LinkedList<Integer>();

	private static Q3EmlImMgr inst_ = new Q3EmlImMgr();

	private static final int MAX_NE_NUM = ConfLoader.getInstance().getInt(ConfigKey.MAX_NE_OF_ONE_EMLIM,
			ConfigKey.DEFAULT_MAX_NE_OF_ONE_EMLIM);

	private AdpNesDbMgr nesDbMgr = new AdpNesDbMgr();

	private ReadWriteLock rwLock = new ReentrantReadWriteLock();

	private int groupId;

	private Q3EmlImMgr() {

	}

	public static Q3EmlImMgr instance() {
		if (inst_ == null) {
			inst_ = new Q3EmlImMgr();
		}
		return inst_;
	}

	public void init(int groupId) throws AdapterException {
		this.groupId = groupId;
		try {
			neIdList = nesDbMgr.getNeIdsByGroupId(String.valueOf(groupId));
		} catch (Exception e) {
			log.error("getNeIdsByGroupId", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}

		// adapter被启动后，需要不断地连接emlim进程，仅此而已(每隔3秒连一次，十次失败，退出)
		CheckQ3EmlImApp.check(groupId);

		Timer timer = new Timer();
		long period = ConfLoader.getInstance().getInt(ConfigKey.EMLIM_MONITOR_INTERVAL,
				ConfigKey.DEFAULT_EMLIM_MONITOR_INTERVAL);
		timer.scheduleAtFixedRate(new Q3EmlImListener(groupId), period, period);
	}

	// public synchronized Pair<Integer, Integer> getGroupNeId() throws
	// AdapterException {
	// if (neIdList.size() < MAX_NE_NUM) {
	// int neId = getMaxNeIdFromDb() + 1;
	// updateMaxNeId2Db(neId);
	// neIdList.add(neId);
	// return new Pair<Integer, Integer>(groupId, neId);
	// }
	//
	// throw new AdapterException(AdapterExceptionType.EXCPT_INTERNAL_ERROR,
	// "The emlim doesn't has capacity to manager NE!!!");
	// }

	public synchronized int getGroupId(Integer neId) throws AdapterException {
		if (neIdList.size() < MAX_NE_NUM) {
			neIdList.add(neId);
			return groupId;
		}
		throw new AdapterException(ErrorCode.FAIL_NO_FREE_ADAPTER);
	}

	// private void updateMaxNeId2Db(int neId) {
	// AdpMaxNeIdMgr.updateNeIdByGroupId(String.valueOf(groupId),
	// String.valueOf(neId));
	// }
	//
	// private int getMaxNeIdFromDb() {
	// String maxNeId = StringUtils.EMPTY;
	// maxNeId = AdpMaxNeIdMgr.getNeIdByGroupId(String.valueOf(groupId));
	// if (StringUtils.isEmpty(maxNeId)) {
	// return 1;
	// }
	// return Integer.valueOf(maxNeId);
	// }

	public void removeNe(int neId) {
		rwLock.writeLock().lock();
		neIdList.remove(new Integer(neId));
		rwLock.writeLock().unlock();
	}

	public void destroy() {
		for (int i = neIdList.size() - 1; i >= 0; i--) {
			try {
				DeleteNe.deleteNe(String.valueOf(groupId), String.valueOf(neIdList.get(i)));
			} catch (AdapterException e) {
				log.error("DeleteNe", e);
			}
		}

		try {
			AdpTpsDbMgr tpsDbMgr = new AdpTpsDbMgr();
			AdpXcsDbMgr xcsDbMgr = new AdpXcsDbMgr();
			AdpEqusDbMgr equsDbMgr = new AdpEqusDbMgr();
			List<AdpNe> nes = nesDbMgr.getNesByGroupId(String.valueOf(groupId));
			for (AdpNe ne : nes) {
				xcsDbMgr.deleteXcsByNeId(ne.getId());
				tpsDbMgr.deleteTpsbyNeId(ne.getId());
				equsDbMgr.deleteEquipmentsByNeId(ne.getId());
				nesDbMgr.deleteNe(ne.getId());
			}
			// nesDbMgr.deleteNesByGroupId(groupId);
			// AdpMaxNeIdMgr.updateNeIdByGroupId(String.valueOf(groupId),
			// String.valueOf(0));
		} catch (Exception e) {
			log.error("deleteNe", e);
		}
		System.exit(0);
	}

	public void updateCommunicationState() {
		try {
			List<AdpNe> neList = nesDbMgr.getNesByGroupId(String.valueOf(groupId));
			for (AdpNe ne : neList) {
				NeStateCallBack callBack = new NeStateCallBack();
				int id = ne.getId();
				callBack.setId(id);
				if (StringUtils.equals(ne.getCommunicationState(), CommunicationState.CONNECTED.name())) {
					NeStateMachineApp.instance().getNeCommunicationStateMachine()
							.setCurrentState(CommunicationState.CONNECTED);
					NeStateMachineApp.instance().getNeCommunicationStateMachine()
							.fire(NeEvent.E_REACHABLE_2_UNREACHABLE, callBack);

					// Long eventTime = TimeUtil.getLocalTmfTime();
					// Long occureTime = eventTime;
					// NotificationSender.instance().sendAlarm(AlarmCode.ALM_NE_MISALIGNMENT,
					// AlarmType.COMMUNICATION,
					// AlarmSeverity.CRITICAL, eventTime, occureTime, null, "",
					// EntityType.NE, Integer.valueOf(id),
					// null, null,
					// AlarmCode.ALM_NE_MISALIGNMENT.getDescription());
					NotificationSender.instance().sendAlarm(new AlarmNEOutOfMngt(id));
				}
			}
		} catch (Exception e) {
			log.error("updateCommunicationState", e);
		}
	}
}