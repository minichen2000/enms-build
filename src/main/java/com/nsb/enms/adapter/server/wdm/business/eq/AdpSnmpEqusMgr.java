package com.nsb.enms.adapter.server.wdm.business.eq;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.business.itf.ObjectIdGenerator;
import com.nsb.enms.adapter.server.common.db.mgr.AdpEqusDbMgr;
import com.nsb.enms.adapter.server.common.db.mgr.AdpNesDbMgr;
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

	private static AdpNesDbMgr nesDbMgr = new AdpNesDbMgr();

	private ObjectIdGenerator objectIdGenerator;

	public AdpSnmpEqusMgr(ObjectIdGenerator objectIdGenerator) {
		this.objectIdGenerator = objectIdGenerator;
	}

	public void syncEqs(String neId) throws AdapterException {
		SnmpClient client = AdpSnmpClientFactory.getInstance().getByNeId(neId);
		List<SnmpEquEntity> equs = GetAllEquipments.getEquipments(client);

		for (SnmpEquEntity equ : equs) {
			log.debug(equ);
			try {
				AdpEquipment eqFromDb = equsDbMgr.getEquipmentByKeyOnNe(neId, equ.getIndex());
				AdpEquipment newEqu = constructEqu(equ, neId);
				if (eqFromDb == null || StringUtils.isEmpty(eqFromDb.getId())) {
					String id = objectIdGenerator.generateEquipmentId(newEqu);
					newEqu.setId(id);
					equsDbMgr.addEquipment(newEqu);
				} else {
					newEqu.setId(eqFromDb.getId());
					equsDbMgr.replaceEquipment(newEqu);
				}
			} catch (Exception e) {
				log.error("syncEq:", e);
				throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
			}
		}
	}

	public AdpEquipment addEquipment(String address, String index, EquType type) throws AdapterException {
		try {
			String neId = nesDbMgr.getIdByAddress(address);
			AdpEquipment eqFromDb = checkIsEqExistedByNeId(neId, index);
			if (eqFromDb == null || StringUtils.isEmpty(eqFromDb.getId())) {
				SnmpClient client = AdpSnmpClientFactory.getInstance().getByNeId(neId);
				SnmpEquEntity equ = GetAllEquipments.getEquipment(client, index, type);
				AdpEquipment newEqu = constructEqu(equ, neId);
				String id = objectIdGenerator.generateEquipmentId(newEqu);
				newEqu.setId(id);
				equsDbMgr.addEquipment(newEqu);
				return newEqu;
			}
		} catch (Exception e) {
			log.error("getEquipment:", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
		return null;
	}

	public void updateEquipment(AdpEquipment adpEq) throws AdapterException {
		try {
			equsDbMgr.updateEquipment(adpEq);
		} catch (Exception e) {
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
	}

	public void deleteEquipmentsUnderShelf(AdpEquipment adpEq) throws AdapterException {
		try {
			equsDbMgr.deleteEquipmentsUnderShelf(adpEq);
		} catch (Exception e) {
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
	}

	public AdpEquipment checkIsEqExistedByNeId(String neId, String index) throws AdapterException {
		try {
			AdpEquipment eqFromDb = equsDbMgr.getEquipmentByKeyOnNe(neId, index);
			if (eqFromDb == null || StringUtils.isEmpty(eqFromDb.getId())) {
				return null;
			}
			return eqFromDb;
		} catch (Exception e) {
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}

	}

	public AdpEquipment checkIsEqExistedByAddress(String address, String index) throws AdapterException {
		try {
			String neId = nesDbMgr.getIdByAddress(address);
			return checkIsEqExistedByNeId(neId, index);
		} catch (Exception e) {
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
	}

	private AdpEquipment constructEqu(SnmpEquEntity equ, String neId) throws AdapterException {
		AdpEquipment adpEqu = new AdpEquipment();
		List<AdpKVPair> params = new ArrayList<AdpKVPair>();
		String index = equ.getIndex();
		String name = equ.getName();
		adpEqu.setNeId(neId);
		adpEqu.setPosition(getPosition(index));
		String type = getType(index);
		adpEqu.setType(type);
		adpEqu.setExpectedType(equ.getProgrammedType());
		adpEqu.setActualType(equ.getPresentType());
		adpEqu.setKeyOnNe(equ.getIndex());
		adpEqu.setUserLabel(name);
		adpEqu.setNativeName(name);

		if (!StringUtils.isEmpty(equ.getPresentType()) && !StringUtils.equals("Empty", equ.getPresentType())) {

			if (StringUtils.equals(EquType.shelf.name(), type)) {
				constructShelfParams(params, equ);
			} else if (StringUtils.equals(EquType.slot.name(), type)) {
				constructSlotCardParams(params, equ);
			}
		}

		if (index.split(".").length > 1) {
			String parentIndex = index.substring(0, index.lastIndexOf("."));
			try {
				String parentId = equsDbMgr.getIdByKeyOnNe(neId, parentIndex);
				if (!StringUtils.isEmpty(parentId)) {
					AdpKVPair parentIdPair = new AdpKVPair();
					parentIdPair.setKey("parentId");
					parentIdPair.setKey(parentId);
					params.add(parentIdPair);
				}
			} catch (Exception e) {
				throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
			}
		}
		adpEqu.setParams(params);
		return adpEqu;
	}

	private void constructShelfParams(List<AdpKVPair> params, SnmpEquEntity equ) {
		String companyID = equ.getCompanyID();
		String mnemonic = equ.getMnemonic();
		String clei = equ.getClei();
		String manufacturingPartNumber = equ.getManufacturingPartNumber();
		String swPartNum = equ.getSWPartNum();
		String factoryID = equ.getFactoryID();
		String serialNumber = equ.getSerialNumber();
		String date = equ.getDate();
		String extraData = equ.getExtraData();

		AdpKVPair companyIDPair = new AdpKVPair();
		companyIDPair.setKey("riCompanyID");
		companyIDPair.setValue(companyID);
		params.add(companyIDPair);

		AdpKVPair mnemonicPair = new AdpKVPair();
		mnemonicPair.setKey("riMnemonic");
		mnemonicPair.setValue(mnemonic);
		params.add(mnemonicPair);

		AdpKVPair cleiPair = new AdpKVPair();
		cleiPair.setKey("riCLEI");
		cleiPair.setValue(clei);
		params.add(cleiPair);

		AdpKVPair unitPartNumberPair = new AdpKVPair();
		unitPartNumberPair.setKey("riManufacturingPartNumber");
		unitPartNumberPair.setValue(manufacturingPartNumber);
		params.add(unitPartNumberPair);

		AdpKVPair softwarePartNumberPair = new AdpKVPair();
		softwarePartNumberPair.setKey("riSWPartNum");
		softwarePartNumberPair.setValue(swPartNum);
		params.add(softwarePartNumberPair);

		AdpKVPair factoryIDPair = new AdpKVPair();
		factoryIDPair.setKey("riFactoryID");
		factoryIDPair.setValue(factoryID);
		params.add(factoryIDPair);

		AdpKVPair serialNumberPair = new AdpKVPair();
		serialNumberPair.setKey("riSerialNumber");
		serialNumberPair.setValue(serialNumber);
		params.add(serialNumberPair);

		AdpKVPair datePair = new AdpKVPair();
		datePair.setKey("riDate");
		datePair.setValue(date);
		params.add(datePair);

		AdpKVPair extraDataPair = new AdpKVPair();
		extraDataPair.setKey("riExtraData");
		extraDataPair.setValue(extraData);
		params.add(extraDataPair);
	}

	private void constructSlotCardParams(List<AdpKVPair> params, SnmpEquEntity equ) {
		String companyID = equ.getCompanyID();
		String mnemonic = equ.getMnemonic();
		String clei = equ.getClei();
		String manufacturingPartNumber = equ.getManufacturingPartNumber();
		String swPartNum = equ.getSWPartNum();
		String factoryID = equ.getFactoryID();
		String serialNumber = equ.getSerialNumber();
		String date = equ.getDate();
		String extraData = equ.getExtraData();
		String hfd = equ.getHfd();
		String marketingPartNumber = equ.getMarketingPartNumber();

		AdpKVPair cleiPair = new AdpKVPair();
		cleiPair.setKey("cardCLEI");
		cleiPair.setValue(clei);
		params.add(cleiPair);

		AdpKVPair hfdPair = new AdpKVPair();
		hfdPair.setKey("cardHFD");
		hfdPair.setValue(hfd);
		params.add(hfdPair);

		AdpKVPair serialNumberPair = new AdpKVPair();
		serialNumberPair.setKey("cardSerialNumber");
		serialNumberPair.setValue(serialNumber);
		params.add(serialNumberPair);

		AdpKVPair manufacturingPartNumberPair = new AdpKVPair();
		manufacturingPartNumberPair.setKey("cardManufacturingPartNumber");
		manufacturingPartNumberPair.setValue(manufacturingPartNumber);
		params.add(manufacturingPartNumberPair);

		AdpKVPair marketingPartNumberPair = new AdpKVPair();
		marketingPartNumberPair.setKey("cardMarketingPartNumber");
		marketingPartNumberPair.setValue(marketingPartNumber);
		params.add(marketingPartNumberPair);

		AdpKVPair companyIDPair = new AdpKVPair();
		companyIDPair.setKey("cardCompanyID");
		companyIDPair.setValue(companyID);
		params.add(companyIDPair);

		AdpKVPair mnemonicPair = new AdpKVPair();
		mnemonicPair.setKey("cardMnemonic");
		mnemonicPair.setValue(mnemonic);
		params.add(mnemonicPair);

		AdpKVPair swPartNumberPair = new AdpKVPair();
		swPartNumberPair.setKey("cardSWPartNum");
		swPartNumberPair.setValue(swPartNum);
		params.add(swPartNumberPair);

		AdpKVPair datePair = new AdpKVPair();
		datePair.setKey("cardDate");
		datePair.setValue(date);
		params.add(datePair);

		AdpKVPair extraDataPair = new AdpKVPair();
		extraDataPair.setKey("cardExtraData");
		extraDataPair.setValue(extraData);
		params.add(extraDataPair);

		AdpKVPair factoryIDPair = new AdpKVPair();
		factoryIDPair.setKey("cardFactoryID");
		factoryIDPair.setValue(factoryID);
		params.add(factoryIDPair);
	}

	private String getType(String index) {
		int len = index.split("\\.").length + 1;
		return EquType.getEquType(len);
	}

	private String getPosition(String index) {
		String position = index.replace(".", "/");
		return position;
	}
}