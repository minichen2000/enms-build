package com.nsb.enms.adapter.server.action.entity;

import java.util.List;

public class TpEntity
{
    private String moc;

    private String moi;

    private String userLabel;

    private int stmLevel;

    // private String alarmStatus;

    private String administrativeState;

    private List<String> supportedByObjectList;

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

    // public String getAlarmStatus() {
    // return alarmStatus;
    // }
    //
    // public void setAlarmStatus(String alarmStatus) {
    // this.alarmStatus = alarmStatus;
    // }

    public String getAdministrativeState()
    {
        return administrativeState;
    }

    public void setAdministrativeState( String administrativeState )
    {
        this.administrativeState = administrativeState;
    }

    public List<String> getSupportedByObjectList()
    {
        return supportedByObjectList;
    }

    public void setSupportedByObjectList( List<String> supportedByObjectList )
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
    public String toString()
    {
        return "TpEntity [moc=" + moc + ", moi=" + moi + ", userLabel="
                + userLabel + ", stmLevel=" + stmLevel
                + ", administrativeState=" + administrativeState
                + ", supportedByObjectList=" + supportedByObjectList
                + ", operationalState=" + operationalState + "]";
    }
}