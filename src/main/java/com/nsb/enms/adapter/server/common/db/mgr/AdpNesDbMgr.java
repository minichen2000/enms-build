package com.nsb.enms.adapter.server.common.db.mgr;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.ne;
import static com.mongodb.client.model.Updates.set;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import com.nsb.enms.adapter.server.common.constants.MethodOperator;
import com.nsb.enms.adapter.server.common.constants.AdpDBConst;
import com.nsb.enms.adapter.server.common.db.mongodb.mgr.AdpMongoDBMgr;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.notification.NotificationSender;
import com.nsb.enms.adapter.server.sdh.action.method.ne.SetManagerAddress;
import com.nsb.enms.common.EntityType;
import com.nsb.enms.restful.model.adapter.AdpAddresses;
import com.nsb.enms.restful.model.adapter.AdpKVPair;
import com.nsb.enms.restful.model.adapter.AdpNe;
import com.nsb.enms.restful.model.adapter.AdpQ3Address;

public class AdpNesDbMgr {
	private final static Logger log = LogManager.getLogger(AdpNesDbMgr.class);

	private MongoDatabase db = AdpMongoDBMgr.getInstance().getDatabase();

	private MongoCollection<Document> dbc = db.getCollection(AdpDBConst.DB_NAME_NE);

	private MongoCollection<BasicDBObject> dbc1 = db.getCollection(AdpDBConst.DB_NAME_NE, BasicDBObject.class);

	private Gson gson = new Gson();

	public AdpNe addNe(AdpNe body) throws Exception {
		log.debug("body=" + body);
		String ne = gson.toJson(body);
		log.debug("ne=" + ne);

		BasicDBObject dbObject = (BasicDBObject) JSON.parse(ne);
		dbc1.insertOne(dbObject);

		return body;
	}

	public Response deleteNe(Integer neid) throws Exception {
		dbc.deleteOne(new BasicDBObject("id", neid));
		return Response.ok().build();
	}

	public AdpNe getNeById(Integer neid) throws Exception {
		List<Document> docList = dbc.find(eq("id", neid)).into(new ArrayList<Document>());
		if (null == docList || docList.isEmpty()) {
			log.error("can not find ne, query by neId = " + neid);
			return new AdpNe();
		}
		Document doc = docList.get(0);
		AdpNe ne = constructNe(doc);
		return ne;
	}

	private AdpNe constructNe(Document doc) {
		AdpNe ne = gson.fromJson(doc.toJson(), AdpNe.class);
		return ne;
	}

	public void updateNe(AdpNe body) throws Exception {
		Integer id = body.getId();
		AdpNe ne = getNeById(id);

		updateLocationName(body, id, ne);

		updateUserLabel(body, id, ne);

		updateQ3Address(body, id, ne);

		updateOperationalState(body, id, ne);

		updateSupervisionState(body, id, ne);

		updateAlignmentState(body, id, ne);

		updateCommunicationState(body, id, ne);

		updateNeRelease(body, id, ne);

		/*
		 * ModelAttrPatchApp modelAttrPatchApp = new ModelAttrPatchApp();
		 * Map<String, Object> nonNullAttrs =
		 * modelAttrPatchApp.getNonNullAttrs(body); for (String attrName :
		 * nonNullAttrs.keySet()) { if (!attrName.equalsIgnoreCase("id")) {
		 * AdpNe ne = getNeById(body.getId()); dbc.updateOne(new
		 * BasicDBObject("id", body.getId()), set(attrName,
		 * nonNullAttrs.get(attrName).toString())); Pair<String, String> pair =
		 * modelAttrPatchApp.getValueByName(ne, attrName);
		 * NotificationSender.instance().sendAvcNotif(new Date(), EntityType.NE,
		 * body.getId(), attrName, pair.getSecond(), (String)
		 * nonNullAttrs.get(attrName), pair.getFirst()); } }
		 */
		/*
		 * for (Field f : body.getClass().getDeclaredFields()) {
		 * f.setAccessible(true); try { if (List.class == f.getType()) { if
		 * (!((List) f.get(body)).isEmpty()) { // TODO 更新list中的数据 } } else {
		 * Object obj = f.get(body); if (null != obj &&
		 * !(obj.toString().equalsIgnoreCase("id"))) { dbc.updateOne(new
		 * BasicDBObject("id", body.getId()), set(f.getName(),
		 * f.get(body).toString())); } }
		 * 
		 * } catch (Exception e) { log.error("updateNe", e); } }
		 */
	}

