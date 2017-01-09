package com.nsb.enms.adapter.server.wdm.business.boardResourceGenerator;


import com.nsb.enms.adapter.server.wdm.business.itf.BoardResourceGenerator;
import com.nsb.enms.adapter.server.wdm.business.itf.PortConverter;
import com.nsb.enms.adapter.server.wdm.business.converter.port.BasicOABoardPortConverter;
import com.nsb.enms.adapter.server.wdm.business.itf.CTPGenerator;
import com.nsb.enms.adapter.server.wdm.business.itf.XCGenerator;

/**
 * Created by minichen on 2017/1/9.
 */
public class BasicOAResourceGenerator implements BoardResourceGenerator {
    public PortConverter getPortConverter() {
        return BasicOABoardPortConverter.getInstance();
    }

    public CTPGenerator getCTPGenerator() {
        return null;
    }

    public XCGenerator getXCGenerator() {
        return null;
    }
}
