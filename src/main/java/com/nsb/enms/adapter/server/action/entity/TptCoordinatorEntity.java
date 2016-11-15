package com.nsb.enms.adapter.server.action.entity;

public class TptCoordinatorEntity
{
    private String moc;

    private String moi;

    private String equMoi;

    private String ipAddress;

    private String ipMask;

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

    public String getEquMoi()
    {
        return equMoi;
    }

    public void setEquMoi( String equMoi )
    {
        this.equMoi = equMoi;
    }

    public String getIpAddress()
    {
        return ipAddress;
    }

    public void setIpAddress( String ipAddress )
    {
        this.ipAddress = ipAddress;
    }

    public String getIpMask()
    {
        return ipMask;
    }

    public void setIpMask( String ipMask )
    {
        this.ipMask = ipMask;
    }
}