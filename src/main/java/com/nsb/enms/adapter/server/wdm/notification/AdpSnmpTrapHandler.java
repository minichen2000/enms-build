package com.nsb.enms.adapter.server.wdm.notification;

import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.adapter.server.common.constants.ConfigKey;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.utils.NotifQueue;
import com.nsb.enms.adapter.server.common.utils.TimeUtil;
import com.nsb.enms.adapter.server.wdm.constants.SnmpTrapAttribute;
import com.nsb.enms.adapter.server.common.utils.INotifProcessor;
import com.nsb.enms.common.utils.snmp.DispatchTrap;
import com.nsb.enms.mib.pss.def.M_sysUpTime;

public class AdpSnmpTrapHandler implements DispatchTrap {
	private static final Logger log = LogManager.getLogger(AdpSnmpTrapHandler.class);

	private static final int MAX_SYSUP_TIME = ConfLoader.getInstance().getInt(ConfigKey.MAX_SYSUP_TIME,
			ConfigKey.DEFAULT_MAX_SYSUP_TIME);

	private static final int MAX_CLEAN_TIME = ConfLoader.getInstance().getInt(ConfigKey.MAX_CLEAN_TIME,
			ConfigKey.DEFAULT_MAX_CLEAN_TIME);

	private static final long NOTIF_CLEAN_PERIOD = ConfLoader.getInstance().getInt(ConfigKey.NOTIF_CLEAN_PERIOD,
			ConfigKey.DEFAULT_NOTIF_CLEAN_PERIOD);

	private static NotifQueue<Map<String, String>> queue;

	private static AdpSnmpTrapHandler INSTANCE = new AdpSnmpTrapHandler();

	private static Map<String, Map<String, Long>> map = new ConcurrentHashMap<String, Map<String, Long>>();

	private AdpSnmpTrapHandler() {
		queue = new NotifQueue<Map<String, String>>(new INotifProcessor<Map<String, String>>() {

			@Override
			public void process(Map<String, String> trapValue) throws AdapterException {
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
	public void onTrap(Map<String, String> trapInfo) {
		String trapChangeObject = trapInfo.get(SnmpTrapAttribute.tnTrapChangedObject);

		if (!StringUtils.isEmpty(trapChangeObject)) {
			long sysUpTime = TimeUtil.getSysUpTime(trapInfo.get(M_sysUpTime.oid + ".0"));
			String ip = trapInfo.get("ip");
			String trapObjectId = trapInfo.get(SnmpTrapAttribute.tnTrapObjectID);
			String trapData = trapInfo.get(SnmpTrapAttribute.tnTrapData);

			if (!map.containsKey(ip)) {
				map.put(ip, new ConcurrentHashMap<String, Long>());
				map.get(ip).put("firstTime", new Date().getTime());
				map.get(ip).put("firstSysUpTime", sysUpTime);
				map.get(ip).put("lastSysUpTime", sysUpTime);
			}

			String key = ip + trapObjectId + trapData + trapChangeObject;
			if (map.get(ip).containsKey(key)) {
				long oldSysUpTime = map.get(ip).get(key);
				long time = sysUpTime - oldSysUpTime;
				if (time >= MAX_SYSUP_TIME) {
					map.get(ip).put(key, sysUpTime);
					map.get(ip).put("lastSysUpTime", sysUpTime);
					queue.push(trapInfo);
				} else {
					log.debug("duplicate notification: " + trapInfo);
				}
			} else {
				map.get(ip).put(key, sysUpTime);
				map.get(ip).put("lastSysUpTime", sysUpTime);
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
				long firstSysUpTime = map.get(ip).get("firstSysUpTime");
				long lastSysUpTime = map.get(ip).get("lastSysUpTime");
				long currentSysTime = currentTime - firstTime;
				long sysUpTime = lastSysUpTime - firstSysUpTime;
				long time = currentSysTime - sysUpTime * 10;
				if (time >= MAX_CLEAN_TIME && !map.get(ip).isEmpty()) {
					map.remove(ip);
					log.debug("Clean the map with ip:" + ip);
				}
			}
		}
	}
}