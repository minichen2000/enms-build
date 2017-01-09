package com.nsb.enms.adapter.server.wdm.business.converter.port;


import com.nsb.enms.adapter.server.wdm.action.entity.SnmpTpEntity;
import com.nsb.enms.adapter.server.wdm.business.itf.PortConverter;
import com.nsb.enms.adapter.server.wdm.utils.PortConverterUtil;
import com.nsb.enms.restful.model.adapter.AdpTp;

/**
 * Created by minichen on 2017/1/5.
 * 一般OA盘所有Port的Converter
 */
public class BasicOABoardPortConverter implements PortConverter {
    private static BasicOABoardPortConverter instance = new BasicOABoardPortConverter();
    public static BasicOABoardPortConverter getInstance() {
        return instance;
    }
    private BasicOABoardPortConverter() {
    }

    public boolean convert(AdpTp adpPort, SnmpTpEntity entity) {
        BasicPortConverter.getInstance().convert(adpPort, entity);
        //TODO: 看文档对于一般OA盘是否需要按不同类型port特殊转换，如果没有，可以不用下面的。
        if(PortConverterUtil.isOASIGPort(entity)){
            //TODO:
        }else if(PortConverterUtil.isOALINEPort(entity)){
            //TODO:
        }else if(PortConverterUtil.isOADCMPort(entity)){
            //TODO:
        }else if(PortConverterUtil.isOAOSCPort(entity)){
            //TODO:
        }else{
            //TODO:
        }
        return true;

    }
}
