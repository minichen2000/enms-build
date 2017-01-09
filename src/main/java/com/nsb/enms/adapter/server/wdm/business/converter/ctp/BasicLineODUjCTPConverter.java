package com.nsb.enms.adapter.server.wdm.business.converter.ctp;

import com.nsb.enms.adapter.server.wdm.action.entity.SnmpTpEntity;
import com.nsb.enms.restful.model.adapter.AdpTp;

/**
 * Created by minichen on 2017/1/5.
 * Line ODUj CTP Converter
 * adpCTP: 转换后填写的CTP
 * adpPort: 它所属的Port
 * entity: 原始的entity
 */
public class BasicLineODUjCTPConverter{
    private static BasicLineODUjCTPConverter instance = new BasicLineODUjCTPConverter();
    public static BasicLineODUjCTPConverter getInstance() {
        return instance;
    }
    private BasicLineODUjCTPConverter() {
    }
    public void convert(int k, int j, int n, AdpTp adpCTP, AdpTp adpPort, SnmpTpEntity entity) {
        //TODO: "//odu<k>=1/odu<j>=1...n"
        //...
    }
}
