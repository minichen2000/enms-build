package com.nsb.enms.restful.server.ne;

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
public class PostNe
{
    MongoDatabase db = MongoDBMgr.getInstance().getDatabase();

    MongoCollection<BasicDBObject> dbc = db.getCollection( DBConst.DB_NAME_NE,
        BasicDBObject.class );

    @POST
    @Path("/ne")
    @Produces(MediaType.APPLICATION_JSON)
    public void addNe( String ne )
    {
        System.out.println( ne );
        BasicDBObject obj = (BasicDBObject) JSON.parse( ne );
        BasicDBObject newObj = (BasicDBObject) obj.get( "NE" );
        dbc.insertOne( newObj );
    }

    @POST
    @Path("/nes")
    @Produces(MediaType.APPLICATION_JSON)
    public void addNes( String nes )
    {
        System.out.println( nes );
        BasicDBObject obj = (BasicDBObject) JSON.parse( nes );
        @SuppressWarnings("unchecked")
        List<BasicDBObject> newObj = (List<BasicDBObject>) obj.get( "NE" );
        dbc.insertMany( newObj );
    }
}
