package com.nsb.enms.adapter.server.wdm.business.converter.ctp;


import com.nsb.enms.adapter.server.wdm.action.entity.SnmpTpEntity;
import com.nsb.enms.restful.model.adapter.AdpTp;

/**
 * Created by minichen on 2017/1/5.
 * Line OCH CTP Converter
 * adpCTP: 转换后填写的CTP
 * adpPort: 它所属的Port
 * entity: 原始的entity
 */
public class BasicLineOCHCTPConverter{
    private static BasicLineOCHCTPConverter instance = new BasicLineOCHCTPConverter();
    public static BasicLineOCHCTPConverter getInstance() {
        return instance;
    }
    private BasicLineOCHCTPConverter() {
    }
    public void convert(AdpTp adpCTP, AdpTp adpPort, SnmpTpEntity entity) {
        //TODO: "/frequency= /tunable-number=1"
        //...
    }
}
