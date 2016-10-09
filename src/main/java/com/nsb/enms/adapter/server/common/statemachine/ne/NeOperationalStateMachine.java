package com.nsb.enms.adapter.server.common.statemachine.ne;

import org.squirrelframework.foundation.fsm.annotation.StateMachineParameters;
import org.squirrelframework.foundation.fsm.impl.AbstractUntypedStateMachine;

import com.nsb.enms.restful.model.adapter.AdpNe;

@StateMachineParameters(stateType = AdpNe.OperationalStateEnum.class, eventType = NeEvent.class, contextType = NeStateCallBack.class)
public class NeOperationalStateMachine extends AbstractUntypedStateMachine
{
    protected void transState( AdpNe.OperationalStateEnum from,
            AdpNe.OperationalStateEnum to, NeEvent event,
            NeStateCallBack context ) throws Exception
    {
        System.out.println( "Transition from '" + from + "' to '" + to
                + "' on event '" + event + "'." );
        context.tellMe( to );
        context = null;
    }

    protected void entringState( AdpNe.OperationalStateEnum from,
            AdpNe.OperationalStateEnum to, NeEvent event,
            NeStateCallBack context )
    {
    }

    protected void leavingState( AdpNe.OperationalStateEnum from,
            AdpNe.OperationalStateEnum to, NeEvent event,
            NeStateCallBack context )
    {
    }
}