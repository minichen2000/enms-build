package com.nsb.enms.adapter.server.common.db.mgr;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.Filters.or;

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
import com.nsb.enms.adapter.server.common.constants.AdpDBConst;
import com.nsb.enms.adapter.server.common.db.mongodb.mgr.AdpMongoDBMgr;
import com.nsb.enms.restful.model.adapter.AdpXc;

public class AdpXcsDbMgr {
	private final static Logger log = LogManager.getLogger(AdpXcsDbMgr.class);
	private MongoDatabase db = AdpMongoDBMgr.getInstance().getDatabase();
	private Gson gson = new Gson();

	private MongoCollection<BasicDBObject> getCustomCollection(String neId) {
		return db.getCollection(AdpDBConst.DB_NAME_XC + "_" + neId, BasicDBObject.class);
	}

	private MongoCollection<Document> getCollection(String neId) {
		return db.getCollection(AdpDBConst.DB_NAME_XC + "_" + neId);
	}

	public AdpXc createXC(AdpXc body) throws Exception {
		String json = gson.toJson(body);
		BasicDBObject dbObject = (BasicDBObject) JSON.parse(json);
		getCustomCollection(body.getNeId()).insertOne(dbObject);
		return body;
	}

	public void deleteXC(String neID, String xcid) throws Exception {
		getCollection(neID).deleteOne(new Document("id", xcid));
	}

	public List<AdpXc> findXCsByTPID(String neID, String tpid) throws Exception {
		List<Document> docList = getCollection(neID).find(or(in("aEndPoints", tpid), in("zEndPoints", tpid)))
				.into(new ArrayList<Document>());
		if (null == docList || docList.isEmpty()) {
			return new ArrayList<AdpXc>();
		}

		List<AdpXc> xcList = new ArrayList<AdpXc>();
		for (Document doc : docList) {
			AdpXc tp = constructXC(doc);
			xcList.add(tp);
		}
		return xcList;
	}

	public AdpXc getXCByID(String neID, String xcID) throws Exception {
		List<Document> docList = getCollection(neID).find(and(eq("neId", neID), eq("id", xcID)))
				.into(new ArrayList<Document>());

		if (null == docList || docList.isEmpty()) {
			return new AdpXc();
		}

		Document doc = docList.get(0);
		AdpXc xc = constructXC(doc);
		return xc;
	}

	private AdpXc constructXC(Document doc) {
		AdpXc xc = gson.fromJson(doc.toJson(), AdpXc.class);
		return xc;
	}

	public void deleteXCsByNeId(String neId) throws Exception {
		getCollection(neId).deleteMany(new Document("neId", neId));
	}

	public List<AdpXc> getXCsByNeId(String neId) throws Exception {
		List<Document> docList = getCollection(neId).find(eq("neId", neId)).into(new ArrayList<Document>());
		if (null == docList || docList.isEmpty()) {
			return new ArrayList<AdpXc>();
		}

		List<AdpXc> xcList = new ArrayList<AdpXc>();
		for (Document doc : docList) {
			AdpXc xc = constructXC(doc);
			xcList.add(xc);
		}
		return xcList;
	}

	public String getIdByKeyOnNe(String neId, String keyOnNe) throws Exception {
		List<Document> docList = getCollection(neId).find(and(eq("keyOnNe", keyOnNe), eq("neId", neId)))
				.into(new ArrayList<Document>());
		if (null == docList || docList.isEmpty()) {
			return null;
		}

		Document doc = docList.get(0);
		AdpXc xc = constructXC(doc);
		return xc.getId();
	}
}
