package com.nsb.enms.adapter.server.statemachine.ne;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.nsb.enms.adapter.server.statemachine.ne.model.MaintenanceState;
import com.nsb.enms.adapter.server.statemachine.ne.model.NeEvent;
import com.nsb.enms.adapter.server.statemachine.ne.model.NeStateCallBack;
import com.nsb.enms.statemachine.annotation.StateMachineParameters;

@StateMachineParameters(stateType = MaintenanceState.class, eventType = NeEvent.class, contextType = NeStateCallBack.class)
public class NeMaintenanceStateMachine
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