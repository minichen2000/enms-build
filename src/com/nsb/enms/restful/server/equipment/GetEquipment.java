package com.nsb.enms.restful.server.equipment;

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
public class GetEquipment
{
    private static final Logger log = Logger.getLogger( GetEquipment.class );

    MongoDatabase db = MongoDBMgr.getInstance().getDatabase();

    MongoCollection<Document> dbc = db
            .getCollection( DBConst.DB_NAME_EQUIPMENT );

    @GET
    @Path("/equipments/{neid}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getEquipmentById( @PathParam("neid") String neId )
    {
        System.out.println( neId );
        Document doc = dbc.find( Filters.eq( "aid", neId ) )
                .projection( filter() ).first();
        if( null == doc )
        {
            log.error( "can not find equipment, query by neid = " + neId );
            return StringUtils.EMPTY;
        }
        return doc.toJson();
    }

    @GET
    @Path("/equipment")
    @Produces(MediaType.APPLICATION_JSON)
    public String getEquipment( @QueryParam("key") String key,
            @QueryParam("value") String value )
    {
        System.out.println( key + "," + value );
        List<Document> list = dbc.find( Filters.eq( key, value ) )
                .projection( filter() ).into( new ArrayList<Document>() );
        if( null == list || list.isEmpty() )
        {
            log.error( "can not find equipment, query by key = " + key
                    + ", value = " + value );
            return StringUtils.EMPTY;
        }
        Document rootDoc = new Document( "EQUIPMENT", list );
        return rootDoc.toJson();
    }

    @GET
    @Path("/equipments")
    @Produces(MediaType.APPLICATION_JSON)
    public String getEquipmentList()
    {
        List<Document> list = dbc.find().projection( filter() )
                .into( new ArrayList<Document>() );
        if( null == list || list.isEmpty() )
        {
            log.error( "can not find equipment" );
            return StringUtils.EMPTY;
        }
        Document rootDoc = new Document( "EQUIPMENT", list );
        return rootDoc.toJson();
    }

    private Bson filter()
    {
        return fields( exclude( "_id" ) );
    }
}
