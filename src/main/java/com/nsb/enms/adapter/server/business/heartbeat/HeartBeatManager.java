package com.nsb.enms.adapter.server.business.heartbeat;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.restful.controller.client.ApiException;
import com.nsb.enms.restful.controller.client.api.SystemApi;

public class HeartBeatManager {
	private static final Logger log = LogManager.getLogger(HeartBeatManager.class);
	private SystemApi systemApi = new SystemApi();

	public void checkHeartbeat() {
		Timer timer = new Timer();
		long time = 5 * 60 * 1000;
		timer.scheduleAtFixedRate(new Task(), time, time);
	}

	class Task extends TimerTask {

		@Override
		public void run() {
			try {
				systemApi.checkHeartbeat();
			} catch (ApiException e) {
				e.printStackTrace();
				log.error("unRegister2Controller", e);
			}
		}
	}
}
