package com.nsb.enms.adapter.server.common.business.itf;

import com.nsb.enms.restful.model.adapter.AdpEquipment;
import com.nsb.enms.restful.model.adapter.AdpTp;

/**
 * Created by Chenmin on 2016/12/30.
 */
public interface ObjectIdGenerator {
    public String generateEquipmentId(AdpEquipment equ);
    public String generatePTPId(AdpTp ptp);
    public String generateCTPId(AdpTp ptp, AdpTp ctp);
}
