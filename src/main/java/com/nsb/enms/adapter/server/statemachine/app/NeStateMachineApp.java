package com.nsb.enms.adapter.server.statemachine.app;

import com.nsb.enms.adapter.server.statemachine.ne.NeCommunicationStateMachine;
import com.nsb.enms.adapter.server.statemachine.ne.NeMaintenanceStateMachine;
import com.nsb.enms.adapter.server.statemachine.ne.NeOperationalStateMachine;
import com.nsb.enms.adapter.server.statemachine.ne.NeSupervisionStateMachine;
import com.nsb.enms.adapter.server.statemachine.ne.NeAlignmentStateMachine;
import com.nsb.enms.adapter.server.statemachine.ne.model.NeEvent;
import com.nsb.enms.adapter.server.statemachine.ne.model.NeStateCallBack;
import com.nsb.enms.common.AlignmentState;
import com.nsb.enms.common.CommunicationState;
import com.nsb.enms.common.MaintenanceState;
import com.nsb.enms.common.OperationState;
import com.nsb.enms.common.SupervisionState;
import com.nsb.enms.statemachine.component.StateMachine;
import com.nsb.enms.statemachine.component.StateMachine.StateMachineBuilder;

public class NeStateMachineApp {
	private static NeStateMachineApp inst_ = new NeStateMachineApp();

	private StateMachine<NeOperationalStateMachine, OperationState, NeEvent, NeStateCallBack> neOperationalStateMachine;

	private StateMachine<NeCommunicationStateMachine, CommunicationState, NeEvent, NeStateCallBack> neCommunicationStateMachine;

	private StateMachine<NeSupervisionStateMachine, SupervisionState, NeEvent, NeStateCallBack> neSupervisionStateMachine;

	private StateMachine<NeAlignmentStateMachine, AlignmentState, NeEvent, NeStateCallBack> neAlignmentStateMachine;

	private StateMachine<NeMaintenanceStateMachine, MaintenanceState, NeEvent, NeStateCallBack> neMaintenanaceStateMachine;

	private NeStateMachineApp() {

	}

	public static NeStateMachineApp instance() {
		if (inst_ == null) {
			inst_ = new NeStateMachineApp();
		}
		return inst_;
	}

	public void init() {
		StateMachineBuilder<NeOperationalStateMachine, OperationState, NeEvent, NeStateCallBack> operationalStateBuilder = new StateMachineBuilder<NeOperationalStateMachine, OperationState, NeEvent, NeStateCallBack>(
				NeOperationalStateMachine.class);
		operationalStateBuilder.registExTransition(OperationState.IDLE, OperationState.SUPERVISING,
				NeEvent.E_IDLE_2_SUPERVISING, "transState");
		operationalStateBuilder.registExTransition(OperationState.SUPERVISING, OperationState.IDLE,
				NeEvent.E_SUPERVISING_2_IDLE, "transState");
		operationalStateBuilder.registExTransition(OperationState.IDLE, OperationState.SYNCING,
				NeEvent.E_IDLE_2_SYNCHRONIZING, "transState");
		operationalStateBuilder.registExTransition(OperationState.SYNCING, OperationState.IDLE,
				NeEvent.E_SYNCHRONIZING_2_IDLE, "transState");
		neOperationalStateMachine = operationalStateBuilder.build(OperationState.IDLE);

		StateMachineBuilder<NeCommunicationStateMachine, CommunicationState, NeEvent, NeStateCallBack> communicationStateBuilder = new StateMachineBuilder<NeCommunicationStateMachine, CommunicationState, NeEvent, NeStateCallBack>(
				NeCommunicationStateMachine.class);
		communicationStateBuilder.registExTransition(CommunicationState.DISCONNECTED, CommunicationState.CONNECTED,
				NeEvent.E_UNREACHABLE_2_REACHABLE, "transState");
		communicationStateBuilder.registExTransition(CommunicationState.CONNECTED, CommunicationState.DISCONNECTED,
				NeEvent.E_REACHABLE_2_UNREACHABLE, "transState");
		neCommunicationStateMachine = communicationStateBuilder.build(CommunicationState.DISCONNECTED);

		StateMachineBuilder<NeSupervisionStateMachine, SupervisionState, NeEvent, NeStateCallBack> supervisionStateBuilder = new StateMachineBuilder<NeSupervisionStateMachine, SupervisionState, NeEvent, NeStateCallBack>(
				NeSupervisionStateMachine.class);
		supervisionStateBuilder.registExTransition(SupervisionState.NOT_SUPERVISED, SupervisionState.SUPERVISED,
				NeEvent.E_UNSUPERVISIED_2_SUPERVISIED, "transState");
		neSupervisionStateMachine = supervisionStateBuilder.build(SupervisionState.NOT_SUPERVISED);

		StateMachineBuilder<NeAlignmentStateMachine, AlignmentState, NeEvent, NeStateCallBack> alignmentStateBuilder = new StateMachineBuilder<NeAlignmentStateMachine, AlignmentState, NeEvent, NeStateCallBack>(
				NeAlignmentStateMachine.class);
		alignmentStateBuilder.registExTransition(AlignmentState.MISALIGNED, AlignmentState.ALIGNED,
				NeEvent.E_MISALIGNED_2_ALIGNED, "transState");
		neAlignmentStateMachine = alignmentStateBuilder.build(AlignmentState.MISALIGNED);

		StateMachineBuilder<NeMaintenanceStateMachine, MaintenanceState, NeEvent, NeStateCallBack> maintenanceStateBuilder = new StateMachineBuilder<NeMaintenanceStateMachine, MaintenanceState, NeEvent, NeStateCallBack>(
				NeMaintenanceStateMachine.class);
		maintenanceStateBuilder.registExTransition(MaintenanceState.FALSE, MaintenanceState.TRUE,
				NeEvent.E_FALSE_2_TRUE, "transState");
		neMaintenanaceStateMachine = maintenanceStateBuilder.build(MaintenanceState.FALSE);

		/*
		 * neCommunicationStateMachine.start();
		 * neMaintenanaceStateMachine.start();
		 * neOperationalStateMachine.start(); neSupervisionStateMachine.start();
		 * neSyncStateMachine.start();
		 */
	}

