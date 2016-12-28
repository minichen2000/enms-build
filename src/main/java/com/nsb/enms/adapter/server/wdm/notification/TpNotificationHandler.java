package com.nsb.enms.adapter.server.wdm.notification;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class TpNotificationHandler extends DefaultNotificationHandler{

	private static final Logger log = LogManager.getLogger(TpNotificationHandler.class);

	private static TpNotificationHandler INSTANCE = new TpNotificationHandler();

	private TpNotificationHandler() {

	}

	public static TpNotificationHandler getInstance() {
		return INSTANCE;
	}

	public void handle(Map<String, String> trap) {
		for (String key : trap.keySet()) {
			System.out.println(key + " = " + trap.get(key));
			log.debug(key + " = " + trap.get(key));
		}
		System.out.println("=================================");
	}
}
