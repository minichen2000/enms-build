package com.nsb.enms.adapter.server.common.constants;

public class ConfigKey {
	public static final String REFERENCE_CONF_FILES = "REFERENCE_CONF_FILES";

	public static final String RI_FILE_PATH = "RI_FILE_PATH";

	public static final String ADP_PING_INTERVAL = "ADP_HEARTBEAT_INTERVAL";

	public static final int DEFAULT_ADP_PING_INTERVAL = 60 * 1000;

	public static final String ADP_PING_MAX_NUM = "ADP_PING_MAX_NUM";

	public static final int DEFAULT_ADP_PING_MAX_NUM = 10;

	public static final String NOTIF_SERVER_PORT = "NOTIF_SERVER_PORT";

	public static final int DEFAULT_NOTIF_SERVER_PORT = 19000;

	public static final String CTRL_URL = "CTRL_URL";

	public static final String ADP_ID = "ADP_ID";

	public static final String ADP_IP = "ADP_IP";

	public static final String ADP_ADDRESS = "ADP_ADDRESS";

	public static final String ADP_PORT = "ADP_PORT";

	public static final int DEFAULT_ADP_PORT = 9000;

	public static final String REG_PERIOD = "REG_PERIOD";

	public static final int DEFAULT_REG_PERIOD = 60 * 1000;

	public static final int DEFAULT_SNMP_PORT = 162;
	
	public static final String MAX_SYSUP_TIME = "MAX_SYSUP_TIME";

	public static final int DEFAULT_MAX_SYSUP_TIME = 50;
	
	public static final String MAX_CLEAN_TIME = "MAX_CLEAN_TIME";

	public static final int DEFAULT_MAX_CLEAN_TIME = 5 * 1000;
	
	public static final String NOTIF_CLEAN_PERIOD = "NOTIF_CLEAN_PERIOD";

	public static final int DEFAULT_NOTIF_CLEAN_PERIOD = 1 * 1000;
}