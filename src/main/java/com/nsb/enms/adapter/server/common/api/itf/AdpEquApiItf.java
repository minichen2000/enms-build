package com.nsb.enms.adapter.server.common.api.itf;

import java.util.List;

import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.restful.model.adapter.AdpEquipment;

public interface AdpEquApiItf extends AdpCommonApiItf {
	
	public AdpEquipment addEquipment(AdpEquipment equipment) throws AdapterException;
	
	public AdpEquipment getEquipmentById(Integer eqid) throws AdapterException;

	public List<AdpEquipment> getEquipmentsByNeId(Integer neid) throws AdapterException;

}
