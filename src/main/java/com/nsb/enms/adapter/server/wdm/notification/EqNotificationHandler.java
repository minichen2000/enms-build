package com.nsb.enms.adapter.server.wdm.notification;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.notification.NotificationSender;
import com.nsb.enms.adapter.server.wdm.business.eq.AdpSnmpEqusMgr;
import com.nsb.enms.adapter.server.wdm.constants.SnmpEqAttribute;
import com.nsb.enms.adapter.server.wdm.constants.SnmpTrapAttribute;
import com.nsb.enms.adapter.server.wdm.utils.SnmpEqShelfSlotTypeMapUtil;
import com.nsb.enms.common.EntityType;
import com.nsb.enms.common.EquType;
import com.nsb.enms.restful.model.adapter.AdpEquipment;

public class EqNotificationHandler extends DefaultNotificationHandler {

	private static final Logger log = LogManager.getLogger(EqNotificationHandler.class);

	private static EqNotificationHandler INSTANCE = new EqNotificationHandler();

	private EqNotificationHandler() {

	}

	public static EqNotificationHandler getInstance() {
		return INSTANCE;
	}

	@Override
	public void handle(Map<String, String> trap) {
		if (StringUtils.equals("Change Notification", trap.get(SnmpTrapAttribute.tnTrapDescr)) && StringUtils.equals(
				trap.get(SnmpTrapAttribute.tnTrapChangedObject), SnmpEqAttribute.ShelfAttribute.shelfProgrammedType)) {
			createOrDeleteShelf(trap);
			return;
		}

		if (StringUtils.equals("Change Notification", trap.get(SnmpTrapAttribute.tnTrapDescr))
				&& StringUtils.equals(trap.get(SnmpTrapAttribute.tnTrapChangedObject),
						SnmpEqAttribute.SlotCardAttribute.slotProgrammedType)) {
			createOrDeleteSlot(trap);
			return;
		}
	}

	private void createOrDeleteShelf(Map<String, String> trap) {
		String address = trap.get("ip");
		String index = getIndex(trap.get(SnmpTrapAttribute.tnTrapObjectID), EquType.shelf);
		String data = trap.get(SnmpTrapAttribute.tnTrapData);
		String shelfProgrammedType = SnmpEqShelfSlotTypeMapUtil.getShelfType(data);
		if (StringUtils.isEmpty(shelfProgrammedType) || StringUtils.equals("Empty", shelfProgrammedType)) {
			
		} else {
			try {
				AdpEquipment equipment = new AdpSnmpEqusMgr().getEquipment(address, index, EquType.shelf);
				if (equipment != null) {
					NotificationSender.instance().sendOcNotif(EntityType.BOARD, equipment.getId());
				}
			} catch (AdapterException e) {
				log.error("Failed to send notification", e);
			}
		}
	}

	private void createOrDeleteSlot(Map<String, String> trap) {
		String address = trap.get("ip");
		String index = getIndex(trap.get(SnmpTrapAttribute.tnTrapObjectID), EquType.slot);
		String data = trap.get(SnmpTrapAttribute.tnTrapData);
		String shelfProgrammedType = SnmpEqShelfSlotTypeMapUtil.getShelfType(data);
		if (StringUtils.isEmpty(shelfProgrammedType) || StringUtils.equals("Empty", shelfProgrammedType)) {
			
		} else {
			try {
				AdpEquipment equipment = new AdpSnmpEqusMgr().getEquipment(address, index, EquType.slot);
				if (equipment != null) {
					NotificationSender.instance().sendOcNotif(EntityType.BOARD, equipment.getId());
				}
			} catch (AdapterException e) {
				log.error("Failed to send notification", e);
			}
		}
	}

	private String getIndex(String objectId, EquType type) {
		String index = Integer.toHexString(Integer.valueOf(objectId));
		switch (type) {
		case shelf:
			if (index.length() == 7) {
				return index.substring(0, 1);
			}

			if (index.length() == 8) {
				return index.substring(0, 2);
			}
		case slot:
			if (index.length() == 7) {
				index = index.substring(0, 3);
				if (StringUtils.equals("0", index.substring(1, 2))) {
					index = index.substring(0, 1) + "." + index.substring(2, 3);
				} else {
					index = index.substring(0, 1) + "." + index.substring(1, 3);
				}
				return index;
			}

			if (index.length() == 8) {
				index = index.substring(0, 4);
				if (StringUtils.equals("0", index.substring(2, 3))) {
					index = index.substring(0, 2) + "." + index.substring(3, 4);
				} else {
					index = index.substring(0, 2) + "." + index.substring(2, 4);
				}
				return index;
			}
		default:
			return null;
		}
	}
}