package com.nsb.enms.adapter.server.wdm.business.generator.xc;



import com.nsb.enms.adapter.server.wdm.business.itf.XCGenerator;
import com.nsb.enms.restful.model.adapter.AdpTp;
import com.nsb.enms.restful.model.adapter.AdpXc;

import java.util.List;

/**
 * Created by minichen on 2017/1/8.
 */
public class BasicOTBoardXCGenerator implements XCGenerator {
    private int clientODUjRate = 2;
    private int lineODUkRate = 4;
    private int lineODUjNumber = 10;

    public BasicOTBoardXCGenerator(int clientODUjRate, int lineODUkRate, int lineODUjNumber) {
        this.clientODUjRate = clientODUjRate;
        this.lineODUkRate = lineODUkRate;
        this.lineODUjNumber = lineODUjNumber;
    }


    public List<AdpXc> generate(List<AdpTp> boardTPs) {
        //TODO: 为某个OT盘的所有TP生成lineODUjNumber个XC
        return null;
    }
}
