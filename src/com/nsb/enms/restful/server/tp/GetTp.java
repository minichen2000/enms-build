package com.nsb.enms.restful.server.tp;

import static com.mongodb.client.model.Projections.exclude;
import static com.mongodb.client.model.Projections.fields;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
public class GetTp
{
    private static final Logger log = Logger.getLogger( GetTp.class );

    MongoDatabase db = MongoDBMgr.getInstance().getDatabase();

    MongoCollection<Document> dbc = db.getCollection( DBConst.DB_NAME_TP );

    @GET
    @Path("/tps/ne/{neid}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getTpByNeId( @PathParam("neid") String neid )
    {
        System.out.println( neid );
        List<Document> docList = dbc.find( Filters.eq( "neid", neid ) )
                .projection( filter() ).into( new ArrayList<Document>() );
        if( null == docList || docList.isEmpty() )
        {
            log.error( "can not find tp, query by neid = " + neid );
            return StringUtils.EMPTY;
        }
        Document rootDoc = new Document( "TP", docList );
        return rootDoc.toJson();
    }

    @GET
    @Path("/tps/{tpid}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getTpByPortId( @PathParam("tpid") String tpId )
    {
        System.out.println( tpId );
        List<Document> docList = dbc.find( Filters.eq( "aid", tpId ) )
                .projection( filter() ).into( new ArrayList<Document>() );
        if( null == docList || docList.isEmpty() )
        {
            log.error( "can not find tp, query by tpId = " + tpId );
            return StringUtils.EMPTY;
        }
        Document rootDoc = new Document( "TP", docList );
        return rootDoc.toJson();
    }

    @GET
    @Path("/tps")
    @Produces(MediaType.APPLICATION_JSON)
    public String getTpList()
    {
        List<Document> docList = dbc.find().projection( filter() )
                .into( new ArrayList<Document>() );
        if( null == docList || docList.isEmpty() )
        {
            log.error( "can not find tp" );
            return StringUtils.EMPTY;
        }
        Document rootDoc = new Document( "TP", docList );
        return rootDoc.toJson();
    }

    private Bson filter()
    {
        return fields( exclude( "_id" ) );
    }
}
