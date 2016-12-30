package com.nsb.enms.adapter.server.wdm.business.eq;

import java.util.ArrayList;
import java.util.List;

import com.nsb.enms.adapter.server.common.business.itf.ObjectIdGenerator;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.db.mgr.AdpEqusDbMgr;
import com.nsb.enms.adapter.server.common.db.mgr.AdpNesDbMgr;
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

	private static AdpNesDbMgr nesDbMgr = new AdpNesDbMgr();

	private ObjectIdGenerator objectIdGenerator;

	public AdpSnmpEqusMgr(ObjectIdGenerator objectIdGenerator) {
		this.objectIdGenerator=objectIdGenerator;
	}

	public void syncEqs(Integer neId) throws AdapterException {
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
					AdpEquipment newEqu = constructEqu(equ, equs, neId);
					equsDbMgr.addEquipment(newEqu);
				}
			} catch (Exception e) {
				log.error("syncEq:", e);
				throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
			}
		}
	}

	public AdpEquipment getEquipment(String address, String index, EquType type) throws AdapterException {
		try {
			Integer neId = nesDbMgr.getIdByAddress(address);
			AdpEquipment eqFromDb = isEqExist(neId, getPosition(index));
			if (eqFromDb == null) {
				SnmpClient client = AdpSnmpClientFactory.getInstance().getByNeId(neId);
				SnmpEquEntity equ = GetAllEquipments.getEquipment(client, index, type);
				AdpEquipment newEqu = constructEqu(equ, neId);
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

	public void deleteEquipment(AdpEquipment adpEq) throws AdapterException {
		try {
			equsDbMgr.deleteEquipment(adpEq);
		} catch (Exception e) {
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
	}

	private AdpEquipment isEqExist(Integer neId, String position) throws AdapterException {
		try {
			AdpEquipment eqFromDb = equsDbMgr.getEquByKeyOnNe(neId, position);
			if (eqFromDb == null || eqFromDb.getId() == null) {
				return null;
			}
			return eqFromDb;
		} catch (Exception e) {
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}

	}

	public AdpEquipment isEqExist(String address, String index) throws AdapterException {
		try {
			Integer neId = nesDbMgr.getIdByAddress(address);
			if (index.length() < 2 || index.contains(".")) {
				index = getPosition(index);
			}
			return isEqExist(neId, index);
		} catch (Exception e) {
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
	}

	private AdpEquipment constructEqu(SnmpEquEntity equ, List<SnmpEquEntity> equs, int neId) throws AdapterException {
		AdpEquipment adpEqu = new AdpEquipment();
		List<AdpKVPair> params = new ArrayList<AdpKVPair>();
		if (equ.getId() == null) {
			try {
				equ.setId(AdpSeqDbMgr.getMaxEquipmentId());
			} catch (Exception e) {
				throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
			}
		}

		constructEq(equ, adpEqu, params, neId);

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

		adpEqu.setParams(params);
		return adpEqu;
	}

	private AdpEquipment constructEqu(SnmpEquEntity equ, Integer neId) throws AdapterException {
		AdpEquipment adpEqu = new AdpEquipment();
		List<AdpKVPair> params = new ArrayList<AdpKVPair>();
		if (equ.getId() == null) {
			try {
				equ.setId(AdpSeqDbMgr.getMaxEquipmentId());
			} catch (Exception e) {
				throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
			}
		}
		constructEq(equ, adpEqu, params, neId);
		try {
			String index = equ.getIndex();
			String parentIndex = index.substring(0, index.lastIndexOf("/"));
			Integer parentId = equsDbMgr.getIdByKeyOnNe(neId, parentIndex);
			if (parentId != null) {
				AdpKVPair parentIdPair = new AdpKVPair();
				parentIdPair.setKey("parentId");
				parentIdPair.setValue(String.valueOf(parentId));
				params.add(parentIdPair);
			}
		} catch (Exception e) {
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}

		adpEqu.setParams(params);
		return adpEqu;
	}

	private void constructEq(SnmpEquEntity equ, AdpEquipment adpEqu, List<AdpKVPair> params, Integer neId) {
		adpEqu.setId(equ.getId());
		adpEqu.setNeId(neId);
		adpEqu.setPosition(equ.getIndex());
		adpEqu.setType(getType(equ.getIndex()));
		adpEqu.setExpectedType(equ.getProgrammedType());
		adpEqu.setActualType(equ.getPresentType());
		adpEqu.setKeyOnNe(equ.getIndex());

		if (!StringUtils.isEmpty(equ.getPresentType()) && !StringUtils.equals("Empty", equ.getPresentType())) {
			String serialNumber = equ.getSerialNumber();
			String unitPartNumber = equ.getUnitPartNumber();
			String softwarePartNumber = equ.getSoftwarePartNumber();
			String clei = equ.getClei();
			String hfd = equ.getHfd();
			String marketingPartNumber = equ.getMarketingPartNumber();
			String companyID = equ.getCompanyID();
			String mnemonic = equ.getMnemonic();
			String date = equ.getDate();
			String extraData = equ.getExtraData();
			String factoryID = equ.getFactoryID();

			AdpKVPair cleiPair = new AdpKVPair();
			cleiPair.setKey("CLEI");
			cleiPair.setValue(clei);
			params.add(cleiPair);

			AdpKVPair hfdPair = new AdpKVPair();
			hfdPair.setKey("HFD");
			hfdPair.setValue(hfd);
			params.add(hfdPair);

			AdpKVPair marketingPartNumberPair = new AdpKVPair();
			marketingPartNumberPair.setKey("MarketingPartNumber");
			marketingPartNumberPair.setValue(marketingPartNumber);
			params.add(marketingPartNumberPair);

			AdpKVPair companyIDPair = new AdpKVPair();
			companyIDPair.setKey("CompanyID");
			companyIDPair.setValue(companyID);
			params.add(companyIDPair);

			AdpKVPair serialNumberPair = new AdpKVPair();
			serialNumberPair.setKey("serialNumber");
			serialNumberPair.setValue(serialNumber);
			params.add(serialNumberPair);

			AdpKVPair mnemonicPair = new AdpKVPair();
			mnemonicPair.setKey("Mnemonic");
			mnemonicPair.setValue(mnemonic);
			params.add(mnemonicPair);

			AdpKVPair datePair = new AdpKVPair();
			datePair.setKey("Date");
			datePair.setValue(date);
			params.add(datePair);

			AdpKVPair extraDataPair = new AdpKVPair();
			extraDataPair.setKey("ExtraData");
			extraDataPair.setValue(extraData);
			params.add(extraDataPair);

			AdpKVPair factoryIDPair = new AdpKVPair();
			factoryIDPair.setKey("FactoryID");
			factoryIDPair.setValue(factoryID);
			params.add(factoryIDPair);

			AdpKVPair unitPartNumberPair = new AdpKVPair();
			unitPartNumberPair.setKey("unitPartNumber");
			unitPartNumberPair.setValue(unitPartNumber);
			params.add(unitPartNumberPair);

			AdpKVPair softwarePartNumberPair = new AdpKVPair();
			softwarePartNumberPair.setKey("softwarePartNumber");
			softwarePartNumberPair.setValue(softwarePartNumber);
			params.add(softwarePartNumberPair);
		}
	}

	private String getType(String position) {
		int len = position.split("/").length;
		return EquType.getEquType(len);
	}

	private String getPosition(String index) {
		return "1/" + index.replace(".", "/");
	}
}