package com.nsb.enms.restful.adapter.server.common.conf;

public class ConfigKey
{
    public static final String TSTMGR_SCRIPT_DIR = "TSTMGR_SCRIPT_DIR";

    public static final String DEFAULT_TSTMGR_SCRIPT_DIR = "/opt/nms/EML_1/TSTMGR/script/";

    public static final String DEFAULT_TSTMGR_SCENARIO_DIR = "/opt/nms/EML_1/TSTMGR/scenario/";

    public static final String TSTMGR_SCRIPT = "TSTMGR_SCRIPT";

    public static final String DEFAULT_TSTMGR_SCRIPT = "/opt/nms/EML_1/TSTMGR/script/tstmgr";

    public static final String EQ_GET_REQ = "EQ_GET_REQ";

    public static final String DEFAULT_EQ_GET_REQ = DEFAULT_TSTMGR_SCENARIO_DIR
            + "syncEquipment.req";

    public static final String NE_GET_REQ = "NE_GET_REQ";

    public static final String DEFAULT_NE_GET_REQ = DEFAULT_TSTMGR_SCENARIO_DIR
            + "ListNe.req";

    public static final String XC_GET_REQ = "XC_GET_REQ";

    public static final String DEFAULT_XC_GET_REQ = DEFAULT_TSTMGR_SCENARIO_DIR
            + "syncXC.req";

    public static final String PORT_GET_REQ = "PORT_GET_REQ";

    public static final String DEFAULT_PORT_GET_REQ = DEFAULT_TSTMGR_SCENARIO_DIR
            + "syncPort.req";

    public static final String NE_CREATE_REQ = "NE_CREATE_REQ";

    public static final String DEFAULT_NE_CREATE_REQ = DEFAULT_TSTMGR_SCENARIO_DIR
            + "CreateNe.req";

    public static final String SET_NE_ADDR_REQ = "SET_NE_ADDR_REQ";

    public static final String DEFAULT_SET_NE_ADDR_REQ = DEFAULT_TSTMGR_SCENARIO_DIR
            + "SetNeAddress.req";

    public static final String SET_NE_ISA_ADDR_REQ = "SET_NE_ISA_ADDR_REQ";

    public static final String DEFAULT_SET_NE_ISA_ADDR_REQ = DEFAULT_TSTMGR_SCENARIO_DIR
            + "SetNeAddressIsa.req";

    public static final String START_SUPERVISION_REQ = "START_SUPERVISION_REQ";

    public static final String DEFAULT_START_SUPERVISION_REQ = DEFAULT_TSTMGR_SCENARIO_DIR
            + "StartSupervision.req";

    public static final String REFERENCE_CONF_FILES = "REFERENCE_CONF_FILES";

    public static final String Q3_EMLIM_SCRIPT = "Q3_EMLIM_SCRIPT";
    
    public static final String DEFAULT_Q3_EMLIM_SCRIPT = "Q3_EMLIM_SCRIPT";
}
