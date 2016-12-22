package com.nsb.enms.adapter.server.wdm.action.method.eq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.wdm.action.entity.SnmpEquEntity;
import com.nsb.enms.adapter.server.wdm.utils.SnmpEqShelfSlotTypeMapUtil;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.common.utils.Pair;
import com.nsb.enms.common.utils.snmp.SnmpClient;
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
import com.nsb.enms.mib.pss.def.M_tnSlotPresentType;
import com.nsb.enms.mib.pss.def.M_tnSlotProgrammedType;

public class GetAllEquipments {
	private static final Logger log = LogManager.getLogger(GetAllEquipments.class);

	public static List<SnmpEquEntity> getEquipments(SnmpClient client) throws AdapterException {
		List<SnmpEquEntity> equipments = new ArrayList<SnmpEquEntity>();
		equipments.addAll(getRacks(client));
		equipments.addAll(getShelfs(client));
		equipments.addAll(getSlotCards(client));
		return equipments;
	}

	private static List<SnmpEquEntity> getRacks(SnmpClient client) throws AdapterException {
		List<SnmpEquEntity> racks = new ArrayList<SnmpEquEntity>();
		SnmpEquEntity rack = new SnmpEquEntity();
		rack.setIndex("1");
		rack.setProgrammedType("RACK");
		rack.setPresentType("RACK");
		racks.add(rack);
		return racks;
	}

	private static List<SnmpEquEntity> getShelfs(SnmpClient client) throws AdapterException {
		log.debug("--------------Start getShelfs--------------");
		List<String> leafOids = new ArrayList<String>();
		leafOids.add(M_tnShelfName.oid);
		leafOids.add(M_tnShelfProgrammedType.oid);
		leafOids.add(M_tnShelfPresentType.oid);

		leafOids.add(M_tnShelfRiManufacturingPartNumber.oid);
		leafOids.add(M_tnShelfRiSWPartNum.oid);
		leafOids.add(M_tnShelfRiSerialNumber.oid);
		List<SnmpEquEntity> shelfs = new ArrayList<SnmpEquEntity>();
		try {
			List<List<Pair<String, String>>> rows = client.snmpWalkTableView(leafOids);
			for (List<Pair<String, String>> row : rows) {
				SnmpEquEntity shelf = new SnmpEquEntity();
				String index = row.get(0).getSecond().replace(".", "/");
				shelf.setIndex("1/" + index);
				shelf.setName(row.get(1).getSecond());
				shelf.setProgrammedType(SnmpEqShelfSlotTypeMapUtil.getShelfType(row.get(2).getSecond()));
				shelf.setPresentType(SnmpEqShelfSlotTypeMapUtil.getShelfType(row.get(3).getSecond()));
				shelf.setUnitPartNumber(row.get(4).getSecond());
				shelf.setSoftwarePartNumber(row.get(5).getSecond());
				shelf.setSerialNumber(row.get(6).getSecond());
				shelfs.add(shelf);
			}
		} catch (IOException e) {
			throw new AdapterException(ErrorCode.FAIL_GET_EQUIPMENT_BY_SNMP);
		}
		log.debug("--------------End getShelfs--------------");
		return shelfs;
	}

	private static List<SnmpEquEntity> getSlotCards(SnmpClient client) throws AdapterException {
		log.debug("--------------Start getSlotCards--------------");
		List<String> leafOids = new ArrayList<String>();
		leafOids.add(M_tnSlotProgrammedType.oid);
		leafOids.add(M_tnSlotPresentType.oid);
		// leafOids.add( TnSlotAdminState.oid );
		// leafOids.add( TnSlotOperationalState.oid );

		leafOids.add(M_tnCardName.oid);
		leafOids.add(M_tnCardSerialNumber.oid);
		leafOids.add(M_tnCardManufacturingPartNumber.oid);
		leafOids.add(M_tnCardSWPartNum.oid);

		List<SnmpEquEntity> slotCards = new ArrayList<SnmpEquEntity>();
		try {
			List<List<Pair<String, String>>> rows = client.snmpWalkTableView(leafOids);
			for (List<Pair<String, String>> row : rows) {
				SnmpEquEntity slotCard = new SnmpEquEntity();
				String index = "1/" + row.get(0).getSecond().replace(".", "/");
				slotCard.setIndex(index);
				slotCard.setProgrammedType(SnmpEqShelfSlotTypeMapUtil.getSlotCardType(row.get(1).getSecond()));
				slotCard.setPresentType(SnmpEqShelfSlotTypeMapUtil.getSlotCardType(row.get(2).getSecond()));
				slotCard.setName(row.get(3).getSecond());
				slotCard.setSerialNumber(row.get(4).getSecond());
				slotCard.setUnitPartNumber(row.get(5).getSecond());
				slotCard.setSoftwarePartNumber(row.get(6).getSecond());
				slotCards.add(slotCard);
			}
		} catch (IOException e) {
			throw new AdapterException(ErrorCode.FAIL_GET_EQUIPMENT_BY_SNMP);
		}
		log.debug("--------------End getSlotCards--------------");
		return slotCards;
	}
}