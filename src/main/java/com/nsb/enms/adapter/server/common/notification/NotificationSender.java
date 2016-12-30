package com.nsb.enms.adapter.server.common.notification;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.adapter.server.common.constants.ConfigKey;
import com.nsb.enms.adapter.server.common.db.mgr.AdpEqusDbMgr;
import com.nsb.enms.adapter.server.common.db.mgr.AdpNesDbMgr;
import com.nsb.enms.adapter.server.common.db.mgr.AdpTpsDbMgr;
import com.nsb.enms.adapter.server.common.db.mgr.AdpXcsDbMgr;
import com.nsb.enms.adapter.server.common.utils.GenerateKeyOnNeUtil;
import com.nsb.enms.adapter.server.sdh.q3gateway.entity.EventType;
import com.nsb.enms.adapter.server.sdh.q3gateway.entity.NotificationEntity;
import com.nsb.enms.alarm.NMSAlarm;
import com.nsb.enms.common.AlarmSeverity;
import com.nsb.enms.common.AlarmType;
import com.nsb.enms.common.EntityType;
import com.nsb.enms.common.enms_mq.EnmsPubFactory;
import com.nsb.enms.common.enms_mq.EnmsPublisher;

public class NotificationSender {
	private static NotificationSender inst_ = null;

	private static EnmsPublisher publisher = null;

	private static final Logger log = LogManager.getLogger(NotificationSender.class);

	private NotificationSender() {

	}

	public static NotificationSender instance() {
		if (inst_ == null) {
			inst_ = new NotificationSender();
		}

		return inst_;
	}

	public void init() {
		int port = ConfLoader.getInstance().getInt(ConfigKey.NOTIF_SERVER_PORT, ConfigKey.DEFAULT_NOTIF_SERVER_PORT);
		publisher = EnmsPubFactory.createMqPub(port);
	}

	public void send(NotificationEntity entity) {
		EventType eventType = entity.getEventType();
		// Long eventTime = TimeUtil.getTime(entity.getEventTime());
		String moc = entity.getMoc().getMoc();
		String moi = entity.getMoi().getMoi();
		String neId = moi.split("/")[1].split("=")[1];
		EntityType objectType = getObjectType(moc);
		String keyOnNe = GenerateKeyOnNeUtil.generateKeyOnNe(objectType, moc, moi);
		String objectID = getObjectId(objectType, neId, keyOnNe);
		if (StringUtils.isEmpty(objectID)) {
			return;
		}

		switch (eventType) {
		case OBJECT_CREATION:
			publisher.sendOC(objectType, objectID);
			break;
		case OBJECT_DELETION:
			publisher.sendOD(objectType, objectID);
			break;
		case ATTRIBUTE_VALUE_CHANGE:
		case STATE_CHANGE:
			publisher.sendAVC(objectType, objectID, entity.getDefinition().getAttributeID(),
					entity.getDefinition().getNewAttributeValue(), entity.getDefinition().getOldAttributeValue());
			break;
		case ALARM:
			// AlarmType alarmType =
			// getAlarmType(entity.getAttributeInfo().get("alarmType"));
			// AlarmSeverity alarmSeverity =
			// getAlarmSeverity(entity.getAttributeInfo().get("perceivedSeverity"));
			// String probableCause =
			// entity.getAttributeInfo().get("probableCause");
			// Alarm alarm = publisher.createAlarm(AlarmCode.ALM_NE_OUT_OF_MNGT,
			// alarmType, alarmSeverity, eventTime, null,
			// null, probableCause, objectType, objectID, null, null, "");
			// publisher.sendAlarm( new AlarmNEMisalignment(objectID) );
			break;
		default:
			break;
		}
	}

	public void sendAvcNotif(EntityType entityType, String objectID, String key, String value, String oldValue) {
		publisher.sendAVC(entityType, objectID, key, value, (oldValue == null ? StringUtils.EMPTY : oldValue));
	}

