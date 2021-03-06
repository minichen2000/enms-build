package com.nsb.enms.adapter.server.wdm.notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nsb.enms.adapter.server.wdm.constants.SnmpEqAttribute;
import com.nsb.enms.adapter.server.wdm.constants.SnmpTrapAttribute;
import com.nsb.enms.mib.pss.def.M_ifTable;
import com.nsb.enms.mib.pss.def.M_tnIfTable;

public class NotificationDispatcher {

	private static List<String> equAttrOidList = new ArrayList<String>();
	private static List<String> tpAttrOidList = new ArrayList<String>();

	private static final NotificationDispatcher INSTANCE = new NotificationDispatcher();

	private NotificationDispatcher() {
		equAttrOidList.add(SnmpEqAttribute.ShelfAttribute.tnShelfName);
		equAttrOidList.add(SnmpEqAttribute.ShelfAttribute.tnShelfProgrammedType);
		equAttrOidList.add(SnmpEqAttribute.ShelfAttribute.tnShelfPresentType);
		equAttrOidList.add(SnmpEqAttribute.ShelfAttribute.tnShelfRiManufacturingPartNumber);
		equAttrOidList.add(SnmpEqAttribute.ShelfAttribute.tnShelfRiSWPartNum);
		equAttrOidList.add(SnmpEqAttribute.ShelfAttribute.tnShelfRiSerialNumber);
		equAttrOidList.add(SnmpEqAttribute.SlotCardAttribute.tnSlotProgrammedType);
		equAttrOidList.add(SnmpEqAttribute.SlotCardAttribute.tnSlotPresentType);
		equAttrOidList.add(SnmpEqAttribute.SlotCardAttribute.tnSlotAdminState);
		equAttrOidList.add(SnmpEqAttribute.SlotCardAttribute.tnSlotOperationalState);
		equAttrOidList.add(SnmpEqAttribute.SlotCardAttribute.tnCardName);
		equAttrOidList.add(SnmpEqAttribute.SlotCardAttribute.tnCardSerialNumber);
		equAttrOidList.add(SnmpEqAttribute.SlotCardAttribute.tnCardManufacturingPartNumber);
		equAttrOidList.add(SnmpEqAttribute.SlotCardAttribute.tnCardSWPartNum);

		tpAttrOidList.add(M_ifTable.ifType);
		tpAttrOidList.add(M_ifTable.ifAdminStatus);
		tpAttrOidList.add(M_ifTable.ifOperStatus);
		tpAttrOidList.add(M_tnIfTable.tnIfType);
		tpAttrOidList.add(M_tnIfTable.tnIfSupportedTypes);
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