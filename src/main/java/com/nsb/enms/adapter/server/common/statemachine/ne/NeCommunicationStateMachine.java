package com.nsb.enms.adapter.server.common.statemachine.ne;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.squirrelframework.foundation.fsm.annotation.StateMachineParameters;
import org.squirrelframework.foundation.fsm.impl.AbstractUntypedStateMachine;

import com.nsb.enms.adapter.server.common.statemachine.ne.model.NeEvent;
import com.nsb.enms.adapter.server.common.statemachine.ne.model.NeStateCallBack;
import com.nsb.enms.restful.model.adapter.AdpNe;

@StateMachineParameters(stateType = AdpNe.CommunicationStateEnum.class, eventType = NeEvent.class, contextType = NeStateCallBack.class)
public class NeCommunicationStateMachine extends AbstractUntypedStateMachine
{
    private static final Logger log = LogManager
            .getLogger( NeCommunicationStateMachine.class );

    protected void transState( AdpNe.CommunicationStateEnum from,
            AdpNe.CommunicationStateEnum to, NeEvent event,
            NeStateCallBack context ) throws Exception
    {
        log.debug( "Transition from '" + from + "' to '" + to + "' on event '"
                + event + "'." );
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