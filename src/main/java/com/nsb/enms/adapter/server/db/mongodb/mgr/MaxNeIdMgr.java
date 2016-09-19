package com.nsb.enms.adapter.server.db.mongodb.mgr;

import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.nsb.enms.adapter.server.db.mongodb.constant.DBConst;

public class MaxNeIdMgr {
	private final static Logger log = LogManager.getLogger(MaxNeIdMgr.class);
	private static MongoDatabase db = MongoDBMgr.getInstance().getDatabase();
	private static MongoCollection<Document> dbc = db.getCollection(DBConst.DB_NAME_MAXNEID);
	private static ReentrantLock lock = new ReentrantLock();

	public static String getId() {
		Document doc;
		try {
			lock.lock();
			doc = dbc.find().first();
			if (null == doc) {
				log.error("doc is null");
				return StringUtils.EMPTY;
			}
		} finally {
			lock.unlock();
		}
		return doc.getString("maxNeId");
	}

	public static void updateId(String maxNeId) {
		try {
			lock.lock();
			Document doc = dbc.find().first();
			if (null == doc) {
				log.error("doc is null, can not update id");
				return;
			}
			dbc.updateOne(new Document("maxNeId", doc.get("maxNeId")),
					new Document("$set", new Document("maxNeId", maxNeId)));
		} finally {
			lock.unlock();
		}
	}

	public static void main(String args[]) {
		String id = getId();
		System.out.println(id);

		String maxNeId = "10";
		updateId(maxNeId);
	}
}
