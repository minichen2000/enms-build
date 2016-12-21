package com.nsb.enms.adapter.server.wdm.factory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.db.mgr.AdpNesDbMgr;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.common.utils.ValidationUtil;
import com.nsb.enms.common.utils.snmpclient.SnmpClient;
import com.nsb.enms.restful.model.adapter.AdpNe;

public class AdpSnmpClientFactory {
	private final static Logger log = LogManager.getLogger(AdpSnmpClientFactory.class);
	private static final AdpSnmpClientFactory factory = new AdpSnmpClientFactory();
	private Map<String, SnmpClient> map = new ConcurrentHashMap<String, SnmpClient>();

	private AdpNesDbMgr mgr = new AdpNesDbMgr();

	private AdpSnmpClientFactory() {
	}

	public static AdpSnmpClientFactory getInstance() {
		return factory;
	}

	public void add(String ip, SnmpClient client) {
		if (!ValidationUtil.isValidIpAddress(ip) || null == client) {
			log.error("invalid parameter");
			return;
		}
		if (map.containsKey(ip)) {
			log.warn("ip was exsited in map, now update it's value");
		}
		map.put(ip, client);
	}

	public SnmpClient getByIp(String ip) {
		if (!ValidationUtil.isValidIpAddress(ip)) {
			return null;
		}
		return map.get(ip);
	}

	public SnmpClient getByNeId(Integer neId) throws AdapterException {
		if (null == neId || neId < 0) {
			return null;
		}
		try {
			AdpNe ne = mgr.getNeById(neId);
			return map.get(ne.getAddresses().getSnmpAddress().getSnmpAgent());
		} catch (Exception e) {
			log.error("getNeById", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
	}
}
