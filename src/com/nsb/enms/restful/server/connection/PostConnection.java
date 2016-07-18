package com.nsb.enms.restful.server.connection;

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
public class PostConnection
{
    MongoDatabase db = MongoDBMgr.getInstance().getDatabase();

    MongoCollection<BasicDBObject> dbc = db
            .getCollection( DBConst.DB_NAME_CONNECTION, BasicDBObject.class );

    @POST
    @Path("/connection")
    @Produces(MediaType.APPLICATION_JSON)
    public void addConnection( String connection )
    {
        System.out.println( connection );
        BasicDBObject obj = (BasicDBObject) JSON.parse( connection );
        BasicDBObject newObj = (BasicDBObject) obj.get( "CONNECTION" );
        dbc.insertOne( newObj );
    }

    @POST
    @Path("/connections")
    @Produces(MediaType.APPLICATION_JSON)
    public void addConnections( String connections )
    {
        System.out.println( connections );
        BasicDBObject obj = (BasicDBObject) JSON.parse( connections );
        @SuppressWarnings("unchecked")
        List<BasicDBObject> newObj = (List<BasicDBObject>) obj
                .get( "CONNECTION" );
        dbc.insertMany( newObj );
    }
}
