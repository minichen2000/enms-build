package com.nsb.enms.adapter.server.wdm.business.itf;


import com.nsb.enms.adapter.server.wdm.action.entity.SnmpTpEntity;
import com.nsb.enms.restful.model.adapter.AdpTp;

/**
 * Created by minichen on 2017/1/5.
 * adpPort: 转换后填写的Port
 * entity: 原始的entity
 */
public interface PortConverter {
    public boolean convert(AdpTp adpPort, SnmpTpEntity entity);
}
