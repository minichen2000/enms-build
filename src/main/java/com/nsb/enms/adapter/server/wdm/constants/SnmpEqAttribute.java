package com.nsb.enms.adapter.server.wdm.constants;

import com.nsb.enms.mib.pss.def.M_tnCardCLEI;
import com.nsb.enms.mib.pss.def.M_tnCardCompanyID;
import com.nsb.enms.mib.pss.def.M_tnCardDate;
import com.nsb.enms.mib.pss.def.M_tnCardExtraData;
import com.nsb.enms.mib.pss.def.M_tnCardFactoryID;
import com.nsb.enms.mib.pss.def.M_tnCardHFD;
import com.nsb.enms.mib.pss.def.M_tnCardManufacturingPartNumber;
import com.nsb.enms.mib.pss.def.M_tnCardMarketingPartNumber;
import com.nsb.enms.mib.pss.def.M_tnCardMnemonic;
import com.nsb.enms.mib.pss.def.M_tnCardName;
import com.nsb.enms.mib.pss.def.M_tnCardSWPartNum;
import com.nsb.enms.mib.pss.def.M_tnCardSerialNumber;
import com.nsb.enms.mib.pss.def.M_tnShelfName;
import com.nsb.enms.mib.pss.def.M_tnShelfPresentType;
import com.nsb.enms.mib.pss.def.M_tnShelfProgrammedType;
import com.nsb.enms.mib.pss.def.M_tnShelfRiCompanyID;
import com.nsb.enms.mib.pss.def.M_tnShelfRiManufacturingPartNumber;
import com.nsb.enms.mib.pss.def.M_tnShelfRiSWPartNum;
import com.nsb.enms.mib.pss.def.M_tnShelfRiSerialNumber;
import com.nsb.enms.mib.pss.def.M_tnSlotAdminState;
import com.nsb.enms.mib.pss.def.M_tnSlotOperationalState;
import com.nsb.enms.mib.pss.def.M_tnSlotPresentType;
import com.nsb.enms.mib.pss.def.M_tnSlotProgrammedType;

public class SnmpEqAttribute {

	public static class ShelfAttribute {
		public static final String tnShelfName = M_tnShelfName.oid;

		public static final String tnShelfProgrammedType = M_tnShelfProgrammedType.oid;

		public static final String tnShelfPresentType = M_tnShelfPresentType.oid;

		public static final String tnShelfRiManufacturingPartNumber = M_tnShelfRiManufacturingPartNumber.oid;

		public static final String tnShelfRiSWPartNum = M_tnShelfRiSWPartNum.oid;

		public static final String tnShelfRiSerialNumber = M_tnShelfRiSerialNumber.oid;
		
		public static final String tnShelfRiCompanyID = M_tnShelfRiCompanyID.oid;
		
		public static final String tnShelfRiMnemonic = M_tnShelfRiCompanyID.oid;
		
		public static final String tnShelfRiCLEI = M_tnShelfRiCompanyID.oid;
		
		public static final String tnShelfRiFactoryID = M_tnShelfRiCompanyID.oid;
		
		public static final String tnShelfRiDate = M_tnShelfRiCompanyID.oid;
		
		public static final String tnShelfRiExtraData = M_tnShelfRiCompanyID.oid;
		
	}

	public static class SlotCardAttribute {
		public static final String tnSlotProgrammedType = M_tnSlotProgrammedType.oid;

		public static final String tnSlotPresentType = M_tnSlotPresentType.oid;

		public static final String tnSlotAdminState = M_tnSlotAdminState.oid;

		public static final String tnSlotOperationalState = M_tnSlotOperationalState.oid;

		public static final String tnCardName = M_tnCardName.oid;

		public static final String tnCardSerialNumber = M_tnCardSerialNumber.oid;

		public static final String tnCardManufacturingPartNumber = M_tnCardManufacturingPartNumber.oid;

		public static final String tnCardSWPartNum = M_tnCardSWPartNum.oid;
		
		public static final String tnCardCLEI = M_tnCardCLEI.oid;
		
		public static final String tnCardHFD = M_tnCardHFD.oid;
		
		public static final String tnCardMarketingPartNumber = M_tnCardMarketingPartNumber.oid;
		
		public static final String tnCardCompanyID = M_tnCardCompanyID.oid;
		
		public static final String tnCardMnemonic = M_tnCardMnemonic.oid;
		
		public static final String tnCardDate = M_tnCardDate.oid;
		
		public static final String tnCardExtraData = M_tnCardExtraData.oid;
		
		public static final String tnCardFactoryID = M_tnCardFactoryID.oid;
	}
}