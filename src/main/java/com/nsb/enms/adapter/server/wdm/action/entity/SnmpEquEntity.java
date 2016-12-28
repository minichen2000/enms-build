package com.nsb.enms.adapter.server.wdm.action.entity;

public class SnmpEquEntity
{
    private Integer id;

    private String index;

    private String name;

    private String programmedType;

    private String presentType;
    
    private String unitPartNumber;

    private String softwarePartNumber;

    private String serialNumber;
    
    private String adminState;
    
    private String operationalState;

    public Integer getId()
    {
        return id;
    }

    public void setId( Integer id )
    {
        this.id = id;
    }

    public String getIndex()
    {
        return index;
    }

    public void setIndex( String index )
    {
        this.index = index;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getProgrammedType()
    {
        return programmedType;
    }

    public void setProgrammedType( String programmedType )
    {
        this.programmedType = programmedType;
    }

    public String getPresentType()
    {
        return presentType;
    }

    public void setPresentType( String presentType )
    {
        this.presentType = presentType;
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
 
    public String getAdminState() {
		return adminState;
	}

	public void setAdminState(String adminState) {
		this.adminState = adminState;
	}

	public String getOperationalState() {
		return operationalState;
	}

	public void setOperationalState(String operationalState) {
		this.operationalState = operationalState;
	}

	@Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("class SnmpEquEntity {\n");
        
        sb.append("    id: ").append(id).append("\n");
        sb.append("    index: ").append(index).append("\n");
        sb.append("    name: ").append(name).append("\n");
        sb.append("    programmedType: ").append(programmedType).append("\n");
        sb.append("    presentType: ").append(presentType).append("\n");
        sb.append("    unitPartNumber: ").append(unitPartNumber).append("\n");
        sb.append("    softwarePartNumber: ").append(softwarePartNumber).append("\n");
        sb.append("    serialNumber: ").append(serialNumber).append("\n");
        sb.append("    adminState: ").append(adminState).append("\n");
        sb.append("    operationalState: ").append(operationalState).append("\n");
        sb.append("}");
        return sb.toString();
    }
}