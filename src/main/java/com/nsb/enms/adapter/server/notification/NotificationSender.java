package com.nsb.enms.adapter.server.notification;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.adapter.server.common.conf.ConfigKey;
import com.nsb.enms.adapter.server.common.utils.GenerateKeyOnNeUtil;
import com.nsb.enms.adapter.server.common.utils.TimeUtil;
import com.nsb.enms.adapter.server.db.mgr.AdpEqusDbMgr;
import com.nsb.enms.adapter.server.db.mgr.AdpNesDbMgr;
import com.nsb.enms.adapter.server.db.mgr.AdpTpsDbMgr;
import com.nsb.enms.adapter.server.db.mgr.AdpXcsDbMgr;
import com.nsb.enms.adapter.server.notification.entity.EventType;
import com.nsb.enms.adapter.server.notification.entity.NotificationEntity;
import com.nsb.enms.common.AlarmSeverity;
import com.nsb.enms.common.AlarmType;
import com.nsb.enms.common.EntityType;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.common.enms_mq.EnmsPubFactory;
import com.nsb.enms.common.enms_mq.EnmsPublisher;
import com.nsb.enms.restful.model.notif.Alarm;
import com.nsb.enms.restful.model.notif.AvcBody;
import com.nsb.enms.restful.model.notif.OcBody;
import com.nsb.enms.restful.model.notif.OdBody;

public class NotificationSender
{
    private static NotificationSender inst_ = null;

    private static EnmsPublisher publisher = null;

    private static final Logger log = LogManager
            .getLogger( NotificationSender.class );

    private NotificationSender()
    {

    }

    public static NotificationSender instance()
    {
        if( inst_ == null )
        {
            inst_ = new NotificationSender();
        }

        return inst_;
    }

    public void init()
    {
        int port = ConfLoader.getInstance().getInt( ConfigKey.NOTIF_SERVER_PORT,
            ConfigKey.DEFAULT_NOTIF_SERVER_PORT );
        publisher = EnmsPubFactory.createMqPub( port );
    }

    public void send( NotificationEntity entity )
    {
        EventType eventType = entity.getEventType();
        String eventTime = entity.getEventTime();
        String moc = entity.getMoc().getMoc();
        String moi = entity.getMoi().getMoi();
        EntityType objectType = getObjectType( moc );
        String keyOnNe = GenerateKeyOnNeUtil.generateKeyOnNe( objectType, moc, moi );
        String objectID = getObjectId( objectType, keyOnNe, moi );
        if (StringUtils.isEmpty( objectID ))
        {
            return;
        }
        
        switch( eventType )
        {
            case OBJECT_CREATION:
                OcBody ocBody = publisher.createOcBody( eventTime, objectType,
                    objectID );
                send( ocBody );
                break;
            case OBJECT_DELETION:
                OdBody odBody = publisher.createOdBody( eventTime, objectType,
                    objectID );
                send( odBody );
                break;
            case ATTRIBUTE_VALUE_CHANGE:
            case STATE_CHANGE:
                AvcBody avc = publisher.createAvcBody( eventTime, objectType,
                    objectID, entity.getDefinition().getAttributeID(), "String",
                    entity.getDefinition().getNewAttributeValue(),
                    entity.getDefinition().getOldAttributeValue() );
                send( avc );
                break;
            case ALARM:
                AlarmType alarmType = getAlarmType( entity.getAttributeInfo().get( "alarmType" ) );
                AlarmSeverity alarmSeverity = getAlarmSeverity(entity.getAttributeInfo().get( "perceivedSeverity" ));
                String probableCause = entity.getAttributeInfo().get( "probableCause" );
                Alarm alarm = publisher.createAlarm( ErrorCode.ALM_NE_OUT_OF_MNGT, alarmType, alarmSeverity, eventTime, "", "", probableCause, objectType, objectID, "", "", "" );
                send( alarm );
                break;
            default:
                break;
        }
    }

    private void send( Object object )
    {
        publisher.sendMessage( object );
    }

    public void sendAvcNotif( EntityType objectType, String objectID,
            String key, String valueType, String value, String oldValue )
    {
        String eventTime = TimeUtil.getLocalTmfTime();
        
        AvcBody avc = publisher.createAvcBody( eventTime, objectType, objectID,
            key, valueType, value, oldValue );

        send( avc );
    }

