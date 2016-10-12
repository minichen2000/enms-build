package com.nsb.enms.adapter.server.statemachine.ne;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.squirrelframework.foundation.fsm.annotation.StateMachineParameters;
import org.squirrelframework.foundation.fsm.impl.AbstractUntypedStateMachine;

import com.nsb.enms.adapter.server.statemachine.ne.model.AdminState;
import com.nsb.enms.adapter.server.statemachine.ne.model.NeEvent;
import com.nsb.enms.adapter.server.statemachine.ne.model.NeStateCallBack;

@StateMachineParameters(stateType = AdminState.class, eventType = NeEvent.class, contextType = NeStateCallBack.class)
public class NeAdminStateMachine extends AbstractUntypedStateMachine
{
    private static final Logger log = LogManager
            .getLogger( NeAdminStateMachine.class );

    protected void transState( AdminState from, AdminState to,
            NeEvent event, NeStateCallBack context ) throws Exception
    {
        log.debug( "Transition from '" + from + "' to '" + to + "' on event '"
                + event + "'." );
        context.tellMe( to );
        context = null;
    }

    protected void entringState( AdminState from, AdminState to,
            NeEvent event, NeStateCallBack context )
    {
    }

    protected void leavingState( AdminState from, AdminState to,
            NeEvent event, NeStateCallBack context )
    {
    }
}