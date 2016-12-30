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
import com.nsb.enms.adapter.server.common.constants.Protocols;
import com.nsb.enms.adapter.server.common.db.mgr.AdpNesDbMgr;
import com.nsb.enms.adapter.server.common.db.mongodb.mgr.AdpMongoDBMgr;
import com.nsb.enms.common.ManagedObjectType;
import com.nsb.enms.restful.model.adapter.AdpTp;

public class AdpTpsDbMgr {
	private final static Logger log = LogManager.getLogger(AdpTpsDbMgr.class);
	private MongoDatabase db = AdpMongoDBMgr.getInstance().getDatabase();
	// private MongoCollection<Document> dbc =
	// db.getCollection(AdpDBConst.DB_NAME_TP);
	// private MongoCollection<BasicDBObject> dbc1 =
	// db.getCollection(AdpDBConst.DB_NAME_TP, BasicDBObject.class);
	private Gson gson = new Gson();

	public AdpTpsDbMgr() {

	}

	private MongoCollection<BasicDBObject> getCustomCollection(String neId) {
		return db.getCollection(AdpDBConst.DB_NAME_TP + "_" + neId, BasicDBObject.class);
	}

	private MongoCollection<Document> getCollection(String neId) {
		return db.getCollection(AdpDBConst.DB_NAME_TP + "_" + neId);
	}

	public List<AdpTp> addTps(List<AdpTp> body) throws Exception {
		for (AdpTp tp : body) {
			addTp(tp);
		}

		return body;
	}

	public AdpTp addTp(AdpTp tp) throws Exception {
		String gsonTp = gson.toJson(tp);
		BasicDBObject dbObject = (BasicDBObject) JSON.parse(gsonTp);
		getCustomCollection(tp.getNeId()).insertOne(dbObject);
		return tp;
	}

