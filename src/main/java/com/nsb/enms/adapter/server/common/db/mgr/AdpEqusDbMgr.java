package com.nsb.enms.adapter.server.common.db.mgr;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.or;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.regex;
import static com.mongodb.client.model.Updates.set;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import com.nsb.enms.adapter.server.common.constants.AdpDBConst;
import com.nsb.enms.adapter.server.common.db.mongodb.mgr.AdpMongoDBMgr;
import com.nsb.enms.adapter.server.common.notification.NotificationSender;
import com.nsb.enms.common.EntityType;
import com.nsb.enms.restful.model.adapter.AdpEquipment;

public class AdpEqusDbMgr {
	private final static Logger log = LogManager.getLogger(AdpEqusDbMgr.class);
	private MongoDatabase db = AdpMongoDBMgr.getInstance().getDatabase();
//	private MongoCollection<Document> dbc = db.getCollection(AdpDBConst.DB_NAME_EQUIPMENT);
//	private MongoCollection<BasicDBObject> dbc1 = db.getCollection(AdpDBConst.DB_NAME_EQUIPMENT, BasicDBObject.class);
	private Gson gson = new Gson();

	private MongoCollection<BasicDBObject> getCustomCollection(String neId) {
		return db.getCollection(AdpDBConst.DB_NAME_EQUIPMENT + "_" + neId, BasicDBObject.class);
	}

	private MongoCollection<Document> getCollection(String neId) {
		return db.getCollection(AdpDBConst.DB_NAME_EQUIPMENT + "_" + neId);
	}
	
	public AdpEquipment addEquipment(AdpEquipment body) throws Exception {
		String equipment = gson.toJson(body);
		BasicDBObject dbObject = (BasicDBObject) JSON.parse(equipment);
		getCustomCollection(body.getNeId()).insertOne(dbObject);
		return body;
	}

	public void deleteEquipmentsByNeId(String neId) throws Exception {
		getCollection(neId).deleteMany(new Document("neId", neId));
	}
	
	public void deleteEquipmentById(String neId, String id) throws Exception {
		getCollection(neId).deleteOne(and(eq("neId", neId), eq("id", id)));
	}
	
	public void deleteEquipmentByKeyOnNe(String neId, String keyOnNe) throws Exception {
		getCollection(neId).deleteOne(and(eq("neId", neId), eq("keyOnNe", keyOnNe)));
	}

	public void deleteEquipmentsUnderShelf(AdpEquipment body) throws Exception {
		String neId = body.getNeId();
		String keyOnNe = body.getKeyOnNe();
		Pattern pattern = Pattern.compile(keyOnNe + "(\\.[0-9]*)*");
		getCollection(neId).deleteMany(and(regex("keyOnNe", pattern), eq("neId", neId)));
	}

	public void replaceEquipment(AdpEquipment body) throws Exception
	{		
		deleteEquipmentById(body.getNeId(), body.getId());
		addEquipment(body);
	}
	
	public void updateEquipment(AdpEquipment body) throws Exception {
		String id = body.getId();
		AdpEquipment eq = getEquipmentById(body.getNeId(), id);
		updateExpectedType(body, id, eq);
	}

	private void updateExpectedType(AdpEquipment body, String id, AdpEquipment eq) throws Exception {
		String expectedType = body.getExpectedType();
		String expectedTypeFromDb = eq.getExpectedType();
		if (null == expectedType || StringUtils.equals(expectedType, expectedTypeFromDb)) {
			return;
		}
		getCollection(body.getNeId()).updateOne(new BasicDBObject("id", id), set("expectedType", expectedType));
		NotificationSender.instance().sendAvcNotif(EntityType.BOARD, id, "expectedType", expectedType,
				expectedTypeFromDb);
	}

	public AdpEquipment getEquipmentById(String neId, String id) throws Exception {
		List<Document> docList = getCollection(neId).find(and(eq("neId", neId), eq("id", id))).into(new ArrayList<Document>());

		if (null == docList || docList.isEmpty()) {
			log.error("can not find equipment, query by id = " + id + " and neId = " + neId);
			return new AdpEquipment();
		}

		log.debug(docList.size());
		for (Document doc : docList) {
			log.debug(doc.toJson());
		}

		Document doc = docList.get(0);
		AdpEquipment equipment = constructEquipment(doc);
		return equipment;
	}

	public List<AdpEquipment> getEquipmentsByNeId(String neId) throws Exception {
		System.out.println("getEquipmentsByNeId, neId = " + neId);
		List<Document> docList = getCollection(neId).find(eq("neId", neId)).into(new ArrayList<Document>());
		if (null == docList || docList.isEmpty()) {
			log.error("can not find equipment, query by neid = " + neId);
			return new ArrayList<AdpEquipment>();
		}

		log.debug(docList.size());
		for (Document doc : docList) {
			log.debug(doc.toJson());
		}

		List<AdpEquipment> equipmentList = new ArrayList<AdpEquipment>();
		for (Document doc : docList) {
			AdpEquipment equipment = constructEquipment(doc);
			equipmentList.add(equipment);
		}
		return equipmentList;
	}

	public String getIdByKeyOnNe(String neId, String keyOnNe) throws Exception {
		AdpEquipment equipment = getEquipmentByKeyOnNe(neId, keyOnNe);
		if (equipment != null) {
			return equipment.getId();
		}
		return null;
	}

	public AdpEquipment getEquipmentByKeyOnNe(String neId, String keyOnNe) throws Exception {
		List<Document> docList = getCollection(neId).find(and(eq("keyOnNe", keyOnNe), eq("neId", neId)))
				.into(new ArrayList<Document>());

		if (null == docList || docList.isEmpty()) {
			log.error("can not find equipment, query by keyOnNe = " + keyOnNe + " and neId = " + neId);
			return null;
		}
		Document doc = docList.get(0);
		return constructEquipment(doc);
	}
	
	private AdpEquipment constructEquipment(Document doc) {
		AdpEquipment equipment = gson.fromJson(doc.toJson(), AdpEquipment.class);
		return equipment;
	}
}