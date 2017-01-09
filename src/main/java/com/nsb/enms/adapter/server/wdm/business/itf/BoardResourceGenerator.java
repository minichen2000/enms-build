package com.nsb.enms.adapter.server.wdm.business.itf;

/**
 * Created by minichen on 2017/1/8.
 */
public interface BoardResourceGenerator {
    public PortConverter getPortConverter();
    public CTPGenerator getCTPGenerator();
    public XCGenerator getXCGenerator();
}
