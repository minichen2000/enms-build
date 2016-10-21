package com.nsb.enms.adapter.server.statemachine.ne;

import com.nsb.enms.adapter.server.statemachine.ne.model.MaintenanceState;
import com.nsb.enms.adapter.server.statemachine.ne.model.NeEvent;
import com.nsb.enms.adapter.server.statemachine.ne.model.NeStateCallBack;
import com.nsb.enms.restful.model.adapter.AdpNe;
import com.nsb.enms.restful.model.adapter.AdpNe.CommunicationStateEnum;
import com.nsb.enms.restful.model.adapter.AdpNe.OperationalStateEnum;
import com.nsb.enms.restful.model.adapter.AdpNe.SupervisionStateEnum;
import com.nsb.enms.restful.model.adapter.AdpNe.SynchStateEnum;
import com.nsb.enms.statemachine.component.StateMachine;
import com.nsb.enms.statemachine.component.StateMachine.StateMachineBuilder;

public class NeStateMachineApp
{
    private static NeStateMachineApp inst_ = new NeStateMachineApp();

    private StateMachine<NeOperationalStateMachine, AdpNe.OperationalStateEnum, NeEvent, NeStateCallBack> neOperationalStateMachine;

    private StateMachine<NeCommunicationStateMachine, AdpNe.CommunicationStateEnum, NeEvent, NeStateCallBack> neCommunicationStateMachine;

    private StateMachine<NeSupervisionStateMachine, AdpNe.SupervisionStateEnum, NeEvent, NeStateCallBack> neSupervisionStateMachine;

    private StateMachine<NeSyncStateMachine, AdpNe.SynchStateEnum, NeEvent, NeStateCallBack> neSyncStateMachine;

