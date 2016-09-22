package com.nsb.enms.adapter.server.db.mgr;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.or;
import static com.mongodb.client.model.Updates.set;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import com.nsb.enms.adapter.server.db.mongodb.constant.DBConst;
import com.nsb.enms.adapter.server.db.mongodb.mgr.MongoDBMgr;
import com.nsb.enms.restful.model.adapter.AdpTp;

public class TpsDbMgr {
	private final static Logger log = LogManager.getLogger(TpsDbMgr.class);
	private MongoDatabase db = MongoDBMgr.getInstance().getDatabase();
	private MongoCollection<Document> dbc = db.getCollection(DBConst.DB_NAME_TP);
	private MongoCollection<BasicDBObject> dbc1 = db.getCollection(DBConst.DB_NAME_TP, BasicDBObject.class);
	private Gson gson = new Gson();

	public List<AdpTp> addTps(List<AdpTp> body) throws Exception {
		// String tps = gson.toJson(body);

		// @SuppressWarnings("unchecked")
		// List<BasicDBObject> dbObject = (List<BasicDBObject>) JSON.parse(tps);
		// dbc1.insertMany(dbObject);

		for (AdpTp tp : body) {
			String gsonTp = gson.toJson(tp);
			BasicDBObject dbObject = (BasicDBObject) JSON.parse(gsonTp);
			dbc1.insertOne(dbObject);
			tp.setId(dbObject.getObjectId("_id").toString());
		}

		return body;
	}

	public AdpTp getTpById(String tpid) throws Exception {
		BasicDBObject query = new BasicDBObject("_id", new ObjectId(tpid));
		List<Document> docList = dbc.find(query).into(new ArrayList<Document>());

		if (null == docList || docList.isEmpty()) {
			log.error("can not find tp, query by tpid = " + tpid);
			return new AdpTp();
		}

		log.debug(docList.size());
		for (Document doc : docList) {
			log.debug(doc.toJson());
		}

		Document doc = docList.get(0);
		AdpTp tp = constructTp(doc);
		return tp;
	}

	private AdpTp constructTp(Document doc) {
	    AdpTp tp = gson.fromJson(doc.toJson(), AdpTp.class);
		//tp.setId(doc.getObjectId("_id").toString());
		return tp;
	}

	public List<AdpTp> getTpsByNeId(String neid) throws Exception {
		log.debug("getTPByNEId, neId = " + neid);
		List<Document> docList = dbc.find(eq("neId", neid)).into(new ArrayList<Document>());
		if (null == docList || docList.isEmpty()) {
			log.error("can not find tp, query by neid = " + neid);
			return new ArrayList<AdpTp>();
		}

		log.debug(docList.size());
		for (Document doc : docList) {
			log.debug(doc.toJson());
		}

		List<AdpTp> tpList = new ArrayList<AdpTp>();
		for (Document doc : docList) {
		    AdpTp tp = constructTp(doc);
			tpList.add(tp);
		}
		return tpList;
	}

	public List<AdpTp> getTpsByLayerRate(String neid, String layerrate) throws Exception {
		List<Document> docList = null;
		if (StringUtils.isEmpty(neid) && StringUtils.isEmpty(layerrate)) {
			docList = dbc.find().into(new ArrayList<Document>());
		} else if (StringUtils.isEmpty(layerrate)) {
			docList = dbc.find(eq("neId", neid)).into(new ArrayList<Document>());
		} else if (StringUtils.isEmpty(neid)) {
			docList = dbc.find(eq("layerRate", layerrate)).into(new ArrayList<Document>());
		} else {
			docList = dbc.find(and(eq("neId", neid), eq("layerRate", layerrate))).into(new ArrayList<Document>());
		}

		if (null == docList || docList.isEmpty()) {
			log.error("can not find tp, query by neid = " + neid + ", layerRate = " + layerrate);
			return new ArrayList<AdpTp>();
		}

		log.debug(docList.size());
		for (Document doc : docList) {
			log.debug(doc.toJson());
		}

		List<AdpTp> tpList = new ArrayList<AdpTp>();
		for (Document doc : docList) {
		    AdpTp tp = constructTp(doc);
			tpList.add(tp);
		}
		return tpList;
	}

