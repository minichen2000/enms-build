package com.nsb.enms.adapter.server.wdm.constants;

import com.nsb.enms.mib.pss.def.M_tnCardManufacturingPartNumber;
import com.nsb.enms.mib.pss.def.M_tnCardName;
import com.nsb.enms.mib.pss.def.M_tnCardSWPartNum;
import com.nsb.enms.mib.pss.def.M_tnCardSerialNumber;
import com.nsb.enms.mib.pss.def.M_tnShelfName;
import com.nsb.enms.mib.pss.def.M_tnShelfPresentType;
import com.nsb.enms.mib.pss.def.M_tnShelfProgrammedType;
import com.nsb.enms.mib.pss.def.M_tnShelfRiManufacturingPartNumber;
import com.nsb.enms.mib.pss.def.M_tnShelfRiSWPartNum;
import com.nsb.enms.mib.pss.def.M_tnShelfRiSerialNumber;
import com.nsb.enms.mib.pss.def.M_tnSlotAdminState;
import com.nsb.enms.mib.pss.def.M_tnSlotOperationalState;
import com.nsb.enms.mib.pss.def.M_tnSlotPresentType;
import com.nsb.enms.mib.pss.def.M_tnSlotProgrammedType;

public class SnmpEqAttribute {

	public static class ShelfAttribute {
		public static final String shelfName = M_tnShelfName.oid;

		public static final String shelfProgrammedType = M_tnShelfProgrammedType.oid;

		public static final String shelfPresentType = M_tnShelfPresentType.oid;

		public static final String shelfRiManufacturingPartNumber = M_tnShelfRiManufacturingPartNumber.oid;

		public static final String shelfRiSWPartNum = M_tnShelfRiSWPartNum.oid;

		public static final String shelfRiSerialNumber = M_tnShelfRiSerialNumber.oid;
	}

	public static class SlotCardAttribute {
		public static final String slotProgrammedType = M_tnSlotProgrammedType.oid;

		public static final String slotPresentType = M_tnSlotPresentType.oid;

		public static final String slotAdminState = M_tnSlotAdminState.oid;

		public static final String slotOperationalState = M_tnSlotOperationalState.oid;

		public static final String cardName = M_tnCardName.oid;

		public static final String cardSerialNumber = M_tnCardSerialNumber.oid;

		public static final String cardManufacturingPartNumber = M_tnCardManufacturingPartNumber.oid;

		public static final String cardSWPartNum = M_tnCardSWPartNum.oid;
	}
}