    private StateMachine<NeMaintenanceStateMachine, MaintenanceState, NeEvent, NeStateCallBack> neMaintenanaceStateMachine;

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
        StateMachineBuilder<NeOperationalStateMachine, AdpNe.OperationalStateEnum, NeEvent, NeStateCallBack> operationalStateBuilder = new StateMachineBuilder<NeOperationalStateMachine, AdpNe.OperationalStateEnum, NeEvent, NeStateCallBack>(
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

        StateMachineBuilder<NeCommunicationStateMachine, AdpNe.CommunicationStateEnum, NeEvent, NeStateCallBack> communicationStateBuilder = new StateMachineBuilder<NeCommunicationStateMachine, AdpNe.CommunicationStateEnum, NeEvent, NeStateCallBack>(
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

        StateMachineBuilder<NeSupervisionStateMachine, AdpNe.SupervisionStateEnum, NeEvent, NeStateCallBack> supervisionStateBuilder = new StateMachineBuilder<NeSupervisionStateMachine, AdpNe.SupervisionStateEnum, NeEvent, NeStateCallBack>(
                NeSupervisionStateMachine.class );
        supervisionStateBuilder.registExTransition(
            AdpNe.SupervisionStateEnum.UNSUPERVISED,
            AdpNe.SupervisionStateEnum.SUPERVISED,
            NeEvent.E_UNSUPERVISIED_2_SUPERVISIED, "transState" );
        neSupervisionStateMachine = supervisionStateBuilder
                .build( AdpNe.SupervisionStateEnum.UNSUPERVISED );

        StateMachineBuilder<NeSyncStateMachine, AdpNe.SynchStateEnum, NeEvent, NeStateCallBack> syncStateBuilder = new StateMachineBuilder<NeSyncStateMachine, AdpNe.SynchStateEnum, NeEvent, NeStateCallBack>(
                NeSyncStateMachine.class );
        syncStateBuilder.registExTransition(
            AdpNe.SynchStateEnum.UNSYNCHRONIZED,
            AdpNe.SynchStateEnum.SYNCHRONIZED,
            NeEvent.E_UNSYNCHRONIZED_2_SYNCHRONIZED, "transState" );
        neSyncStateMachine = syncStateBuilder
                .build( AdpNe.SynchStateEnum.UNSYNCHRONIZED );

        StateMachineBuilder<NeMaintenanceStateMachine, MaintenanceState, NeEvent, NeStateCallBack> maintenanceStateBuilder = new StateMachineBuilder<NeMaintenanceStateMachine, MaintenanceState, NeEvent, NeStateCallBack>(
                NeMaintenanceStateMachine.class );
        maintenanceStateBuilder.registExTransition( MaintenanceState.FALSE,
            MaintenanceState.TRUE, NeEvent.E_FALSE_2_TRUE, "transState" );
        neMaintenanaceStateMachine = maintenanceStateBuilder
                .build( MaintenanceState.FALSE );

        /*
         * neCommunicationStateMachine.start();
         * neMaintenanaceStateMachine.start();
         * neOperationalStateMachine.start(); neSupervisionStateMachine.start();
         * neSyncStateMachine.start();
         */
    }

   /* public void beforeSuperviseNe( String id )
    {
        NeStateCallBack ne = new NeStateCallBack();
        ne.setId( id );
        neOperationalStateMachine.setCurrentState( OperationalStateEnum.IDLE );
        neOperationalStateMachine.fire( NeEvent.E_IDLE_2_SUPERVISING, ne );
    }*/

    public void afterSuperviseNe( String id )
    {
        NeStateCallBack ne = new NeStateCallBack();
        ne.setId( id );
        neSupervisionStateMachine
                .setCurrentState( SupervisionStateEnum.UNSUPERVISED );
        neCommunicationStateMachine
                .setCurrentState( CommunicationStateEnum.UNREACHABLE );
        neOperationalStateMachine
                .setCurrentState( OperationalStateEnum.SUPERVISING );
        neSupervisionStateMachine.fire( NeEvent.E_UNSUPERVISIED_2_SUPERVISIED,
            ne );
        neCommunicationStateMachine.fire( NeEvent.E_UNREACHABLE_2_REACHABLE,
            ne );
        neOperationalStateMachine.fire( NeEvent.E_SUPERVISING_2_IDLE, ne );
    }

    /*public void beforeSynchData( String id )
    {
        NeStateCallBack ne = new NeStateCallBack();
        ne.setId( id );
        neOperationalStateMachine.setCurrentState( OperationalStateEnum.IDLE );
        neOperationalStateMachine.fire( NeEvent.E_IDLE_2_SYNCHRONIZING, ne );
    }*/

    public void afterSynchData( String id )
    {
        NeStateCallBack ne = new NeStateCallBack();
        ne.setId( id );
        neSyncStateMachine.setCurrentState( SynchStateEnum.UNSYNCHRONIZED );
        neSyncStateMachine.fire( NeEvent.E_UNSYNCHRONIZED_2_SYNCHRONIZED, ne );
        neOperationalStateMachine
                .setCurrentState( OperationalStateEnum.SYNCHRONIZING );
        neOperationalStateMachine.fire( NeEvent.E_SYNCHRONIZING_2_IDLE, ne );
    }

    public StateMachine<NeOperationalStateMachine, AdpNe.OperationalStateEnum, NeEvent, NeStateCallBack> getNeOperationalStateMachine()
    {
        return neOperationalStateMachine;
    }

    public StateMachine<NeCommunicationStateMachine, AdpNe.CommunicationStateEnum, NeEvent, NeStateCallBack> getNeCommunicationStateMachine()
    {
        return neCommunicationStateMachine;
    }

    public StateMachine<NeSupervisionStateMachine, AdpNe.SupervisionStateEnum, NeEvent, NeStateCallBack> getNeSupervisionStateMachine()
    {
        return neSupervisionStateMachine;
    }

    public StateMachine<NeSyncStateMachine, AdpNe.SynchStateEnum, NeEvent, NeStateCallBack> getNeSyncStateMachine()
    {
        return neSyncStateMachine;
    }

    public StateMachine<NeMaintenanceStateMachine, MaintenanceState, NeEvent, NeStateCallBack> getNeMaintenanaceStateMachine()
    {
        return neMaintenanaceStateMachine;
    }
}