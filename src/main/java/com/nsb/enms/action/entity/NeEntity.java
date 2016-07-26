package com.nsb.enms.action.entity;

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

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((administrativeState == null) ? 0
                : administrativeState.hashCode());
        result = prime * result
                + ((locationName == null) ? 0 : locationName.hashCode());
        result = prime * result + ((moc == null) ? 0 : moc.hashCode());
        result = prime * result + ((moi == null) ? 0 : moi.hashCode());
        result = prime * result
                + ((neRelease == null) ? 0 : neRelease.hashCode());
        result = prime * result + ((neType == null) ? 0 : neType.hashCode());
        result = prime * result
                + ((networkAddress == null) ? 0 : networkAddress.hashCode());
        result = prime * result + (ntpEnabled ? 1231 : 1237);
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
        if( !(obj instanceof NeEntity) )
            return false;
        NeEntity other = (NeEntity) obj;
        if( administrativeState == null )
        {
            if( other.administrativeState != null )
                return false;
        }
        else if( !administrativeState.equals( other.administrativeState ) )
            return false;
        if( locationName == null )
        {
            if( other.locationName != null )
                return false;
        }
        else if( !locationName.equals( other.locationName ) )
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
        if( neRelease == null )
        {
            if( other.neRelease != null )
                return false;
        }
        else if( !neRelease.equals( other.neRelease ) )
            return false;
        if( neType == null )
        {
            if( other.neType != null )
                return false;
        }
        else if( !neType.equals( other.neType ) )
            return false;
        if( networkAddress == null )
        {
            if( other.networkAddress != null )
                return false;
        }
        else if( !networkAddress.equals( other.networkAddress ) )
            return false;
        if( ntpEnabled != other.ntpEnabled )
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
        return "NeEntity [moc=" + moc + ", moi=" + moi + ", userLabel="
                + userLabel + ", neType=" + neType + ", neRelease=" + neRelease
                + ", locationName=" + locationName + ", ntpEnabled="
                + ntpEnabled + ", networkAddress=" + networkAddress
                + ", administrativeState=" + administrativeState + "]";
    }
}
