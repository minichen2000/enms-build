package com.nsb.enms.adapter.server.common.statemachine.ne;

import org.squirrelframework.foundation.fsm.annotation.StateMachineParameters;
import org.squirrelframework.foundation.fsm.impl.AbstractUntypedStateMachine;

import com.nsb.enms.restful.model.adapter.AdpNe;

@StateMachineParameters(stateType = AdpNe.CommunicationStateEnum.class, eventType = NeEvent.class, contextType = NeStateCallBack.class)
public class NeCommunicationStateMachine extends AbstractUntypedStateMachine
{
    protected void transState( AdpNe.CommunicationStateEnum from,
            AdpNe.CommunicationStateEnum to, NeEvent event,
            NeStateCallBack context ) throws Exception
    {
        System.out.println( "Transition from '" + from + "' to '" + to
                + "' on event '" + event + "'." );
        context.tellMe( to );
        context = null;
    }

    protected void entringState( AdpNe.CommunicationStateEnum from,
            AdpNe.CommunicationStateEnum to, NeEvent event,
            NeStateCallBack context ) throws Exception
    {
    }

    protected void leavingState( AdpNe.CommunicationStateEnum from,
            AdpNe.CommunicationStateEnum to, NeEvent event,
            NeStateCallBack context ) throws Exception
    {
    }
}