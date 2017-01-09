package com.nsb.enms.adapter.server.wdm.business.converter.ctp;


import com.nsb.enms.adapter.server.wdm.action.entity.SnmpTpEntity;
import com.nsb.enms.restful.model.adapter.AdpTp;

/**
 * Created by minichen on 2017/1/5.
 * Client OCH CTP Converter
 * adpCTP: 转换后填写的CTP
 * adpPort: 它所属的Port
 * entity: 原始的entity
 */
public class BasicClientOCHCTPConverter{
    private static BasicClientOCHCTPConverter instance = new BasicClientOCHCTPConverter();
    public static BasicClientOCHCTPConverter getInstance() {
        return instance;
    }
    private BasicClientOCHCTPConverter() {
    }
    public void convert(AdpTp adpCTP, AdpTp adpPort, SnmpTpEntity entity) {
        //TODO: "/frequency=WXYZ"
        //...
    }
}
