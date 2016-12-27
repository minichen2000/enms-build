package com.nsb.enms.adapter.server.wdm.notification;

import java.util.List;

import com.nsb.enms.common.utils.Pair;

public abstract class DefaultNotificationHandler implements INotificationHandler {

	@Override
	public abstract void handle(List<Pair<String, String>> trap);

}
