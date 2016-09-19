package com.nsb.enms.adapter.server.db.mgr;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.nsb.enms.restful.model.adapter.Connection;

public class ConnectionsDbMgr {
	private static final Logger log = LogManager.getLogger(ConnectionsDbMgr.class);
	private MongoDatabase db = MongoDBMgr.getInstance().getDatabase();
	private MongoCollection<Document> dbc = db.getCollection(DBConst.DB_NAME_CONNECTION);
	private MongoCollection<BasicDBObject> dbc1 = db.getCollection(DBConst.DB_NAME_CONNECTION, BasicDBObject.class);

	private Gson gson = new Gson();

	public Connection addConnection(Connection body) throws Exception {
		String connection = gson.toJson(body);
		log.debug("CONNECTION=" + connection);

		BasicDBObject dbObject = (BasicDBObject) JSON.parse(connection);
		dbc1.insertOne(dbObject);

		String id = dbObject.getObjectId("_id").toString();
		body.setId(id);

		return body;
	}

	public Connection getConnectionById(String connectionid) throws Exception {
		List<Document> docList = dbc.find(new BasicDBObject("_id", new ObjectId(connectionid)))
				.into(new ArrayList<Document>());
		if (null == docList || docList.isEmpty()) {
			log.error("can not find connection, query by connectionid = " + connectionid);
			return new Connection();
		}
		Document doc = docList.get(0);
		Connection connection = constructConnection(doc);
		return connection;
	}

	public List<Connection> getConnectionsByLayerRate(String layerrate)
			throws Exception {
		Date begin = new Date();
		List<Document> docList = dbc.find(eq("layerRate", layerrate)).into(new ArrayList<Document>());
		if (null == docList || docList.isEmpty()) {
			log.error("can not find connection, query by layerRate = " + layerrate);
			return new ArrayList<Connection>();
		}
		List<Connection> connectionList = new ArrayList<Connection>();
		for (Document doc : docList) {
			Connection connection = constructConnection(doc);
			connectionList.add(connection);
		}

		Date end = new Date();
		log.debug("getConnectionsByLayerrate, cost time = " + (end.getTime() - begin.getTime()));
		return connectionList;
	}

	private Connection constructConnection(Document doc) {
		Connection connection = (Connection) gson.fromJson(doc.toJson(), Connection.class);
		connection.setId(doc.getObjectId("_id").toString());
		return connection;
	}

	public List<Connection> getConnections() throws Exception {
		Date begin = new Date();
		List<Document> docList = dbc.find().into(new ArrayList<Document>());
		if (null == docList || docList.isEmpty()) {
			log.error("can not find connection");
			return new ArrayList<Connection>();
		}
		List<Connection> connectionList = new ArrayList<Connection>();
		for (Document doc : docList) {
			Connection connection = constructConnection(doc);
			connectionList.add(connection);
		}

		Date end = new Date();
		log.debug("connectionsGet, cost time = " + (end.getTime() - begin.getTime()));
		return connectionList;
	}

	public List<Connection> getConnectionsByType(String connectiontype)
			throws Exception {
		Date begin = new Date();
		List<Document> docList = dbc.find(eq("connectionType", connectiontype)).into(new ArrayList<Document>());
		if (null == docList || docList.isEmpty()) {
			log.error("can not find connection, query by connectionType = " + connectiontype);
			return new ArrayList<Connection>();
		}
		List<Connection> connectionList = new ArrayList<Connection>();
		for (Document doc : docList) {
			Connection connection = constructConnection(doc);
			connectionList.add(connection);
		}

		Date end = new Date();
		log.debug("getConnectionByType, cost time = " + (end.getTime() - begin.getTime()));
		return connectionList;
	}

	public Connection getConnectionByUserLabel(String userlabel) throws Exception {
		Date begin = new Date();
		List<Document> docList = dbc.find(eq("userLabel", userlabel)).into(new ArrayList<Document>());
		if (null == docList || docList.isEmpty()) {
			log.error("can not find connection, query by userLabel = " + userlabel);
			return new Connection();
		}
		Connection connection = constructConnection(docList.get(0));

		Date end = new Date();
		log.debug("getConnectionByUserLabel, cost time = " + (end.getTime() - begin.getTime()));
		return connection;
	}
}
