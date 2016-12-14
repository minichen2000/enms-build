package com.nsb.enms.adapter.server.common.db.mongodb.mgr;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.nsb.enms.adapter.server.common.db.mongodb.constant.AdpDBConst;

public class AdpMaxNeIdMgr {
	private final static Logger log = LogManager.getLogger(AdpMaxNeIdMgr.class);

	private static MongoDatabase db = AdpMongoDBMgr.getInstance().getDatabase();

	private static MongoCollection<Document> dbc = db.getCollection(AdpDBConst.DB_NAME_MAXNEID);

	private static ReentrantLock lock = new ReentrantLock();

	public static String getNeIdByGroupId(String groupId) {
		Document doc;
		try {
			lock.lock();
			doc = dbc.find(eq("groupId", groupId)).first();
			if (null == doc) {
				log.error("doc is null");
				return StringUtils.EMPTY;
			}
		} finally {
			lock.unlock();
		}
		return doc.getString("maxNeId");
	}

	public static void updateNeIdByGroupId(String groupId, String maxNeId) {
		try {
			lock.lock();
			Document doc = dbc.find(eq("groupId", groupId)).first();
			if (null == doc) {
				log.error("doc is null, can not update id");
				return;
			}
			dbc.updateOne(new BasicDBObject("groupId", groupId), set("maxNeId", maxNeId));
		} finally {
			lock.unlock();
		}
	}

	public static void main(String args[]) {
		String id = getNeIdByGroupId("100");
		System.out.println(id);

		String maxNeId = "10";
		updateNeIdByGroupId("100", maxNeId);
	}
}
