package com.nsb.enms.adapter.server.wdm.notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nsb.enms.adapter.server.wdm.constants.SnmpEqAttribute;
import com.nsb.enms.adapter.server.wdm.constants.SnmpTrapAttribute;
import com.nsb.enms.mib.pss.def.M_ifAdminStatus;
import com.nsb.enms.mib.pss.def.M_ifOperStatus;
import com.nsb.enms.mib.pss.def.M_ifType;
import com.nsb.enms.mib.pss.def.M_tnIfSupportedTypes;
import com.nsb.enms.mib.pss.def.M_tnIfType;

public class NotificationDispatcher {

	private static List<String> equAttrOidList = new ArrayList<String>();
	private static List<String> tpAttrOidList = new ArrayList<String>();

	private static final NotificationDispatcher INSTANCE = new NotificationDispatcher();

	private NotificationDispatcher() {
		equAttrOidList.add(SnmpEqAttribute.ShelfAttribute.shelfName);
		equAttrOidList.add(SnmpEqAttribute.ShelfAttribute.shelfProgrammedType);
		equAttrOidList.add(SnmpEqAttribute.ShelfAttribute.shelfPresentType);
		equAttrOidList.add(SnmpEqAttribute.ShelfAttribute.shelfRiManufacturingPartNumber);
		equAttrOidList.add(SnmpEqAttribute.ShelfAttribute.shelfRiSWPartNum);
		equAttrOidList.add(SnmpEqAttribute.ShelfAttribute.shelfRiSerialNumber);
		equAttrOidList.add(SnmpEqAttribute.SlotCardAttribute.slotProgrammedType);
		equAttrOidList.add(SnmpEqAttribute.SlotCardAttribute.slotPresentType);
		equAttrOidList.add(SnmpEqAttribute.SlotCardAttribute.slotAdminState);
		equAttrOidList.add(SnmpEqAttribute.SlotCardAttribute.slotOperationalState);
		equAttrOidList.add(SnmpEqAttribute.SlotCardAttribute.cardName);
		equAttrOidList.add(SnmpEqAttribute.SlotCardAttribute.cardSerialNumber);
		equAttrOidList.add(SnmpEqAttribute.SlotCardAttribute.cardManufacturingPartNumber);
		equAttrOidList.add(SnmpEqAttribute.SlotCardAttribute.cardSWPartNum);

		tpAttrOidList.add(M_ifType.oid);
		tpAttrOidList.add(M_ifAdminStatus.oid);
		tpAttrOidList.add(M_ifOperStatus.oid);
		tpAttrOidList.add(M_tnIfType.oid);
		tpAttrOidList.add(M_tnIfSupportedTypes.oid);
	}

	public static NotificationDispatcher getInstance() {
		return INSTANCE;
	}

	public void dispatcher(Map<String, String> trap) {
		String changeObject = trap.get(SnmpTrapAttribute.tnTrapChangedObject);
		if (equAttrOidList.contains(changeObject))
		{
			EqNotificationHandler.getInstance().handle(trap);
			return;
		}
		
		if (tpAttrOidList.contains(changeObject))
		{
			TpNotificationHandler.getInstance().handle(trap);
			return;
		}		
	}
}