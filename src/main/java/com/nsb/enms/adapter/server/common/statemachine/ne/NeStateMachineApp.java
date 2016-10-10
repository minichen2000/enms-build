package com.nsb.enms.adapter.server.common.statemachine.ne;

import java.util.List;

import com.nsb.enms.adapter.server.common.statemachine.ne.model.AdminState;
import com.nsb.enms.adapter.server.common.statemachine.ne.model.MaintenanceState;
import com.nsb.enms.adapter.server.common.statemachine.ne.model.NeEvent;
import com.nsb.enms.adapter.server.common.statemachine.ne.model.NeStateCallBack;
import com.nsb.enms.adapter.server.db.mgr.AdpNesDbMgr;
import com.nsb.enms.restful.model.adapter.AdpNe;
import com.nsb.enms.statemachine.wrapper.StateMachineWrapper;
import com.nsb.enms.statemachine.wrapper.StateMachineWrapper.STMachineBuilder;

public class NeStateMachineApp
{
    private static NeStateMachineApp inst_ = new NeStateMachineApp();

    private StateMachineWrapper<NeOperationalStateMachine, AdpNe.OperationalStateEnum, NeEvent, NeStateCallBack> neOperationalStateMachine;

    private StateMachineWrapper<NeCommunicationStateMachine, AdpNe.CommunicationStateEnum, NeEvent, NeStateCallBack> neCommunicationStateMachine;

    private StateMachineWrapper<NeSupervisionStateMachine, AdpNe.SupervisionStateEnum, NeEvent, NeStateCallBack> neSupervisionStateMachine;

    private StateMachineWrapper<NeSyncStateMachine, AdpNe.SynchStateEnum, NeEvent, NeStateCallBack> neSyncStateMachine;

    private StateMachineWrapper<NeMaintenanceStateMachine, MaintenanceState, NeEvent, NeStateCallBack> neMaintenanaceStateMachine;

    private StateMachineWrapper<NeAdminStateMachine, AdminState, NeEvent, NeStateCallBack> neAdminStateMachine;

    private static AdpNesDbMgr nesDbMgr = new AdpNesDbMgr();

    private NeStateMachineApp()
    {

    }

    public static NeStateMachineApp instance()
    {
        if( inst_ == null )
        {
            inst_ = new NeStateMachineApp();
        }
        return inst_;
    }

    public void init()
    {
        STMachineBuilder<NeOperationalStateMachine, AdpNe.OperationalStateEnum, NeEvent, NeStateCallBack> operationalStateBuilder = new STMachineBuilder<NeOperationalStateMachine, AdpNe.OperationalStateEnum, NeEvent, NeStateCallBack>(
                NeOperationalStateMachine.class );
        operationalStateBuilder.registExTransition(
            AdpNe.OperationalStateEnum.IDLE,
            AdpNe.OperationalStateEnum.SUPERVISING,
            NeEvent.E_IDLE_2_SUPERVISING, "transState" );
        operationalStateBuilder.registExTransition(
            AdpNe.OperationalStateEnum.SUPERVISING,
            AdpNe.OperationalStateEnum.IDLE, NeEvent.E_SUPERVISING_2_IDLE,
            "transState" );
        operationalStateBuilder.registExTransition(
            AdpNe.OperationalStateEnum.IDLE,
            AdpNe.OperationalStateEnum.SYNCHRONIZING,
            NeEvent.E_IDLE_2_SYNCHRONIZING, "transState" );
        operationalStateBuilder.registExTransition(
            AdpNe.OperationalStateEnum.SYNCHRONIZING,
            AdpNe.OperationalStateEnum.IDLE, NeEvent.E_SYNCHRONIZING_2_IDLE,
            "transState" );
        neOperationalStateMachine = operationalStateBuilder
                .build( AdpNe.OperationalStateEnum.IDLE );

        STMachineBuilder<NeCommunicationStateMachine, AdpNe.CommunicationStateEnum, NeEvent, NeStateCallBack> communicationStateBuilder = new STMachineBuilder<NeCommunicationStateMachine, AdpNe.CommunicationStateEnum, NeEvent, NeStateCallBack>(
                NeCommunicationStateMachine.class );
        communicationStateBuilder.registExTransition(
            AdpNe.CommunicationStateEnum.UNREACHABLE,
            AdpNe.CommunicationStateEnum.REACHABLE,
            NeEvent.E_UNREACHABLE_2_REACHABLE, "transState" );
        communicationStateBuilder.registExTransition(
            AdpNe.CommunicationStateEnum.REACHABLE,
            AdpNe.CommunicationStateEnum.UNREACHABLE,
            NeEvent.E_REACHABLE_2_UNREACHABLE, "transState" );
        neCommunicationStateMachine = communicationStateBuilder
                .build( AdpNe.CommunicationStateEnum.UNREACHABLE );

        STMachineBuilder<NeSupervisionStateMachine, AdpNe.SupervisionStateEnum, NeEvent, NeStateCallBack> supervisionStateBuilder = new STMachineBuilder<NeSupervisionStateMachine, AdpNe.SupervisionStateEnum, NeEvent, NeStateCallBack>(
                NeSupervisionStateMachine.class );
        supervisionStateBuilder.registExTransition(
            AdpNe.SupervisionStateEnum.UNSUPERVISED,
            AdpNe.SupervisionStateEnum.SUPERVISIED,
            NeEvent.E_UNSUPERVISIED_2_SUPERVISIED, "transState" );
        neSupervisionStateMachine = supervisionStateBuilder
                .build( AdpNe.SupervisionStateEnum.UNSUPERVISED );

        STMachineBuilder<NeSyncStateMachine, AdpNe.SynchStateEnum, NeEvent, NeStateCallBack> syncStateBuilder = new STMachineBuilder<NeSyncStateMachine, AdpNe.SynchStateEnum, NeEvent, NeStateCallBack>(
                NeSyncStateMachine.class );
        syncStateBuilder.registExTransition(
            AdpNe.SynchStateEnum.UNSYNCHRONIZED,
            AdpNe.SynchStateEnum.SYNCHRONIZED,
            NeEvent.E_UNSYNCHRONIZED_2_SYNCHRONIZED, "transState" );
        neSyncStateMachine = syncStateBuilder
                .build( AdpNe.SynchStateEnum.UNSYNCHRONIZED );

        STMachineBuilder<NeMaintenanceStateMachine, MaintenanceState, NeEvent, NeStateCallBack> maintenanceStateBuilder = new STMachineBuilder<NeMaintenanceStateMachine, MaintenanceState, NeEvent, NeStateCallBack>(
                NeMaintenanceStateMachine.class );
        maintenanceStateBuilder.registExTransition( MaintenanceState.FALSE,
            MaintenanceState.TRUE, NeEvent.E_FALSE_2_TRUE, "transState" );
        neMaintenanaceStateMachine = maintenanceStateBuilder
                .build( MaintenanceState.FALSE );

        STMachineBuilder<NeAdminStateMachine, AdminState, NeEvent, NeStateCallBack> adminStateBuilder = new STMachineBuilder<NeAdminStateMachine, AdminState, NeEvent, NeStateCallBack>(
                NeAdminStateMachine.class );
        adminStateBuilder.registExTransition( AdminState.FALSE, AdminState.TRUE,
            NeEvent.E_FALSE_2_TRUE, "transState" );
        neAdminStateMachine = adminStateBuilder.build( AdminState.FALSE );
        
        neAdminStateMachine.start();
        neCommunicationStateMachine.start();
        neMaintenanaceStateMachine.start();
        neOperationalStateMachine.start();
        neSupervisionStateMachine.start();
        neSyncStateMachine.start();
    }