	private void updateNeRelease(AdpNe body, Integer id, AdpNe ne) {
		String neRelease = body.getNeRelease();
		String neReleaseFromDb = ne.getNeRelease();
		if (StringUtils.isEmpty(neRelease) || StringUtils.equals(neRelease, neReleaseFromDb)) {
			return;
		}
		dbc.updateOne(new BasicDBObject("id", id), set("neRelease", neRelease));
		NotificationSender.instance().sendAvcNotif(EntityType.NE, id, "neRelease", neRelease, neReleaseFromDb);
	}

	private void updateOperationalState(AdpNe body, Integer id, AdpNe ne) {
		String operationalState = body.getOperationalState();
		String operationalStateFromDb = ne.getOperationalState();
		if (operationalState == null || StringUtils.equals(operationalStateFromDb, operationalState)) {
			return;
		}
		dbc.updateOne(new BasicDBObject("id", id), set("operationalState", operationalState));
		NotificationSender.instance().sendAvcNotif(EntityType.NE, id, "operationalState", operationalState,
				operationalStateFromDb);
	}

	private void updateCommunicationState(AdpNe body, Integer id, AdpNe ne) {
		String communicationState = body.getCommunicationState();
		String communicationStateFromDb = ne.getCommunicationState();
		if (communicationState == null || StringUtils.equals(communicationState, communicationStateFromDb)) {
			return;
		}
		dbc.updateOne(new BasicDBObject("id", id), set("communicationState", communicationState));
		NotificationSender.instance().sendAvcNotif(EntityType.NE, id, "communicationState", communicationState,
				communicationStateFromDb);
	}

	private void updateUserLabel(AdpNe body, Integer id, AdpNe ne) {
		String userLabel = body.getUserLabel();
		if (userLabel != null && !userLabel.equals(ne.getUserLabel())) {
			dbc.updateOne(new BasicDBObject("id", id), set("userLabel", userLabel));
			NotificationSender.instance().sendAvcNotif(EntityType.NE, id, "userLabel", body.getUserLabel(),
					ne.getUserLabel());
		}
	}

	private void updateLocationName(AdpNe body, Integer id, AdpNe ne) {
		// String locationName = body.getLocationName();
		// if (locationName != null &&
		// !locationName.equals(ne.getLocationName())) {
		// dbc.updateOne(new BasicDBObject("id", id), set("locationName",
		// locationName));
		// NotificationSender.instance().sendAvcNotif(EntityType.NE, id,
		// "locationName", ValueType.STRING.getCode(),
		// body.getLocationName(), ne.getLocationName());
		// }
	}

	private void updateQ3Address(AdpNe body, Integer id, AdpNe ne) {
		AdpAddresses addresses = body.getAddresses();
		AdpAddresses addressesFromDb = ne.getAddresses();

		if (null == addresses || addresses.equals(addressesFromDb)) {
			return;
		}

		AdpQ3Address q3Address = addresses.getQ3Address();
		if (null == q3Address) {
			return;
		}

		String address = q3Address.getAddress();
		String osMain = q3Address.getOsMain();
		String osSpare = q3Address.getOsSpare();
		String groupId = "100";
		String neId = String.valueOf(id);
		if (null == addressesFromDb) {
			if (StringUtils.isNotEmpty(address)) {
				dbc.updateOne(new BasicDBObject("id", id), set("addresses.q3Address.address", address));
				NotificationSender.instance().sendAvcNotif(EntityType.NE, id, "address", address, null);
			}

			if (StringUtils.isNotEmpty(osMain)) {
				dbc.updateOne(new BasicDBObject("id", id), set("addresses.q3Address.osMain", osMain));
				NotificationSender.instance().sendAvcNotif(EntityType.NE, id, "osMain", osMain, null);
				setOsMainAddress(groupId, neId, osMain);
			}

			if (StringUtils.isNotEmpty(osSpare)) {
				dbc.updateOne(new BasicDBObject("id", id), set("addresses.q3Address.osSpare", osSpare));
				NotificationSender.instance().sendAvcNotif(EntityType.NE, id, "osSpare", osSpare, null);
				setOsSpareAddress(groupId, neId, osSpare);
			}
		} else {
			String addressFromDb = addressesFromDb.getQ3Address().getAddress();
			if (StringUtils.isNotEmpty(address) && !address.equals(addressFromDb)) {
				dbc.updateOne(new BasicDBObject("id", id), set("addresses.q3Address.address", address));
				NotificationSender.instance().sendAvcNotif(EntityType.NE, id, "address", address, addressFromDb);
			}

			String osMainFromDb = addressesFromDb.getQ3Address().getOsMain();
			if (StringUtils.isNotEmpty(osMain) && !osMain.equals(osMainFromDb)) {
				dbc.updateOne(new BasicDBObject("id", id), set("addresses.q3Address.osMain", osMain));
				NotificationSender.instance().sendAvcNotif(EntityType.NE, id, "osMain", osMain, osMainFromDb);
				setOsMainAddress(groupId, neId, osMainFromDb);
			}

			String osSpareFromDb = addressesFromDb.getQ3Address().getOsSpare();
			if (StringUtils.isNotEmpty(osSpare) && !osSpare.equals(osSpareFromDb)) {
				dbc.updateOne(new BasicDBObject("id", id), set("addresses.q3Address.osSpare", osSpare));
				NotificationSender.instance().sendAvcNotif(EntityType.NE, id, "osSpare", osSpare, osSpareFromDb);
				setOsSpareAddress(groupId, neId, osSpareFromDb);
			}
		}
	}

