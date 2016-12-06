package com.nsb.enms.adapter.server.business.eq;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.action.entity.EquipmentEntity;
import com.nsb.enms.adapter.server.action.entity.TptCoordinatorEntity;
import com.nsb.enms.adapter.server.action.method.eq.GetEquipment;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.utils.GenerateKeyOnNeUtil;
import com.nsb.enms.adapter.server.db.mgr.AdpEqusDbMgr;
import com.nsb.enms.adapter.server.db.mgr.AdpSeqDbMgr;
import com.nsb.enms.common.EntityType;
import com.nsb.enms.common.EquType;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.restful.model.adapter.AdpEquipment;
import com.nsb.enms.restful.model.adapter.AdpKVPair;

public class AdpEqusMgr {
	private final static Logger log = LogManager.getLogger(AdpEqusMgr.class);

	private AdpEqusDbMgr equsDbMgr = new AdpEqusDbMgr();

	public AdpEqusMgr() {
	}

	public void syncEquip(String groupId, String neId, int id) throws AdapterException {
		List<TptCoordinatorEntity> tptCoordinatorList = GetEquipment.getISAs(groupId, neId);
		if (null != tptCoordinatorList && !tptCoordinatorList.isEmpty()) {
			for (TptCoordinatorEntity entity : tptCoordinatorList) {
				log.debug(entity);
			}
		}

		List<EquipmentEntity> equList = GetEquipment.getEquipments(groupId, neId);
		log.debug("equList=" + equList.size() + ", neId=" + neId);

		for (EquipmentEntity equ : equList) {
			try {
				equ.setId(AdpSeqDbMgr.getMaxEquipmentId());
			} catch (Exception e) {
				throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
			}
		}

		for (EquipmentEntity equ : equList) {
			log.debug(equ);
			AdpEquipment newEqu = constructEquip(equ, equList, tptCoordinatorList, id);
			AdpEquipment adpEqu;
			try {
				adpEqu = equsDbMgr.getEquipmentById(newEqu.getId());
			} catch (Exception e) {
				log.error("getEquipmentById:", e);
				throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
			}
			if (null == adpEqu || adpEqu.getId() == null) {
				try {
					equsDbMgr.addEquipment(newEqu);
				} catch (Exception e) {
					log.error("addEquipment:", e);
					throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
				}
			}
		}
		log.debug("sync equipment end");
	}

	private AdpEquipment constructEquip(EquipmentEntity equ, List<EquipmentEntity> equList,
			List<TptCoordinatorEntity> tptCoordinatorList, int id) {
		AdpEquipment adpEqu = new AdpEquipment();
		List<AdpKVPair> params = new ArrayList<AdpKVPair>();
		String moc = equ.getMoc();
		String moi = equ.getMoi();
		String expectedType = equ.getEquipmentExpected();
		String actualType = equ.getEquipmentActual();
		String keyOnNe = GenerateKeyOnNeUtil.generateKeyOnNe(EntityType.BOARD, moc, moi);
		adpEqu.setId(equ.getId());
		adpEqu.setNeId(id);
		String position = getPosition(moi);
		adpEqu.setPosition(position);
		String type = getType(moi);
		adpEqu.setType(type);
		adpEqu.setExpectedType(expectedType);
		adpEqu.setActualType(actualType);
		adpEqu.setKeyOnNe(keyOnNe);
		//adpEqu.setAlarmState(equ.getAlarmStatus());
		if (!StringUtils.isEmpty( actualType ))
		{
		    AdpKVPair param1 = new AdpKVPair();
            param1.setKey("unitPartNumber");
            param1.setValue("");
            params.add(param1);
            AdpKVPair param2 = new AdpKVPair();
            param2.setKey("softwarePartNumber");
            param2.setValue("");
            params.add(param2);
            AdpKVPair param3 = new AdpKVPair();
            param3.setKey("serialNumber");
            param3.setValue("");
            params.add(param3);
		}

		for (int i = tptCoordinatorList.size() - 1; i >= 0; i--) {
			TptCoordinatorEntity tptCoordinatorEntity = tptCoordinatorList.get(i);
			if (moi.equals(tptCoordinatorEntity.getEquMoi())) {
				if (!StringUtils.isEmpty(tptCoordinatorEntity.getIpMask())) {
					AdpKVPair param1 = new AdpKVPair();
					param1.setKey("ipAddress");
					param1.setValue(tptCoordinatorEntity.getIpAddress());
					params.add(param1);
					AdpKVPair param2 = new AdpKVPair();
					param2.setKey("ipMask");
					param2.setValue(tptCoordinatorEntity.getIpMask());
					params.add(param2);
					AdpKVPair param3 = new AdpKVPair();
					param3.setKey("maxPosition");
					param3.setValue(tptCoordinatorEntity.getMaxPosition());
					params.add(param3);
					AdpKVPair param4 = new AdpKVPair();
					param4.setKey("maxVc4nv");
					param4.setValue(tptCoordinatorEntity.getMaxVc4nv());
					params.add(param4);
					AdpKVPair param5 = new AdpKVPair();
					param5.setKey("maxVc3nv");
					param5.setValue(tptCoordinatorEntity.getMaxVc3nv());
					params.add(param5);
					AdpKVPair param6 = new AdpKVPair();
					param6.setKey("maxVc12nv");
					param6.setValue(tptCoordinatorEntity.getMaxVc12nv());
					params.add(param6);

				}
				tptCoordinatorList.remove(i);
				break;
			}
		}

		for (EquipmentEntity equipment : equList) {
			String parentMoi = equipment.getMoi();
			if (!parentMoi.equals(moi) && moi.matches(parentMoi + "/equipment=[0-9]+")) {
				AdpKVPair param = new AdpKVPair();
				param.setKey("parentId");
				param.setValue(String.valueOf(equipment.getId()));
				params.add(param);
				break;
			}
		}
		adpEqu.setParams(params);
		return adpEqu;
	}

	private String getType(String moi) {
		String[] elements = moi.split("/");
		switch (elements.length) {
		case 3:
		    return EquType.rack.name();
		case 4:
			return EquType.shelf.name();
		case 5:
			return EquType.slot.name();
		case 6:
			return EquType.subslot.name();
		default:
			return null;
		}
	}
	
	private String getPosition(String moi)
	{
	    StringBuilder position = new StringBuilder("");
	    String[] elements = moi.split( "/" );
	    for (int i = 2; i < elements.length; i++)
	    {
	        position.append( elements[i].split( "=" )[1] ).append( "/" );
	    }
	    return position.substring( 0, position.length() - 1 );
	}
}