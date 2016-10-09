package com.nsb.enms.adapter.server.common.statemachine.ne;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.squirrelframework.foundation.fsm.annotation.StateMachineParameters;
import org.squirrelframework.foundation.fsm.impl.AbstractUntypedStateMachine;

import com.nsb.enms.adapter.server.common.statemachine.ne.model.NeEvent;
import com.nsb.enms.adapter.server.common.statemachine.ne.model.NeStateCallBack;
import com.nsb.enms.restful.model.adapter.AdpNe;

@StateMachineParameters(stateType = AdpNe.OperationalStateEnum.class, eventType = NeEvent.class, contextType = NeStateCallBack.class)
public class NeOperationalStateMachine extends AbstractUntypedStateMachine
{
    private static final Logger log = LogManager
            .getLogger( NeOperationalStateMachine.class );

    protected void transState( AdpNe.OperationalStateEnum from,
            AdpNe.OperationalStateEnum to, NeEvent event,
            NeStateCallBack context ) throws Exception
    {
        log.debug( "Transition from '" + from + "' to '" + to + "' on event '"
                + event + "'." );
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