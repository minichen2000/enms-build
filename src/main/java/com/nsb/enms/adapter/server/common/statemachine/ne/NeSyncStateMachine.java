package com.nsb.enms.adapter.server.common.statemachine.ne;

import org.squirrelframework.foundation.fsm.annotation.StateMachineParameters;
import org.squirrelframework.foundation.fsm.impl.AbstractUntypedStateMachine;

import com.nsb.enms.restful.model.adapter.AdpNe;

@StateMachineParameters(stateType = AdpNe.SynchStateEnum.class, eventType = NeEvent.class, contextType = NeStateCallBack.class)
public class NeSyncStateMachine extends AbstractUntypedStateMachine
{
    protected void transState( AdpNe.SynchStateEnum from, AdpNe.SynchStateEnum to, NeEvent event,
            NeStateCallBack context ) throws Exception
    {
        System.out.println( "Transition from '" + from + "' to '" + to
                + "' on event '" + event + "'." );
        context.tellMe( to );
        context = null;
    }

    protected void entringState( AdpNe.SynchStateEnum from, AdpNe.SynchStateEnum to, NeEvent event,
            NeStateCallBack context )
    {
    }

    protected void leavingState( AdpNe.SynchStateEnum from, AdpNe.SynchStateEnum to, NeEvent event,
            NeStateCallBack context )
    {
    }    
}