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
	private MongoCollection<Document> dbc = db.getCollection(AdpDBConst.DB_NAME_XC);
	private MongoCollection<BasicDBObject> dbc1 = db.getCollection(AdpDBConst.DB_NAME_XC, BasicDBObject.class);
	private Gson gson = new Gson();

	public AdpXc createXc(AdpXc body) throws Exception {
		String json = gson.toJson(body);
		BasicDBObject dbObject = (BasicDBObject) JSON.parse(json);
		dbc1.insertOne(dbObject);
		return body;
	}

	public void deleteXc(String xcid) throws Exception {
		dbc.deleteOne(new Document("id", xcid));
	}

	public List<AdpXc> findXcsByTpId(String tpid) throws Exception {
		List<Document> docList = dbc.find(or(in("aEndPoints", tpid), in("zEndPoints", tpid)))
				.into(new ArrayList<Document>());
		if (null == docList || docList.isEmpty()) {
			log.error("can not find xc, query by tpid = {}", tpid);
			return new ArrayList<AdpXc>();
		}

		log.debug(docList.size());
		for (Document doc : docList) {
			log.debug(doc.toJson());
		}

		List<AdpXc> xcList = new ArrayList<AdpXc>();
		for (Document doc : docList) {
			AdpXc tp = constructXC(doc);
			xcList.add(tp);
		}
		return xcList;
	}

	public AdpXc getXcById(String xcid) throws Exception {
		Document query = new Document("id", xcid);
		List<Document> docList = dbc.find(query).into(new ArrayList<Document>());

		if (null == docList || docList.isEmpty()) {
			log.error("can not find xc, query by id = {}", xcid);
			return new AdpXc();
		}

		log.debug(docList.size());
		for (Document doc : docList) {
			log.debug(doc.toJson());
		}

		Document doc = docList.get(0);
		AdpXc xc = constructXC(doc);
		return xc;
	}

	public AdpXc getXcById(String neid, String xcid) throws Exception {
		List<Document> docList = dbc.find(and(eq("neId", neid), eq("id", xcid))).into(new ArrayList<Document>());

		if (null == docList || docList.isEmpty()) {
			log.error("can not find xc, query by id = {}", xcid);
			return new AdpXc();
		}

		log.debug(docList.size());
		for (Document doc : docList) {
			log.debug(doc.toJson());
		}

		Document doc = docList.get(0);
		AdpXc xc = constructXC(doc);
		return xc;
	}

	private AdpXc constructXC(Document doc) {
		AdpXc xc = gson.fromJson(doc.toJson(), AdpXc.class);
		return xc;
	}

	public void deleteXcsByNeId(String neId) throws Exception {
		dbc.deleteMany(new Document("neId", neId));
	}

	public List<AdpXc> getXcsByNeId(String neId) throws Exception {
		System.out.println("getXcsByNeId, neId = " + neId);
		List<Document> docList = dbc.find(eq("neId", neId)).into(new ArrayList<Document>());
		if (null == docList || docList.isEmpty()) {
			log.error("can not find xc, query by neid = " + neId);
			return new ArrayList<AdpXc>();
		}

		log.debug(docList.size());
		for (Document doc : docList) {
			log.debug(doc.toJson());
		}

		List<AdpXc> xcList = new ArrayList<AdpXc>();
		for (Document doc : docList) {
			AdpXc xc = constructXC(doc);
			xcList.add(xc);
		}
		return xcList;
	}

	public String getIdByKeyOnNe(String neId, String keyOnNe) throws Exception {
		List<Document> docList = dbc.find(and(eq("keyOnNe", keyOnNe), eq("neId", neId)))
				.into(new ArrayList<Document>());
		if (null == docList || docList.isEmpty()) {
			log.error("can not find xc, query by keyOnNe = " + keyOnNe + " and neId = " + neId);
			return null;
		}

		Document doc = docList.get(0);
		AdpXc xc = constructXC(doc);
		return xc.getId();
	}
}
