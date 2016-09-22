package com.nsb.enms.adapter.server.db.mgr;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.types.ObjectId;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.nsb.enms.adapter.server.db.mongodb.constant.DBConst;
import com.nsb.enms.adapter.server.db.mongodb.mgr.MongoDBMgr;
import com.nsb.enms.restful.model.adapter.AdpEquipment;

public class EquipmentsDbMgr {
	private final static Logger log = LogManager.getLogger(EquipmentsDbMgr.class);
	private MongoDatabase db = MongoDBMgr.getInstance().getDatabase();
	private MongoCollection<Document> dbc = db.getCollection(DBConst.DB_NAME_EQUIPMENT);
	private Gson gson = new Gson();

	public void deleteEquipmentsByNeId(String neId) throws Exception {
		dbc.deleteMany(new Document("neId", neId));
	}

	public AdpEquipment getEquipmentById(String neId) throws Exception {
		BasicDBObject query = new BasicDBObject("_id", new ObjectId(neId));
		List<Document> docList = dbc.find(query).into(new ArrayList<Document>());

		if (null == docList || docList.isEmpty()) {
			log.error("can not find equipment, query by id = {}", neId);
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

	private AdpEquipment constructEquipment(Document doc) {
	    AdpEquipment equipment = gson.fromJson(doc.toJson(), AdpEquipment.class);
		equipment.setId(doc.getObjectId("_id").toString());
		return equipment;
	}
}
