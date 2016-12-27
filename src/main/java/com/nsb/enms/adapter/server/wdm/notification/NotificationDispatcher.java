package com.nsb.enms.adapter.server.wdm.notification;

import java.util.ArrayList;
import java.util.List;

import com.nsb.enms.common.utils.Pair;
import com.nsb.enms.mib.pss.def.M_ifAdminStatus;
import com.nsb.enms.mib.pss.def.M_ifOperStatus;
import com.nsb.enms.mib.pss.def.M_ifType;
import com.nsb.enms.mib.pss.def.M_tnCardManufacturingPartNumber;
import com.nsb.enms.mib.pss.def.M_tnCardName;
import com.nsb.enms.mib.pss.def.M_tnCardSWPartNum;
import com.nsb.enms.mib.pss.def.M_tnCardSerialNumber;
import com.nsb.enms.mib.pss.def.M_tnIfSupportedTypes;
import com.nsb.enms.mib.pss.def.M_tnIfType;
import com.nsb.enms.mib.pss.def.M_tnShelfName;
import com.nsb.enms.mib.pss.def.M_tnShelfPresentType;
import com.nsb.enms.mib.pss.def.M_tnShelfProgrammedType;
import com.nsb.enms.mib.pss.def.M_tnShelfRiManufacturingPartNumber;
import com.nsb.enms.mib.pss.def.M_tnShelfRiSWPartNum;
import com.nsb.enms.mib.pss.def.M_tnShelfRiSerialNumber;
import com.nsb.enms.mib.pss.def.M_tnSlotPresentType;
import com.nsb.enms.mib.pss.def.M_tnSlotProgrammedType;

public class NotificationDispatcher {

	private static List<String> equAttrOidList = new ArrayList<String>();
	private static List<String> tpAttrOidList = new ArrayList<String>();

	private static final NotificationDispatcher INSTANCE = new NotificationDispatcher();

	private NotificationDispatcher() {
		equAttrOidList.add(M_tnShelfName.oid);
		equAttrOidList.add(M_tnShelfProgrammedType.oid);
		equAttrOidList.add(M_tnShelfPresentType.oid);
		equAttrOidList.add(M_tnShelfRiManufacturingPartNumber.oid);
		equAttrOidList.add(M_tnShelfRiSWPartNum.oid);
		equAttrOidList.add(M_tnShelfRiSerialNumber.oid);
		equAttrOidList.add(M_tnSlotProgrammedType.oid);
		equAttrOidList.add(M_tnSlotPresentType.oid);
		equAttrOidList.add(M_tnCardName.oid);
		equAttrOidList.add(M_tnCardSerialNumber.oid);
		equAttrOidList.add(M_tnCardManufacturingPartNumber.oid);
		equAttrOidList.add(M_tnCardSWPartNum.oid);

		tpAttrOidList.add(M_ifType.oid);
		tpAttrOidList.add(M_ifAdminStatus.oid);
		tpAttrOidList.add(M_ifOperStatus.oid);
		tpAttrOidList.add(M_tnIfType.oid);
		tpAttrOidList.add(M_tnIfSupportedTypes.oid);
	}

	public static NotificationDispatcher getInstance() {
		return INSTANCE;
	}

	public void dispatcher(List<Pair<String, String>> trap) {
		for (Pair<String, String> pair : trap) {
			if (equAttrOidList.contains(pair.getSecond())) {
				EqNotificationHandler.getInstance().handle(trap);
				break;
			}

			if (tpAttrOidList.contains(pair.getSecond())) {
				TpNotificationHandler.getInstance().handle(trap);
				break;
			}
		}
	}
}