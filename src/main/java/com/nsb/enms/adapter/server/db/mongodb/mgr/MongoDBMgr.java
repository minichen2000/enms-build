/********************************
 * Copyright (c) 2016, ASB
 * All right reserved.
 * Author:Li Hongji
 * Date: 2016/7/11
 * Description: MongoDB数据库的管理类
 ********************************/
package com.nsb.enms.adapter.server.db.mongodb.mgr;

import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Projections.exclude;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.descending;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import com.nsb.enms.adapter.server.db.mongodb.AlarmBeanCodec;
import com.nsb.enms.adapter.server.db.mongodb.bean.CommonBean;

public class MongoDBMgr
{
    private static final Logger log = Logger.getLogger( MongoDBMgr.class );

    private final static MongoDBMgr mgr = new MongoDBMgr();

    private MongoClient mongoClient = null;

    private MongoDatabase database = null;

    private MongoCollection<Document> dbc = null;

    private MongoCollection<CommonBean> customDbc = null;

    private MongoDBMgr()
    {
        mongoClient = new MongoClient( "localhost", 27017 );
        database = mongoClient.getDatabase( "eNMS" );
    }

    public void getCustomDbc( String dbName )
    {
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
            CodecRegistries.fromCodecs( new AlarmBeanCodec() ),
            MongoClient.getDefaultCodecRegistry() );
        customDbc = database.getCollection( dbName, CommonBean.class )
                .withCodecRegistry( codecRegistry );
    }

    public void getDefaultDbc( String dbName )
    {
        dbc = database.getCollection( dbName );
    }

    public static MongoDBMgr getInstance()
    {
        return mgr;
    }

    public MongoClient getClient()
    {
        if( this.mongoClient == null )
        {
            throw new IllegalStateException(
                    "mongo client was not initialized" );
        }

        return this.mongoClient;
    }

    public MongoDatabase getDatabase()
    {
        return database;
    }

    /**
     * 插入一条数据至集合中
     * 
     * @param bean
     */
    public synchronized void insert( CommonBean bean )
    {
        Document doc = new Document( "aId", bean.getaId() ).append( "userLable",
            bean.getUserLable() );

        dbc.insertOne( doc );
    }

    /**
     * 插入一条数据至集合中
     * 
     * @param bean
     */
    public synchronized void insertByJson( String json )
    {
        Document doc = (Document) JSON.parse( json );
        dbc.insertOne( doc );
    }

    /**
     * 插入一条数据至集合中
     * 
     * @param bean
     */
    public synchronized void customInsert( CommonBean bean )
    {
        customDbc.insertOne( bean );
    }

    // TODO 不使用的话，则删除该方法
    /**
     * 获取大于等于指定序列号的告警
     * 
     * @param alarmSeq
     */
    public void getAlarmsGteID( int alarmSeq )
    {
        FindIterable<Document> iterable = dbc
                .find( gte( "alarmSeq", alarmSeq ) )
                .sort( ascending( "alarmSeq" ) );
        iterable.forEach( new Block<Document>()
        {
            @Override
            public void apply( final Document document )
            {
                System.out.println( document );
            }
        } );
    }

    public long getCurrentMaxAlarmSeq()
    {
        if( dbc.count() <= 0 )
        {
            return 0;
        }

        FindIterable<Document> iterable = dbc.find()
                .sort( descending( "alarmSeq" ) ).limit( 1 );
        Document doc = iterable.first();
        long maxAlarmSeq = doc.getLong( "alarmSeq" );
        log.debug( "maxAlarmSeq = " + maxAlarmSeq );
        return maxAlarmSeq;
    }

    /**
     * 关闭数据库连接
     */
    public void close()
    {
        mongoClient.close();
    }

    private Bson filter()
    {
        return fields( exclude( "_id", "createTime" ) );
    }
}
