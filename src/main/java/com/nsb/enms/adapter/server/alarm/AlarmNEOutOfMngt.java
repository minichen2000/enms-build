package com.nsb.enms.adapter.server.alarm;

import com.nsb.enms.alarm.DefaultAlarm;
import com.nsb.enms.common.AlarmCode;
import com.nsb.enms.common.EntityType;

public class AlarmNEOutOfMngt extends DefaultAlarm {

    public AlarmNEOutOfMngt(String neid) {
        super(neid, AlarmCode.ALM_NE_LOST_SPARE_Q3MNGT, EntityType.NE);
        // TBC: Use NE User Label instead of NEID
//        this.setDescription(String.format(AlarmCode.ALM_NE_LOST_SPARE_Q3MNGT.getDescription(), neid));
    }

}