    public void sendOcNotif( EntityType objectType, String objectID )
    {
        String eventTime = TimeUtil.getLocalTmfTime();
        OcBody oc = publisher.createOcBody( eventTime, objectType, objectID );
        send( oc );
    }

    public void sendOdNotif( EntityType objectType, String objectID )
    {
        String eventTime = TimeUtil.getLocalTmfTime();
        OdBody od = publisher.createOdBody( eventTime, objectType, objectID );
        send( od );
    }

    public void sendAlarm( ErrorCode alarmCode, AlarmType alarmType,
            AlarmSeverity severity, String eventTime, String occureTime,
            String clearTime, String probableCause, EntityType objectType,
            String objectId, String ackStatus, String ackTime,
            String description )
    {
        Alarm alarm = publisher.createAlarm( alarmCode, alarmType, severity,
            eventTime, occureTime, clearTime, probableCause, objectType,
            objectId, ackStatus, ackTime, description );
        send( alarm );
    }

    private EntityType getObjectType( String moc )
    {
        moc = moc.toLowerCase();
        if( moc.contains( "networkelement" )
                || moc.contains( "ne" ) )
        {
            return EntityType.NE;
        }

        if( moc.contains( "ttp" ) )
        {
            return EntityType.TP;
        }

        if( moc.contains( "equipment" ) )
        {
            return EntityType.BOARD;
        }
        
        if (moc.contains( "connection" ))
        {
            return EntityType.CONNECTION;
        }

        return EntityType.NE;
    }
    
    private AlarmType getAlarmType( String type )
    {
        if( "equipmentAlarm".equals( type ) )
        {
            return AlarmType.ALM_EQUIPMENT;
        }
        else if( "communicationsAlarm".equals( type ) )
        {
            return AlarmType.ALM_COMMUNICATION;
        }
        else if( "environmentalAlarm".equals( type ) )
        {
            return AlarmType.ALM_ENVIRONMENT;
        }
        else if( "processingErrorAlarm".equals( type ) )
        {
            return AlarmType.ALM_PROCESSING_ERROR;
        }
        else if( "qualityofServiceAlarm".equals( type ) )
        {
            return AlarmType.ALM_QoS;
        }
        else
        {
            return AlarmType.ALM_NONE;
        }
    }
    
    private AlarmSeverity getAlarmSeverity( String type )
    {
        if( "major".equals( type ) )
        {
            return AlarmSeverity.MAJOR;
        }
        else if( "minor".equals( type ) )
        {
            return AlarmSeverity.MINOR;
        }
        else if( "critical".equals( type ) )
        {
            return AlarmSeverity.CRITICAL;
        }
        else if( "warning".equals( type ) )
        {
            return AlarmSeverity.WARNING;
        }
        else if( "cleared".equals( type ) )
        {
            return AlarmSeverity.CLEARED;
        }
        else
        {
            return AlarmSeverity.INDETERMINATE;
        }
    }

    private String getObjectId( EntityType objectType, String keyOnNe, String moi )
    {
        switch( objectType )
        {
            case NE:
                AdpNesDbMgr nesDbMgr = new AdpNesDbMgr();
                try
                {
                    return nesDbMgr.getIdByKeyOnNe( keyOnNe );
                }
                catch( Exception e )
                {
                    log.error( "AdpNesDbMgr::getIdByKeOneNe", e );
                    return null;
                }
            case TP:
                AdpTpsDbMgr tpsDbMgr = new AdpTpsDbMgr();
                try
                {
                    return tpsDbMgr.getIdByKeyOnNe( keyOnNe );
                }
                catch( Exception e )
                {
                    log.error( "AdpTpsDbMgr::getIdByKeOneNe", e );
                    return null;
                }
            case BOARD:
                AdpEqusDbMgr equsDbMgr = new AdpEqusDbMgr();
                try
                {
                    return equsDbMgr.getIdByAid( moi );
                }
                catch (Exception e) 
                {
                    log.error( "AdpEqusDbMgr::getIdByAid", e );
                    return null;
                }
            case CONNECTION:
                AdpXcsDbMgr xcsDbMgr = new AdpXcsDbMgr();
                try
                {
                    return xcsDbMgr.getIdByAid( moi );
                }
                catch (Exception e) 
                {
                    log.error( "AdpXcsDbMgr::getIdByAid", e );
                    return null;
                }
            default:
                return null;
        }
    }
}