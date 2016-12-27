package com.nsb.enms.adapter.server.common.db.mgr;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.nsb.enms.adapter.server.common.constants.AdpDBConst;
import com.nsb.enms.adapter.server.common.db.mongodb.mgr.AdpMongoDBMgr;

public class AdpSeqDbMgr {
	private final static Logger log = LogManager.getLogger(AdpSeqDbMgr.class);
	private static MongoDatabase db = AdpMongoDBMgr.getInstance().getDatabase();
	private static MongoCollection<Document> dbc = db.getCollection(AdpDBConst.DB_NAME_SEQUENCE);
	private static final String NE = "ne";
	private static final String TP = "tp";
	private static final String XC = "xc";
	private static final String BORAD = "board";

	public static synchronized Integer getMaxNeId() throws Exception {
		return getMaxIdByType(NE);
	}

	public static synchronized Integer getMaxTpId(Integer neId) throws Exception {
		return getMaxIdByType(TP + "_" + neId);
	}

	public static synchronized Integer getMaxXcId() throws Exception {
		return getMaxIdByType(XC);
	}

	public static synchronized Integer getMaxEquipmentId() throws Exception {
		return getMaxIdByType(BORAD);
	}

	private static Integer getMaxIdByType(String type) throws Exception {
		Document doc = dbc.findOneAndUpdate(dbc.find().first(), new Document("$inc", new Document(type, 1)));
		if (null == doc) {
			dbc.insertOne(new Document(type, 1));
			return 1;
		}
		if (null == doc.getInteger(type)) {
			return 1;
		}
		return doc.getInteger(type) + 1;
	}

	public static void main(String args[]) {
		try {
			System.out.println("maxneid = " + getMaxNeId());
		} catch (Exception e) {
			log.error("", e);
			e.printStackTrace();
		}
	}
}
