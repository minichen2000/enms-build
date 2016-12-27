package com.nsb.enms.adapter.server.wdm.notification;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.common.utils.Pair;

public class EqNotificationHandler extends DefaultNotificationHandler{

	private static final Logger log = LogManager.getLogger(EqNotificationHandler.class);

	private static EqNotificationHandler INSTANCE = new EqNotificationHandler();

	private EqNotificationHandler() {

	}

	public static EqNotificationHandler getInstance() {
		return INSTANCE;
	}

	@Override
	public void handle(List<Pair<String, String>> trap) {
		for (Pair<String, String> pair : trap) {
			System.out.println(pair.getFirst() + " = " + pair.getSecond());
			log.debug(pair.getFirst() + " = " + pair.getSecond());
		}
		System.out.println("=================================");
	}
}