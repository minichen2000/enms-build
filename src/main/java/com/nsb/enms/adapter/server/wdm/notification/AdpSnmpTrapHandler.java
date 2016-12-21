package com.nsb.enms.adapter.server.wdm.notification;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.common.utils.snmpclient.DispatchTrap;

public class AdpSnmpTrapHandler implements DispatchTrap {
	private static final Logger log = LogManager.getLogger(AdpSnmpTrapHandler.class);

	@Override
	public void onTrap(Map<String, String> trapValue) {
		for (String key : trapValue.keySet()) {
			System.out.println("key = " + key + ", value = " + trapValue.get(key));
			log.debug("key = " + key + ", value = " + trapValue.get(key));
		}
	}
}