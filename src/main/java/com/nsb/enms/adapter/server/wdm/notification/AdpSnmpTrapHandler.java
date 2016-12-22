package com.nsb.enms.adapter.server.wdm.notification;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.utils.NotifQueue;
import com.nsb.enms.adapter.server.common.utils.TimeUtil;
import com.nsb.enms.adapter.server.common.utils.INotifProcessor;
import com.nsb.enms.common.utils.Pair;
import com.nsb.enms.common.utils.snmp.DispatchTrap;

public class AdpSnmpTrapHandler implements DispatchTrap {
	private static final Logger log = LogManager.getLogger(AdpSnmpTrapHandler.class);

	private static final int MAX_TIME = 3;
	
	private static NotifQueue<List<Pair<String, String>>> queue;

	private static Map<String, Long> map = new HashMap<String, Long>();

	public AdpSnmpTrapHandler() {
		queue = new NotifQueue<List<Pair<String, String>>>(new INotifProcessor<List<Pair<String, String>>>() {

			@Override
			public void process(List<Pair<String, String>> trapValue) throws AdapterException {
				for (Pair<String, String> pair : trapValue) {
					System.out.println(pair.getFirst() + " = " + pair.getSecond());
					log.debug(pair.getFirst() + " = " + pair.getSecond());
				}
			}
		});
	}

	@Override
	public void onTrap(List<Pair<String, String>> trapInfo) {
		long timeStamp = 0;
		boolean flag = false;
		String ip = "";
		String objectId = "";
		String data = "";
		String changeObject = "";
		for (Pair<String, String> pair : trapInfo) {
			if (pair.getFirst().startsWith("1.3.6.1.4.1.7483.2.1.2.2.1.1.1.3")) {
				timeStamp = TimeUtil.getSysUpTime(pair.getSecond());
				continue;
			}

			if (pair.getFirst().startsWith("1.3.6.1.4.1.7483.2.1.2.2.1.1.1.5")) {
				objectId = pair.getSecond();
				continue;
			}

			if (pair.getFirst().startsWith("1.3.6.1.4.1.7483.2.1.2.2.1.1.1.9")) {
				data = pair.getSecond();
				continue;
			}

			if (pair.getFirst().startsWith("1.3.6.1.4.1.7483.2.1.2.2.1.1.1.8")) {
				changeObject = pair.getSecond();
				flag = true;
			}
		}

		if (flag) {
			String key = ip + objectId + data + changeObject;
			if (map.containsKey(key)) {
				long oldTimeStamp = map.get(key);
				if (timeStamp - oldTimeStamp > MAX_TIME) {
					queue.push(trapInfo);
				} 
				map.put(key, timeStamp);
			} else {
				map.put(key, timeStamp);
				queue.push(trapInfo);
			}
		}
		log.debug("drop notification: " + trapInfo);
	}
}