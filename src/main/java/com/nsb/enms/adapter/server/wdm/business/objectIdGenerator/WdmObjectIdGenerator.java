package com.nsb.enms.adapter.server.wdm.business.objectIdGenerator;

import com.nsb.enms.adapter.server.common.business.itf.ObjectIdGenerator;
import com.nsb.enms.restful.model.adapter.AdpEquipment;
import com.nsb.enms.restful.model.adapter.AdpTp;

/**
 * Created by Chenmin on 2016/12/30.
 */
public class WdmObjectIdGenerator implements ObjectIdGenerator{
    @Override
    public String generateEquipmentId(AdpEquipment equ) {
        return equ.getKeyOnNe();
    }

    @Override
    public String generatePTPId(AdpTp ptp) {
        return ptp.getKeyOnNe();
    }

    @Override
    public String generateCTPId(AdpTp ptp, AdpTp ctp) {
        return ptp.getKeyOnNe()+'_'+ctp.getUserLabel();
    }
}
