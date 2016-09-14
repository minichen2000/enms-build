package com.nsb.enms.restful.adapter.server.business.register;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.restful.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.restful.controller.client.ApiException;
import com.nsb.enms.restful.controller.client.api.SystemApi;
import com.nsb.enms.restful.controller.client.model.HOST;

public class RegisterManager {
	private static final Logger log = LogManager.getLogger(RegisterManager.class);
	private SystemApi systemApi = new SystemApi();

	public boolean register2Controller() {
		HOST host = constructAdapterHost();

		try {
			systemApi.registerHost(host);
		} catch (Exception e) {
			log.error("register2Controller", e);
			return false;
		}
		return true;
	}

	public void unRegister2Controller() {
		HOST host = constructAdapterHost();

		try {
			systemApi.unRegisterHost(host);
		} catch (ApiException e) {
			log.error("unRegister2Controller", e);
		}
	}

	private HOST constructAdapterHost() {
		HOST host = new HOST();
		host.setType("Adapter");
		String id = ConfLoader.getInstance().getConf("ADP_ID", "adapter_" + System.currentTimeMillis());
		host.setId(id);
		String ip = ConfLoader.getInstance().getConf("ADP_IP", "127.0.0.1");
		int port = ConfLoader.getInstance().getInt("ADP_PORT", 8080);
		host.setIpAddress(ip + ":" + port);
		host.setQ3Address(ip);
		host.setRemark("");
		return host;
	}
}
