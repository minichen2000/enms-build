package com.nsb.enms.restful.adapter.server.action.entity;

public class NeEntity
{
    private String moc;

    private String moi;

    private String userLabel;

    private String neType;

    private String neRelease;

    private String locationName;

    private boolean ntpEnabled;

    private String networkAddress;

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

    public String getUserLabel()
    {
        return userLabel;
    }

    public void setUserLabel( String userLabel )
    {
        this.userLabel = userLabel;
    }

    public String getNeType()
    {
        return neType;
    }

    public void setNeType( String neType )
    {
        this.neType = neType;
    }

    public String getNeRelease()
    {
        return neRelease;
    }

    public void setNeRelease( String neRelease )
    {
        this.neRelease = neRelease;
    }

    public String getLocationName()
    {
        return locationName;
    }

    public void setLocationName( String locationName )
    {
        this.locationName = locationName;
    }

    public boolean isNtpEnabled()
    {
        return ntpEnabled;
    }

    public void setNtpEnabled( boolean ntpEnabled )
    {
        this.ntpEnabled = ntpEnabled;
    }

    public String getNetworkAddress()
    {
        return networkAddress;
    }

    public void setNetworkAddress( String networkAddress )
    {
        this.networkAddress = networkAddress;
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
