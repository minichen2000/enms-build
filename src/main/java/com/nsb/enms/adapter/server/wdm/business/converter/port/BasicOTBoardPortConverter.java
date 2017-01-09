package com.nsb.enms.adapter.server.wdm.business.converter.port;


import com.nsb.enms.adapter.server.wdm.action.entity.SnmpTpEntity;
import com.nsb.enms.adapter.server.wdm.business.itf.PortConverter;
import com.nsb.enms.adapter.server.wdm.utils.PortConverterUtil;
import com.nsb.enms.restful.model.adapter.AdpTp;

/**
 * Created by minichen on 2017/1/5.
 * 一般OT盘所有Port的Converter
 */
public class BasicOTBoardPortConverter implements PortConverter {
    private static BasicOTBoardPortConverter instance = new BasicOTBoardPortConverter();
    public static BasicOTBoardPortConverter getInstance() {
        return instance;
    }
    private BasicOTBoardPortConverter() {
    }

    public boolean convert(AdpTp adpPort, SnmpTpEntity entity) {
        //区分Client侧Port和Line侧Port，分别转换
        if(PortConverterUtil.isOTClientPort(entity)){
            BasicOTClientPortConverter.getInstance().convert(adpPort, entity);
        }else if(PortConverterUtil.isOTLinePort(entity)){
            BasicOTLinePortConverter.getInstance().convert(adpPort, entity);
        }
        return true;
    }
}
