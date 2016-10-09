package com.nsb.enms.adapter.server.common.statemachine.ne;

class NeStateCallBack
{
    private NeState.AdminStates adminStates;

    private NeState.CommunicationState communicationState;

    private NeState.MaintenanceState maintenanceState;

    private NeState.OperationalState operationalState;

    private NeState.SupervisionState supervisionState;

    private NeState.SynchState synchState;
    
    private String id;

    public void tellMe( NeState.AdminStates adminStates )
    {
        System.out.println(
            "Tell me, current state is: " + adminStates.getValue() );
    }

    public void tellMe( NeState.CommunicationState communicationState )
    {
        System.out.println(
            "Tell me, current state is: " + communicationState.toString()
            + " , id is " + id);
    }

    public void tellMe( NeState.MaintenanceState maintenanceState )
    {
        System.out.println(
            "Tell me, current state is: " + maintenanceState.toString() );
    }

    public void tellMe( NeState.OperationalState operationalState )
    {
        System.out.println(
            "Tell me, current state is: " + operationalState.toString()
            + ", id is " + id);
    }

    public void tellMe( NeState.SupervisionState supervisionState )
    {
        System.out.println(
            "Tell me, current state is: " + supervisionState.toString()
            + ", id is " + id);
    }

    public void tellMe( NeState.SynchState synchState )
    {
        System.out.println(
            "Tell me, current state is: " + synchState.toString()
            + ", id is " + id);
    }
    
    public String getId()
    {
        return id;
    }

    public void setId( String id )
    {
        this.id = id;
    }

    public NeState.AdminStates getAdminStates()
    {
        return adminStates;
    }

    public void setAdminStates( NeState.AdminStates adminStates )
    {
        this.adminStates = adminStates;
    }

    public NeState.CommunicationState getCommunicationState()
    {
        return communicationState;
    }

    public void setCommunicationState(
            NeState.CommunicationState communicationState )
    {
        this.communicationState = communicationState;
    }

    public NeState.MaintenanceState getMaintenanceState()
    {
        return maintenanceState;
    }

    public void setMaintenanceState( NeState.MaintenanceState maintenanceState )
    {
        this.maintenanceState = maintenanceState;
    }

    public NeState.OperationalState getOperationalState()
    {
        return operationalState;
    }

    public void setOperationalState( NeState.OperationalState operationalState )
    {
        this.operationalState = operationalState;
    }

    public NeState.SupervisionState getSupervisionState()
    {
        return supervisionState;
    }

    public void setSupervisionState( NeState.SupervisionState supervisionState )
    {
        this.supervisionState = supervisionState;
    }

    public NeState.SynchState getSynchState()
    {
        return synchState;
    }

    public void setSynchState( NeState.SynchState synchState )
    {
        this.synchState = synchState;
    }
}