	@SuppressWarnings("rawtypes")
	public boolean updateTp(AdpTp body) throws Exception {
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
						dbc.updateOne(new BasicDBObject("id", body.getId()),
								set(f.getName(), f.get(body).toString()));
					}
				}

			} catch (Exception e) {
				log.error("updateTp", e);
				return false;
			}
		}
		return true;
	}

	public List<AdpTp> getTps() throws Exception {
		Date begin = new Date();

		List<Document> docList = dbc.find().into(new ArrayList<Document>());

		if (null == docList || docList.isEmpty()) {
			log.error("can not find tp");
			return new ArrayList<AdpTp>();
		}

		log.debug(docList.size());
		for (Document doc : docList) {
			log.debug(doc.toJson());
		}

		List<AdpTp> tpList = new ArrayList<AdpTp>();
		for (Document doc : docList) {
		    AdpTp tp = constructTp(doc);
			tpList.add(tp);
		}

		Date end = new Date();
		log.debug("getTPs, cost time = " + (end.getTime() - begin.getTime()));

		return tpList;
	}

	public List<AdpTp> getTpsByType(String neid, String tptype) throws Exception {
		/*Date begin = new Date();
		log.debug("getTPs, tptype = " + tptype);

		List<Document> docList = null;
		if (StringUtils.isEmpty(tptype)) {
			docList = dbc.find().into(new ArrayList<Document>());
		} else {
			docList = dbc.find(eq("tpType", tptype)).into(new ArrayList<Document>());
		}

		if (null == docList || docList.isEmpty()) {
			log.error("can not find tp, query by tptype = " + tptype);
			return new ArrayList<AdpTp>();
		}

		log.debug(docList.size());
		for (Document doc : docList) {
			log.debug(doc.toJson());
		}

		List<AdpTp> tpList = new ArrayList<AdpTp>();
		for (Document doc : docList) {
		    AdpTp tp = constructTp(doc);
			tpList.add(tp);
		}

		Date end = new Date();
		log.debug("getTPsByType cost time = " + (end.getTime() - begin.getTime()));

		return tpList;*/
	    
	    List<Document> docList = null;
        /*if (StringUtils.isEmpty(neid) && StringUtils.isEmpty(tptype)) {
            docList = dbc.find().into(new ArrayList<Document>());
        } else if (StringUtils.isEmpty(tptype)) {
            docList = dbc.find(eq("neId", neid)).into(new ArrayList<Document>());
        } else if (StringUtils.isEmpty(neid)) {
            docList = dbc.find(eq("tpType", tptype)).into(new ArrayList<Document>());
        } else {*/
        docList = dbc.find(and(eq("neId", neid), eq("tpType", tptype))).into(new ArrayList<Document>());
        //}

        if (null == docList || docList.isEmpty()) {
            log.error("can not find tp, query by neid = " + neid + ", tptype = " + tptype);
            return new ArrayList<AdpTp>();
        }

        log.debug(docList.size());
        for (Document doc : docList) {
            log.debug(doc.toJson());
        }

        List<AdpTp> tpList = new ArrayList<AdpTp>();
        for (Document doc : docList) {
            AdpTp tp = constructTp(doc);
            tpList.add(tp);
        }
        return tpList;
	}

	public void deleteTpsbyNeId(String neid) throws Exception {
		dbc.deleteMany(new Document("neId", neid));
	}

	public List<AdpTp> getCtpsByTpId(String neid, String ptpid) throws Exception {
		Date begin = new Date();
		log.debug("getCTPsByTP, neid = {}, ptpid = {}", neid, ptpid);

		Bson filter = and(eq("neId", neid), eq("parentTpId", ptpid),
				or(eq("tpType", "au4CTPBidirectionalR1"), eq("tpType", "vc12PathTraceTTPBidirectional")));
		List<Document> docList = dbc.find(filter).into(new ArrayList<Document>());

		if (null == docList || docList.isEmpty()) {
			log.error("can not find ctp, query by neid = {} and ptpid = {}", neid, ptpid);
			return new ArrayList<AdpTp>();
		}

		log.debug(docList.size());
		for (Document doc : docList) {
			log.debug(doc.toJson());
		}

		List<AdpTp> tpList = new ArrayList<AdpTp>();
		for (Document doc : docList) {
		    AdpTp tp = constructTp(doc);
			tpList.add(tp);
		}

		Date end = new Date();
		log.debug("getCTPsByTP cost time = " + (end.getTime() - begin.getTime()));

		return tpList;
	}

	public List<AdpTp> getChildrenTps(String tpid) throws Exception {
		Date begin = new Date();
		log.debug("getCTPsByTP, tpid = {}", tpid);

		Bson filter = and(eq("parentTpId", tpid), eq("tpType", "tu12CTPBidirectionalR1"));
		List<Document> docList = dbc.find(filter).into(new ArrayList<Document>());

		if (null == docList || docList.isEmpty()) {
			log.error("can not find children tps, query by tpid = {}", tpid);
			return new ArrayList<AdpTp>();
		}

		log.debug(docList.size());
		for (Document doc : docList) {
			log.debug(doc.toJson());
		}

		List<AdpTp> tpList = new ArrayList<AdpTp>();
		for (Document doc : docList) {
		    AdpTp tp = constructTp(doc);
			tpList.add(tp);
		}

		Date end = new Date();
		log.debug("getChildrenTPs cost time = " + (end.getTime() - begin.getTime()));

		return tpList;
	}
}