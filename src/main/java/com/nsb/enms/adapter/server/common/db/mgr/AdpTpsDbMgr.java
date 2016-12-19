package com.nsb.enms.adapter.server.common.db.mgr;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.Filters.or;
import static com.mongodb.client.model.Updates.set;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import com.nsb.enms.adapter.server.common.constants.AdpDBConst;
import com.nsb.enms.adapter.server.common.db.mongodb.mgr.AdpMongoDBMgr;
import com.nsb.enms.common.ManagedObjectType;
import com.nsb.enms.restful.model.adapter.AdpTp;

public class AdpTpsDbMgr {
	private final static Logger log = LogManager.getLogger(AdpTpsDbMgr.class);
	private MongoDatabase db = AdpMongoDBMgr.getInstance().getDatabase();
	private MongoCollection<Document> dbc = db.getCollection(AdpDBConst.DB_NAME_TP);
	private MongoCollection<BasicDBObject> dbc1 = db.getCollection(AdpDBConst.DB_NAME_TP, BasicDBObject.class);
	private Gson gson = new Gson();

	public List<AdpTp> addTps(List<AdpTp> body) throws Exception {
		for (AdpTp tp : body) {
			addTp(tp);
		}

		return body;
	}

	public AdpTp addTp(AdpTp tp) throws Exception {
		String gsonTp = gson.toJson(tp);
		BasicDBObject dbObject = (BasicDBObject) JSON.parse(gsonTp);
		dbc1.insertOne(dbObject);
		return tp;
	}

