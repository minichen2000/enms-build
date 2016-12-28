package com.nsb.enms.adapter.server.wdm.constants;

import com.nsb.enms.mib.pss.def.M_tnTrapCategory;
import com.nsb.enms.mib.pss.def.M_tnTrapChangedObject;
import com.nsb.enms.mib.pss.def.M_tnTrapCondition;
import com.nsb.enms.mib.pss.def.M_tnTrapData;
import com.nsb.enms.mib.pss.def.M_tnTrapDescr;
import com.nsb.enms.mib.pss.def.M_tnTrapEntityType;
import com.nsb.enms.mib.pss.def.M_tnTrapObjectID;
import com.nsb.enms.mib.pss.def.M_tnTrapObjectIDType;
import com.nsb.enms.mib.pss.def.M_tnTrapSerialNumber;
import com.nsb.enms.mib.pss.def.M_tnTrapServiceAffecting;
import com.nsb.enms.mib.pss.def.M_tnTrapTime;
import com.nsb.enms.mib.pss.def.M_tnTrapType;

public class SnmpTrapAttribute {

	public static final String tnTrapSerialNumber = M_tnTrapSerialNumber.oid;

	public static final String tnTrapType = M_tnTrapType.oid;

	public static final String tnTrapTime = M_tnTrapTime.oid;

	public static final String tnTrapObjectIDType = M_tnTrapObjectIDType.oid;

	public static final String tnTrapObjectID = M_tnTrapObjectID.oid;

	public static final String tnTrapCategory = M_tnTrapCategory.oid;

	public static final String tnTrapDescr = M_tnTrapDescr.oid;

	public static final String tnTrapChangedObject = M_tnTrapChangedObject.oid;

	public static final String tnTrapData = M_tnTrapData.oid;

	public static final String tnTrapServiceAffecting = M_tnTrapServiceAffecting.oid;

	public static final String tnTrapEntityType = M_tnTrapEntityType.oid;

	public static final String tnTrapCondition = M_tnTrapCondition.oid;
}