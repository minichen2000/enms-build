package com.nsb.enms.adapter.server.wdm.utils;

import com.nsb.enms.adapter.server.wdm.action.entity.SnmpTpEntity;

/**
 * Created by minichen on 2017/1/5.
 */
public class PortConverterUtil {
    static public boolean isOTClientPort(SnmpTpEntity entity){
        //TODO: entity.nativeName.match("...-C[x]");
        return true;
    }
    static public boolean isOTLinePort(SnmpTpEntity entity){
        //TODO: entity.nativeName.match("...-L[x]");
        return true;
    }
    static public boolean isSFDChannelPort(SnmpTpEntity entity){
        //TODO: entity.nativeName.match("...-<numbers>");
        return true;
    }
    static public boolean isSFDOMDPort(SnmpTpEntity entity){
        //TODO: entity.nativeName.match("...-OMD");
        return true;
    }
    static public boolean isOASIGPort(SnmpTpEntity entity){
        //TODO: entity.nativeName.match("...-SIG");
        return true;
    }
    static public boolean isOALINEPort(SnmpTpEntity entity){
        //TODO: entity.nativeName.match("...-LINE");
        return true;
    }
    static public boolean isOADCMPort(SnmpTpEntity entity){
        //TODO: entity.nativeName.match("...-DCM");
        return true;
    }
    static public boolean isOAOSCPort(SnmpTpEntity entity){
        //TODO: entity.nativeName.match("...-OSC");
        return true;
    }
}
