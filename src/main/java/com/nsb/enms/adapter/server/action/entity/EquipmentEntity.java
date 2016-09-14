package com.nsb.enms.adapter.server.action.entity;

public class EquipmentEntity
{
    private String moc;

    private String moi;

    private String equipmentActual;

    private String equipmentExpected;

    private String version;

    private String availabilityStatus;

    private String alarmStatus;

    private String locationName;

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

    public String getEquipmentActual()
    {
        return equipmentActual;
    }

    public void setEquipmentActual( String equipmentActual )
    {
        this.equipmentActual = equipmentActual;
    }

    public String getEquipmentExpected()
    {
        return equipmentExpected;
    }

    public void setEquipmentExpected( String equipmentExpected )
    {
        this.equipmentExpected = equipmentExpected;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion( String version )
    {
        this.version = version;
    }

    public String getAvailabilityStatus()
    {
        return availabilityStatus;
    }

    public void setAvailabilityStatus( String availabilityStatus )
    {
        this.availabilityStatus = availabilityStatus;
    }

    public String getAlarmStatus()
    {
        return alarmStatus;
    }

    public void setAlarmStatus( String alarmStatus )
    {
        this.alarmStatus = alarmStatus;
    }

    public String getLocationName()
    {
        return locationName;
    }

    public void setLocationName( String locationName )
    {
        this.locationName = locationName;
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