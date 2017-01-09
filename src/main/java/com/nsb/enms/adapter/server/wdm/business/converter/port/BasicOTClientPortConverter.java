package com.nsb.enms.adapter.server.wdm.business.converter.port;

import com.nsb.enms.adapter.server.wdm.action.entity.SnmpTpEntity;
import com.nsb.enms.adapter.server.wdm.business.itf.PortConverter;
import com.nsb.enms.restful.model.adapter.AdpTp;

/**
 * Created by minichen on 2017/1/5.
 * 一般OT盘Client侧Port的Converter
 */
public class BasicOTClientPortConverter implements PortConverter {
    private static BasicOTClientPortConverter instance = new BasicOTClientPortConverter();
    public static BasicOTClientPortConverter getInstance() {
        return instance;
    }
    private BasicOTClientPortConverter() {
    }
    public boolean convert(AdpTp adpPort, SnmpTpEntity entity) {
        BasicPortConverter.getInstance().convert(adpPort, entity);

        //TODO:一般OT盘Client侧Port特有属性的转换过程
        //...
        return true;
    }
}
