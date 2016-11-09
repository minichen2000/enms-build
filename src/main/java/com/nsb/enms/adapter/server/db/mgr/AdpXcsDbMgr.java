package com.nsb.enms.adapter.server.db.mgr;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.Filters.or;

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
import com.mongodb.util.JSON;
import com.nsb.enms.adapter.server.db.mongodb.constant.AdpDBConst;
import com.nsb.enms.adapter.server.db.mongodb.mgr.AdpMongoDBMgr;
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
		body.setId(dbObject.getObjectId("_id").toString());
		return body;
	}

	public void deleteXc(String xcid) throws Exception {
		dbc.deleteOne(new BasicDBObject("_id", new ObjectId(xcid)));
	}

	public List<AdpXc> findXcsByTpId(String tpid) throws Exception {
		List<Document> docList = dbc.find(or(in("atps", tpid), in("ztps", tpid))).into(new ArrayList<Document>());
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
		//BasicDBObject query = new BasicDBObject("_id", new ObjectId(xcid));
		List<Document> docList = dbc.find(eq( "id", xcid )).into(new ArrayList<Document>());

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
		//xc.setId(doc.getObjectId("_id").toString());
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
	
	public String getIdByAid(String aid) throws Exception {
	    List<Document> docList = dbc.find(eq("aid", aid)).into(new ArrayList<Document>());
        if (null == docList || docList.isEmpty()) {
            log.error("can not find xc, query by aid = {}", aid);
            return null;
        }
        
        Document doc = docList.get(0);
        AdpXc xc = constructXC(doc);
        return xc.getId();
    }
}
