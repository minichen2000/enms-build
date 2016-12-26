package com.nsb.enms.adapter.server.wdm.business.eq;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.db.mgr.AdpEqusDbMgr;
import com.nsb.enms.adapter.server.common.db.mgr.AdpSeqDbMgr;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.wdm.action.entity.SnmpEquEntity;
import com.nsb.enms.adapter.server.wdm.action.method.eq.GetAllEquipments;
import com.nsb.enms.adapter.server.wdm.factory.AdpSnmpClientFactory;
import com.nsb.enms.common.EquType;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.common.utils.snmp.SnmpClient;
import com.nsb.enms.restful.model.adapter.AdpEquipment;
import com.nsb.enms.restful.model.adapter.AdpKVPair;

public class AdpSnmpEqusMgr {
	private static final Logger log = LogManager.getLogger(AdpSnmpEqusMgr.class);

	private static AdpEqusDbMgr equsDbMgr = new AdpEqusDbMgr();

	public AdpSnmpEqusMgr() {

	}

	public void syncEquip(Integer neId) throws AdapterException {
		SnmpClient client = AdpSnmpClientFactory.getInstance().getByNeId(neId);
		List<SnmpEquEntity> equs = GetAllEquipments.getEquipments(client);

		// for (SnmpEquEntity equ : equs) {
		// try {
		// equ.setId(AdpSeqDbMgr.getMaxEquipmentId());
		// } catch (Exception e) {
		// throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		// }
		// }

		for (SnmpEquEntity equ : equs) {
			log.debug(equ);
			try {
				AdpEquipment eqFromDb = equsDbMgr.getEquByKeyOnNe(neId, equ.getIndex());
				if (eqFromDb == null || eqFromDb.getId() == null) {
					AdpEquipment newEqu = constructEquip(equ, equs, neId);
					equsDbMgr.addEquipment(newEqu);
				}
			} catch (Exception e) {
				log.error("addEquipment:", e);
				throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
			}
		}
	}

	private AdpEquipment constructEquip(SnmpEquEntity equ, List<SnmpEquEntity> equs, int neId) throws AdapterException {
		AdpEquipment adpEqu = new AdpEquipment();
		List<AdpKVPair> params = new ArrayList<AdpKVPair>();
		if (equ.getId() == null) {
			try {
				equ.setId(AdpSeqDbMgr.getMaxEquipmentId());
			} catch (Exception e) {
				throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
			}
		}
		adpEqu.setId(equ.getId());
		adpEqu.setNeId(neId);
		adpEqu.setPosition(equ.getIndex());
		adpEqu.setType(getType(equ.getIndex()));
		adpEqu.setExpectedType(equ.getProgrammedType());
		adpEqu.setActualType(equ.getPresentType());
		adpEqu.setKeyOnNe(equ.getIndex());

		for (SnmpEquEntity equipment : equs) {
			String parentIndex = equipment.getIndex();
			String index = equ.getIndex();
			if (index.matches(parentIndex + "/[0-9]+")) {
				if (equipment.getId() == null) {
					AdpEquipment eqFromDb;
					try {
						eqFromDb = equsDbMgr.getEquByKeyOnNe(neId, equipment.getIndex());
						if (eqFromDb == null || eqFromDb.getId() == null) {
							equipment.setId(AdpSeqDbMgr.getMaxEquipmentId());
						} else {
							equipment.setId(eqFromDb.getId());
						}
					} catch (Exception e) {
						throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
					}
				}
				AdpKVPair parentIdPair = new AdpKVPair();
				parentIdPair.setKey("parentId");
				parentIdPair.setValue(String.valueOf(equipment.getId()));
				params.add(parentIdPair);
			}
		}

		if (!StringUtils.isEmpty(equ.getPresentType()) && !StringUtils.equals("Empty", equ.getPresentType())) {
			String serialNumber = equ.getSerialNumber();
			String unitPartNumber = equ.getUnitPartNumber();
			String softwarePartNumber = equ.getSoftwarePartNumber();
			AdpKVPair serialNumberPair = new AdpKVPair();
			serialNumberPair.setKey("serialNumber");
			serialNumberPair.setValue(serialNumber);
			params.add(serialNumberPair);

			AdpKVPair unitPartNumberPair = new AdpKVPair();
			unitPartNumberPair.setKey("unitPartNumber");
			unitPartNumberPair.setValue(unitPartNumber);
			params.add(unitPartNumberPair);

			AdpKVPair softwarePartNumberPair = new AdpKVPair();
			softwarePartNumberPair.setKey("softwarePartNumber");
			softwarePartNumberPair.setValue(softwarePartNumber);
			params.add(softwarePartNumberPair);
		}

		adpEqu.setParams(params);

		return adpEqu;
	}

	private String getType(String index) {
		int len = index.split("/").length;
		return EquType.getEquType(len);
	}
}