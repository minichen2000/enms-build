package com.nsb.enms.action.common.conf;

public class CommonConstants
{
    public static final String TSTMGR_SCRIPT_DIR = "/opt/nms/EML_1/TSTMGR/script/";

    public static final String TSTMGR_SCENARIO_DIR = "/opt/nms/EML_1/TSTMGR/scenario/";

    public static final String TSTMGR_SCRIPT = "/opt/nms/EML_1/TSTMGR/script/tstmgr";

    public static final String PORT_GET_REQ = TSTMGR_SCENARIO_DIR
            + "syncPort.req";

    public static final String XC_GET_REQ = TSTMGR_SCENARIO_DIR + "syncXC.req";

    public static final String NE_GET_REQ = TSTMGR_SCENARIO_DIR + "ListNe.req";

    public static final String EQ_GET_REQ = TSTMGR_SCENARIO_DIR
            + "syncEquipment.req";

    public static final String NE_CREATE_REQ = TSTMGR_SCENARIO_DIR
            + "CreateNe.req";

    public static final String SET_NE_ADDR_REQ = TSTMGR_SCENARIO_DIR
            + "SetNeAddress.req";

    public static final String SET_NE_ISA_ADDR_REQ = TSTMGR_SCENARIO_DIR
            + "SetNeAddressIsa.req";

    public static final String START_SUPERVISION_REQ = TSTMGR_SCENARIO_DIR
            + "StartSupervision.req";
}
