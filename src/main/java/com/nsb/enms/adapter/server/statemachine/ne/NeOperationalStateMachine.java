package com.nsb.enms.adapter.server.statemachine.ne;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.statemachine.ne.model.NeEvent;
import com.nsb.enms.adapter.server.statemachine.ne.model.NeStateCallBack;
import com.nsb.enms.state.OperationalState;
import com.nsb.enms.statemachine.annotation.StateMachineParameters;

@StateMachineParameters(stateType = OperationalState.class, eventType = NeEvent.class, contextType = NeStateCallBack.class)
public class NeOperationalStateMachine
{
    private static final Logger log = LogManager
            .getLogger( NeOperationalStateMachine.class );

    public void transState( OperationalState from,
            OperationalState to, NeEvent event,
            NeStateCallBack context ) throws AdapterException
    {
        log.debug( "Transition from '" + from + "' to '" + to + "' on event '"
                + event + "'." );
        context.tellMe( to );
        context = null;
    }

    public void entringState( OperationalState from,
            OperationalState to, NeEvent event,
            NeStateCallBack context )
    {
    }

    public void leavingState( OperationalState from,
            OperationalState to, NeEvent event,
            NeStateCallBack context )
    {
    }
}