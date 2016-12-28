package com.nsb.enms.adapter.server.wdm.action.method.eq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.wdm.action.entity.SnmpEquEntity;
import com.nsb.enms.adapter.server.wdm.constants.SnmpEqAttribute;
import com.nsb.enms.adapter.server.wdm.constants.SnmpSlotState;
import com.nsb.enms.adapter.server.wdm.utils.SnmpEqShelfSlotTypeMapUtil;
import com.nsb.enms.common.EquType;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.common.utils.Pair;
import com.nsb.enms.common.utils.snmp.SnmpClient;

public class GetAllEquipments {
	private static final Logger log = LogManager.getLogger(GetAllEquipments.class);

	public static List<SnmpEquEntity> getEquipments(SnmpClient client) throws AdapterException {
		List<SnmpEquEntity> equipments = new ArrayList<SnmpEquEntity>();
		equipments.addAll(getRacks(client));
		equipments.addAll(getShelfs(client));
		equipments.addAll(getSlotCards(client));
		return equipments;
	}

	public static SnmpEquEntity getEquipment(SnmpClient client, String index, EquType type) throws AdapterException {
		switch (type) {
		case shelf:
			return getShelf(client, index);
		case slot:
			return getSlotCard(client, index);
		default:
			return null;
		}
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
		leafOids.add(SnmpEqAttribute.ShelfAttribute.shelfName);
		leafOids.add(SnmpEqAttribute.ShelfAttribute.shelfProgrammedType);
		leafOids.add(SnmpEqAttribute.ShelfAttribute.shelfPresentType);

		leafOids.add(SnmpEqAttribute.ShelfAttribute.shelfRiManufacturingPartNumber);
		leafOids.add(SnmpEqAttribute.ShelfAttribute.shelfRiSWPartNum);
		leafOids.add(SnmpEqAttribute.ShelfAttribute.shelfRiSerialNumber);
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

	private static SnmpEquEntity getShelf(SnmpClient client, String index) throws AdapterException {
		log.debug("--------------Start getShelf--------------");
		List<String> leafOids = new ArrayList<String>();
		leafOids.add(SnmpEqAttribute.ShelfAttribute.shelfName );
		leafOids.add(SnmpEqAttribute.ShelfAttribute.shelfProgrammedType );
		leafOids.add(SnmpEqAttribute.ShelfAttribute.shelfPresentType );

		leafOids.add(SnmpEqAttribute.ShelfAttribute.shelfRiManufacturingPartNumber );
		leafOids.add(SnmpEqAttribute.ShelfAttribute.shelfRiSWPartNum );
		leafOids.add(SnmpEqAttribute.ShelfAttribute.shelfRiSerialNumber );
		SnmpEquEntity shelf = new SnmpEquEntity();
		try {
			List<Pair<String, String>> row = client.snmpMultiGet(leafOids, index);
			shelf.setIndex("1/" + index.replace(".", "/"));
			shelf.setName(row.get(0).getSecond());
			shelf.setProgrammedType(SnmpEqShelfSlotTypeMapUtil.getShelfType(row.get(1).getSecond()));
			shelf.setPresentType(SnmpEqShelfSlotTypeMapUtil.getShelfType(row.get(2).getSecond()));
			shelf.setUnitPartNumber(row.get(3).getSecond());
			shelf.setSoftwarePartNumber(row.get(4).getSecond());
			shelf.setSerialNumber(row.get(5).getSecond());

		} catch (IOException e) {
			throw new AdapterException(ErrorCode.FAIL_GET_EQUIPMENT_BY_SNMP);
		}
		log.debug("--------------End getShelf--------------");
		return shelf;
	}

	private static List<SnmpEquEntity> getSlotCards(SnmpClient client) throws AdapterException {
		log.debug("--------------Start getSlotCards--------------");
		List<String> leafOids = new ArrayList<String>();
		leafOids.add(SnmpEqAttribute.SlotCardAttribute.slotProgrammedType);
		leafOids.add(SnmpEqAttribute.SlotCardAttribute.slotPresentType);
		leafOids.add(SnmpEqAttribute.SlotCardAttribute.slotAdminState);
		leafOids.add(SnmpEqAttribute.SlotCardAttribute.slotOperationalState);

		leafOids.add(SnmpEqAttribute.SlotCardAttribute.cardName);
		leafOids.add(SnmpEqAttribute.SlotCardAttribute.cardSerialNumber);
		leafOids.add(SnmpEqAttribute.SlotCardAttribute.cardManufacturingPartNumber);
		leafOids.add(SnmpEqAttribute.SlotCardAttribute.cardSWPartNum);

		List<SnmpEquEntity> slotCards = new ArrayList<SnmpEquEntity>();
		try {
			List<List<Pair<String, String>>> rows = client.snmpWalkTableView(leafOids);
			for (List<Pair<String, String>> row : rows) {
				SnmpEquEntity slotCard = new SnmpEquEntity();
				String index = "1/" + row.get(0).getSecond().replace(".", "/");
				slotCard.setIndex(index);
				slotCard.setProgrammedType(SnmpEqShelfSlotTypeMapUtil.getSlotCardType(row.get(1).getSecond()));
				slotCard.setPresentType(SnmpEqShelfSlotTypeMapUtil.getSlotCardType(row.get(2).getSecond()));
				slotCard.setAdminState(SnmpSlotState.getSlotState(row.get(3).getSecond()));
				slotCard.setOperationalState(SnmpSlotState.getSlotState(row.get(4).getSecond()));
				slotCard.setName(row.get(5).getSecond());
				slotCard.setSerialNumber(row.get(6).getSecond());
				slotCard.setUnitPartNumber(row.get(7).getSecond());
				slotCard.setSoftwarePartNumber(row.get(8).getSecond());
				slotCards.add(slotCard);
			}
		} catch (IOException e) {
			throw new AdapterException(ErrorCode.FAIL_GET_EQUIPMENT_BY_SNMP);
		}
		log.debug("--------------End getSlotCards--------------");
		return slotCards;
	}

	private static SnmpEquEntity getSlotCard(SnmpClient client, String index) throws AdapterException {
		log.debug("--------------Start getSlotCard--------------");
		List<String> leafOids = new ArrayList<String>();
		leafOids.add(SnmpEqAttribute.SlotCardAttribute.slotProgrammedType );
		leafOids.add(SnmpEqAttribute.SlotCardAttribute.slotPresentType );
		leafOids.add(SnmpEqAttribute.SlotCardAttribute.slotAdminState );
		leafOids.add(SnmpEqAttribute.SlotCardAttribute.slotOperationalState );

		leafOids.add(SnmpEqAttribute.SlotCardAttribute.cardName );
		leafOids.add(SnmpEqAttribute.SlotCardAttribute.cardSerialNumber );
		leafOids.add(SnmpEqAttribute.SlotCardAttribute.cardManufacturingPartNumber );
		leafOids.add(SnmpEqAttribute.SlotCardAttribute.cardSWPartNum );

		SnmpEquEntity slotCard = new SnmpEquEntity();
		try {
			List<Pair<String, String>> row = client.snmpMultiGet(leafOids, index);
			slotCard.setIndex("1/" + index.replace(".", "/"));
			slotCard.setProgrammedType(SnmpEqShelfSlotTypeMapUtil.getSlotCardType(row.get(0).getSecond()));
			slotCard.setPresentType(SnmpEqShelfSlotTypeMapUtil.getSlotCardType(row.get(1).getSecond()));
			slotCard.setAdminState(SnmpSlotState.getSlotState(row.get(2).getSecond()));
			slotCard.setOperationalState(SnmpSlotState.getSlotState(row.get(3).getSecond()));
			slotCard.setName(row.get(4).getSecond());
			slotCard.setSerialNumber(row.get(5).getSecond());
			slotCard.setUnitPartNumber(row.get(6).getSecond());
			slotCard.setSoftwarePartNumber(row.get(7).getSecond());
		} catch (IOException e) {
			throw new AdapterException(ErrorCode.FAIL_GET_EQUIPMENT_BY_SNMP);
		}
		log.debug("--------------End getSlotCard--------------");
		return slotCard;
	}
	
	public static void main(String[] args) {
		SnmpClient client = new SnmpClient("135.251.96.5", 161, "admin_snmp");
		try {
			SnmpEquEntity shelf = getSlotCard(client, "1.19");
			System.out.println(shelf);
		} catch (AdapterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}