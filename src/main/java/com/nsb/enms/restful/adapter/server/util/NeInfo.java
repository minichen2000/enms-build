package com.nsb.enms.restful.adapter.server.util;

public class NeInfo
{

    private int groupId;

    private int neId;

    private String neRelease;

    private String neType;

    private String userLabel;

    private String locationName;

    private String neAddress;

    public NeInfo( int groupId, int neId, String neRelease, String neType,
            String userLabel, String locationName, String neAddress )
    {
        this.groupId = groupId;
        this.neId = neId;
        this.neRelease = neRelease;
        this.neType = neType;
        this.userLabel = userLabel;
        this.locationName = locationName;
        this.neAddress = neAddress;
    }

    @Override
    public String toString()
    {
        return "GroupID: " + groupId + ", NeID: " + neId + ", NeRelease: '"
                + neRelease + "', NeType: '" + neType + "', UserLabel: '"
                + userLabel + ", LocationName: '" + locationName + "'"
                + ", NeAddress: '" + neAddress + "'";
    }

    public int getGroupId()
    {
        return groupId;
    }

    public int getNeId()
    {
        return neId;
    }

    public String getNeRelease()
    {
        return neRelease;
    }

    public void setNeRelease( String neRelease )
    {
        this.neRelease = neRelease;
    }

    public String getNeType()
    {
        return neType;
    }

    public void setNeType( String neType )
    {
        this.neType = neType;
    }

    public String getUserLabel()
    {
        return userLabel;
    }

    public void setUserLabel( String userLabel )
    {
        this.userLabel = userLabel;
    }

    public String getLocationName()
    {
        return locationName;
    }

    public void setLocationName( String locationName )
    {
        this.locationName = locationName;
    }

    public String getNeAddress()
    {
        return neAddress;
    }

    public void setNeAddress( String neAddress )
    {
        this.neAddress = neAddress;
    }
}
