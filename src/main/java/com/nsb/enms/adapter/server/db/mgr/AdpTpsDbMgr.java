package com.nsb.enms.adapter.server.db.mgr;

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
import com.nsb.enms.adapter.server.db.mongodb.constant.AdpDBConst;
import com.nsb.enms.adapter.server.db.mongodb.mgr.AdpMongoDBMgr;
import com.nsb.enms.common.LayerRate;
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

	private AdpTp constructTp(Document doc) {
		AdpTp tp = gson.fromJson(doc.toJson(), AdpTp.class);
		return tp;
	}

	public List<AdpTp> getTpsByNeId(String neid) throws Exception {
		log.debug("getTPByNEId, neId = " + neid);
		List<Document> docList = dbc
				.find(and(eq("neId", neid),
						or(eq("tpType", "labelledOpticalSPITTPBidirectional"), eq("tpType", "pPITTPBidirectionalR1"))))
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

	public void updateTpLayerRate(Integer tpId, int layerRate) throws Exception {
		log.debug("layerRate = " + layerRate);
		List<String> layerRates = new ArrayList<String>();
		layerRates.add(String.valueOf(layerRate));
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

	public List<AdpTp> getTpsByType(String neid, String tptype) throws Exception {
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

	public void deleteTpsbyNeId(int neid) throws Exception {
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

	public Integer getIdByKeyOnNe(String keyOnNe) throws Exception {
		List<Document> docList = dbc.find(eq("keyOnNe", keyOnNe)).into(new ArrayList<Document>());

		if (null == docList || docList.isEmpty()) {
			log.error("can not find tp, query by keyOnNe = " + keyOnNe);
			return null;
		}

		Document doc = docList.get(0);
		AdpTp tp = constructTp(doc);
		return tp.getId();
	}

	public Integer getTpByParentIdAndLayerRate(String parentId, LayerRate layerRate) throws Exception {
		log.debug("getTpByParentIdAndLayerRate, parentId = {}", parentId);

		List<Document> docList = dbc
				.find(and(eq("parentTpId", parentId), in("layerRates", String.valueOf(layerRate.getCode()))))
				.into(new ArrayList<Document>());

		if (null == docList || docList.isEmpty()) {
			log.error("can not find tp, query by parentId = " + parentId);
			return null;
		}

		Document doc = docList.get(0);
		return constructTp(doc).getId();
	}
}