	private void setOsMainAddress(String groupId, String neId, String osMain) {
		try {
			SetManagerAddress.setMainOSAddress(groupId, neId, osMain);
		} catch (AdapterException e) {
			log.error("setMainOSAddress", e);
		}
	}

	private void setOsSpareAddress(String groupId, String neId, String osSpare) {
		try {
			SetManagerAddress.setSpareOSAddress(groupId, neId, osSpare);
		} catch (AdapterException e) {
			log.error("setMainOSAddress", e);
		}
	}

	private void updateSupervisionState(AdpNe body, Integer id, AdpNe ne) {
		String supervisionState = body.getSupervisionState();
		String supervisionStateFromDb = ne.getSupervisionState();
		if (null == supervisionState || StringUtils.equals(supervisionState, supervisionStateFromDb)) {
			return;
		}
		dbc.updateOne(new BasicDBObject("id", id), set("supervisionState", supervisionState));
		NotificationSender.instance().sendAvcNotif(EntityType.NE, id, "supervisionState", supervisionState,
				supervisionStateFromDb);
	}

	private void updateAlignmentState(AdpNe body, Integer id, AdpNe ne) {
		String alignmentState = body.getAlignmentState();
		String alignmentStateFromDb = ne.getAlignmentState();
		if (StringUtils.equals(alignmentState, alignmentStateFromDb)) {
			return;
		}
		dbc.updateOne(new BasicDBObject("id", id), set("alignmentState", alignmentState));
		NotificationSender.instance().sendAvcNotif(EntityType.NE, id, "alignmentState", alignmentState,
				alignmentStateFromDb);
	}

	/*
	 * public List<AdpNe> findNesByType(String netype) throws Exception { Date
	 * begin = new Date(); List<Document> docList = dbc.find(eq("neType",
	 * netype)).into(new ArrayList<Document>()); if (null == docList ||
	 * docList.isEmpty()) { log.error("can not find ne"); return new
	 * ArrayList<AdpNe>(); } List<AdpNe> neList = new ArrayList<AdpNe>(); for
	 * (Document doc : docList) { AdpNe ne = constructNe(doc); neList.add(ne);
	 * System.out.println(ne); } Date end = new Date(); log.debug(
	 * "findNeByType, cost time = " + (end.getTime() - begin.getTime())); return
	 * neList; }
	 * 
	 * public List<AdpNe> findNeByTypeVersion(String netype, String neversion)
	 * throws Exception { Date begin = new Date(); List<Document> docList =
	 * dbc.find(and(eq("neType", netype), eq("version", neversion))) .into(new
	 * ArrayList<Document>()); if (null == docList || docList.isEmpty()) {
	 * log.error("can not find ne"); return new ArrayList<AdpNe>(); }
	 * List<AdpNe> neList = new ArrayList<AdpNe>(); for (Document doc : docList)
	 * { AdpNe ne = constructNe(doc); neList.add(ne); System.out.println(ne); }
	 * Date end = new Date(); log.debug("findNeByTypeVersion, cost time = " +
	 * (end.getTime() - begin.getTime())); return neList; }
	 * 
	 * public List<AdpNe> findNesByVersion(String neversion) throws Exception {
	 * Date begin = new Date(); List<Document> docList = dbc.find(eq("version",
	 * neversion)).into(new ArrayList<Document>()); if (null == docList ||
	 * docList.isEmpty()) { log.error("can not find ne"); return new
	 * ArrayList<AdpNe>(); } List<AdpNe> neList = new ArrayList<AdpNe>(); for
	 * (Document doc : docList) { AdpNe ne = constructNe(doc); neList.add(ne);
	 * System.out.println(ne); } Date end = new Date(); log.debug(
	 * "findNeByVersion, cost time = " + (end.getTime() - begin.getTime()));
	 * return neList; }
	 */

