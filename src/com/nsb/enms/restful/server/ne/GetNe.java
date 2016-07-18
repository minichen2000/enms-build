package com.nsb.enms.restful.server.ne;

import static com.mongodb.client.model.Projections.exclude;
import static com.mongodb.client.model.Projections.fields;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.nsb.enms.mongodb.constant.DBConst;
import com.nsb.enms.mongodb.mgr.MongoDBMgr;

@Path("")
public class GetNe
{
    private static final Logger log = Logger.getLogger( GetNe.class );

    MongoDatabase db = MongoDBMgr.getInstance().getDatabase();

    MongoCollection<Document> dbc = db.getCollection( DBConst.DB_NAME_NE );

    @GET
    @Path("/ne/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getNeById( @PathParam("id") String id )
    {
        System.out.println( id );
        Document doc = dbc.find( Filters.eq( "aid", id ) )
                .projection( filter() ).first();
        if( null == doc )
        {
            log.error( "can not find ne, query by id = " + id );
            return StringUtils.EMPTY;
        }
        return doc.toJson();
    }

    @GET
    @Path("/ne")
    @Produces(MediaType.APPLICATION_JSON)
    public String getNe( @QueryParam("key") String key,
            @QueryParam("value") String value )
    {
        System.out.println( key + "," + value );
        List<Document> list = dbc.find( Filters.eq( key, value ) )
                .projection( filter() ).into( new ArrayList<Document>() );
        if( null == list || list.isEmpty() )
        {
            log.error( "can not find ne, query by key = " + key + ", value = "
                    + value );
            return StringUtils.EMPTY;
        }
        Document rootDoc = new Document( "NE", list );
        return rootDoc.toJson();
    }

    @GET
    @Path("/nes")
    @Produces(MediaType.APPLICATION_JSON)
    public String getNeList()
    {
        List<Document> list = dbc.find().projection( filter() )
                .into( new ArrayList<Document>() );
        if( null == list || list.isEmpty() )
        {
            log.error( "can not find ne" );
            return StringUtils.EMPTY;
        }
        Document rootDoc = new Document( "NE", list );
        return rootDoc.toJson();
    }

    private Bson filter()
    {
        return fields( exclude( "_id" ) );
    }
}
