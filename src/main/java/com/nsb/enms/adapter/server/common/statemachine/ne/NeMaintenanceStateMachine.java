package com.nsb.enms.adapter.server.common.statemachine.ne;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.squirrelframework.foundation.fsm.annotation.StateMachineParameters;
import org.squirrelframework.foundation.fsm.impl.AbstractUntypedStateMachine;

@StateMachineParameters(stateType = MaintenanceState.class, eventType = NeEvent.class, contextType = NeStateCallBack.class)
public class NeMaintenanceStateMachine extends AbstractUntypedStateMachine
{
    private static final Logger log = LogManager
            .getLogger( NeMaintenanceStateMachine.class );

    protected void transState( MaintenanceState from, MaintenanceState to,
            NeEvent event, NeStateCallBack context ) throws Exception
    {
        log.debug( "Transition from '" + from + "' to '" + to + "' on event '"
                + event + "'." );
        context.tellMe( to );
        context = null;
    }

    protected void entringState( MaintenanceState from, MaintenanceState to,
            NeEvent event, NeStateCallBack context )
    {
    }

    protected void leavingState( MaintenanceState from, MaintenanceState to,
            NeEvent event, NeStateCallBack context )
    {
    }
}