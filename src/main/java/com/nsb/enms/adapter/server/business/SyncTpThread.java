package com.nsb.enms.adapter.server.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.action.entity.TpEntity;
import com.nsb.enms.adapter.server.action.method.ne.StartSuppervision;
import com.nsb.enms.adapter.server.action.method.tp.GetCtp;
import com.nsb.enms.adapter.server.action.method.tp.GetTp;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.utils.GenerateKeyOnNeUtil;
import com.nsb.enms.adapter.server.common.utils.GenerateUserLabelUtil;
import com.nsb.enms.adapter.server.db.mgr.AdpNesDbMgr;
import com.nsb.enms.adapter.server.db.mgr.AdpTpsDbMgr;
import com.nsb.enms.adapter.server.notification.NotificationSender;
import com.nsb.enms.common.LayerRate;
import com.nsb.enms.common.util.ObjectType;
import com.nsb.enms.common.utils.Pair;
import com.nsb.enms.restful.model.adapter.AdpNe;
import com.nsb.enms.restful.model.adapter.AdpNe.OperationalStateEnum;
import com.nsb.enms.restful.model.adapter.AdpNe.SynchStateEnum;
import com.nsb.enms.restful.model.adapter.AdpTp;

public class SyncTpThread extends Thread
{
    private final static Logger log = LogManager
            .getLogger( SyncTpThread.class );

    private AdpTpsDbMgr tpsDbMgr = new AdpTpsDbMgr();

    private int groupId, neId;

    private String id;

    public SyncTpThread( int groupId, int neId, String id )
    {
        this.groupId = groupId;
        this.neId = neId;
        this.id = id;
    }

    @Override
    public void run()
    {
        // StartSuppervision start = new StartSuppervision();
        boolean isSuccess;
        try
        {
            log.debug( "before startSuppervision" );
            // NeStateMachineApp.instance().beforeSuperviseNe(id);
            NotificationSender.instance().sendAvcNotif( new Date(), ObjectType.NE,
                id, "operationalState", "enum",
                OperationalStateEnum.IDLE.toString(),
                OperationalStateEnum.SYNCHRONIZING.toString() );
            isSuccess = StartSuppervision.startSuppervision( groupId, neId );
            log.debug( "isSuccess = " + isSuccess );
            if( !isSuccess )
            {
                return;
            }
            // NeStateMachineApp.instance().afterSuperviseNe(id);
        }
        catch( AdapterException e )
        {
            e.printStackTrace();
            return;
        }

        // NeStateMachineApp.instance().beforeSynchData(id);
        syncTp();
        // NeStateMachineApp.instance().afterSynchData(id);

        // update the value of alignmentStatus for ne to true
        //updateNeAttr( id );        
        NotificationSender.instance().sendAvcNotif( new Date(), ObjectType.NE,
            id, "synchState", "enum", SynchStateEnum.UNSYNCHRONIZED.toString(),
            SynchStateEnum.SYNCHRONIZED.toString() );
        NotificationSender.instance().sendAvcNotif( new Date(), ObjectType.NE,
            id, "operationalState", "enum",
            OperationalStateEnum.SYNCHRONIZING.toString(),
            OperationalStateEnum.IDLE.toString() );

        log.debug( "sync tp end" );
    }

    private void syncTp()
    {
        // GetTp getTp = new GetTp();
        try
        {
            List<TpEntity> tpList = GetTp.getTp( groupId, neId );
            log.debug( "tpList = " + tpList.size() + ", neId = " + neId );

            for( TpEntity tp : tpList )
            {
                List<AdpTp> tps = new ArrayList<AdpTp>();
                log.debug( "tp = " + tp );
                AdpTp newTp = new AdpTp();
                newTp.setNeId( id );
                String moi = tp.getMoi();
                String keyOnNe = GenerateKeyOnNeUtil.generateKeyOnNe( ObjectType.TP,
                    tp.getMoc(), moi );
                newTp.setId( id + ":" + keyOnNe );
                newTp.setKeyOnNe( keyOnNe );
                newTp.setAdminState( tp.getAdministrativeState() );
                newTp.setOperationalState( tp.getOperationalState() );
                String userLabel = GenerateUserLabelUtil
                        .generateTpUserLabel( tp );
                newTp.setUserLabel( userLabel );
                newTp.setNativeName( userLabel );
                newTp.setTpType( tp.getMoc() );

                // TODO 读取映射文件获取层速率
                LayerRate layerRate = getLayerRate( tp );
                List<String> layerRates = new ArrayList<String>();
                layerRates.add( String.valueOf( layerRate.getVal() ) );
                newTp.setLayerRates( layerRates );
                tps.add( newTp );

                tps = tpsDbMgr.addTps( tps );
                String tpId = moi.split( "/" )[2];
                log.debug( "tpId = " + tpId );
                String ptpId = tpId.split( "=" )[1];
                log.debug( "ptpId = " + ptpId );
                String ptpDbId = tps.get( 0 ).getId();
                if( tpId.startsWith( "opticalSPI" ) )
                {
                    syncSdhCtp( moi, ptpId, ptpDbId );
                }
                else if( tpId.startsWith( "pPI" ) )
                {
                    syncPdhCtp( moi, ptpId, ptpDbId );
                }
                else
                {
                    log.error( "tpId is not valid:" + tpId );
                    return;
                }
            }

        }
        catch( Exception e )
        {
            log.error( "syncTp", e );
        }
    }

