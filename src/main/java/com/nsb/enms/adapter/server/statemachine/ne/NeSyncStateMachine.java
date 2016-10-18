package com.nsb.enms.adapter.server.statemachine.ne;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.squirrelframework.foundation.fsm.annotation.StateMachineParameters;
import org.squirrelframework.foundation.fsm.impl.AbstractUntypedStateMachine;

import com.nsb.enms.adapter.server.statemachine.ne.model.NeEvent;
import com.nsb.enms.adapter.server.statemachine.ne.model.NeStateCallBack;
import com.nsb.enms.restful.model.adapter.AdpNe;

@StateMachineParameters(stateType = AdpNe.SynchStateEnum.class, eventType = NeEvent.class, contextType = NeStateCallBack.class)
public class NeSyncStateMachine extends AbstractUntypedStateMachine
{
    private static final Logger log = LogManager
            .getLogger( NeSyncStateMachine.class );

    protected void transState( AdpNe.SynchStateEnum from,
            AdpNe.SynchStateEnum to, NeEvent event, NeStateCallBack context )
            throws Exception
    {
        log.debug( "Transition from '" + from + "' to '" + to + "' on event '"
                + event + "'." );
        context.tellMe( to );
        context = null;
    }

    protected void entringState( AdpNe.SynchStateEnum from,
            AdpNe.SynchStateEnum to, NeEvent event, NeStateCallBack context )
    {
    }

    protected void leavingState( AdpNe.SynchStateEnum from,
            AdpNe.SynchStateEnum to, NeEvent event, NeStateCallBack context )
    {
    }
}