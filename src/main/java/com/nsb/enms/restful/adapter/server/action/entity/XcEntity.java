package com.nsb.enms.restful.adapter.server.action.entity;

public class XcEntity
{
    private String moc;

    private String moi;

    private String directionality;

    private String toTermination;

    private String fromTermination;

    private String signalType;

    private String operationalState;

    private String administrativeState;

    public String getMoc()
    {
        return moc;
    }

    public void setMoc( String moc )
    {
        this.moc = moc;
    }

    public String getMoi()
    {
        return moi;
    }

    public void setMoi( String moi )
    {
        this.moi = moi;
    }

    public String getDirectionality()
    {
        return directionality;
    }

    public void setDirectionality( String directionality )
    {
        this.directionality = directionality;
    }

    public String getToTermination()
    {
        return toTermination;
    }

    public void setToTermination( String toTermination )
    {
        this.toTermination = toTermination;
    }

    public String getFromTermination()
    {
        return fromTermination;
    }

    public void setFromTermination( String fromTermination )
    {
        this.fromTermination = fromTermination;
    }

    public String getSignalType()
    {
        return signalType;
    }

    public void setSignalType( String signalType )
    {
        this.signalType = signalType;
    }

    public String getOperationalState()
    {
        return operationalState;
    }

    public void setOperationalState( String operationalState )
    {
        this.operationalState = operationalState;
    }

    public String getAdministrativeState()
    {
        return administrativeState;
    }

    public void setAdministrativeState( String administrativeState )
    {
        this.administrativeState = administrativeState;
    }
}