    private void syncSdhCtp( String moi, String ptpId, String ptpDbId )
            throws AdapterException
    {
        List<TpEntity> ctpList = GetCtp.getSdhCtp( groupId, neId, ptpId );
        if( null == ctpList || ctpList.isEmpty() )
        {
            log.error( "ctpList is null or empty" );
            return;
        }

        List<AdpTp> tps = new ArrayList<AdpTp>();
        for( TpEntity ctp : ctpList )
        {
            AdpTp newCtp = new AdpTp();
            newCtp.setNeId( id );
            String ctpMoi = ctp.getMoi();
            String keyOnNe = GenerateKeyOnNeUtil.generateKeyOnNe( ObjectType.TP,
                ctp.getMoc(), ctpMoi );
            newCtp.setId( id + ":" + keyOnNe );
            newCtp.setKeyOnNe( keyOnNe );
            newCtp.setAdminState( ctp.getAdministrativeState() );
            newCtp.setOperationalState( ctp.getOperationalState() );
            String userLabel = GenerateUserLabelUtil
                    .generateTpUserLabel( ctp );
            newCtp.setUserLabel( userLabel );
            newCtp.setNativeName( userLabel );
            newCtp.setTpType( ctp.getMoc() );
            newCtp.setParentTpId( ptpDbId );

            // TODO 读取映射文件获取层速率
            List<String> layerRates = new ArrayList<String>();
            layerRates.add( String.valueOf( LayerRate.LR_AU4 ) );
            newCtp.setLayerRates( layerRates );
            tps.add( newCtp );
        }

        try
        {
            tpsDbMgr.addTps( tps );
        }
        catch( Exception e )
        {
            log.error( "syncCtp", e );
        }
    }

    private void syncPdhCtp( String moi, String ptpId, String ptpDbId )
            throws AdapterException
    {
        Pair<Integer, List<TpEntity>> pair = GetCtp.getPdhCtp( groupId, neId,
            ptpId );
        if( null == pair )
        {
            log.error( "pair is null" );
            return;
        }
        List<TpEntity> ctpList = pair.getSecond();
        if( null == ctpList || ctpList.isEmpty() )
        {
            log.error( "ctpList is null or empty" );
            return;
        }
        List<AdpTp> tps = new ArrayList<AdpTp>();
        for( TpEntity ctp : ctpList )
        {
            AdpTp newCtp = new AdpTp();
            newCtp.setNeId( id );
            String ctpMoi = ctp.getMoi();
            String keyOnNe = GenerateKeyOnNeUtil.generateKeyOnNe( ObjectType.TP,
                ctp.getMoc(), ctpMoi );
            newCtp.setId( id + ":" + keyOnNe );
            newCtp.setKeyOnNe( keyOnNe );
            newCtp.setAdminState( ctp.getAdministrativeState() );
            newCtp.setOperationalState( ctp.getOperationalState() );
            String userLabel = GenerateUserLabelUtil
                    .generateTpUserLabel( ctp );
            newCtp.setUserLabel( userLabel );
            newCtp.setNativeName( userLabel );
            newCtp.setTpType( ctp.getMoc() );
            newCtp.setParentTpId( ptpDbId );

            // TODO 读取映射文件获取层速率
            List<String> layerRates = new ArrayList<String>();
            layerRates.add( String.valueOf( LayerRate.LR_TUVC12 ) );
            newCtp.setLayerRates( layerRates );
            tps.add( newCtp );
        }

        AdpTp pdhPTP = new AdpTp();
        pdhPTP.setId( ptpDbId );
        int layerRate = pair.getFirst();
        List<String> layerRates = new ArrayList<String>();
        layerRates.add( String.valueOf( layerRate ) );
        pdhPTP.setLayerRates( layerRates );
        try
        {
            tpsDbMgr.addTps( tps );
            tpsDbMgr.updateTp( pdhPTP );
        }
        catch( Exception e )
        {
            log.error( "syncCtp", e );
        }
    }

    /**
     * update the value of alignmentStatus for ne to true
     */

    /*private void updateNeAttr( String id )
    {
        AdpNesDbMgr nesDbMgr = new AdpNesDbMgr();
        AdpNe ne = new AdpNe();
        ne.setId( id );
        ne.setAdminState( true );
        try
        {
            nesDbMgr.updateNe( ne );
        }
        catch( Exception e )
        {
            log.error( "updateNeAttr", e );
        }
    }*/

    private LayerRate getLayerRate( TpEntity tp )
    {
        String moc = tp.getMoc();
        if( moc.contains( "OpticalSPITTP" ) )
        {
            int stmLevel = tp.getStmLevel();
            switch( stmLevel )
            {
                case 1:
                    return LayerRate.LR_STM1;
                case 4:
                    return LayerRate.LR_STM4;
                case 16:
                    return LayerRate.LR_STM16;
                case 64:
                    return LayerRate.LR_STM64;
                case 256:
                    return LayerRate.LR_STM256;
                default:
                    return null;
            }
        }
        else if( moc.contains( "pPITTP" ) )
        {
            return LayerRate.LR_ELECTRICAL;
        }
        return null;
    }
}
