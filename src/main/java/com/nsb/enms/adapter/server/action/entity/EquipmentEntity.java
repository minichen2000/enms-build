package com.nsb.enms.adapter.server.action.entity;

import java.util.List;

public class EquipmentEntity {
    private int id;
    
	private String moc;

	private String moi;

	private List<String> allowedEquipmentTypes;

	private String unitPartNumber;
	
	private String softwarePartNumber;
	
	private String serialNumber;

	private String equipmentActual;

	private String equipmentExpected;

	private String version;

	private String availabilityStatus;

	private String alarmStatus;

	private String operationalState;

	private String administrativeState;

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public String getMoc() {
		return moc;
	}

	public void setMoc(String moc) {
		this.moc = moc;
	}

	public String getMoi() {
		return moi;
	}

	public void setMoi(String moi) {
		this.moi = moi;
	}

	public List<String> getAllowedEquipmentTypes() {
		return allowedEquipmentTypes;
	}

	public void setAllowedEquipmentTypes(List<String> allowedEquipmentTypes) {
		this.allowedEquipmentTypes = allowedEquipmentTypes;
	}

	public String getUnitPartNumber()
    {
        return unitPartNumber;
    }

    public void setUnitPartNumber( String unitPartNumber )
    {
        this.unitPartNumber = unitPartNumber;
    }

    public String getSoftwarePartNumber()
    {
        return softwarePartNumber;
    }

    public void setSoftwarePartNumber( String softwarePartNumber )
    {
        this.softwarePartNumber = softwarePartNumber;
    }

    public String getSerialNumber()
    {
        return serialNumber;
    }

    public void setSerialNumber( String serialNumber )
    {
        this.serialNumber = serialNumber;
    }

    public String getEquipmentActual() {
		return equipmentActual;
	}

	public void setEquipmentActual(String equipmentActual) {
		this.equipmentActual = equipmentActual;
	}

	public String getEquipmentExpected() {
		return equipmentExpected;
	}

	public void setEquipmentExpected(String equipmentExpected) {
		this.equipmentExpected = equipmentExpected;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getAvailabilityStatus() {
		return availabilityStatus;
	}

	public void setAvailabilityStatus(String availabilityStatus) {
		this.availabilityStatus = availabilityStatus;
	}

	public String getAlarmStatus() {
		return alarmStatus;
	}

	public void setAlarmStatus(String alarmStatus) {
		this.alarmStatus = alarmStatus;
	}

	public String getOperationalState() {
		return operationalState;
	}

	public void setOperationalState(String operationalState) {
		this.operationalState = operationalState;
	}

	public String getAdministrativeState() {
		return administrativeState;
	}

	public void setAdministrativeState(String administrativeState) {
		this.administrativeState = administrativeState;
	}
}