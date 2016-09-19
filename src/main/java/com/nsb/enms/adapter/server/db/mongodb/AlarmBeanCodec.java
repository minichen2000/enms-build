package com.nsb.enms.adapter.server.db.mongodb;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import com.nsb.enms.adapter.server.db.mongodb.bean.CommonBean;

public class AlarmBeanCodec implements Codec<CommonBean>
{
    @Override
    public void encode( BsonWriter writer, CommonBean bean,
            EncoderContext arg2 )
    {
        writer.writeStartDocument();
        // writer.writeInt64( "alarmSeq", bean.getAlarmSeq() );
        // writer.writeString( "alarmTitle", bean.getAlarmTitle() );
        // writer.writeInt32( "alarmStatus", bean.getAlarmStatus().getValue() );
        // writer.writeString( "alarmType", bean.getAlarmType() );
        // writer.writeInt32( "origSeverity", bean.getOrigSeverity().getValue()
        // );
        // writer.writeDateTime( "eventTime", bean.getEventTime().getTime() );
        // writer.writeString( "alarmId", bean.getAlarmId() );
        // writer.writeString( "specificProblemID", bean.getSpecificProblemID()
        // );
        // writer.writeString( "specificProblem", bean.getSpecificProblem() );
        // writer.writeString( "neUID", bean.getNeUID() );
        // writer.writeString( "neName", bean.getNeName() );
        // writer.writeString( "neType", bean.getNeType() );
        // writer.writeString( "objectUID", bean.getObjectUID() );
        // writer.writeString( "objectName", bean.getObjectName() );
        // writer.writeString( "objectType", bean.getObjectType() );
        // writer.writeString( "locationInfo", bean.getLocationInfo() );
        // writer.writeString( "addInfo", bean.getAddInfo() );
        // writer.writeString( "holderType", bean.getHolderType() );
        // writer.writeString( "alarmCheck", bean.getAlarmCheck() );
        // writer.writeInt32( "layer", bean.getLayer() );
        // writer.writeDateTime( "createTime", bean.getCreateTime().getTime() );
        writer.writeEndDocument();
    }

    @Override
    public Class<CommonBean> getEncoderClass()
    {
        return CommonBean.class;
    }

    @Override
    public CommonBean decode( BsonReader reader, DecoderContext arg1 )
    {
        CommonBean bean = new CommonBean();
        reader.readStartDocument();
        // bean.setAlarmSeq( reader.readInt64( "alarmSeq" ) );
        // bean.setAlarmTitle( reader.readString( "alarmTitle" ) );
        // int alarmStatus = reader.readInt32( "alarmStatus" );
        // bean.setAlarmStatus( AlarmDataEnum.getAlarmStatus( alarmStatus ) );
        // bean.setAlarmType( reader.readString( "alarmType" ) );
        // int origSeverity = reader.readInt32( "origSeverity" );
        // bean.setOrigSeverity( AlarmDataEnum.getOrigSeverity( origSeverity )
        // );
        // bean.setEventTime( new Date( reader.readDateTime( "eventTime" ) ) );
        // bean.setAlarmId( reader.readString( "alarmId" ) );
        // bean.setSpecificProblemID( reader.readString( "specificProblemID" )
        // );
        // bean.setSpecificProblem( reader.readString( "specificProblem" ) );
        // bean.setNeUID( reader.readString( "neUID" ) );
        // bean.setNeName( reader.readString( "neName" ) );
        // bean.setNeType( reader.readString( "neType" ) );
        // bean.setObjectUID( reader.readString( "objectUID" ) );
        // bean.setObjectName( reader.readString( "objectName" ) );
        // bean.setObjectType( reader.readString( "objectType" ) );
        // bean.setLocationInfo( reader.readString( "locationInfo" ) );
        // bean.setAddInfo( reader.readString( "addInfo" ) );
        // bean.setHolderType( reader.readString( "holderType" ) );
        // bean.setAlarmCheck( reader.readString( "alarmCheck" ) );
        // bean.setLayer( reader.readInt32( "layer" ) );
        reader.readEndDocument();
        return bean;
    }
}
