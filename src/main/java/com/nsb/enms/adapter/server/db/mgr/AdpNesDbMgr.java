package com.nsb.enms.adapter.server.db.mgr;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import com.nsb.enms.adapter.server.common.utils.GenerateKeyOnNeUtil;
import com.nsb.enms.adapter.server.db.mongodb.constant.AdpDBConst;
import com.nsb.enms.adapter.server.db.mongodb.mgr.AdpMongoDBMgr;
import com.nsb.enms.adapter.server.notification.NotificationSender;
import com.nsb.enms.common.EntityType;
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

	public Response deleteNe(String neid) throws Exception {
		dbc.deleteOne(new BasicDBObject("id", neid));
		return Response.ok().build();
	}

	public AdpNe getNeById(String neid) throws Exception {
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
		// ne.setId(doc.getObjectId("_id").toString());
		return ne;
	}

	public Response updateNe(AdpNe body) throws Exception {
	    String id = body.getId();
	    AdpNe ne = getNeById( id );
	    if (body.getLocationName() != null && !body.getLocationName().equals( ne.getLocationName() ))
	    {
	        dbc.updateOne( new BasicDBObject("id", id),
                            set("locationName", body.getLocationName()) );
	        NotificationSender.instance().sendAvcNotif(EntityType.NE, id, "locationName",
                "String", body.getLocationName(), ne.getLocationName());
	    }
	    
	    if (body.getUserLabel() != null && !body.getUserLabel().equals( ne.getUserLabel() ))
	    {
	        dbc.updateOne( new BasicDBObject("id", id),
                set("userLabel", body.getUserLabel()) );
	        NotificationSender.instance().sendAvcNotif(EntityType.NE, id, "userLabel",
	            "String", body.getUserLabel(), ne.getUserLabel());
	    }
	    
	    if (body.getSupervisionState() != null && !body.getSupervisionState().equals( ne.getSupervisionState() ))
	    {
	        dbc.updateOne( new BasicDBObject("id", id),
                set("supervisionState", body.getSupervisionState().name()) );
            NotificationSender.instance().sendAvcNotif(EntityType.NE, id, "supervisionState",
                "enum", body.getSupervisionState().name(), ne.getSupervisionState().name());
	    }
	    
	    if (body.getSynchState() != null && !body.getSynchState().equals( ne.getSynchState() ))
        {
            dbc.updateOne( new BasicDBObject("id", id),
                set("synchState", body.getSupervisionState().name()) );
            NotificationSender.instance().sendAvcNotif(EntityType.NE, id, "synchState",
                "enum", body.getSynchState().name(), ne.getSynchState().name());
        }
	    
	    if (body.getAddresses() != null && !body.getAddresses().equals( ne.getAddresses() ))
	    {
	        AdpQ3Address q3Address = body.getAddresses().getQ3Address();
	        if (q3Address != null && !q3Address.equals( ne.getAddresses().getQ3Address() ))
	        {
	            String address = q3Address.getAddress();
	            String osMain = q3Address.getOsMain();
	            String osSpare = q3Address.getOsSpare();
	            if (address != null && !address.equals( ne.getAddresses().getQ3Address().getAddress() ))
	            {
	                dbc.updateOne( new BasicDBObject("id", id),
	                    set("addresses.q3Address.address", body.getAddresses().getQ3Address().getAddress()) );
	                NotificationSender.instance().sendAvcNotif(EntityType.NE, id, "addresses.q3Address.address",
	                    "String", body.getAddresses().getQ3Address().getAddress(), ne.getAddresses().getQ3Address().getAddress());
	            }
	            
	            if (osMain != null && !osMain.equals( ne.getAddresses().getQ3Address().getOsMain() ))
                {
                    dbc.updateOne( new BasicDBObject("id", id),
                        set("addresses.q3Address.osMain", body.getAddresses().getQ3Address().getOsMain()) );
                    NotificationSender.instance().sendAvcNotif(EntityType.NE, id, "addresses.q3Address.osMain",
                        "String", body.getAddresses().getQ3Address().getOsMain(), ne.getAddresses().getQ3Address().getOsMain());
                }
	            
	            if (osSpare != null && !osSpare.equals( ne.getAddresses().getQ3Address().getOsSpare() ))
                {
                    dbc.updateOne( new BasicDBObject("id", id),
                        set("addresses.q3Address.osSpare", body.getAddresses().getQ3Address().getOsSpare()) );
                    NotificationSender.instance().sendAvcNotif(EntityType.NE, id, "addresses.q3Address.osSpare",
                        "String", body.getAddresses().getQ3Address().getOsSpare(), ne.getAddresses().getQ3Address().getOsSpare());
                }
	        }
	        
	    }
	    
	    
		/*ModelAttrPatchApp modelAttrPatchApp = new ModelAttrPatchApp();
		Map<String, Object> nonNullAttrs = modelAttrPatchApp.getNonNullAttrs(body);
		for (String attrName : nonNullAttrs.keySet()) {
			if (!attrName.equalsIgnoreCase("id")) {
				AdpNe ne = getNeById(body.getId());
				dbc.updateOne(new BasicDBObject("id", body.getId()),
						set(attrName, nonNullAttrs.get(attrName).toString()));
				Pair<String, String> pair = modelAttrPatchApp.getValueByName(ne, attrName);
				NotificationSender.instance().sendAvcNotif(new Date(), EntityType.NE, body.getId(), attrName,
							pair.getSecond(), (String) nonNullAttrs.get(attrName), pair.getFirst());
			}
		}*/
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
		return Response.ok().build();
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
			String moi = nes.get(i).getKeyOnNe();
			if (!moi.split("/")[0].split("=")[1].equals(groupId)) {
				nes.remove(i);
			}
		}
		return nes;
	}

	public List<Integer> getNeIdsByGroupId(String groupId) throws Exception {
		List<AdpNe> nes = getNesByGroupId(groupId);
		List<Integer> neIdList = new ArrayList<Integer>();
		for (AdpNe ne : nes) {
			neIdList.add(Integer.parseInt(ne.getKeyOnNe().split("/")[1].split("=")[1]));
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

	public boolean isUserLabelExisted(String userLabel) throws Exception {
		Date begin = new Date();
		List<Document> docList = dbc.find(eq("userLabel", userLabel)).into(new ArrayList<Document>());
		if (null == docList || docList.isEmpty()) {
			return false;
		}
		Date end = new Date();
		log.debug("isUserLabelExisted, cost time = " + (end.getTime() - begin.getTime()));

		return true;
	}

    public String getIdByGroupAndNeId( String groupId, String neId ) throws Exception
    {
        List<AdpNe> nes = getNesByGroupId( groupId );
        for (AdpNe ne : nes)
        {
            String keyOnNe = ne.getKeyOnNe();
            String moi = GenerateKeyOnNeUtil.getMoi( keyOnNe );
            if (moi.split( "/" )[1].split( "=" )[1].equals( neId ))
            {
                return ne.getId();
            }
        }
        return null;
    }
}
