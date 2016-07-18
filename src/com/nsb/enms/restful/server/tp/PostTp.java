package com.nsb.enms.restful.server.tp;

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
public class PostTp
{
    MongoDatabase db = MongoDBMgr.getInstance().getDatabase();

    MongoCollection<BasicDBObject> dbc = db.getCollection( DBConst.DB_NAME_TP,
        BasicDBObject.class );

    @POST
    @Path("/tp")
    @Produces(MediaType.APPLICATION_JSON)
    public void addTp( String port )
    {
        System.out.println( port );
        BasicDBObject obj = (BasicDBObject) JSON.parse( port );
        BasicDBObject newObj = (BasicDBObject) obj.get( "TP" );
        dbc.insertOne( newObj );
    }

    @POST
    @Path("/tps")
    @Produces(MediaType.APPLICATION_JSON)
    public void addTps( String port )
    {
        System.out.println( port );
        BasicDBObject obj = (BasicDBObject) JSON.parse( port );
        @SuppressWarnings("unchecked")
        List<BasicDBObject> newObj = (List<BasicDBObject>) obj.get( "TP" );
        dbc.insertMany( newObj );
    }
}
