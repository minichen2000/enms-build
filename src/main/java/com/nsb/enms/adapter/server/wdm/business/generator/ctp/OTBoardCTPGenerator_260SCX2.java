package com.nsb.enms.adapter.server.wdm.business.generator.ctp;


import com.nsb.enms.adapter.server.wdm.action.entity.SnmpTpEntity;
import com.nsb.enms.adapter.server.wdm.business.itf.CTPGenerator;
import com.nsb.enms.adapter.server.wdm.utils.PortConverterUtil;
import com.nsb.enms.restful.model.adapter.AdpTp;

import java.util.List;

/**
 * Created by minichen on 2017/1/8.
 */
public class OTBoardCTPGenerator_260SCX2 implements CTPGenerator {
    private static OTBoardCTPGenerator_260SCX2 instance = new OTBoardCTPGenerator_260SCX2();
    public static OTBoardCTPGenerator_260SCX2 getInstance() {
        return instance;
    }
    private OTBoardCTPGenerator_260SCX2() {
    }

    public List<AdpTp> generate(AdpTp adpPort, SnmpTpEntity entity) {
        //区分Client侧Port和Line侧Port，分别转换
        if (PortConverterUtil.isOTClientPort(entity)) {
            return BasicOTBoardClientCTPGenerator.getInstance().generate(4, adpPort, entity);
        } else if (PortConverterUtil.isOTLinePort(entity)) {
            return OTBoardLineCTPGenerator_260SCX2.getInstance().generate(adpPort, entity);
        } else {
            return null;
        }
    }
}
