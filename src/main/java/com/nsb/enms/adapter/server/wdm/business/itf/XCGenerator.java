package com.nsb.enms.adapter.server.wdm.business.itf;


import com.nsb.enms.restful.model.adapter.AdpTp;
import com.nsb.enms.restful.model.adapter.AdpXc;

import java.util.List;

/**
 * Created by minichen on 2017/1/9.
 */
public interface XCGenerator {
    public List<AdpXc> generate(List<AdpTp> boardTPs);
}
