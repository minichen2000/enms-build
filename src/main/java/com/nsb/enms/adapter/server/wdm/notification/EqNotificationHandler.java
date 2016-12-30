package com.nsb.enms.adapter.server.wdm.notification;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.notification.NotificationSender;
import com.nsb.enms.adapter.server.wdm.business.eq.AdpSnmpEqusMgr;
import com.nsb.enms.adapter.server.wdm.business.objectIdGenerator.WdmObjectIdGenerator;
import com.nsb.enms.adapter.server.wdm.constants.SnmpEqAttribute;
import com.nsb.enms.adapter.server.wdm.constants.SnmpTrapAttribute;
import com.nsb.enms.adapter.server.wdm.utils.SnmpEqShelfSlotTypeMapUtil;
import com.nsb.enms.common.EntityType;
import com.nsb.enms.common.EquType;
import com.nsb.enms.restful.model.adapter.AdpEquipment;

public class EqNotificationHandler extends DefaultNotificationHandler {

	private static final Logger log = LogManager.getLogger(EqNotificationHandler.class);

	private static EqNotificationHandler INSTANCE = new EqNotificationHandler();

	private static AdpSnmpEqusMgr snmpEqusMgr = new AdpSnmpEqusMgr(new WdmObjectIdGenerator());

	private EqNotificationHandler() {

	}

	public static EqNotificationHandler getInstance() {
		return INSTANCE;
	}

	@Override
	public void handle(Map<String, String> trap) {
		if (StringUtils.equals("Change Notification", trap.get(SnmpTrapAttribute.tnTrapDescr))
				&& StringUtils.equals(trap.get(SnmpTrapAttribute.tnTrapChangedObject),
						SnmpEqAttribute.ShelfAttribute.tnShelfProgrammedType)) {
			createOrDeleteShelf(trap);
			return;
		}

		if (StringUtils.equals("Change Notification", trap.get(SnmpTrapAttribute.tnTrapDescr))
				&& StringUtils.equals(trap.get(SnmpTrapAttribute.tnTrapChangedObject),
						SnmpEqAttribute.SlotCardAttribute.tnSlotProgrammedType)) {
			createOrDeleteSlot(trap);
			return;
		}
	}

	private void createOrDeleteShelf(Map<String, String> trap) {
		String address = trap.get("ip");
		String index = getIndex(trap.get(SnmpTrapAttribute.tnTrapObjectID), EquType.shelf);
		String data = trap.get(SnmpTrapAttribute.tnTrapData);
		String shelfProgrammedType = SnmpEqShelfSlotTypeMapUtil.getShelfType(data);

		try {
			if (StringUtils.isEmpty(shelfProgrammedType) || StringUtils.equals("Empty", shelfProgrammedType)) {
				AdpEquipment equipment = snmpEqusMgr.checkIsEqExistedByAddress(address, index);
				if (equipment != null) {
					snmpEqusMgr.deleteEquipmentsUnderShelf(equipment);
					NotificationSender.instance().sendOdNotif(EntityType.BOARD, equipment.getId());
				}
			} else {
				AdpEquipment equipment = snmpEqusMgr.addEquipment(address, index, EquType.shelf);
				if (equipment != null) {
					NotificationSender.instance().sendOcNotif(EntityType.BOARD, equipment.getId());
				}

			}
		} catch (AdapterException e) {
			log.error("Failed to send notification", e);
		}
	}

	private void createOrDeleteSlot(Map<String, String> trap) {
		String address = trap.get("ip");
		String index = getIndex(trap.get(SnmpTrapAttribute.tnTrapObjectID), EquType.slot);
		String data = trap.get(SnmpTrapAttribute.tnTrapData);
		String slotProgrammedType = SnmpEqShelfSlotTypeMapUtil.getSlotCardType(data);

		try {
			AdpEquipment eqFromDb = snmpEqusMgr.checkIsEqExistedByAddress(address, index);
			if (eqFromDb != null) {
				eqFromDb.setExpectedType(slotProgrammedType);
				snmpEqusMgr.updateEquipment(eqFromDb);
			}
		} catch (Exception e) {
			log.error("Failed to send notification", e);
		}
	}

	private String getIndex(String objectId, EquType type) {
		String index = Integer.toHexString(Integer.valueOf(objectId));
		String shelfIndex = "";
		String slotIndex = "";
		switch (type) {
		case shelf:
			if (index.length() == 7) {
				shelfIndex = index.substring(0, 1);
			} else if (index.length() == 8) {
				shelfIndex = index.substring(0, 2);
			}
			return hex2Oct(shelfIndex);
		case slot:
			if (index.length() == 7) {
				index = index.substring(0, 3);
				shelfIndex = index.substring(0, 1);
				slotIndex = index.substring(1, 3);
			} else if (index.length() == 8) {
				index = index.substring(0, 4);
				shelfIndex = index.substring(0, 2);
				slotIndex = index.substring(2, 4);
			}
			return hex2Oct(shelfIndex) + "." + hex2Oct(slotIndex);
		default:
			return null;
		}
	}

	private String hex2Oct(String hex) {
		if (StringUtils.isEmpty(hex)) {
			return null;
		}
		int index = Integer.valueOf(hex, 16);
		return String.valueOf(index);
	}
}