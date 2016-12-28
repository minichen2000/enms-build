package com.nsb.enms.adapter.server.wdm.notification;

import java.util.Map;


public abstract class DefaultNotificationHandler implements INotificationHandler {

	@Override
	public abstract void handle(Map<String, String> trap);

}
