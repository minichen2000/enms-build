package com.nsb.enms.adapter.server.db.mgr;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
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
import com.nsb.enms.adapter.server.db.mongodb.mgr.MaxNeIdMgr;
import com.nsb.enms.adapter.server.db.mongodb.mgr.MongoDBMgr;
import com.nsb.enms.restful.model.adapter.Ne;

public class NesDbMgr {
	private final static Logger log = LogManager.getLogger(NesDbMgr.class);
	private MongoDatabase db = MongoDBMgr.getInstance().getDatabase();
	private MongoCollection<Document> dbc = db.getCollection(DBConst.DB_NAME_NE);
	private MongoCollection<BasicDBObject> dbc1 = db.getCollection(DBConst.DB_NAME_NE, BasicDBObject.class);
	private Gson gson = new Gson();

	public Ne addNe(Ne body) throws Exception {
		log.debug("body=" + body);
		String ne = gson.toJson(body);
		log.debug("ne=" + ne);

		BasicDBObject dbObject = (BasicDBObject) JSON.parse(ne);
		dbc1.insertOne(dbObject);

		String id = dbObject.getObjectId("_id").toString();
		body.setId(id);

		return body;
	}

	public Response deleteNe(String neid) throws Exception {
		dbc.deleteOne(new BasicDBObject("_id", new ObjectId(neid)));
		return Response.ok().build();
	}

	public Ne getNeById(String neid) throws Exception {
		List<Document> docList = dbc.find(new BasicDBObject("_id", new ObjectId(neid))).into(new ArrayList<Document>());
		if (null == docList || docList.isEmpty()) {
			log.error("can not find ne, query by neId = " + neid);
			return new Ne();
		}
		Document doc = docList.get(0);
		Ne ne = constructNe(doc);
		return ne;
	}

	private Ne constructNe(Document doc) {
		Ne ne = gson.fromJson(doc.toJson(), Ne.class);
		ne.setId(doc.getObjectId("_id").toString());
		return ne;
	}

	@SuppressWarnings("rawtypes")
	public Response updateNe(Ne body) throws Exception {
		for (Field f : body.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (List.class == f.getType()) {
					if (!((List) f.get(body)).isEmpty()) {
						// TODO 更新list中的数据
					}
				} else {
					Object obj = f.get(body);
					if (null != obj && !(obj.toString().equalsIgnoreCase("id"))) {
						dbc.updateOne(new BasicDBObject("_id", new ObjectId(body.getId())),
								set(f.getName(), f.get(body).toString()));
					}
				}

			} catch (Exception e) {
				log.error("updateNe", e);
			}
		}
		return Response.ok().build();
	}

	public List<Ne> findNesByType(String netype) throws Exception {
		Date begin = new Date();
		List<Document> docList = dbc.find(eq("neType", netype)).into(new ArrayList<Document>());
		if (null == docList || docList.isEmpty()) {
			log.error("can not find ne");
			return new ArrayList<Ne>();
		}
		List<Ne> neList = new ArrayList<Ne>();
		for (Document doc : docList) {
			Ne ne = constructNe(doc);
			neList.add(ne);
			System.out.println(ne);
		}
		Date end = new Date();
		log.debug("findNeByType, cost time = " + (end.getTime() - begin.getTime()));
		return neList;
	}

	public List<Ne> findNeByTypeVersion(String netype, String neversion) throws Exception {
		Date begin = new Date();
		List<Document> docList = dbc.find(and(eq("neType", netype), eq("version", neversion)))
				.into(new ArrayList<Document>());
		if (null == docList || docList.isEmpty()) {
			log.error("can not find ne");
			return new ArrayList<Ne>();
		}
		List<Ne> neList = new ArrayList<Ne>();
		for (Document doc : docList) {
			Ne ne = constructNe(doc);
			neList.add(ne);
			System.out.println(ne);
		}
		Date end = new Date();
		log.debug("findNeByTypeVersion, cost time = " + (end.getTime() - begin.getTime()));
		return neList;
	}

	public List<Ne> findNesByVersion(String neversion) throws Exception {
		Date begin = new Date();
		List<Document> docList = dbc.find(eq("version", neversion)).into(new ArrayList<Document>());
		if (null == docList || docList.isEmpty()) {
			log.error("can not find ne");
			return new ArrayList<Ne>();
		}
		List<Ne> neList = new ArrayList<Ne>();
		for (Document doc : docList) {
			Ne ne = constructNe(doc);
			neList.add(ne);
			System.out.println(ne);
		}
		Date end = new Date();
		log.debug("findNeByVersion, cost time = " + (end.getTime() - begin.getTime()));
		return neList;
	}

	public List<Ne> getNes() throws Exception {
		Date begin = new Date();
		List<Document> docList = dbc.find().into(new ArrayList<Document>());
		if (null == docList || docList.isEmpty()) {
			log.error("can not find ne");
			return new ArrayList<Ne>();
		}
		List<Ne> neList = new ArrayList<Ne>();
		for (Document doc : docList) {
			Ne ne = constructNe(doc);
			neList.add(ne);
			System.out.println(ne);
		}
		Date end = new Date();
		log.debug("nesGet, cost time = " + (end.getTime() - begin.getTime()));

		return neList;
	}

	public Response getMaxNeId(SecurityContext securityContext) throws Exception {
		String maxNeId = MaxNeIdMgr.getId();
		return Response.ok().entity(maxNeId).build();
	}

	public Response updateMaxNeId(String body) throws Exception {
		MaxNeIdMgr.updateId(body);
		return Response.ok().build();
	}
}
