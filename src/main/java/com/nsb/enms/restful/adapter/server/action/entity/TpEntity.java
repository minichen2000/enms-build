package com.nsb.enms.restful.adapter.server.action.entity;

public class TpEntity
{
    private String moc;

    private String moi;

    private String userLabel;
    
    private int stmLevel;

    private String alarmStatus;

    private String administrativeState;

    private String supportedByObjectList;

    private String operationalState;

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

    public String getUserLabel()
    {
        return userLabel;
    }

    public void setUserLabel( String userLabel )
    {
        this.userLabel = userLabel;
    }

    public int getStmLevel()
    {
        return stmLevel;
    }

    public void setStmLevel( int stmLevel )
    {
        this.stmLevel = stmLevel;
    }

    public String getAlarmStatus()
    {
        return alarmStatus;
    }

    public void setAlarmStatus( String alarmStatus )
    {
        this.alarmStatus = alarmStatus;
    }

    public String getAdministrativeState()
    {
        return administrativeState;
    }

    public void setAdministrativeState( String administrativeState )
    {
        this.administrativeState = administrativeState;
    }

    public String getSupportedByObjectList()
    {
        return supportedByObjectList;
    }

    public void setSupportedByObjectList( String supportedByObjectList )
    {
        this.supportedByObjectList = supportedByObjectList;
    }

    public String getOperationalState()
    {
        return operationalState;
    }

    public void setOperationalState( String operationalState )
    {
        this.operationalState = operationalState;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((administrativeState == null) ? 0
                : administrativeState.hashCode());
        result = prime * result
                + ((alarmStatus == null) ? 0 : alarmStatus.hashCode());
        result = prime * result + ((moc == null) ? 0 : moc.hashCode());
        result = prime * result + ((moi == null) ? 0 : moi.hashCode());
        result = prime * result + ((operationalState == null) ? 0
                : operationalState.hashCode());
        result = prime * result + stmLevel;
        result = prime * result + ((supportedByObjectList == null) ? 0
                : supportedByObjectList.hashCode());
        result = prime * result
                + ((userLabel == null) ? 0 : userLabel.hashCode());
        return result;
    }

    @Override
    public boolean equals( Object obj )
    {
        if( this == obj )
            return true;
        if( obj == null )
            return false;
        if( !(obj instanceof TpEntity) )
            return false;
        TpEntity other = (TpEntity) obj;
        if( administrativeState == null )
        {
            if( other.administrativeState != null )
                return false;
        }
        else if( !administrativeState.equals( other.administrativeState ) )
            return false;
        if( alarmStatus == null )
        {
            if( other.alarmStatus != null )
                return false;
        }
        else if( !alarmStatus.equals( other.alarmStatus ) )
            return false;
        if( moc == null )
        {
            if( other.moc != null )
                return false;
        }
        else if( !moc.equals( other.moc ) )
            return false;
        if( moi == null )
        {
            if( other.moi != null )
                return false;
        }
        else if( !moi.equals( other.moi ) )
            return false;
        if( operationalState == null )
        {
            if( other.operationalState != null )
                return false;
        }
        else if( !operationalState.equals( other.operationalState ) )
            return false;
        if( stmLevel != other.stmLevel )
            return false;
        if( supportedByObjectList == null )
        {
            if( other.supportedByObjectList != null )
                return false;
        }
        else if( !supportedByObjectList.equals( other.supportedByObjectList ) )
            return false;
        if( userLabel == null )
        {
            if( other.userLabel != null )
                return false;
        }
        else if( !userLabel.equals( other.userLabel ) )
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "TpEntity [moc=" + moc + ", moi=" + moi + ", userLabel="
                + userLabel + ", stmLevel=" + stmLevel + ", alarmStatus="
                + alarmStatus + ", administrativeState=" + administrativeState
                + ", supportedByObjectList=" + supportedByObjectList
                + ", operationalState=" + operationalState + "]";
    }
}
