package com.nsb.enms.adapter.server.wdm.business.boardResourceGenerator;


import com.nsb.enms.adapter.server.wdm.business.converter.port.BasicOTBoardPortConverter;
import com.nsb.enms.adapter.server.wdm.business.generator.ctp.BasicOTBoardCTPGenerator;
import com.nsb.enms.adapter.server.wdm.business.generator.xc.BasicOTBoardXCGenerator;
import com.nsb.enms.adapter.server.wdm.business.itf.BoardResourceGenerator;
import com.nsb.enms.adapter.server.wdm.business.itf.CTPGenerator;
import com.nsb.enms.adapter.server.wdm.business.itf.PortConverter;
import com.nsb.enms.adapter.server.wdm.business.itf.XCGenerator;

/**
 * Created by minichen on 2017/1/9.
 */
public class BasicOTResourceGenerator implements BoardResourceGenerator {
    protected int clientODUjRate = 2;
    protected int lineODUkRate = 4;
    protected int lineODUjNumber = 10;

    private CTPGenerator ctpGenerator=null;

    public BasicOTResourceGenerator(int clientODUjRate, int lineODUkRate, int lineODUjNumber) {
        this.clientODUjRate = clientODUjRate;
        this.lineODUkRate = lineODUkRate;
        this.lineODUjNumber = lineODUjNumber;
    }

    public PortConverter getPortConverter() {
        return BasicOTBoardPortConverter.getInstance();
    }

    public CTPGenerator getCTPGenerator() {
        ctpGenerator= (null==ctpGenerator) ? new BasicOTBoardCTPGenerator(clientODUjRate, lineODUkRate, lineODUjNumber) : ctpGenerator;
        return ctpGenerator;
    }

    public XCGenerator getXCGenerator() {
        return new BasicOTBoardXCGenerator(clientODUjRate, lineODUkRate, lineODUjNumber);
    }
}
