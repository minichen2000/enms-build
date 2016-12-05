package com.nsb.enms.adapter.server.db.mgr;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import com.nsb.enms.adapter.server.db.mongodb.constant.AdpDBConst;
import com.nsb.enms.adapter.server.db.mongodb.mgr.AdpMongoDBMgr;
import com.nsb.enms.restful.model.adapter.AdpEquipment;

public class AdpEqusDbMgr {
	private final static Logger log = LogManager.getLogger(AdpEqusDbMgr.class);
	private MongoDatabase db = AdpMongoDBMgr.getInstance().getDatabase();
	private MongoCollection<Document> dbc = db.getCollection(AdpDBConst.DB_NAME_EQUIPMENT);
	private MongoCollection<BasicDBObject> dbc1 = db.getCollection(AdpDBConst.DB_NAME_EQUIPMENT, BasicDBObject.class);
	private Gson gson = new Gson();

	public AdpEquipment addEquipment(AdpEquipment body) throws Exception {
		log.debug("body=" + body);
		String equipment = gson.toJson(body);
		log.debug("equipment=" + equipment);

		BasicDBObject dbObject = (BasicDBObject) JSON.parse(equipment);
		dbc1.insertOne(dbObject);

		return body;
	}

	public void deleteEquipmentsByNeId(int neId) throws Exception {
		dbc.deleteMany(new Document("neId", neId));
	}

	public AdpEquipment getEquipmentById(int id) throws Exception {
		List<Document> docList = dbc.find(eq("id", id)).into(new ArrayList<Document>());

		if (null == docList || docList.isEmpty()) {
			log.error("can not find equipment, query by id = {}", id);
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

	public List<AdpEquipment> getEquipmentsByNeId(Integer neId) throws Exception {
		System.out.println("getEquipmentsByNeId, neId = " + neId);
		List<Document> docList = dbc.find(eq("neId", neId)).into(new ArrayList<Document>());
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

	public Integer getIdByAid(String aid) throws Exception {
		List<Document> docList = dbc.find(eq("aid", aid)).into(new ArrayList<Document>());

		if (null == docList || docList.isEmpty()) {
			log.error("can not find equipment, query by aid = {}", aid);
			return null;
		}
		Document doc = docList.get(0);
		AdpEquipment equipment = constructEquipment(doc);
		return equipment.getId();
	}

	private AdpEquipment constructEquipment(Document doc) {
		AdpEquipment equipment = gson.fromJson(doc.toJson(), AdpEquipment.class);
		return equipment;
	}
}
