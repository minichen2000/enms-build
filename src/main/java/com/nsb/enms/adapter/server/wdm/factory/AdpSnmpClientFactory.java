package com.nsb.enms.adapter.server.wdm.factory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.db.mgr.AdpNesDbMgr;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.common.utils.snmp.SnmpClient;
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

	/**
	 * 获取snmpClient
	 * 
	 * @param address
	 *            address=ip:port
	 * @param client
	 */
	public void add(String address, SnmpClient client) {
		if (StringUtils.isEmpty(address) || null == client) {
			log.error("address or client was invalid parameter");
			return;
		}
		if (map.containsKey(address)) {
			log.warn("address was exsited in map, now update it's value");
		}
		map.put(address, client);
	}

	public SnmpClient getByAddress(String address) {
		if (StringUtils.isEmpty(address)) {
			return null;
		}
		return map.get(address);
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

	public void removeByNeId(Integer neId) throws AdapterException {
		if (null == neId || neId < 0) {
			return;
		}
		try {
			AdpNe ne = mgr.getNeById(neId);
			map.remove(ne.getAddresses().getSnmpAddress().getSnmpAgent());
		} catch (Exception e) {
			log.error("getNeById", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
	}

	public void removeByAddress(String address) throws AdapterException {
		if (StringUtils.isEmpty(address)) {
			return;
		}
		map.remove(address);
	}
}
