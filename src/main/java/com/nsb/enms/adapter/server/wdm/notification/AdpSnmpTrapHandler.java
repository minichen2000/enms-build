package com.nsb.enms.adapter.server.wdm.notification;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.utils.INotifProcessor;
import com.nsb.enms.adapter.server.common.utils.NotifQueue;
import com.nsb.enms.common.utils.Pair;
import com.nsb.enms.common.utils.snmp.DispatchTrap;

public class AdpSnmpTrapHandler implements DispatchTrap {
	private static final Logger log = LogManager.getLogger(AdpSnmpTrapHandler.class);

	private static NotifQueue<Map<String, String>> queue;

	public AdpSnmpTrapHandler() {
		queue = new NotifQueue<Map<String, String>>(new INotifProcessor<Map<String, String>>() {

			@Override
			public void process(Map<String, String> trapValue) throws AdapterException {
				for (String key : trapValue.keySet()) {
					System.out.println("key = " + key + ", value = " + trapValue.get(key));
					log.debug("key = " + key + ", value = " + trapValue.get(key));
				}
			}
		});
	}

	@Override
	public void onTrap(List<Pair<String, String>> trapInfo) {

	}
}