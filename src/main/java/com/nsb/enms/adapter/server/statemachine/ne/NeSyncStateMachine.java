package com.nsb.enms.adapter.server.statemachine.ne;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.statemachine.ne.model.NeEvent;
import com.nsb.enms.adapter.server.statemachine.ne.model.NeStateCallBack;
import com.nsb.enms.restful.model.adapter.AdpNe;
import com.nsb.enms.statemachine.annotation.StateMachineParameters;

@StateMachineParameters(stateType = AdpNe.SynchStateEnum.class, eventType = NeEvent.class, contextType = NeStateCallBack.class)
public class NeSyncStateMachine
{
    private static final Logger log = LogManager
            .getLogger( NeSyncStateMachine.class );

    public void transState( AdpNe.SynchStateEnum from, AdpNe.SynchStateEnum to,
            NeEvent event, NeStateCallBack context ) throws AdapterException
    {
        log.debug( "Transition from '" + from + "' to '" + to + "' on event '"
                + event + "'." );
        context.tellMe( to );
        context = null;
    }

    public void entringState( AdpNe.SynchStateEnum from,
            AdpNe.SynchStateEnum to, NeEvent event, NeStateCallBack context )
    {
    }

    public void leavingState( AdpNe.SynchStateEnum from,
            AdpNe.SynchStateEnum to, NeEvent event, NeStateCallBack context )
    {
    }
}