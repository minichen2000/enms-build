package com.nsb.enms.adapter.server.common.statemachine.ne;

import org.squirrelframework.foundation.fsm.annotation.StateMachineParameters;
import org.squirrelframework.foundation.fsm.impl.AbstractUntypedStateMachine;

import com.nsb.enms.adapter.server.common.statemachine.ne.NeState.SynchState;

@StateMachineParameters(stateType = SynchState.class, eventType = NeEvent.class, contextType = NeStateCallBack.class)
public class NeSyncStateMachine extends AbstractUntypedStateMachine
{
    protected void transState( SynchState from, SynchState to, NeEvent event,
            NeStateCallBack context )
    {
        System.out.println( "Transition from '" + from + "' to '" + to
                + "' on event '" + event + "'." );
        context.tellMe( to );
        context = null;
    }

    protected void entringState( SynchState from, SynchState to, NeEvent event,
            NeStateCallBack context )
    {
        System.out.println( "Entry State \'" + to + "\'." );
        context.tellMe( to );
    }

    protected void leavingState( SynchState from, SynchState to, NeEvent event,
            NeStateCallBack context )
    {
        if( context != null )
        {
            System.out.println( "Exiting State \'" + from + "\'" );
            context.tellMe( from );
            context = null;
        }
    }    
}