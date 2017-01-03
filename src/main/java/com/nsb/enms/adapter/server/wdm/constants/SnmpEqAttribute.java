package com.nsb.enms.adapter.server.wdm.constants;

import com.nsb.enms.mib.pss.def.M_tnCardTable;
import com.nsb.enms.mib.pss.def.M_tnShelfRiDataTable;
import com.nsb.enms.mib.pss.def.M_tnShelfTable;
import com.nsb.enms.mib.pss.def.M_tnSlotTable;

public class SnmpEqAttribute {

	public static class ShelfAttribute {
		public static final String tnShelfName = M_tnShelfTable.tnShelfName;

		public static final String tnShelfProgrammedType = M_tnShelfTable.tnShelfProgrammedType;

		public static final String tnShelfPresentType = M_tnShelfTable.tnShelfPresentType;

		public static final String tnShelfRiManufacturingPartNumber = M_tnShelfRiDataTable.tnShelfRiManufacturingPartNumber;

		public static final String tnShelfRiSWPartNum = M_tnShelfRiDataTable.tnShelfRiSWPartNum;

		public static final String tnShelfRiSerialNumber = M_tnShelfRiDataTable.tnShelfRiSerialNumber;
		
		public static final String tnShelfRiCompanyID = M_tnShelfRiDataTable.tnShelfRiCompanyID;
		
		public static final String tnShelfRiMnemonic = M_tnShelfRiDataTable.tnShelfRiMnemonic;
		
		public static final String tnShelfRiCLEI = M_tnShelfRiDataTable.tnShelfRiCLEI;
		
		public static final String tnShelfRiFactoryID = M_tnShelfRiDataTable.tnShelfRiFactoryID;
		
		public static final String tnShelfRiDate = M_tnShelfRiDataTable.tnShelfRiDate;
		
		public static final String tnShelfRiExtraData = M_tnShelfRiDataTable.tnShelfRiExtraData;
		
	}

	public static class SlotCardAttribute {
		public static final String tnSlotProgrammedType = M_tnSlotTable.tnSlotProgrammedType;

		public static final String tnSlotPresentType = M_tnSlotTable.tnSlotPresentType;

		public static final String tnSlotAdminState = M_tnSlotTable.tnSlotAdminState;

		public static final String tnSlotOperationalState = M_tnSlotTable.tnSlotOperationalState;

		public static final String tnCardName = M_tnCardTable.tnCardName;

		public static final String tnCardSerialNumber = M_tnCardTable.tnCardSerialNumber;

		public static final String tnCardManufacturingPartNumber = M_tnCardTable.tnCardManufacturingPartNumber;

		public static final String tnCardSWPartNum = M_tnCardTable.tnCardSWPartNum;
		
		public static final String tnCardCLEI = M_tnCardTable.tnCardCLEI;
		
		public static final String tnCardHFD = M_tnCardTable.tnCardHFD;
		
		public static final String tnCardMarketingPartNumber = M_tnCardTable.tnCardMarketingPartNumber;
		
		public static final String tnCardCompanyID = M_tnCardTable.tnCardCompanyID;
		
		public static final String tnCardMnemonic = M_tnCardTable.tnCardMnemonic;
		
		public static final String tnCardDate = M_tnCardTable.tnCardDate;
		
		public static final String tnCardExtraData = M_tnCardTable.tnCardExtraData;
		
		public static final String tnCardFactoryID = M_tnCardTable.tnCardFactoryID;
	}
}