package com.nsb.enms.adapter.server.db.mgr;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.Filters.or;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.types.ObjectId;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import com.nsb.enms.adapter.server.db.mongodb.constant.DBConst;
import com.nsb.enms.adapter.server.db.mongodb.mgr.MongoDBMgr;
import com.nsb.enms.restful.model.adapter.Xc;

public class XcsDbMgr {
	private final static Logger log = LogManager.getLogger(XcsDbMgr.class);
	private MongoDatabase db = MongoDBMgr.getInstance().getDatabase();
	private MongoCollection<Document> dbc = db.getCollection(DBConst.DB_NAME_XC);
	private MongoCollection<BasicDBObject> dbc1 = db.getCollection(DBConst.DB_NAME_XC, BasicDBObject.class);
	private Gson gson = new Gson();

	public Response createXc(Xc body) throws Exception {
		String json = gson.toJson(body);
		BasicDBObject dbObject = (BasicDBObject) JSON.parse(json);
		dbc1.insertOne(dbObject);
		body.setId(dbObject.getObjectId("_id").toString());
		return Response.ok().entity(body).build();
	}

	public Response deleteXc(String xcid) throws Exception {
		dbc.deleteOne(new BasicDBObject("_id", new ObjectId(xcid)));
		return Response.ok().build();
	}

	public Response findXcsByTpId(String tpid) throws Exception {
		List<Document> docList = dbc.find(or(in("atps", tpid), in("ztps", tpid))).into(new ArrayList<Document>());
		if (null == docList || docList.isEmpty()) {
			log.error("can not find xc, query by tpid = {}", tpid);
			return Response.ok().entity(new ArrayList<Xc>()).build();
		}

		log.debug(docList.size());
		for (Document doc : docList) {
			log.debug(doc.toJson());
		}

		List<Xc> xcList = new ArrayList<Xc>();
		for (Document doc : docList) {
			Xc tp = constructXC(doc);
			xcList.add(tp);
		}
		return Response.ok().entity(xcList).build();
	}

	public Response getXcById(String xcid) throws Exception {
		BasicDBObject query = new BasicDBObject("_id", new ObjectId(xcid));
		List<Document> docList = dbc.find(query).into(new ArrayList<Document>());

		if (null == docList || docList.isEmpty()) {
			log.error("can not find xc, query by id = {}", xcid);
			return Response.ok().entity(new Xc()).build();
		}

		log.debug(docList.size());
		for (Document doc : docList) {
			log.debug(doc.toJson());
		}

		Document doc = docList.get(0);
		Xc xc = constructXC(doc);
		return Response.ok().entity(xc).build();
	}

	private Xc constructXC(Document doc) {
		Xc xc = gson.fromJson(doc.toJson(), Xc.class);
		xc.setId(doc.getObjectId("_id").toString());
		return xc;
	}

	public void deleteXcsByNeId(String neId) throws Exception {
		dbc.deleteMany(new Document("neId", neId));
	}

	public Response getXcsByNeId(String neId, SecurityContext arg1) throws Exception {
		System.out.println("getXcsByNeId, neId = " + neId);
		List<Document> docList = dbc.find(eq("neId", neId)).into(new ArrayList<Document>());
		if (null == docList || docList.isEmpty()) {
			log.error("can not find xc, query by neid = " + neId);
			return Response.ok().entity(new ArrayList<Xc>()).build();
		}

		log.debug(docList.size());
		for (Document doc : docList) {
			log.debug(doc.toJson());
		}

		List<Xc> xcList = new ArrayList<Xc>();
		for (Document doc : docList) {
			Xc xc = constructXC(doc);
			xcList.add(xc);
		}
		return Response.ok().entity(xcList).build();
	}
}