	/*
	 * public void beforeSuperviseNe( String id ) { NeStateCallBack ne = new
	 * NeStateCallBack(); ne.setId( id );
	 * neOperationalStateMachine.setCurrentState( OperationalStateEnum.IDLE );
	 * neOperationalStateMachine.fire( NeEvent.E_IDLE_2_SUPERVISING, ne ); }
	 */

	public void afterSuperviseNe(String id) {
		NeStateCallBack ne = new NeStateCallBack();
		ne.setId(id);
		neSupervisionStateMachine.setCurrentState(SupervisionState.NOT_SUPERVISED);
		neCommunicationStateMachine.setCurrentState(CommunicationState.DISCONNECTED);
		neOperationalStateMachine.setCurrentState(OperationState.SUPERVISING);
		neSupervisionStateMachine.fire(NeEvent.E_UNSUPERVISIED_2_SUPERVISIED, ne);
		neCommunicationStateMachine.fire(NeEvent.E_UNREACHABLE_2_REACHABLE, ne);
		neOperationalStateMachine.fire(NeEvent.E_SUPERVISING_2_IDLE, ne);
	}

	/*
	 * public void beforeSynchData( String id ) { NeStateCallBack ne = new
	 * NeStateCallBack(); ne.setId( id );
	 * neOperationalStateMachine.setCurrentState( OperationalStateEnum.IDLE );
	 * neOperationalStateMachine.fire( NeEvent.E_IDLE_2_SYNCHRONIZING, ne ); }
	 */

	public void afterSynchData(String id) {
		NeStateCallBack ne = new NeStateCallBack();
		ne.setId(id);
		neAlignmentStateMachine.setCurrentState(AlignmentState.MISALIGNED);
		neAlignmentStateMachine.fire(NeEvent.E_MISALIGNED_2_ALIGNED, ne);
		neOperationalStateMachine.setCurrentState(OperationState.SYNCING);
		neOperationalStateMachine.fire(NeEvent.E_SYNCHRONIZING_2_IDLE, ne);
	}

	public StateMachine<NeOperationalStateMachine, OperationState, NeEvent, NeStateCallBack> getNeOperationalStateMachine() {
		return neOperationalStateMachine;
	}

	public StateMachine<NeCommunicationStateMachine, CommunicationState, NeEvent, NeStateCallBack> getNeCommunicationStateMachine() {
		return neCommunicationStateMachine;
	}

	public StateMachine<NeSupervisionStateMachine, SupervisionState, NeEvent, NeStateCallBack> getNeSupervisionStateMachine() {
		return neSupervisionStateMachine;
	}

	public StateMachine<NeAlignmentStateMachine, AlignmentState, NeEvent, NeStateCallBack> getNeAlignmentStateMachine() {
		return neAlignmentStateMachine;
	}

	public StateMachine<NeMaintenanceStateMachine, MaintenanceState, NeEvent, NeStateCallBack> getNeMaintenanaceStateMachine() {
		return neMaintenanaceStateMachine;
	}
}