	public AdpTp getTpById(String neId, String tpid) throws Exception {
		List<Document> docList = getCollection(neId).find(and(eq("neId", neId), eq("id", tpid)))
				.into(new ArrayList<Document>());

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

	// TODO 是否需要增加ptpId作为参数
	public AdpTp getTpByKeyOnNe(String neId, String keyOnNe) throws Exception {
		List<Document> docList = getCollection(neId).find(and(eq("neId", neId), eq("keyOnNe", keyOnNe)))
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

	public List<AdpTp> getTpsByNeId(String neid, Protocols protocols) throws Exception {
		log.debug("getTpsByNeId, neId = " + neid);
		List<Document> docList = new ArrayList<Document>();
		if (Protocols.ALUQ3 == protocols) {
			docList = getTpsByQ3Ne(neid);
		} else if (Protocols.SNMP == protocols) {
			docList = getTpsBySnmpNe(neid);
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

	private List<Document> getTpsByQ3Ne(String neId) {
		List<Document> docList = getCollection(neId)
				.find(and(eq("neId", neId),
						in("layerRates", ManagedObjectType.STM1_OPTICAL, ManagedObjectType.STM4_OPTICAL,
								ManagedObjectType.STM16_OPTICAL, ManagedObjectType.STM64_OPTICAL,
								ManagedObjectType.STM256_OPTICAL, ManagedObjectType.STM1_ELECTRICAL)))
				.into(new ArrayList<Document>());
		if (null == docList || docList.isEmpty()) {
			log.error("can not find q3 tp, query by neid = " + neId);
			return new ArrayList<Document>();
		}
		return docList;
	}

	private List<Document> getTpsBySnmpNe(String neId) {
		List<Document> docList = getCollection(neId).find(eq("neId", neId)).into(new ArrayList<Document>());
		if (null == docList || docList.isEmpty()) {
			log.error("can not find snmp tp, query by neid = " + neId);
			return new ArrayList<Document>();
		}
		return docList;
	}

	// TODO layerRate在数据库中保存的是List，这里传入的String，这个方法是有问题的
	public List<AdpTp> getTpsByLayerRate(String neId, String layerrate) throws Exception {
		MongoCollection<Document> dbc = getCollection(neId);
		List<Document> docList = null;
		if (StringUtils.isEmpty(neId) && StringUtils.isEmpty(layerrate)) {
			docList = dbc.find().into(new ArrayList<Document>());
		} else if (StringUtils.isEmpty(layerrate)) {
			docList = dbc.find(eq("neId", neId)).into(new ArrayList<Document>());
		} else if (StringUtils.isEmpty(neId)) {
			docList = dbc.find(eq("layerRate", layerrate)).into(new ArrayList<Document>());
		} else {
			docList = dbc.find(and(eq("neId", neId), eq("layerRate", layerrate))).into(new ArrayList<Document>());
		}

		if (null == docList || docList.isEmpty()) {
			log.error("can not find tp, query by neid = " + neId + ", layerRate = " + layerrate);
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
						getCollection(body.getNeId()).updateOne(new BasicDBObject("id", body.getId()),
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

	public void updateTpLayerRate(String neId, String tpId, List<String> layerRates) throws Exception {
		getCollection(neId).updateOne(eq("id", tpId), set("layerRates", layerRates));
	}

	public List<AdpTp> getTps(Protocols protocol) throws Exception {
		Date begin = new Date();
		AdpNesDbMgr nesDbMgr = new AdpNesDbMgr();
		List<String> idList = nesDbMgr.getNeIds();
		if (null == idList || idList.isEmpty()) {
			log.error("there is no ne found");
			return new ArrayList<AdpTp>();
		}

		List<AdpTp> allTpList = new ArrayList<AdpTp>();
		for (String i : idList) {
			List<AdpTp> tpList = getTpsByNeId(i, protocol);
			if (null == tpList || tpList.isEmpty()) {
				log.error("can not find tp list by neid :" + i);
				continue;
			}
			allTpList.addAll(tpList);
		}

		Date end = new Date();
		log.debug("getTPs, cost time = " + (end.getTime() - begin.getTime()));

		return allTpList;
	}

	public List<AdpTp> getTpsByType(String neId, String tptype) throws Exception {
		List<Document> docList = null;
		docList = getCollection(neId).find(and(eq("neId", neId), eq("tpType", tptype))).into(new ArrayList<Document>());

		if (null == docList || docList.isEmpty()) {
			log.error("can not find tp, query by neid = " + neId + ", tptype = " + tptype);
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

	public void deleteTpsbyNeId(String neId) throws Exception {
		getCollection(neId).deleteMany(new Document("neId", neId));
	}

	public List<AdpTp> getCtpsByTpId(String neId, String ptpid) throws Exception {
		Date begin = new Date();
		log.debug("getCTPsByTP, neid = {}, ptpid = {}", neId, ptpid);

		Bson filter = and(eq("neId", neId), eq("parentTpId", ptpid),
				or(eq("tpType", "au4CTPBidirectionalR1"), eq("tpType", "vc12PathTraceTTPBidirectional")));
		List<Document> docList = getCollection(neId).find(filter).into(new ArrayList<Document>());

		if (null == docList || docList.isEmpty()) {
			log.error("can not find ctp, query by neid = {} and ptpid = {}", neId, ptpid);
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

	public List<AdpTp> getChildrenTps(String neId, String tpId) throws Exception {
		Date begin = new Date();
		log.debug("getChildrenTps, neId = {}, tpid = {}", neId, tpId);

		@SuppressWarnings("unchecked")
		Bson filter = and(eq("neId", neId), eq("parentTpID", tpId),
				in("layerRates", ManagedObjectType.VC12.getLayerRates(), ManagedObjectType.TU12.getLayerRates(),
						ManagedObjectType.VC3.getLayerRates(), ManagedObjectType.TU3.getLayerRates()));
		List<Document> docList = getCollection(neId).find(filter).into(new ArrayList<Document>());

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

	public AdpTp getTpByLayerRateAndTimeSlot(String neId, String suffixId, String layerRate) throws Exception {
		Date begin = new Date();
		log.debug("getTpByLayerRateAndTimeSlot, suffixId = {}, layerRate = {}", suffixId, layerRate);

		BasicDBObject q = new BasicDBObject();
		q.put("id", Pattern.compile(""));

		Bson filter = and(q, eq("tpType", layerRate));
		List<Document> docList = getCollection(neId).find(filter).into(new ArrayList<Document>());

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

	public AdpTp getTpByTimeSlot(String neId, String suffixId) throws Exception {
		Date begin = new Date();
		log.debug("getTpByTimeSlot, suffixId = {}", suffixId);

		BasicDBObject filter = new BasicDBObject();
		filter.put("id", Pattern.compile(""));

		List<Document> docList = getCollection(neId).find(filter).into(new ArrayList<Document>());

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

	public String getIdByKeyOnNe(String neId, String keyOnNe) throws Exception {
		List<Document> docList = getCollection(neId).find(and(eq("keyOnNe", keyOnNe), eq("neId", neId)))
				.into(new ArrayList<Document>());

		if (null == docList || docList.isEmpty()) {
			log.error("can not find tp, query by keyOnNe = " + keyOnNe + " and neId = " + neId);
			return null;
		}

		Document doc = docList.get(0);
		AdpTp tp = constructTp(doc);
		return tp.getId();
	}

	public AdpTp getTpByParentIdAndLayerRate(String neId, String parentId, List<String> layerRates) throws Exception {
		log.debug("getTpByParentIdAndLayerRate, parentId = {}", parentId);

		List<Document> docList = getCollection(neId).find(and(eq("parentTpId", parentId), eq("layerRates", layerRates)))
				.into(new ArrayList<Document>());

		if (null == docList || docList.isEmpty()) {
			log.error("can not find tp, query by parentId = " + parentId);
			return null;
		}

		Document doc = docList.get(0);
		return constructTp(doc);
	}

	public AdpTp getTpByIdAndLayerRate(String neId, String tpId, List<String> layerRates) throws Exception {
		log.debug("getTpByIdAndLayerRate, tpId = {}", tpId);

		List<Document> docList = getCollection(neId).find(and(eq("id", tpId), eq("layerRates", layerRates)))
				.into(new ArrayList<Document>());

		if (null == docList || docList.isEmpty()) {
			log.error("can not find tp, query by tpId = " + tpId);
			return null;
		}

		Document doc = docList.get(0);
		return constructTp(doc);
	}

	public void deleteTpByKeyOnNe(String neId, String keyOnNe) throws Exception {
		getCollection(neId).deleteMany(new Document("keyOnNe", keyOnNe));
	}
}