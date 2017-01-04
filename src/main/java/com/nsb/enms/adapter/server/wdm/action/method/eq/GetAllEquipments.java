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
		//equipments.addAll(getRacks(client));
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
		List<String> leafOids = getShelfLeafOids();
		List<SnmpEquEntity> shelfs = new ArrayList<SnmpEquEntity>();
		try {
			List<List<Pair<String, String>>> rows = client.snmpWalkTableView(leafOids);
			for (List<Pair<String, String>> row : rows) {
				SnmpEquEntity shelf = new SnmpEquEntity();
//				String index = row.get(0).getSecond().replace(".", "/");
//				shelf.setIndex("1/" + index);
				String index = row.get(0).getSecond();
//				shelf.setId(index);
				shelf.setIndex(index);
				row.remove(0);
				constructShelf(shelf, row);
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
		List<String> leafOids = getShelfLeafOids();
		SnmpEquEntity shelf = new SnmpEquEntity();
		try {
			List<Pair<String, String>> row = client.snmpMultiGet(leafOids, index);
//			shelf.setIndex("1/" + index.replace(".", "/"));
//			shelf.setId(index);
			shelf.setIndex(index);
			constructShelf(shelf, row);
		} catch (IOException e) {
			throw new AdapterException(ErrorCode.FAIL_GET_EQUIPMENT_BY_SNMP);
		}
		log.debug("--------------End getShelf--------------");
		return shelf;
	}

	private static List<SnmpEquEntity> getSlotCards(SnmpClient client) throws AdapterException {
		log.debug("--------------Start getSlotCards--------------");
		List<String> leafOids = getSlotCardLeafOids();

		List<SnmpEquEntity> slotCards = new ArrayList<SnmpEquEntity>();
		try {
			List<List<Pair<String, String>>> rows = client.snmpWalkTableView(leafOids);
			for (List<Pair<String, String>> row : rows) {
				SnmpEquEntity slotCard = new SnmpEquEntity();
				//String index = "1/" + row.get(0).getSecond().replace(".", "/");
				String index = row.get(0).getSecond();
//				slotCard.setId(index);
				slotCard.setIndex(index);				
				row.remove(0);
				constructSlotCard(slotCard, row);
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
		List<String> leafOids = getSlotCardLeafOids();
		SnmpEquEntity slotCard = new SnmpEquEntity();
		try {
			List<Pair<String, String>> row = client.snmpMultiGet(leafOids, index);
			//slotCard.setIndex("1/" + index.replace(".", "/"));
//			slotCard.setId(index);
			slotCard.setIndex(index);
			constructSlotCard(slotCard, row);
		} catch (IOException e) {
			throw new AdapterException(ErrorCode.FAIL_GET_EQUIPMENT_BY_SNMP);
		}
		log.debug("--------------End getSlotCard--------------");
		return slotCard;
	}
	
	private static void constructShelf(SnmpEquEntity shelf, List<Pair<String, String>> row)
	{
		shelf.setName(row.get(0).getSecond());
		shelf.setProgrammedType(SnmpEqShelfSlotTypeMapUtil.getShelfType(row.get(1).getSecond()));
		shelf.setPresentType(SnmpEqShelfSlotTypeMapUtil.getShelfType(row.get(2).getSecond()));
		shelf.setManufacturingPartNumber(row.get(3).getSecond());
		shelf.setSWPartNum(row.get(4).getSecond());
		shelf.setSerialNumber(row.get(5).getSecond());
		shelf.setClei(row.get(6).getSecond());
		shelf.setCompanyID(row.get(7).getSecond());
		shelf.setDate(row.get(8).getSecond());
		shelf.setExtraData(row.get(9).getSecond());
		shelf.setFactoryID(row.get(10).getSecond());
		shelf.setMnemonic(row.get(11).getSecond());
	}
	
	private static void constructSlotCard(SnmpEquEntity slotCard, List<Pair<String, String>> row)
	{
		slotCard.setProgrammedType(SnmpEqShelfSlotTypeMapUtil.getSlotCardType(row.get(0).getSecond()));
		slotCard.setPresentType(SnmpEqShelfSlotTypeMapUtil.getSlotCardType(row.get(1).getSecond()));
		slotCard.setAdminState(SnmpSlotState.getSlotState(row.get(2).getSecond()));
		slotCard.setOperationalState(SnmpSlotState.getSlotState(row.get(3).getSecond()));
		slotCard.setName(row.get(4).getSecond());
		slotCard.setSerialNumber(row.get(5).getSecond());
		slotCard.setManufacturingPartNumber(row.get(6).getSecond());
		slotCard.setSWPartNum(row.get(7).getSecond());
		slotCard.setClei(row.get(8).getSecond());
		slotCard.setCompanyID(row.get(9).getSecond());
		slotCard.setDate(row.get(10).getSecond());
		slotCard.setExtraData(row.get(11).getSecond());
		slotCard.setFactoryID(row.get(12).getSecond());
		slotCard.setHfd(row.get(13).getSecond());
		slotCard.setMarketingPartNumber(row.get(14).getSecond());
		slotCard.setMnemonic(row.get(15).getSecond());
	}
	
	private static List<String> getShelfLeafOids()
	{
		List<String> leafOids = new ArrayList<String>();
		leafOids.add(SnmpEqAttribute.ShelfAttribute.tnShelfName );
		leafOids.add(SnmpEqAttribute.ShelfAttribute.tnShelfProgrammedType );
		leafOids.add(SnmpEqAttribute.ShelfAttribute.tnShelfPresentType );

		leafOids.add(SnmpEqAttribute.ShelfAttribute.tnShelfRiManufacturingPartNumber );
		leafOids.add(SnmpEqAttribute.ShelfAttribute.tnShelfRiSWPartNum );
		leafOids.add(SnmpEqAttribute.ShelfAttribute.tnShelfRiSerialNumber );
		leafOids.add(SnmpEqAttribute.ShelfAttribute.tnShelfRiCLEI);
		leafOids.add(SnmpEqAttribute.ShelfAttribute.tnShelfRiCompanyID);
		leafOids.add(SnmpEqAttribute.ShelfAttribute.tnShelfRiDate);
		leafOids.add(SnmpEqAttribute.ShelfAttribute.tnShelfRiExtraData);
		leafOids.add(SnmpEqAttribute.ShelfAttribute.tnShelfRiFactoryID);
		leafOids.add(SnmpEqAttribute.ShelfAttribute.tnShelfRiMnemonic);
		return leafOids;
	}
	
	private static List<String> getSlotCardLeafOids()
	{
		List<String> leafOids = new ArrayList<String>();
		leafOids.add(SnmpEqAttribute.SlotCardAttribute.tnSlotProgrammedType );
		leafOids.add(SnmpEqAttribute.SlotCardAttribute.tnSlotPresentType );
		leafOids.add(SnmpEqAttribute.SlotCardAttribute.tnSlotAdminState );
		leafOids.add(SnmpEqAttribute.SlotCardAttribute.tnSlotOperationalState );

		leafOids.add(SnmpEqAttribute.SlotCardAttribute.tnCardName );
		leafOids.add(SnmpEqAttribute.SlotCardAttribute.tnCardSerialNumber );
		leafOids.add(SnmpEqAttribute.SlotCardAttribute.tnCardManufacturingPartNumber );
		leafOids.add(SnmpEqAttribute.SlotCardAttribute.tnCardSWPartNum );
		leafOids.add(SnmpEqAttribute.SlotCardAttribute.tnCardCLEI);
		leafOids.add(SnmpEqAttribute.SlotCardAttribute.tnCardCompanyID);
		leafOids.add(SnmpEqAttribute.SlotCardAttribute.tnCardDate);
		leafOids.add(SnmpEqAttribute.SlotCardAttribute.tnCardExtraData);
		leafOids.add(SnmpEqAttribute.SlotCardAttribute.tnCardFactoryID);
		leafOids.add(SnmpEqAttribute.SlotCardAttribute.tnCardHFD);
		leafOids.add(SnmpEqAttribute.SlotCardAttribute.tnCardMarketingPartNumber);
		leafOids.add(SnmpEqAttribute.SlotCardAttribute.tnCardMnemonic);
		return leafOids;
	}
}