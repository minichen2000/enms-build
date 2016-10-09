package com.nsb.enms.adapter.server.common.statemachine.ne;

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
        STMachineBuilder<NeOperationalStateMachine, AdpNe.OperationalStateEnum, NeEvent, NeStateCallBack> builder1 = new STMachineBuilder<NeOperationalStateMachine, AdpNe.OperationalStateEnum, NeEvent, NeStateCallBack>(
                NeOperationalStateMachine.class );
        builder1.registExTransition( AdpNe.OperationalStateEnum.IDLE,
            AdpNe.OperationalStateEnum.SUPERVISING, NeEvent.E_IDLE_2_SUPERVISING,
            "transState" );
        builder1.registExTransition( AdpNe.OperationalStateEnum.SUPERVISING,
            AdpNe.OperationalStateEnum.IDLE, NeEvent.E_SUPERVISING_2_IDLE, "transState" );
        builder1.registExTransition( AdpNe.OperationalStateEnum.IDLE,
            AdpNe.OperationalStateEnum.SYNCHRONIZING, NeEvent.E_IDLE_2_SYNCHRONIZING,
            "transState" );
        builder1.registExTransition( AdpNe.OperationalStateEnum.SYNCHRONIZING,
            AdpNe.OperationalStateEnum.IDLE, NeEvent.E_SYNCHRONIZING_2_IDLE,
            "transState" );
        neOperationalStateMachine = builder1.build( AdpNe.OperationalStateEnum.IDLE );

        STMachineBuilder<NeCommunicationStateMachine, AdpNe.CommunicationStateEnum, NeEvent, NeStateCallBack> builder2 = new STMachineBuilder<NeCommunicationStateMachine, AdpNe.CommunicationStateEnum, NeEvent, NeStateCallBack>(
                NeCommunicationStateMachine.class );
        builder2.registExTransition( AdpNe.CommunicationStateEnum.UNREACHABLE,
            AdpNe.CommunicationStateEnum.REACHABLE, NeEvent.E_UNREACHABLE_2_REACHABLE,
            "transState" );
        builder2.registExTransition( AdpNe.CommunicationStateEnum.REACHABLE,
            AdpNe.CommunicationStateEnum.UNREACHABLE, NeEvent.E_REACHABLE_2_UNREACHABLE,
            "transState" );
        neCommunicationStateMachine = builder2
                .build( AdpNe.CommunicationStateEnum.UNREACHABLE );

        STMachineBuilder<NeSupervisionStateMachine, AdpNe.SupervisionStateEnum, NeEvent, NeStateCallBack> builder3 = new STMachineBuilder<NeSupervisionStateMachine, AdpNe.SupervisionStateEnum, NeEvent, NeStateCallBack>(
                NeSupervisionStateMachine.class );
        builder3.registExTransition( AdpNe.SupervisionStateEnum.UNSUPERVISED,
            AdpNe.SupervisionStateEnum.SUPERVISIED, NeEvent.E_UNSUPERVISIED_2_SUPERVISIED,
            "transState" );
        neSupervisionStateMachine = builder3
                .build( AdpNe.SupervisionStateEnum.UNSUPERVISED );

        STMachineBuilder<NeSyncStateMachine, AdpNe.SynchStateEnum, NeEvent, NeStateCallBack> builder4 = new STMachineBuilder<NeSyncStateMachine, AdpNe.SynchStateEnum, NeEvent, NeStateCallBack>(
                NeSyncStateMachine.class );
        builder4.registExTransition( AdpNe.SynchStateEnum.UNSYNCHRONIZED,
            AdpNe.SynchStateEnum.SYNCHRONIZED, NeEvent.E_UNSYNCHRONIZED_2_SYNCHRONIZED,
            "transState" );
        neSyncStateMachine = builder4.build( AdpNe.SynchStateEnum.UNSYNCHRONIZED );

        STMachineBuilder<NeMaintenanceStateMachine, MaintenanceState, NeEvent, NeStateCallBack> builder5 = new STMachineBuilder<NeMaintenanceStateMachine, MaintenanceState, NeEvent, NeStateCallBack>(
                NeMaintenanceStateMachine.class );
        builder5.registExTransition( MaintenanceState.FALSE,
            MaintenanceState.TRUE, NeEvent.E_FALSE_2_TRUE, "transState" );
        neMaintenanaceStateMachine = builder5.build( MaintenanceState.FALSE );
    }
    
    public void beforeSuperviseNe(String id)
    {
        NeStateCallBack ne = new NeStateCallBack();
        ne.setId( id );
        neOperationalStateMachine.start();
        neOperationalStateMachine.fire( NeEvent.E_IDLE_2_SUPERVISING, ne );
        neOperationalStateMachine.terminate();
        
    }
    
    public void afterSuperviseNe(String id)
    {
        NeStateCallBack ne = new NeStateCallBack();
        ne.setId( id );
        neSupervisionStateMachine.start();
        neOperationalStateMachine.start();
        neCommunicationStateMachine.start();
        neSupervisionStateMachine.fire( NeEvent.E_UNSUPERVISIED_2_SUPERVISIED, ne );
        neCommunicationStateMachine.fire( NeEvent.E_UNREACHABLE_2_REACHABLE, ne );
        neOperationalStateMachine.fire( NeEvent.E_SUPERVISING_2_IDLE, ne );
        neSupervisionStateMachine.terminate();
        neCommunicationStateMachine.terminate();
        neOperationalStateMachine.terminate();
    }
    
    public void beforeSynchData(String id)
    {
        NeStateCallBack ne = new NeStateCallBack();
        ne.setId( id );
        neOperationalStateMachine.start();
        neOperationalStateMachine.fire( NeEvent.E_IDLE_2_SYNCHRONIZING, ne );
        neOperationalStateMachine.terminate();
        
    }
    
    public void afterSynchData(String id)
    {
        NeStateCallBack ne = new NeStateCallBack();
        ne.setId( id );
        neSyncStateMachine.start();
        neOperationalStateMachine.start();
        neSyncStateMachine.fire( NeEvent.E_UNSYNCHRONIZED_2_SYNCHRONIZED, ne );
        neOperationalStateMachine.fire( NeEvent.E_SYNCHRONIZING_2_IDLE, ne );
        neSyncStateMachine.terminate();
        neOperationalStateMachine.terminate();
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
}