	public AdpTp getTpById(Integer tpid) throws Exception {
		List<Document> docList = dbc.find(eq("id", tpid)).into(new ArrayList<Document>());

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

	public AdpTp getTpById(Integer neId, Integer tpid) throws Exception {
		List<Document> docList = dbc.find(and(eq("neId", neId), eq("id", tpid))).into(new ArrayList<Document>());

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

	public AdpTp getTpByKeyOnNe(Integer neId, String keyOnNe) throws Exception {
		List<Document> docList = dbc.find(and(eq("neId", neId), eq("keyOnNe", keyOnNe)))
				.into(new ArrayList<Document>());

		if (null == docList || docList.isEmpty()) {
			log.error("can not find tp, query by keyOnNe = " + keyOnNe);
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
		return tp;
	}

	public List<AdpTp> getTpsByNeId(Integer neid) throws Exception {
		log.debug("getTPByNEId, neId = " + neid);
		List<Document> docList = dbc
				.find(and(eq("neId", neid),
						in("layerRates", ManagedObjectType.STM1_OPTICAL, ManagedObjectType.STM4_OPTICAL,
								ManagedObjectType.STM16_OPTICAL, ManagedObjectType.STM64_OPTICAL,
								ManagedObjectType.STM256_OPTICAL, ManagedObjectType.STM1_ELECTRICAL)))
				.into(new ArrayList<Document>());
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

	// TODO layerRate在数据库中保存的是List，这里传入的String，这个方法是有问题的
	public List<AdpTp> getTpsByLayerRate(Integer neid, String layerrate) throws Exception {
		List<Document> docList = null;
		if ((null == neid || neid < 0) && StringUtils.isEmpty(layerrate)) {
			docList = dbc.find().into(new ArrayList<Document>());
		} else if (StringUtils.isEmpty(layerrate)) {
			docList = dbc.find(eq("neId", neid)).into(new ArrayList<Document>());
		} else if (null == neid || neid < 0) {
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
						dbc.updateOne(new BasicDBObject("id", body.getId()), set(f.getName(), f.get(body).toString()));
					}
				}

			} catch (Exception e) {
				log.error("updateTp", e);
				return false;
			}
		}
		return true;
	}

	public void updateTpLayerRate(Integer tpId, List<String> layerRates) throws Exception {
		dbc.updateOne(eq("id", tpId), set("layerRates", layerRates));
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

	public List<AdpTp> getTpsByType(Integer neid, String tptype) throws Exception {
		List<Document> docList = null;
		docList = dbc.find(and(eq("neId", neid), eq("tpType", tptype))).into(new ArrayList<Document>());

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

	public void deleteTpsbyNeId(Integer neid) throws Exception {
		dbc.deleteMany(new Document("neId", neid));
	}

	public List<AdpTp> getCtpsByTpId(Integer neid, String ptpid) throws Exception {
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

	public List<AdpTp> getChildrenTps(Integer tpid) throws Exception {
		Date begin = new Date();
		log.debug("getChildrenTps, tpid = {}", tpid);

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

	public List<AdpTp> getChildrenTps(Integer neId, Integer tpId) throws Exception {
		Date begin = new Date();
		log.debug("getChildrenTps, neId = {}, tpid = {}", neId, tpId);

		@SuppressWarnings("unchecked")
		Bson filter = and(eq("neId", neId), eq("parentTpID", tpId),
				in("layerRates", ManagedObjectType.VC12.getLayerRates(), ManagedObjectType.TU12.getLayerRates(),
						ManagedObjectType.VC3.getLayerRates(), ManagedObjectType.TU3.getLayerRates()));
		List<Document> docList = dbc.find(filter).into(new ArrayList<Document>());

		if (null == docList || docList.isEmpty()) {
			log.error("can not find children tps, query by tpid = {}", tpId);
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

	public AdpTp getTpByLayerRateAndTimeSlot(String suffixId, String layerRate) throws Exception {
		Date begin = new Date();
		log.debug("getTpByLayerRateAndTimeSlot, suffixId = {}, layerRate = {}", suffixId, layerRate);

		BasicDBObject q = new BasicDBObject();
		q.put("id", Pattern.compile(""));

		Bson filter = and(q, eq("tpType", layerRate));
		List<Document> docList = dbc.find(filter).into(new ArrayList<Document>());

		if (null == docList || docList.isEmpty()) {
			log.error("can not find tp, query by suffixId = {}", suffixId);
			return new AdpTp();
		}

		log.debug(docList.size());
		for (Document doc : docList) {
			log.debug(doc.toJson());
		}

		AdpTp tp = constructTp(docList.get(0));

		Date end = new Date();
		log.debug("getTpByLayerRateAndTimeSlot cost time = " + (end.getTime() - begin.getTime()));
		return tp;
	}

	public AdpTp getTpByTimeSlot(String suffixId) throws Exception {
		Date begin = new Date();
		log.debug("getTpByTimeSlot, suffixId = {}", suffixId);

		BasicDBObject filter = new BasicDBObject();
		filter.put("id", Pattern.compile(""));

		List<Document> docList = dbc.find(filter).into(new ArrayList<Document>());

		if (null == docList || docList.isEmpty()) {
			log.error("can not find tp, query by suffixId = {}", suffixId);
			return new AdpTp();
		}

		log.debug(docList.size());
		for (Document doc : docList) {
			log.debug(doc.toJson());
		}

		AdpTp tp = constructTp(docList.get(0));

		Date end = new Date();
		log.debug("getTpByTimeSlot cost time = " + (end.getTime() - begin.getTime()));
		return tp;
	}

	public Integer getIdByKeyOnNe(Integer neId, String keyOnNe) throws Exception {
		List<Document> docList = dbc.find(and(eq("keyOnNe", keyOnNe), eq("neId", neId)))
				.into(new ArrayList<Document>());

		if (null == docList || docList.isEmpty()) {
			log.error("can not find tp, query by keyOnNe = " + keyOnNe + " and neId = " + neId);
			return null;
		}

		Document doc = docList.get(0);
		AdpTp tp = constructTp(doc);
		return tp.getId();
	}

	public AdpTp getTpByParentIdAndLayerRate(Integer parentId, List<String> layerRates) throws Exception {
		log.debug("getTpByParentIdAndLayerRate, parentId = {}", parentId);

		List<Document> docList = dbc.find(and(eq("parentTpId", parentId), eq("layerRates", layerRates)))
				.into(new ArrayList<Document>());

		if (null == docList || docList.isEmpty()) {
			log.error("can not find tp, query by parentId = " + parentId);
			return null;
		}

		Document doc = docList.get(0);
		return constructTp(doc);
	}

	public AdpTp getTpByIdAndLayerRate(Integer tpId, List<String> layerRates) throws Exception {
		log.debug("getTpByIdAndLayerRate, tpId = {}", tpId);

		List<Document> docList = dbc.find(and(eq("id", tpId), eq("layerRates", layerRates)))
				.into(new ArrayList<Document>());

		if (null == docList || docList.isEmpty()) {
			log.error("can not find tp, query by tpId = " + tpId);
			return null;
		}

		Document doc = docList.get(0);
		return constructTp(doc);
	}
}