    public void beforeSuperviseNe( String id )
    {
        NeStateCallBack ne = new NeStateCallBack();
        ne.setId( id );
        neOperationalStateMachine.fire( NeEvent.E_IDLE_2_SUPERVISING, ne );
    }

    public void afterSuperviseNe( String id )
    {
        NeStateCallBack ne = new NeStateCallBack();
        ne.setId( id );

        neSupervisionStateMachine.fire( NeEvent.E_UNSUPERVISIED_2_SUPERVISIED,
            ne );
        neCommunicationStateMachine.fire( NeEvent.E_UNREACHABLE_2_REACHABLE,
            ne );
        neOperationalStateMachine.fire( NeEvent.E_SUPERVISING_2_IDLE, ne );
    }

    public void beforeSynchData( String id )
    {
        NeStateCallBack ne = new NeStateCallBack();
        ne.setId( id );
        neOperationalStateMachine.fire( NeEvent.E_IDLE_2_SYNCHRONIZING, ne );
    }

    public void afterSynchData( String id )
    {
        NeStateCallBack ne = new NeStateCallBack();
        ne.setId( id );
        neAdminStateMachine.fire( NeEvent.E_FALSE_2_TRUE, ne );
        neSyncStateMachine.fire( NeEvent.E_UNSYNCHRONIZED_2_SYNCHRONIZED, ne );
        neOperationalStateMachine.fire( NeEvent.E_SYNCHRONIZING_2_IDLE, ne );
    }

    public void updateCommunicationStateForNes( int groupId )
    {
        try
        {
            NeStateCallBack ne = new NeStateCallBack();
            List<AdpNe> nes = nesDbMgr
                    .getNesByGroupId( String.valueOf( groupId ) );
            for( AdpNe adpNe : nes )
            {
                if( adpNe.getCommunicationState()
                        .equals( AdpNe.CommunicationStateEnum.REACHABLE ) )
                {
                    ne.setId( adpNe.getId() );
                    neCommunicationStateMachine
                            .fire( NeEvent.E_REACHABLE_2_UNREACHABLE, ne );
                }
            }
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }

    }

    public StateMachineWrapper<NeOperationalStateMachine, AdpNe.OperationalStateEnum, NeEvent, NeStateCallBack> getNeOperationalStateMachine()
    {
        return neOperationalStateMachine;
    }

    public StateMachineWrapper<NeCommunicationStateMachine, AdpNe.CommunicationStateEnum, NeEvent, NeStateCallBack> getNeCommunicationStateMachine()
    {
        return neCommunicationStateMachine;
    }

    public StateMachineWrapper<NeSupervisionStateMachine, AdpNe.SupervisionStateEnum, NeEvent, NeStateCallBack> getNeSupervisionStateMachine()
    {
        return neSupervisionStateMachine;
    }

    public StateMachineWrapper<NeSyncStateMachine, AdpNe.SynchStateEnum, NeEvent, NeStateCallBack> getNeSyncStateMachine()
    {
        return neSyncStateMachine;
    }

    public StateMachineWrapper<NeMaintenanceStateMachine, MaintenanceState, NeEvent, NeStateCallBack> getNeMaintenanaceStateMachine()
    {
        return neMaintenanaceStateMachine;
    }

    public StateMachineWrapper<NeAdminStateMachine, AdminState, NeEvent, NeStateCallBack> getNeAdminStateMachine()
    {
        return neAdminStateMachine;
    }
}