package com.nsb.enms.adapter.server.business.register;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.restful.controllerclient.ApiException;
import com.nsb.enms.restful.controllerclient.api.CtlSystemApi;
import com.nsb.enms.restful.model.Host;

public class RegisterManager {
	private static final Logger log = LogManager.getLogger(RegisterManager.class);
	private CtlSystemApi systemApi = new CtlSystemApi();

	public boolean register2Controller() {
	Host host = constructAdapterHost();

		try {
			systemApi.registerHost(host);
		} catch (Exception e) {
			log.error("register2Controller", e);
			return false;
		}
		return true;
	}

	public void unRegister2Controller() {
		Host host = constructAdapterHost();

		try {
			systemApi.unRegisterHost(host);
		} catch (ApiException e) {
			log.error("unRegister2Controller", e);
		}
	}

	private Host constructAdapterHost() {
		Host host = new Host();
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
