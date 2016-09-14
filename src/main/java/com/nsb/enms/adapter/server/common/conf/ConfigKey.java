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

	public static final String EMLIM_SCRIPT_DIR = "EMLIM_SCRIPT_DIR";

	public static final String DEFAULT_EMLIM_SCRIPT_DIR = "";

	public static final String Q3_EMLIM_SCRIPT = "Q3_EMLIM_SCRIPT";

	public static final String DEFAULT_Q3_EMLIM_SCRIPT = "";

	public static final String LIST_GROUP_SCRIPT = "LIST_GROUP_SCRIPT";

	public static final String DEFAULT_LIST_GROUP_SCRIPT = DEFAULT_TSTMGR_SCENARIO_DIR + "ListGroup.req";

	public static final String KILL_EMLIM_SCRIPT = "KILL_EMLIM_SCRIPT";

	public static final String DEFAULT_KILL_EMLIM_SCRIPT = "";

	public static final String DELETE_NE_REQ = "DELETE_NE_REQ";

	public static final String DEFAULT_DELETE_NE_REQ = DEFAULT_TSTMGR_SCENARIO_DIR + "RemoveNe.req";

	public static final String STOP_SUPERVISION_REQ = "STOP_SUPERVISION_REQ";

	public static final String DEFAULT_STOP_SUPERVISION_REQ = DEFAULT_TSTMGR_SCENARIO_DIR + "StopSupervision.req";

	public static final String EMLIM_MONITOR_INTERVAL = "EMLIM_MONITOR_INTERVAL";

	public static final int DEFAULT_EMLIM_MONITOR_INTERVAL = 10 * 60 * 1000;

	public static final String GET_MANAGER_ADDRESS_REQ = "GET_MANAGER_ADDRESS_REQ";

	public static final String DEFAULT_GET_MANAGER_ADDRESS_REQ = DEFAULT_TSTMGR_SCENARIO_DIR + "GetManagerAddress.req ";

	public static final String TERMINATE_TUG3_TO_TU12_REQ = "TERMINATE_TUG3_TO_TU12_REQ";

	public static final String DEFAULT_TERMINATE_TUG3_TO_TU12_REQ = DEFAULT_TSTMGR_SCENARIO_DIR
			+ "terminateTug3-TU12.req";

	public static final String OID_TABLE_FILE = "OID_TABLE_FILE";

	public static final String DEFAULT_GET_T_PORT_REQ = DEFAULT_TSTMGR_SCENARIO_DIR + "syncTTP.req";

	public static final String GET_T_PORT_REQ = "GET_T_PORT_REQ";
}
