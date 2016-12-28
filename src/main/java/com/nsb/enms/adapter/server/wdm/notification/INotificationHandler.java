package com.nsb.enms.adapter.server.wdm.notification;

import java.util.Map;


public interface INotificationHandler {
	public void handle(Map<String, String> trap);
}
