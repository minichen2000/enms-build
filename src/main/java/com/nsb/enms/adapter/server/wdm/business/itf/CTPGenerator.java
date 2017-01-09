package com.nsb.enms.adapter.server.wdm.business.itf;

import com.nsb.enms.restful.model.adapter.AdpTp;
import com.nsb.enms.adapter.server.wdm.action.entity.SnmpTpEntity;

import java.util.List;

/**
 * Created by minichen on 2017/1/5.
 */
public interface CTPGenerator {
    public List<AdpTp> generate(AdpTp adpPort, SnmpTpEntity entity);
}
