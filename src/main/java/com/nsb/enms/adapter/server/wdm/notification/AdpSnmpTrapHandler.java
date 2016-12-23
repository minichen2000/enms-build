package com.nsb.enms.adapter.server.wdm.notification;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.utils.NotifQueue;
import com.nsb.enms.adapter.server.common.utils.TimeUtil;
import com.nsb.enms.adapter.server.common.utils.INotifProcessor;
import com.nsb.enms.common.utils.Pair;
import com.nsb.enms.common.utils.snmp.DispatchTrap;
import com.nsb.enms.common.utils.snmp.SnmpTrap;
import com.nsb.enms.mib.pss.def.M_tnTrapChangedObject;
import com.nsb.enms.mib.pss.def.M_tnTrapData;
import com.nsb.enms.mib.pss.def.M_tnTrapObjectID;
import com.nsb.enms.mib.pss.def.M_tnTrapTime;

public class AdpSnmpTrapHandler implements DispatchTrap {
	private static final Logger log = LogManager.getLogger(AdpSnmpTrapHandler.class);

	private static final int MAX_TIME = 3;

	private static final long PERIOD = 10 * 1000;

	private static NotifQueue<List<Pair<String, String>>> queue;

	private static Map<String, Long> map = new HashMap<String, Long>();

	private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	public AdpSnmpTrapHandler() {
		queue = new NotifQueue<List<Pair<String, String>>>(new INotifProcessor<List<Pair<String, String>>>() {

			@Override
			public void process(List<Pair<String, String>> trapValue) throws AdapterException {
				NotificationDispatcher.getInstance().dispatcher(trapValue);
			}
		});

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new CleanMapTask(), PERIOD, PERIOD);
	}

	@Override
	public void onTrap(List<Pair<String, String>> trapInfo) {
		lock.writeLock().lock();
		try {
			long trapTimeStamp = 0;
			boolean flag = false;
			String ip = "";
			String trapObjectId = "";
			String trapData = "";
			String trapChangeObject = "";
			for (Pair<String, String> pair : trapInfo) {
				if (pair.getFirst().equals("ip")) {
					ip = pair.getSecond();
					continue;
				}

				if (pair.getFirst().startsWith(M_tnTrapTime.oid)) {
					trapTimeStamp = TimeUtil.getSysUpTime(pair.getSecond());
					continue;
				}

				if (pair.getFirst().startsWith(M_tnTrapObjectID.oid)) {
					trapObjectId = pair.getSecond();
					continue;
				}

				if (pair.getFirst().startsWith(M_tnTrapData.oid)) {
					trapData = pair.getSecond();
					continue;
				}

				if (pair.getFirst().startsWith(M_tnTrapChangedObject.oid)) {
					trapChangeObject = pair.getSecond();
					flag = true;
					continue;
				}
			}

			if (flag) {
				String key = ip + trapObjectId + trapData + trapChangeObject;
				if (map.containsKey(key)) {
					long oldTrapTimeStamp = map.get(key);
					if (trapTimeStamp - oldTrapTimeStamp > MAX_TIME) {
						map.put(key, trapTimeStamp);
						queue.push(trapInfo);
					} else {
						map.put(key, trapTimeStamp);
						log.debug("duplicate notification: " + trapInfo);
					}
				} else {
					map.put(key, trapTimeStamp);
					queue.push(trapInfo);
				}
			} else {
				System.out.println("drop notification: " + trapInfo);
				log.debug("drop notification: " + trapInfo);
			}
		} finally {
			lock.writeLock().unlock();
		}
	}

	class CleanMapTask extends TimerTask {
		@Override
		public void run() {
			lock.writeLock().lock();
			try {
				if (!map.isEmpty()) {
					map = new HashMap<String, Long>();
					System.out.println("clean the map");
					log.debug("clean the map");
				}
			} finally {
				lock.writeLock().unlock();
			}
		}
	}

	public static void main(String[] args) {
		SnmpTrap trap = new SnmpTrap();
		trap.setTrapCaller(new AdpSnmpTrapHandler());
		trap.run("135.251.99.37", 163);
	}
}