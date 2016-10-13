package com.nsb.enms.adapter.server.statemachine.ne.model;

import com.nsb.enms.adapter.server.db.mgr.AdpNesDbMgr;
import com.nsb.enms.restful.model.adapter.AdpNe;

public class NeStateCallBack
{
    private MaintenanceState maintenanceState;

    private AdpNe.CommunicationStateEnum communicationState;

    private AdpNe.OperationalStateEnum operationalState;

    private AdpNe.SupervisionStateEnum supervisionState;

    private AdpNe.SynchStateEnum synchState;

    private String id;

    private static AdpNesDbMgr nesDbMgr = new AdpNesDbMgr();

    public void tellMe( AdpNe.CommunicationStateEnum communicationState )
            throws Exception
    {
        AdpNe ne = new AdpNe();
        ne.setId( id );
        ne.setCommunicationState( communicationState );
        nesDbMgr.updateNe( ne );
    }

    public void tellMe( MaintenanceState maintenanceState ) throws Exception
    {
        AdpNe ne = new AdpNe();
        ne.setId( id );
        ne.maintenanceState( Boolean.valueOf( maintenanceState.toString() ) );
        nesDbMgr.updateNe( ne );
    }

    public void tellMe( AdpNe.OperationalStateEnum operationalState )
            throws Exception
    {
        AdpNe ne = new AdpNe();
        ne.setId( id );
        ne.setOperationalState( operationalState );
        nesDbMgr.updateNe( ne );
    }

    public void tellMe( AdpNe.SupervisionStateEnum supervisionState )
            throws Exception
    {
        AdpNe ne = new AdpNe();
        ne.setId( id );
        ne.setSupervisionState( supervisionState );
        nesDbMgr.updateNe( ne );
    }

    public void tellMe( AdpNe.SynchStateEnum synchState ) throws Exception
    {
        AdpNe ne = new AdpNe();
        ne.setId( id );
        ne.setSynchState( synchState );
        nesDbMgr.updateNe( ne );
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

    public MaintenanceState getMaintenanceState()
    {
        return maintenanceState;
    }

    public void setMaintenanceState( MaintenanceState maintenanceState )
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