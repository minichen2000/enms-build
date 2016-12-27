package com.nsb.enms.adapter.server.wdm.notification;

import java.util.List;

import com.nsb.enms.common.utils.Pair;

public interface INotificationHandler {
	public void handle(List<Pair<String, String>> trap);
}
