package com.nsb.enms.restful.server.equipment;

import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import com.nsb.enms.mongodb.constant.DBConst;
import com.nsb.enms.mongodb.mgr.MongoDBMgr;

@Path("")
public class PostEquipment
{
    MongoDatabase db = MongoDBMgr.getInstance().getDatabase();

    MongoCollection<BasicDBObject> dbc = db
            .getCollection( DBConst.DB_NAME_EQUIPMENT, BasicDBObject.class );

    @POST
    @Path("/equipment")
    @Produces(MediaType.APPLICATION_JSON)
    public void addEquipment( String equipment )
    {
        System.out.println( equipment );
        BasicDBObject obj = (BasicDBObject) JSON.parse( equipment );
        BasicDBObject newObj = (BasicDBObject) obj.get( "EQUIPMENT" );
        dbc.insertOne( newObj );
    }

    @POST
    @Path("/equipments")
    @Produces(MediaType.APPLICATION_JSON)
    public void addEquipments( String equipments )
    {
        System.out.println( equipments );
        BasicDBObject obj = (BasicDBObject) JSON.parse( equipments );
        @SuppressWarnings("unchecked")
        List<BasicDBObject> newObj = (List<BasicDBObject>) obj
                .get( "EQUIPMENT" );
        dbc.insertMany( newObj );
    }
}
