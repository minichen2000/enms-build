package com.nsb.enms.adapter.server.statemachine.ne;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.statemachine.ne.model.NeEvent;
import com.nsb.enms.adapter.server.statemachine.ne.model.NeStateCallBack;
import com.nsb.enms.restful.model.adapter.AdpNe;
import com.nsb.enms.statemachine.annotation.StateMachineParameters;

@StateMachineParameters(stateType = AdpNe.CommunicationStateEnum.class, eventType = NeEvent.class, contextType = NeStateCallBack.class)
public class NeCommunicationStateMachine
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