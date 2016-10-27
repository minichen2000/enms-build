package com.nsb.enms.adapter.server.common.conf;

public class ConfigKey {
	public static final String REFERENCE_CONF_FILES = "REFERENCE_CONF_FILES";

	public static final String TSTMGR_SCRIPT_DIR = "TSTMGR_SCRIPT_DIR";

	public static final String DEFAULT_TSTMGR_SCRIPT_DIR = "/opt/nms/EML_1/TSTMGR/script/";

	public static final String DEFAULT_TSTMGR_SCENARIO_DIR = "/opt/nms/EML_1/TSTMGR/scenario/";

	public static final String TSTMGR_SCRIPT = "TSTMGR_SCRIPT";

	public static final String DEFAULT_TSTMGR_SCRIPT = DEFAULT_TSTMGR_SCRIPT_DIR + "tstmgr";

	public static final String GET_EQ_REQ = "GET_EQ_REQ";

	public static final String DEFAULT_GET_EQ_REQ = DEFAULT_TSTMGR_SCENARIO_DIR + "syncEquipment.req";

	public static final String GET_NE_REQ = "GET_NE_REQ";

	public static final String DEFAULT_GET_NE_REQ = DEFAULT_TSTMGR_SCENARIO_DIR + "ListNe.req";

	public static final String GET_XC_REQ = "GET_XC_REQ";

	public static final String DEFAULT_XC_GET_REQ = DEFAULT_TSTMGR_SCENARIO_DIR + "syncXC.req";

	public static final String CREATE_XC_VC4_REQ = "CREATE_XC_VC4_REQ";

	public static final String DEFAULT_CREATE_XC_VC4_REQ = DEFAULT_TSTMGR_SCENARIO_DIR + "createXC-VC4-withMatrix.req";

	public static final String CREATE_XC_VC12_REQ = "CREATE_XC_VC12_REQ";

	public static final String DEFAULT_CREATE_XC_VC12_REQ = DEFAULT_TSTMGR_SCENARIO_DIR
			+ "createXC-VC12-withMatrixCTP.req";

	public static final String DELETE_XC_REQ = "DELETE_XC_REQ";

	public static final String DEFAULT_DELETE_XC_REQ = DEFAULT_TSTMGR_SCENARIO_DIR + "deleteXC.req";

	public static final String GET_PORT_REQ = "GET_PORT_REQ";

	public static final String DEFAULT_GET_PORT_REQ = DEFAULT_TSTMGR_SCENARIO_DIR + "syncPort.req";

	public static final String GET_CONNECTION_PORT_REQ = "GET_CONNECTION_PORT_REQ";

	public static final String DEFAULT_GET_CONNECTION_PORT_REQ = DEFAULT_TSTMGR_SCENARIO_DIR + "syncCTP.req";

	public static final String CREATE_NE_REQ = "CREATE_NE_REQ";

	public static final String DEFAULT_CREATE_NE_REQ = DEFAULT_TSTMGR_SCENARIO_DIR + "CreateNe.req";

	public static final String SET_NE_ADDR_REQ = "SET_NE_ADDR_REQ";

	public static final String DEFAULT_SET_NE_ADDR_REQ = DEFAULT_TSTMGR_SCENARIO_DIR + "SetNeAddress.req";

	public static final String SET_NE_ISA_ADDR_REQ = "SET_NE_ISA_ADDR_REQ";

	public static final String DEFAULT_SET_NE_ISA_ADDR_REQ = DEFAULT_TSTMGR_SCENARIO_DIR + "SetNeAddressIsa.req";

	public static final String START_SUPERVISION_REQ = "START_SUPERVISION_REQ";

	public static final String DEFAULT_START_SUPERVISION_REQ = DEFAULT_TSTMGR_SCENARIO_DIR + "StartSupervision.req";

	// public static final String Q3_EMLIM_SCRIPT_DIR = "EMLIM_SCRIPT_DIR";

	// public static final String DEFAULT_Q3_EMLIM_SCRIPT_DIR =
	// "/opt/nms/EML_1/eml/adm/script/";

	// public static final String START_Q3_EMLIM_SCRIPT = "Q3_EMLIM_SCRIPT";

	// public static final String DEFAULT_START_Q3_EMLIM_SCRIPT =
	// DEFAULT_Q3_EMLIM_SCRIPT_DIR + "run";

	public static final String LIST_GROUP_SCRIPT = "LIST_GROUP_SCRIPT";

