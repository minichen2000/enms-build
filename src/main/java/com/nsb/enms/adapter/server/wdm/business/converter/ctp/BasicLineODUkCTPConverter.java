package com.nsb.enms.adapter.server.wdm.business.converter.ctp;


import com.nsb.enms.adapter.server.wdm.action.entity.SnmpTpEntity;
import com.nsb.enms.restful.model.adapter.AdpTp;

/**
 * Created by minichen on 2017/1/5.
 * Line ODUk CTP Converter
 * adpCTP: 转换后填写的CTP
 * adpPort: 它所属的Port
 * entity: 原始的entity
 */
public class BasicLineODUkCTPConverter{
    private static BasicLineODUkCTPConverter instance = new BasicLineODUkCTPConverter();
    public static BasicLineODUkCTPConverter getInstance() {
        return instance;
    }
    private BasicLineODUkCTPConverter() {
    }
    public void convert(int k, AdpTp adpCTP, AdpTp adpPort, SnmpTpEntity entity) {
        //TODO: "/odu<k>=1"
        //...
    }
}
