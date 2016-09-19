package com.nsb.enms.adapter.server.db.mgr;

import static com.mongodb.client.model.Accumulators.addToSet;
import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.group;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.nsb.enms.adapter.server.db.mongodb.constant.DBConst;
import com.nsb.enms.adapter.server.db.mongodb.mgr.MongoDBMgr;
import com.nsb.enms.restful.model.adapter.NeStatistics;
import com.nsb.enms.restful.model.adapter.NeStatisticsDetail;

public class StatisticsDbMgr {
	private final static Logger log = LogManager.getLogger(StatisticsDbMgr.class);
	private MongoDatabase db = MongoDBMgr.getInstance().getDatabase();
	private MongoCollection<Document> dbc = db.getCollection(DBConst.DB_NAME_NE);

	public List<NeStatistics> getNeNumbersPerType() throws Exception {
		List<Document> list = dbc.find().into(new ArrayList<Document>());
		if (null == list || list.isEmpty()) {
			log.error("no ne find");
			return new ArrayList<NeStatistics>();
		}
		List<NeStatisticsDetail> neStatisticsDetailList = new ArrayList<NeStatisticsDetail>();
		list = dbc.aggregate(asList(group("$neType", sum("num", 1), addToSet("versions", "$version"))))
				.into(new ArrayList<Document>());
		for (Document doc : list) {
			log.debug(doc.toJson());
			NeStatisticsDetail neStatisticsDetail = new NeStatisticsDetail();
			neStatisticsDetail.setNeType(doc.getString("_id"));
			@SuppressWarnings("unchecked")
			List<String> versions = (ArrayList<String>) doc.get("versions");
			neStatisticsDetail.setVersions(versions);

			neStatisticsDetailList.add(neStatisticsDetail);
		}

		NeStatistics neStatistics = new NeStatistics();
		neStatistics.setNes(neStatisticsDetailList);
		neStatistics.setSum(1);

		List<NeStatistics> neStatisticsList = new ArrayList<NeStatistics>();
		neStatisticsList.add(neStatistics);

		return neStatisticsList;
	}

	public List<NeStatistics> getSupportedNeTypes() throws Exception {
		List<Document> list = dbc.find().into(new ArrayList<Document>());
		if (null == list || list.isEmpty()) {
			log.error("no ne find");
			return new ArrayList<NeStatistics>();
		}

		// TODO 读取支持的网元类型，再进行过滤

		NeStatistics neStatistics = new NeStatistics();
		List<NeStatisticsDetail> neStatisticsDetailList = new ArrayList<NeStatisticsDetail>();
		list = dbc.aggregate(asList(group("$neType", sum("num", 1), addToSet("versions", "$version"))))
				.into(new ArrayList<Document>());
		for (Document doc : list) {
			System.out.println(doc.toJson());
			log.debug(doc.toJson());
			NeStatisticsDetail neStatisticsDetail = new NeStatisticsDetail();
			neStatisticsDetail.setNeType(doc.getString("_id"));
			@SuppressWarnings("unchecked")
			List<String> versions = (ArrayList<String>) doc.get("versions");
			neStatisticsDetail.setVersions(versions);

			neStatisticsDetailList.add(neStatisticsDetail);
		}
		neStatistics.setNes(neStatisticsDetailList);

		List<NeStatistics> neStatisticsList = new ArrayList<NeStatistics>();
		neStatisticsList.add(neStatistics);

		return neStatisticsList;
	}
}
