package com.nsb.enms.adapter.server.wdm.action.method.eq;

import java.util.HashMap;
import java.util.Map;

public class TypeMapUtil
{
    private static Map<String, String> oidShelfTypeMap = new HashMap<String, String>();
    
    private static Map<String, String> oidCardTypeMap = new HashMap<String, String>();
    
    static 
    {
        //Shelf type
        oidShelfTypeMap.put( "1.3.6.1.4.1.7483.1.4.1", "Empty" );
        oidShelfTypeMap.put( "1.3.6.1.4.1.7483.1.4.2", "Unknow" );
        oidShelfTypeMap.put( "1.3.6.1.4.1.7483.1.4.7", "SFD44" );
        oidShelfTypeMap.put( "1.3.6.1.4.1.7483.1.4.8", "DCM" );
        oidShelfTypeMap.put( "1.3.6.1.4.1.7483.1.4.9", "Universal" );
        oidShelfTypeMap.put( "1.3.6.1.4.1.7483.1.4.10", "SFD44B" );
        oidShelfTypeMap.put( "1.3.6.1.4.1.7483.1.4.11", "ITLB" );
        oidShelfTypeMap.put( "1.3.6.1.4.1.7483.1.4.12", "PSS-32" );
        oidShelfTypeMap.put( "1.3.6.1.4.1.7483.1.4.13", "PSS-16" );
        oidShelfTypeMap.put( "1.3.6.1.4.1.7483.1.4.14", "SFD40" );
        oidShelfTypeMap.put( "1.3.6.1.4.1.7483.1.4.15", "SFD40B" );
        oidShelfTypeMap.put( "1.3.6.1.4.1.7483.1.4.16", "PSS-4" );
        oidShelfTypeMap.put( "1.3.6.1.4.1.7483.1.4.18", "PSS-36" );
        oidShelfTypeMap.put( "1.3.6.1.4.1.7483.1.4.23", "PSS-64" );
        oidShelfTypeMap.put( "1.3.6.1.4.1.7483.1.4.24", "ITLU" );
        oidShelfTypeMap.put( "1.3.6.1.4.1.7483.1.4.25", "PSS32 Switch Universal" );
        oidShelfTypeMap.put( "1.3.6.1.4.1.7483.1.4.26", "PSS32 Switch 1P2T Universal" );
        oidShelfTypeMap.put( "1.3.6.1.4.1.7483.1.4.27", "PSC1*6" );
        oidShelfTypeMap.put( "1.3.6.1.4.1.7483.1.4.28", "PSS-8" );
        oidShelfTypeMap.put( "1.3.6.1.4.1.7483.1.4.29", "PSS-16II" );
        oidShelfTypeMap.put( "1.3.6.1.4.1.7483.1.4.30", "PSS-48" );
        oidShelfTypeMap.put( "1.3.6.1.4.1.7483.1.4.31", "PSS-96" );
        oidShelfTypeMap.put( "1.3.6.1.4.1.7483.1.4.32", "MSH8-FSM" );
        oidShelfTypeMap.put( "1.3.6.1.4.1.7483.1.4.33", "VWM-CW" );
        oidShelfTypeMap.put( "1.3.6.1.4.1.7483.1.4.34", "VWM-DM" );
        oidShelfTypeMap.put( "1.3.6.1.4.1.7483.1.4.35", "PSI1T Universal" );
        oidShelfTypeMap.put( "1.3.6.1.4.1.7483.1.4.36", "PSI1T Universal" );
        
        //Card type
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.1.1", "Empty" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.11.14", "112SCA1" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.11.13", "112SCX10" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.11.58", "112SDX11" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.11.24", "112SNA1" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.11.23", "112SNX10" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.11.3", "11DPE12" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.11.36", "11DPE12A" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.11.17", "11DPE12E" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.11.20", "11DPM12" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.11.56", "11DPM4E" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.11.55", "11DPM4M" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.11.54", "11DPM8" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.11.49", "11OPE8" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.11.50", "11QCE12X" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.11.12", "11QPA4" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.11.32", "11QPE24" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.11.29", "11QPEN4" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.11.1", "11STAR1" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.11.33", "11STAR1A" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.11.2", "11STGE12" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.11.4", "11STMM10" );
        oidCardTypeMap.put( ".3.6.1.4.1.7483.1.5.11.72", "12CE120" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.3.6", "12P120" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.11.59", "130SCA1" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.11.38", "130SCX10" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.11.66", "130SNQ10" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.11.52", "130SNX10" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.11.26", "1DPP24M" );
        
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.3.8", "1UD200" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.3.7", "20P200" );
        
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.11.69", "20UC200" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.11.51", "260SCX2" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.11.67", "2UC400" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.11.64", "30AN300" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.11.21", "43SCA1" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.11.31", "43SCGE1" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.11.16", "43SCX4" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.11.30", "43SCX4E" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.11.8", "43STA1P" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.11.6", "43STX4" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.11.9", "43STX4P" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.11.65", "4AN400" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.11.68", "4UC400" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.11.7", "4DPA2" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.11.5", "4DPA4" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.11.39", "4QPA8" );
        
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.7.10", "A2325A" );        
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.7.21", "A2P2125" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.7.27", "A4PSWG" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.7.24", "AA2DONW" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.7.29", "AAR-8A" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.7.4", "AHPHG" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.7.6", "AHPLG" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.7.5", "ALPHG" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.7.23", "AM2032A" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.7.18", "AM2125A" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.7.19", "AM2125B" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.7.17", "AM2318A" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.7.22", "AM2625A" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.7.26", "ASWG" );
        //oidCardTypeMap.put( "A1", "AWBEGR" );
        //oidCardTypeMap.put( "A2", "AWBILA" );
        //oidCardTypeMap.put( "A3", "AWBING" );
        //oidCardTypeMap.put( "A4", "CEC2" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.2.18", "CEC2" );
        
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.6.9", "CWR8" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.6.12", "CWR8-88" );
        
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.11.70", "D5X500" );
        
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.8.3", "DCM" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.2.3", "EC" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.2.8", "EC" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.2.21", "EC2I" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.2.15", "8EC2" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.2.23", "8EC2E" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.2.17", "32EC2" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.2.24", "32EC2E" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.2.19", "EC-CW" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.2.20", "EC-DW" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.6.5", "FAN" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.6.14", "FAN" );
        //oidCardTypeMap.put( "A5", "IROADM9M" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.1.10", "IROADMF" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.1.9", "IROADMV" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.6.14", "ITLB" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.6.23", "ITLU" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.11.74", "LCI1000" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.3.4", "MCS8-16" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.6.25", "MESH4" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.6.11", "MFC" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.61", "MON-OCM" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.7.30", "MON-OTDR" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.1.8", "MSH8-FSM" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.6.22", "MVAC" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.6.26", "MVAC8B" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.3.2", "OPSA" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.3.3", "OPSB" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.7.12", "OSCT" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.7.28", "OTDR" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.6.12", "PF" );
        //oidCardTypeMap.put( "A6", "PF" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.6.13", "PF" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.6.16", "PF" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.6.4", "PF" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.6.28", "PSC1-6" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.11.41", "PTPCTL" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.11.42", "PTPIO" );
        //oidCardTypeMap.put( "A7", "PTPIOC" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.7.16", "RA2P" );
        //oidCardTypeMap.put( "A8", "RA5P" );
        //oidCardTypeMap.put( "A9", "S13X100R" );
        //oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.3.5", "SC16" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.3.5", "SC96" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.20", "SFC1A" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.21", "SFC1B" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.22", "SFC1C" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.23", "SFC1D" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.24", "SFC1E" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.25", "SFC1F" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.26", "SFC1G" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.27", "SFC1H" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.13", "SFC2A" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.14", "SFC2B" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.15", "SFC2C" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.16", "SFC2D" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.17", "SFC4A" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.18", "SFC4B" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.19", "SFC8" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.40", "SFD2A" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.41", "SFD2B" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.42", "SFD2C" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.43", "SFD2D" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.44", "SFD2E" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.45", "SFD2F" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.46", "SFD2G" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.47", "SFD2H" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.48", "SFD2I" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.49", "SFD2L" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.50", "SFD2M" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.51", "SFD2N" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.52", "SFD2O" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.53", "SFD2P" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.54", "SFD2Q" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.55", "SFD2R" );
        
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.32", "SFD4A" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.33", "SFD4B" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.34", "SFD4C" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.35", "SFD4D" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.36", "SFD4E" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.37", "SFD4F" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.38", "SFD4G" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.39", "SFD4H" );
        
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.1", "SFD5A" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.2", "SFD5B" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.3", "SFD5C" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.4", "SFD5D" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.5", "SFD5E" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.6", "SFD5F" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.7", "SFD5G" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.8", "SFD5H" );
        
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.28", "SFD8A" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.29", "SFD8B" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.30", "SFD8C" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.12.31", "SFD8D" );
        
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.6.19", "SFD40" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.6.20", "SFD40B" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.6.10", "SFD44" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.6.13", "SFD44B" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.6.9", "SHFPNL" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.6.11", "SVAC" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.6.6", "USRPNL" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.6.10", "8USRPNL" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.1.6", "Virtual/Preset" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.6.21", "WR2-88" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.6.29", "WR20-TF" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.6.30", "WR20-TFM" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.6.24", "WR8-88A" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.6.27", "WR8-88AF" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.7.13", "WTOCM" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.7.20", "WTOCMA" );
        oidCardTypeMap.put( "1.3.6.1.4.1.7483.1.5.7.25", "WTOCM-F" );        
    }
    
    public static String getShelfType(String oid)
    {
        if (oidShelfTypeMap.containsKey( oid ))
        {
            return oidShelfTypeMap.get( oid );
        }
        return oid;
    }
    
    public static String getSlotCardType(String oid)
    {
        if (oidCardTypeMap.containsKey( oid ))
        {
            return oidCardTypeMap.get( oid );
        }
        return oid;
    }       
}