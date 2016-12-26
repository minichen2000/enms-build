package com.nsb.enms.adapter.server.wdm.notification;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.adapter.server.common.constants.ConfigKey;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.utils.NotifQueue;
import com.nsb.enms.adapter.server.common.utils.TimeUtil;
import com.nsb.enms.adapter.server.common.utils.INotifProcessor;
import com.nsb.enms.common.utils.Pair;
import com.nsb.enms.common.utils.snmp.DispatchTrap;
import com.nsb.enms.common.utils.snmp.SnmpTrap;
import com.nsb.enms.mib.pss.def.M_sysUpTime;
import com.nsb.enms.mib.pss.def.M_tnTrapChangedObject;
import com.nsb.enms.mib.pss.def.M_tnTrapData;
import com.nsb.enms.mib.pss.def.M_tnTrapObjectID;

public class AdpSnmpTrapHandler implements DispatchTrap {
	private static final Logger log = LogManager.getLogger(AdpSnmpTrapHandler.class);

	private static final int MAX_SYSUP_TIME = ConfLoader.getInstance().getInt(ConfigKey.MAX_SYSUP_TIME,
			ConfigKey.DEFAULT_MAX_SYSUP_TIME);

	private static final int MAX_CLEAN_TIME = ConfLoader.getInstance().getInt(ConfigKey.MAX_CLEAN_TIME,
			ConfigKey.DEFAULT_MAX_CLEAN_TIME);

	private static final long NOTIF_CLEAN_PERIOD = ConfLoader.getInstance().getInt(ConfigKey.NOTIF_CLEAN_PERIOD,
			ConfigKey.DEFAULT_NOTIF_CLEAN_PERIOD);

	private static NotifQueue<List<Pair<String, String>>> queue;

	private static AdpSnmpTrapHandler INSTANCE = new AdpSnmpTrapHandler();

	private static Map<String, Map<String, Long>> map = new ConcurrentHashMap<String, Map<String, Long>>();

	private AdpSnmpTrapHandler() {
		queue = new NotifQueue<List<Pair<String, String>>>(new INotifProcessor<List<Pair<String, String>>>() {

			@Override
			public void process(List<Pair<String, String>> trapValue) throws AdapterException {
				NotificationDispatcher.getInstance().dispatcher(trapValue);
			}
		});

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new CleanMapTask(), 0, NOTIF_CLEAN_PERIOD);
	}

	public static AdpSnmpTrapHandler getInstance() {
		return INSTANCE;
	}

	@Override
	public void onTrap(List<Pair<String, String>> trapInfo) {
		long sysUpTime = 0;
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

			if (pair.getFirst().startsWith(M_sysUpTime.oid)) {
				sysUpTime = TimeUtil.getSysUpTime(pair.getSecond());
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
			if (!map.containsKey(ip)) {
				map.put(ip, new ConcurrentHashMap<String, Long>());
				map.get(ip).put("firstTime", new Date().getTime());
			}

			String key = ip + trapObjectId + trapData + trapChangeObject;
			if (map.get(ip).containsKey(key)) {
				long oldSysUpTime = map.get(ip).get(key);
				long time = sysUpTime - oldSysUpTime;
				if (time >= MAX_SYSUP_TIME) {
					queue.push(trapInfo);
				} else {
					log.debug("duplicate notification: " + trapInfo);
				}
			} else {
				map.get(ip).put(key, sysUpTime);
				queue.push(trapInfo);
			}

		} else {
			log.debug("drop notification: " + trapInfo);
		}
	}

	public void destroyMap() {
		map = new ConcurrentHashMap<String, Map<String, Long>>();
	}

	class CleanMapTask extends TimerTask {
		@Override
		public void run() {
			for (String ip : map.keySet()) {
				long currentTime = new Date().getTime();
				long firstTime = map.get(ip).get("firstTime");
				long time = currentTime - firstTime;
				if (time  >= MAX_CLEAN_TIME) {
					map.remove(ip);
					log.debug("Clean the map with ip:" + ip);
				}
			}
		}
	}

	public static void main(String[] args) {
		SnmpTrap trap = new SnmpTrap();
		trap.setTrapCaller(new AdpSnmpTrapHandler());
		trap.run("135.251.99.37", 163);
	}
}