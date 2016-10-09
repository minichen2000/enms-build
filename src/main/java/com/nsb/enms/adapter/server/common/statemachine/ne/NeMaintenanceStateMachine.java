package com.nsb.enms.adapter.server.common.statemachine.ne;

import org.squirrelframework.foundation.fsm.annotation.StateMachineParameters;
import org.squirrelframework.foundation.fsm.impl.AbstractUntypedStateMachine;

import com.nsb.enms.adapter.server.common.statemachine.ne.NeState.CommunicationState;
import com.nsb.enms.adapter.server.common.statemachine.ne.NeState.MaintenanceState;

@StateMachineParameters(stateType = MaintenanceState.class, eventType = NeEvent.class, contextType = NeStateCallBack.class)
public class NeMaintenanceStateMachine extends AbstractUntypedStateMachine
{
    protected void transState( CommunicationState from, CommunicationState to,
            NeEvent event, NeStateCallBack context )
    {
        System.out.println( "Transition from '" + from + "' to '" + to
                + "' on event '" + event + "'." );
        context.tellMe( to );
        context = null;
    }

    protected void entringState( MaintenanceState from, MaintenanceState to,
            NeEvent event, NeStateCallBack context )
    {
        System.out.println( "Entry State \'" + to + "\'." );
        context.tellMe( to );
    }

    protected void leavingState( MaintenanceState from, MaintenanceState to,
            NeEvent event, NeStateCallBack context )
    {
        if( context != null )
        {
            System.out.println( "Exiting State \'" + from + "\'" );
            context.tellMe( from );
            context = null;
        }
    }

}