	public static final String DEFAULT_LIST_GROUP_SCRIPT = DEFAULT_TSTMGR_SCENARIO_DIR + "ListGroup.req";

	// public static final String KILL_EMLIM_SCRIPT = "KILL_EMLIM_SCRIPT";

	// public static final String DEFAULT_KILL_EMLIM_SCRIPT =
	// DEFAULT_TSTMGR_SCENARIO_DIR + "killEmlIm.sh";

	public static final String DELETE_NE_REQ = "DELETE_NE_REQ";

	public static final String DEFAULT_DELETE_NE_REQ = DEFAULT_TSTMGR_SCENARIO_DIR + "RemoveNe.req";

	public static final String STOP_SUPERVISION_REQ = "STOP_SUPERVISION_REQ";

	public static final String DEFAULT_STOP_SUPERVISION_REQ = DEFAULT_TSTMGR_SCENARIO_DIR + "StopSupervision.req";

	public static final String GET_MANAGER_ADDRESS_REQ = "GET_MANAGER_ADDRESS_REQ";

	public static final String DEFAULT_GET_MANAGER_ADDRESS_REQ = DEFAULT_TSTMGR_SCENARIO_DIR + "GetManagerAddress.req ";

	public static final String TERMINATE_TUG3_TO_TU12_REQ = "TERMINATE_TUG3_TO_TU12_REQ";

	public static final String DEFAULT_TERMINATE_TUG3_TO_TU12_REQ = DEFAULT_TSTMGR_SCENARIO_DIR
			+ "terminateTug3-TU12.req";

	public static final String TERMINATE_TUG3_TO_TU3_REQ = "TERMINATE_TUG3_TO_TU3_REQ";

	public static final String DEFAULT_TERMINATE_TUG3_TO_TU3_REQ = DEFAULT_TSTMGR_SCENARIO_DIR
			+ "terminateTug3-TU3.req";

	public static final String OID_TABLE_FILE = "OID_TABLE_FILE";

	public static final String DEFAULT_GET_T_PORT_REQ = DEFAULT_TSTMGR_SCENARIO_DIR + "syncTTP.req";

	public static final String SET_MAIN_OS_ADDR_REQ = "SET_MAIN_OS_ADDR_REQ";

	public static final String DEFAULT_SET_MAIN_OS_ADDR_REQ = DEFAULT_TSTMGR_SCENARIO_DIR + "SetMainOSAddress.req";

	public static final String SET_SPARE_OS_ADDR_REQ = "SET_MAIN_OS_ADDR_REQ";

	public static final String DEFAULT_SET_SPARE_OS_ADDR_REQ = DEFAULT_TSTMGR_SCENARIO_DIR + "SetSpareOSAddress.req";

	public static final String EMLIM_MONITOR_INTERVAL = "EMLIM_MONITOR_INTERVAL";

	public static final int DEFAULT_EMLIM_MONITOR_INTERVAL = 300 * 1000;

	public static final String ADP_PING_INTERVAL = "ADP_HEARTBEAT_INTERVAL";

	public static final int DEFAULT_ADP_PING_INTERVAL = 60 * 1000;

	public static final String ADP_PING_MAX_NUM = "ADP_PING_MAX_NUM";

	public static final int DEFAULT_ADP_PING_MAX_NUM = 10;

	public static final String GET_T_PORT_REQ = "GET_T_PORT_REQ";

	public static final String NOTIF_SERVER_PORT = "NOTIF_SERVER_PORT";

	public static final int DEFAULT_NOTIF_SERVER_PORT = 19000;

	public static final String Q3_WS_SERVER_URI = "Q3_WS_SERVER_URI";

	public static final String CTRL_URL = "CTRL_URL";

	public static final String ADP_ID = "ADP_ID";

	public static final String ADP_IP = "ADP_IP";

	public static final String ADP_ADDRESS = "ADP_ADDRESS";

	public static final String ADP_PORT = "ADP_PORT";

	public static final int DEFAULT_ADP_PORT = 9000;

	public static final String REG_PERIOD = "REG_PERIOD";

	public static final int DEFAULT_REG_PERIOD = 60 * 1000;

	public static final String MAX_NE_OF_ONE_EMLIM = "MAX_NE_OF_ONE_EMLIM";

	public static final int DEFAULT_MAX_NE_OF_ONE_EMLIM = 200;
}