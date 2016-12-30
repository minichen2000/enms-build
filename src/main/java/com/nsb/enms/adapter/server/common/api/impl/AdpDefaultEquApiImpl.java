package com.nsb.enms.adapter.server.common.api.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.api.itf.AdpEquApiItf;
import com.nsb.enms.adapter.server.common.db.mgr.AdpEqusDbMgr;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.restful.model.adapter.AdpEquipment;
import com.nsb.enms.restful.model.adapter.AdpNe;

public abstract class AdpDefaultEquApiImpl implements AdpEquApiItf {
	private Logger log = LogManager.getLogger(AdpDefaultEquApiImpl.class);
	protected AdpNe data;
	private String neId;
	private static AdpEqusDbMgr equsDbMgr = new AdpEqusDbMgr();

	public AdpDefaultEquApiImpl(AdpNe ne) {
		data = ne;
		neId = data.getId();
	}

	public AdpNe getData() {
		return data;
	}

	protected String getNeId() {
		return neId;
	}

	@Override
	public AdpEquipment getEquipmentById(String id) throws AdapterException {
		try {
			AdpEquipment equipment = equsDbMgr.getEquipmentById(getNeId(), id);
			return equipment;
		} catch (Exception e) {
			log.error("getEquipmentById");
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
	}

	@Override
	public List<AdpEquipment> getEquipmentsByNeId(String neId) throws AdapterException {
		try {
			List<AdpEquipment> equipments = equsDbMgr.getEquipmentsByNeId(neId);
			return equipments;
		} catch (Exception e) {
			log.error("getEquipmentsByNeId");
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
	}

}