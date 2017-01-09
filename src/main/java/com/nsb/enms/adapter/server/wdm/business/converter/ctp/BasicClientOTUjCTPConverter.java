package com.nsb.enms.adapter.server.wdm.business.converter.ctp;


import com.nsb.enms.adapter.server.wdm.action.entity.SnmpTpEntity;
import com.nsb.enms.restful.model.adapter.AdpTp;

/**
 * Created by minichen on 2017/1/5.
 * Client OTUj CTP Converter
 * adpCTP: 转换后填写的CTP
 * adpPort: 它所属的Port
 * entity: 原始的entity
 */
public class BasicClientOTUjCTPConverter{
    private static BasicClientOTUjCTPConverter instance = new BasicClientOTUjCTPConverter();
    public static BasicClientOTUjCTPConverter getInstance() {
        return instance;
    }
    private BasicClientOTUjCTPConverter() {
    }

    public void convert(int j, AdpTp adpCTP, AdpTp adpPort, SnmpTpEntity entity) {
        //TODO: "/otu<j>=1"
        //...
    }
}
