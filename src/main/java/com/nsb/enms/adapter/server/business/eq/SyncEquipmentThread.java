package com.nsb.enms.adapter.server.business.eq;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.action.entity.EquipmentEntity;
import com.nsb.enms.adapter.server.action.method.eq.GetEquipment;
import com.nsb.enms.adapter.server.common.utils.GenerateKeyOnNeUtil;
import com.nsb.enms.adapter.server.db.mgr.AdpEqusDbMgr;
import com.nsb.enms.common.EntityType;
import com.nsb.enms.restful.model.adapter.AdpEquipment;

public class SyncEquipmentThread implements Callable<Object>
{
    private final static Logger log = LogManager
            .getLogger( SyncEquipmentThread.class );
    
    private AdpEqusDbMgr equsDbMgr = new AdpEqusDbMgr();

    private String groupId, neId, id;

    public SyncEquipmentThread( String groupId, String neId, String id )
    {
        this.groupId = groupId;
        this.neId = neId;
        this.id = id;
    }

    @Override
    public Object call() throws Exception
    {
        List<EquipmentEntity> equList = GetEquipment.getEquipment( groupId, neId );
        List<AdpEquipment> newEquList = new ArrayList<AdpEquipment>();
        log.debug("equList=" + equList.size() + ", neId=" + neId);
        for (EquipmentEntity equ : equList)
        {
            AdpEquipment newEqu = constructEquipment(equ);
            newEquList.add( newEqu );
        }
        
        for (AdpEquipment newEqu : newEquList)
        {
            String aid = newEqu.getAid();
            for (AdpEquipment equ : newEquList)
            {
                if (!aid.equals( equ.getAid() ) && aid.matches( equ.getAid() + "/equipment=[0-9]+" ))
                {
                    //newEqu.setParentId(equ.getId());
                    break;
                }
            }
            
            AdpEquipment adpEqu = equsDbMgr.getEquipmentById( newEqu.getId() );
            if (null == adpEqu || StringUtils.isEmpty( adpEqu.getId() ))
            {
                equsDbMgr.addEquipment( newEqu );
            }
        }
        
        log.debug( "sync equipment end" );
        return null;
    }
    
    private AdpEquipment constructEquipment(EquipmentEntity equ)
    {
        AdpEquipment adpEqu = new AdpEquipment();
        String moc = equ.getMoc();
        String moi = equ.getMoi();
        String keyOnNe = GenerateKeyOnNeUtil.generateKeyOnNe( EntityType.BOARD, moc, moi );
        adpEqu.setId( id + ":" + keyOnNe );
        adpEqu.setAid( moi );
        adpEqu.setNeId( id );
        adpEqu.setUserLabel( "" );
        adpEqu.setNativeName( "" );
        return adpEqu;
    }

}