	public List<AdpNe> getNes() throws Exception {
		Date begin = new Date();
		List<Document> docList = dbc.find().into(new ArrayList<Document>());
		if (null == docList || docList.isEmpty()) {
			log.error("can not find ne");
			return new ArrayList<AdpNe>();
		}
		List<AdpNe> neList = new ArrayList<AdpNe>();
		for (Document doc : docList) {
			AdpNe ne = constructNe(doc);
			neList.add(ne);
			System.out.println(ne);
		}
		Date end = new Date();
		log.debug("nesGet, cost time = " + (end.getTime() - begin.getTime()));

		return neList;
	}

	public List<AdpNe> getNesByGroupId(String groupId) throws Exception {
		List<AdpNe> nes = getNes();
		for (int i = nes.size() - 1; i >= 0; i--) {
			List<AdpKVPair> params = nes.get(i).getParams();
			String dbGroupId = "";
			for (AdpKVPair param : params) {
				if (param.getKey().equals("GROUP_ID")) {
					dbGroupId = param.getValue();
					break;
				}
			}
			if (!dbGroupId.equals(groupId)) {
				nes.remove(i);
			}
		}
		return nes;
	}

	public List<Integer> getNeIdsByGroupId(String groupId) throws Exception {
		List<AdpNe> nes = getNesByGroupId(groupId);
		List<Integer> neIdList = new ArrayList<Integer>();
		for (AdpNe ne : nes) {
			neIdList.add(ne.getId());
		}
		return neIdList;
	}

	public Response deleteNesByGroupId(int groupId) throws Exception {
		List<AdpNe> nes = getNesByGroupId(String.valueOf(groupId));
		for (AdpNe ne : nes) {
			deleteNe(ne.getId());
		}
		return Response.ok().build();

	}

	/*
	 * public Response getMaxNeId(SecurityContext securityContext) throws
	 * Exception { String maxNeId = MaxNeIdMgr.getId(); return
	 * Response.ok().entity(maxNeId).build(); }
	 * 
	 * public Response updateMaxNeId(String body) throws Exception {
	 * MaxNeIdMgr.updateId(body); return Response.ok().build(); }
	 */

	public boolean isUserLabelExisted(Integer id, String userLabel, MethodOperator operator) throws Exception {
		Date begin = new Date();
		List<Document> docList = null;
		if (MethodOperator.ADD == operator) {
			docList = dbc.find(eq("userLabel", userLabel)).into(new ArrayList<Document>());
		} else {
			docList = dbc.find(and(ne("id", id), eq("userLabel", userLabel))).into(new ArrayList<Document>());
		}
		if (null == docList || docList.isEmpty()) {
			return false;
		}
		Date end = new Date();
		log.debug("isUserLabelExisted, cost time = " + (end.getTime() - begin.getTime()));

		return true;
	}

	public Integer getIdByKeyOnNe(String keyOnNe) throws Exception {
		List<Document> docList = dbc.find(eq("keyOnNe", keyOnNe)).into(new ArrayList<Document>());
		if (null == docList || docList.isEmpty()) {
			log.error("can not find ne, query by keyOnNe = " + keyOnNe);
			return null;
		}
		Document doc = docList.get(0);
		AdpNe ne = constructNe(doc);
		return ne.getId();
	}

	public Integer getIdByAddress(String address) throws Exception {
		List<Document> docList = dbc.find(eq("addresses.snmpAddress.snmpAgent", address))
				.into(new ArrayList<Document>());
		if (null == docList || docList.isEmpty()) {
			log.error("can not find ne, query by addresses.snmpAddress.snmpAgent = " + address);
			return null;
		}
		Document doc = docList.get(0);
		AdpNe ne = constructNe(doc);
		return ne.getId();
	}

	public List<Integer> getNeIds() throws Exception {
		Date begin = new Date();
		List<Document> docList = dbc.find().projection(new Document("id", 1)).into(new ArrayList<Document>());
		if (null == docList || docList.isEmpty()) {
			log.error("can not find ne");
			return new ArrayList<Integer>();
		}
		List<Integer> neList = new ArrayList<Integer>();
		for (Document doc : docList) {
			neList.add(doc.getInteger("id"));
		}
		Date end = new Date();
		log.debug("getNeIds, cost time = " + (end.getTime() - begin.getTime()));

		return neList;
	}
}
