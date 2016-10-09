package com.nsb.enms.adapter.server.common.statemachine.ne;

import com.nsb.enms.adapter.server.common.statemachine.ne.NeState.CommunicationState;
import com.nsb.enms.adapter.server.common.statemachine.ne.NeState.MaintenanceState;
import com.nsb.enms.adapter.server.common.statemachine.ne.NeState.OperationalState;
import com.nsb.enms.adapter.server.common.statemachine.ne.NeState.SupervisionState;
import com.nsb.enms.adapter.server.common.statemachine.ne.NeState.SynchState;
import com.nsb.enms.statemachine.wrapper.StateMachineWrapper;
import com.nsb.enms.statemachine.wrapper.StateMachineWrapper.STMachineBuilder;

public class NeStateMachineApp
{
    private static NeStateMachineApp inst_ = new NeStateMachineApp();

    private StateMachineWrapper<NeOperationalStateMachine, OperationalState, NeEvent, NeStateCallBack> neOperationalStateMachine;

    private StateMachineWrapper<NeCommunicationStateMachine, CommunicationState, NeEvent, NeStateCallBack> neCommunicationStateMachine;

    private StateMachineWrapper<NeSupervisionStateMachine, SupervisionState, NeEvent, NeStateCallBack> neSupervisionStateMachine;

    private StateMachineWrapper<NeSyncStateMachine, SynchState, NeEvent, NeStateCallBack> neSyncStateMachine;

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
        STMachineBuilder<NeOperationalStateMachine, OperationalState, NeEvent, NeStateCallBack> builder1 = new STMachineBuilder<NeOperationalStateMachine, OperationalState, NeEvent, NeStateCallBack>(
                NeOperationalStateMachine.class );
        builder1.registExTransition( OperationalState.IDLE,
            OperationalState.SUPERVISING, NeEvent.E_IDLE_2_SUPERVISING,
            "transState" );
        builder1.registExTransition( OperationalState.SUPERVISING,
            OperationalState.IDLE, NeEvent.E_SUPERVISING_2_IDLE, "transState" );
        builder1.registExTransition( OperationalState.IDLE,
            OperationalState.SYNCHRONIZING, NeEvent.E_IDLE_2_SYNCHRONIZING,
            "transState" );
        builder1.registExTransition( OperationalState.SYNCHRONIZING,
            OperationalState.IDLE, NeEvent.E_SYNCHRONIZING_2_IDLE,
            "transState" );
        neOperationalStateMachine = builder1.build( OperationalState.IDLE );

        STMachineBuilder<NeCommunicationStateMachine, CommunicationState, NeEvent, NeStateCallBack> builder2 = new STMachineBuilder<NeCommunicationStateMachine, CommunicationState, NeEvent, NeStateCallBack>(
                NeCommunicationStateMachine.class );
        builder2.registExTransition( CommunicationState.UNREACHABLE,
            CommunicationState.REACHABLE, NeEvent.E_UNREACHABLE_2_REACHABLE,
            "transState" );
        builder2.registExTransition( CommunicationState.REACHABLE,
            CommunicationState.UNREACHABLE, NeEvent.E_REACHABLE_2_UNREACHABLE,
            "transState" );
        neCommunicationStateMachine = builder2
                .build( CommunicationState.UNREACHABLE );

        STMachineBuilder<NeSupervisionStateMachine, SupervisionState, NeEvent, NeStateCallBack> builder3 = new STMachineBuilder<NeSupervisionStateMachine, SupervisionState, NeEvent, NeStateCallBack>(
                NeSupervisionStateMachine.class );
        builder3.registExTransition( SupervisionState.UNSUPERVISED,
            SupervisionState.SUPERVISED, NeEvent.E_UNSUPERVISIED_2_SUPERVISIED,
            "transState" );
        neSupervisionStateMachine = builder3
                .build( SupervisionState.UNSUPERVISED );

        STMachineBuilder<NeSyncStateMachine, SynchState, NeEvent, NeStateCallBack> builder4 = new STMachineBuilder<NeSyncStateMachine, SynchState, NeEvent, NeStateCallBack>(
                NeSyncStateMachine.class );
        builder4.registExTransition( SynchState.UNSYNCHRONIZED,
            SynchState.SYNCHRONIZED, NeEvent.E_UNSYNCHRONIZED_2_SYNCHRONIZED,
            "transState" );
        neSyncStateMachine = builder4.build( SynchState.UNSYNCHRONIZED );

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

    public StateMachineWrapper<NeOperationalStateMachine, OperationalState, NeEvent, NeStateCallBack> getNeOperationalStateMachine()
    {
        return neOperationalStateMachine;
    }

    public StateMachineWrapper<NeCommunicationStateMachine, CommunicationState, NeEvent, NeStateCallBack> getNeCommunicationStateMachine()
    {
        return neCommunicationStateMachine;
    }

    public StateMachineWrapper<NeSupervisionStateMachine, SupervisionState, NeEvent, NeStateCallBack> getNeSupervisionStateMachine()
    {
        return neSupervisionStateMachine;
    }

    public StateMachineWrapper<NeSyncStateMachine, SynchState, NeEvent, NeStateCallBack> getNeSyncStateMachine()
    {
        return neSyncStateMachine;
    }

    public StateMachineWrapper<NeMaintenanceStateMachine, MaintenanceState, NeEvent, NeStateCallBack> getNeMaintenanaceStateMachine()
    {
        return neMaintenanaceStateMachine;
    }
}