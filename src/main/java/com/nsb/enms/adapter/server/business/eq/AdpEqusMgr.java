package com.nsb.enms.adapter.server.business.eq;

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
import com.nsb.enms.common.EntityType;
import com.nsb.enms.common.EquType;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.restful.model.adapter.AdpEquipment;

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
		String moc = equ.getMoc();
		String moi = equ.getMoi();
		String keyOnNe = GenerateKeyOnNeUtil.generateKeyOnNe(EntityType.BOARD, moc, moi);
		adpEqu.setId( );
		adpEqu.setNeId( id );
		adpEqu.setPosition( "" );
		String type = getType( moi );
		adpEqu.setType( type );
		adpEqu.setExpectedType( equ.getEquipmentExpected() );
		adpEqu.setActualType( equ.getEquipmentActual() );
		adpEqu.setKeyOnNe( keyOnNe );
		adpEqu.setUnitPartNumber(  );
		adpEqu.setSoftwarePartNumber(  );
		adpEqu.setSerialNumber(  );
		adpEqu.setSlotState(  );
		adpEqu.setAlarmState( equ.getAlarmStatus() );
		adpEqu.setMaintenanceState( );
		

		for (int i = tptCoordinatorList.size() - 1; i >= 0; i--) {
			if (moi.equals(tptCoordinatorList.get(i).getEquMoi())) {
				// adpEqu.setAddresses( null );
				tptCoordinatorList.remove(i);
				break;
			}
		}

		for (EquipmentEntity equipment : equList) {
			String parentMoi = equipment.getMoi();
			if (!parentMoi.equals(moi) && moi.matches(parentMoi + "/equipment=[0-9]+")) {
				// newEqu.setParentId(equ.getId());
				break;
			}
		}
		return adpEqu;
	}
	
	private String getType(String moi)
	{
	    String[] elements = moi.split( "/" );
	    switch( elements.length )
        {
            case 4:
                return EquType.shelf.name();
            case 5:
                return EquType.slot.name();
            case 6:
                return EquType.subslot.name();
            default:
                return EquType.unknow.name();
        }
	}
}