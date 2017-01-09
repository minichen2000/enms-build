package com.nsb.enms.adapter.server.wdm.business.generator.ctp;



import com.nsb.enms.adapter.server.wdm.action.entity.SnmpTpEntity;
import com.nsb.enms.adapter.server.wdm.business.converter.ctp.BasicClientDSRCTPConverter;
import com.nsb.enms.adapter.server.wdm.business.converter.ctp.BasicClientOCHCTPConverter;
import com.nsb.enms.adapter.server.wdm.business.converter.ctp.BasicClientODUjCTPConverter;
import com.nsb.enms.adapter.server.wdm.business.converter.ctp.BasicClientOTUjCTPConverter;
import com.nsb.enms.restful.model.adapter.AdpTp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by minichen on 2017/1/8.
 */
public class BasicOTBoardClientCTPGenerator {
    private static BasicOTBoardClientCTPGenerator instance = new BasicOTBoardClientCTPGenerator();
    public static BasicOTBoardClientCTPGenerator getInstance() {
        return instance;
    }
    private BasicOTBoardClientCTPGenerator() {
    }
    public List<AdpTp> generate(int clientODUjRate, AdpTp adpPort, SnmpTpEntity entity) {
        List<AdpTp> rlt=new ArrayList<AdpTp>();
        AdpTp ctp=new AdpTp();
        BasicClientDSRCTPConverter.getInstance().convert(ctp, adpPort, entity);
        rlt.add(ctp);

        ctp=new AdpTp();
        BasicClientOCHCTPConverter.getInstance().convert(ctp, adpPort,entity);
        rlt.add(ctp);

        ctp=new AdpTp();
        BasicClientOTUjCTPConverter.getInstance().convert(clientODUjRate, ctp, adpPort,entity);
        rlt.add(ctp);

        ctp=new AdpTp();
        BasicClientODUjCTPConverter.getInstance().convert(clientODUjRate, ctp, adpPort,entity);
        rlt.add(ctp);
        return rlt;

    }
}