	public void sendOcNotif(EntityType objectType, String objectID) {
		publisher.sendOC(objectType, objectID);
	}

	public void sendOdNotif(EntityType objectType, String objectID) {
		publisher.sendOD(objectType, objectID);
	}

	// public void sendAlarm(AlarmCode alarmCode, AlarmType alarmType,
	// AlarmSeverity severity, Long eventTime,
	// Long occureTime, Long clearTime, String probableCause, EntityType
	// objectType, Integer objectId,
	// Integer ackStatus, Long ackTime, String description) {
	// Alarm alarm = publisher.createAlarm(alarmCode, alarmType, severity,
	// eventTime, occureTime, clearTime,
	// probableCause, objectType, objectId, ackStatus, ackTime, description);
	// send(alarm);
	// }

	public void sendAlarm(NMSAlarm alarm) {
		publisher.sendAlarm(alarm);
	}

	private EntityType getObjectType(String moc) {
		moc = moc.toLowerCase();
		if (moc.contains("networkelement") || moc.contains("ne")) {
			return EntityType.NE;
		}

		if (moc.contains("ttp")) {
			return EntityType.TP;
		}

		if (moc.contains("equipment")) {
			return EntityType.BOARD;
		}

		if (moc.contains("connection")) {
			return EntityType.CONNECTION;
		}

		return EntityType.NE;
	}

	private AlarmType getAlarmType(String type) {
		if ("equipmentAlarm".equals(type)) {
			return AlarmType.EQUIPMENT;
		} else if ("communicationsAlarm".equals(type)) {
			return AlarmType.COMMUNICATION;
		} else if ("environmentalAlarm".equals(type)) {
			return AlarmType.ENVIRONMENT;
		} else if ("processingErrorAlarm".equals(type)) {
			return AlarmType.PROCESSING_ERROR;
		} else if ("qualityofServiceAlarm".equals(type)) {
			return AlarmType.QoS;
		} else {
			return AlarmType.NONE;
		}
	}

	private AlarmSeverity getAlarmSeverity(String type) {
		if ("major".equals(type)) {
			return AlarmSeverity.MAJOR;
		} else if ("minor".equals(type)) {
			return AlarmSeverity.MINOR;
		} else if ("critical".equals(type)) {
			return AlarmSeverity.CRITICAL;
		} else if ("warning".equals(type)) {
			return AlarmSeverity.WARNING;
		} else if ("cleared".equals(type)) {
			return AlarmSeverity.CLEARED;
		} else {
			return AlarmSeverity.INDETERMINATE;
		}
	}

	private String getObjectId(EntityType objectType, String neId, String keyOnNe) {
		switch (objectType) {
		case NE:
			AdpNesDbMgr nesDbMgr = new AdpNesDbMgr();
			try {
				return nesDbMgr.getIdByKeyOnNe(keyOnNe);
			} catch (Exception e) {
				log.error("AdpNesDbMgr::getIdByKeOneNe", e);
				return null;
			}
		case TP:
			AdpTpsDbMgr tpsDbMgr = new AdpTpsDbMgr();
			try {
				return tpsDbMgr.getIdByKeyOnNe(neId, keyOnNe);
			} catch (Exception e) {
				log.error("AdpTpsDbMgr::getIdByKeOneNe", e);
				return null;
			}
		case BOARD:
			AdpEqusDbMgr equsDbMgr = new AdpEqusDbMgr();
			try {
				return equsDbMgr.getIdByKeyOnNe(neId, keyOnNe);
			} catch (Exception e) {
				log.error("AdpEqusDbMgr::getIdByKeyOnNe", e);
				return null;
			}
		case CONNECTION:
			AdpXcsDbMgr xcsDbMgr = new AdpXcsDbMgr();
			try {
				return xcsDbMgr.getIdByKeyOnNe(neId, keyOnNe);
			} catch (Exception e) {
				log.error("AdpXcsDbMgr::getIdByKeyOnNe", e);
				return null;
			}
		default:
			return null;
		}
	}
}