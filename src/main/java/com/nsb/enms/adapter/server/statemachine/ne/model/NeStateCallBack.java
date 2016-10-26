package com.nsb.enms.adapter.server.statemachine.ne.model;

import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.exception.AdapterExceptionType;
import com.nsb.enms.adapter.server.db.mgr.AdpNesDbMgr;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.restful.model.adapter.AdpNe;

public class NeStateCallBack
{
    private Boolean maintenanceState;

    private AdpNe.CommunicationStateEnum communicationState;

    private AdpNe.OperationalStateEnum operationalState;

    private AdpNe.SupervisionStateEnum supervisionState;

    private AdpNe.SynchStateEnum synchState;

    private String id;

    private static AdpNesDbMgr nesDbMgr = new AdpNesDbMgr();

    public void tellMe( AdpNe.CommunicationStateEnum communicationState )
            throws AdapterException
    {
        AdpNe ne = new AdpNe();
        ne.setId( id );
        ne.setCommunicationState( communicationState );
        updateNe( ne );
    }

    public void tellMe( Boolean maintenanceState )
            throws AdapterException
    {
        AdpNe ne = new AdpNe();
        ne.setId( id );
        ne.setMaintenanceState( maintenanceState );
        updateNe( ne );
    }

    public void tellMe( AdpNe.OperationalStateEnum operationalState )
            throws AdapterException
    {
        AdpNe ne = new AdpNe();
        ne.setId( id );
        ne.setOperationalState( operationalState );
        updateNe( ne );
    }

    public void tellMe( AdpNe.SupervisionStateEnum supervisionState )
            throws AdapterException
    {
        AdpNe ne = new AdpNe();
        ne.setId( id );
        ne.setSupervisionState( supervisionState );
        updateNe( ne );
    }

    public void tellMe( AdpNe.SynchStateEnum synchState )
            throws AdapterException
    {
        AdpNe ne = new AdpNe();
        ne.setId( id );
        ne.setSynchState( synchState );
        updateNe( ne );
    }

    private void updateNe( AdpNe ne ) throws AdapterException
    {
        try
        {
            nesDbMgr.updateNe( ne );
        }
        catch( Exception e )
        {
            throw new AdapterException(
                    AdapterExceptionType.EXCPT_INTERNAL_ERROR,
                    ErrorCode.FAIL_DB_OPERATION );
        }
    }

    public String getId()
    {
        return id;
    }

    public void setId( String id )
    {
        this.id = id;
    }

    public AdpNe.CommunicationStateEnum getCommunicationState()
    {
        return communicationState;
    }

    public void setCommunicationState(
            AdpNe.CommunicationStateEnum communicationState )
    {
        this.communicationState = communicationState;
    }

    public Boolean getMaintenanceState()
    {
        return maintenanceState;
    }

    public void setMaintenanceState( Boolean maintenanceState )
    {
        this.maintenanceState = maintenanceState;
    }

    public AdpNe.OperationalStateEnum getOperationalState()
    {
        return operationalState;
    }

    public void setOperationalState(
            AdpNe.OperationalStateEnum operationalState )
    {
        this.operationalState = operationalState;
    }

    public AdpNe.SupervisionStateEnum getSupervisionState()
    {
        return supervisionState;
    }

    public void setSupervisionState(
            AdpNe.SupervisionStateEnum supervisionState )
    {
        this.supervisionState = supervisionState;
    }

    public AdpNe.SynchStateEnum getSynchState()
    {
        return synchState;
    }

    public void setSynchState( AdpNe.SynchStateEnum synchState )
    {
        this.synchState = synchState;
    }
}