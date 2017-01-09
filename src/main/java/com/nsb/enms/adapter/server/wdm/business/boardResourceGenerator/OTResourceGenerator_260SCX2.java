package com.nsb.enms.adapter.server.wdm.business.boardResourceGenerator;

import com.nsb.enms.adapter.server.wdm.business.converter.port.OTBoardPortConverter_260SCX2;
import com.nsb.enms.adapter.server.wdm.business.generator.ctp.OTBoardCTPGenerator_260SCX2;
import com.nsb.enms.adapter.server.wdm.business.itf.BoardResourceGenerator;
import com.nsb.enms.adapter.server.wdm.business.itf.CTPGenerator;
import com.nsb.enms.adapter.server.wdm.business.itf.PortConverter;
import com.nsb.enms.adapter.server.wdm.business.itf.XCGenerator;

/**
 * Created by minichen on 2017/1/9.
 */
public class OTResourceGenerator_260SCX2 implements BoardResourceGenerator {
    public PortConverter getPortConverter() {
        return OTBoardPortConverter_260SCX2.getInstance();
    }

    public CTPGenerator getCTPGenerator() {
        return OTBoardCTPGenerator_260SCX2.getInstance();
    }

    public XCGenerator getXCGenerator() {
        return null;
    }
}
