package com.nsb.enms.adapter.server.wdm.business.converter.ctp;


import com.nsb.enms.adapter.server.wdm.action.entity.SnmpTpEntity;
import com.nsb.enms.restful.model.adapter.AdpTp;

/**
 * Created by minichen on 2017/1/5.
 * Client ODUj CTP Converter
 * adpCTP: 转换后填写的CTP
 * adpPort: 它所属的Port
 * entity: 原始的entity
 */
public class BasicClientODUjCTPConverter{
    private static BasicClientODUjCTPConverter instance = new BasicClientODUjCTPConverter();
    public static BasicClientODUjCTPConverter getInstance() {
        return instance;
    }
    private BasicClientODUjCTPConverter() {
    }

    public void convert(int j, AdpTp adpCTP, AdpTp adpPort, SnmpTpEntity entity) {
        //TODO: "/odu<j>=1"
        //...
    }
}
