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

	public void register2Controller() {
		HOST host = constructAdapterHost();

		try {
			systemApi.registerHost(host);
		} catch (ApiException e) {
			e.printStackTrace();
			log.error("register2Controller", e);
			return;
		}
	}

	public void unRegister2Controller() {
		HOST host = constructAdapterHost();

		try {
			systemApi.unRegisterHost(host);
		} catch (ApiException e) {
			e.printStackTrace();
			log.error("unRegister2Controller", e);
		}
	}

	private HOST constructAdapterHost() {
		HOST host = new HOST();
		host.setType("Adapter");
		String ip = ConfLoader.getInstance().getConf("ADP_IP", "127.0.0.1");
		int port = ConfLoader.getInstance().getInt("ADP_PORT", 8080);
		host.setIpaddress(ip);
		host.setPort(port);
		host.setRemark("");
		return host;
	}
}
