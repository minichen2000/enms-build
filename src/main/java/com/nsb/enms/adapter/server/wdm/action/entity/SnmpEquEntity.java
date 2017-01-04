package com.nsb.enms.adapter.server.wdm.action.entity;

public class SnmpEquEntity
{
    private String id;

    private String index;

    private String name;

    private String programmedType;

    private String presentType;
    
    private String manufacturingPartNumber;

    private String swPartNum;

    private String serialNumber;
    
    private String adminState;
    
    private String operationalState;
    
    private String clei;
    
    private String hfd;
    
    private String marketingPartNumber;
    
    private String companyID;
    
    private String mnemonic;
    
    private String date;
    
    private String extraData;
    
    private String factoryID;

    public String getId()
    {
        return id;
    }

    public void setId( String id )
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

    public String getManufacturingPartNumber()
    {
        return manufacturingPartNumber;
    }

    public void setManufacturingPartNumber( String manufacturingPartNumber )
    {
        this.manufacturingPartNumber = manufacturingPartNumber;
    }

    public String getSWPartNum()
    {
        return swPartNum;
    }

    public void setSWPartNum( String swPartNum )
    {
        this.swPartNum = swPartNum;
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

	public String getClei() {
		return clei;
	}

	public void setClei(String clei) {
		this.clei = clei;
	}

	public String getHfd() {
		return hfd;
	}

	public void setHfd(String hfd) {
		this.hfd = hfd;
	}

	public String getMarketingPartNumber() {
		return marketingPartNumber;
	}

	public void setMarketingPartNumber(String marketingPartNumber) {
		this.marketingPartNumber = marketingPartNumber;
	}

	public String getCompanyID() {
		return companyID;
	}

	public void setCompanyID(String companyID) {
		this.companyID = companyID;
	}

	public String getMnemonic() {
		return mnemonic;
	}

	public void setMnemonic(String mnemonic) {
		this.mnemonic = mnemonic;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getExtraData() {
		return extraData;
	}

	public void setExtraData(String extraData) {
		this.extraData = extraData;
	}

	public String getFactoryID() {
		return factoryID;
	}

	public void setFactoryID(String factoryID) {
		this.factoryID = factoryID;
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
        sb.append("    manufacturingPartNumber: ").append(manufacturingPartNumber).append("\n");
        sb.append("    swPartNum: ").append(swPartNum).append("\n");
        sb.append("    serialNumber: ").append(serialNumber).append("\n");
        sb.append("    adminState: ").append(adminState).append("\n");
        sb.append("    clei: ").append(clei).append("\n");
        sb.append("    hfd: ").append(hfd).append("\n");
        sb.append("    marketingPartNumber: ").append(marketingPartNumber).append("\n");
        sb.append("    companyID: ").append(companyID).append("\n");
        sb.append("    mnemonic: ").append(mnemonic).append("\n");
        sb.append("    date: ").append(date).append("\n");
        sb.append("    extraData: ").append(extraData).append("\n");
        sb.append("    factoryID: ").append(factoryID).append("\n");        
        sb.append("}");
        return sb.toString();
    }
}