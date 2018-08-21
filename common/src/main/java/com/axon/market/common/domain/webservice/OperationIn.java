package com.axon.market.common.domain.webservice;

import java.io.Serializable;

/**
 * Created by yuanfei on 2017/5/25.
 */
public class OperationIn implements Serializable
{
    private static final long serialVersionUID = 677484458789332877L;

    private String saleId;

    private String saleName;

    private String saleCustName;

    private String saleDesc;

    private String saleEparchyCode;

    private String saleBoidId;

    private String startDate;

    private String endDate;

    private AimList aimList;

    public String getSaleId()
    {
        return saleId;
    }

    public void setSaleId(String saleId)
    {
        this.saleId = saleId;
    }

    public String getSaleName()
    {
        return saleName;
    }

    public void setSaleName(String saleName)
    {
        this.saleName = saleName;
    }

    public String getSaleCustName()
    {
        return saleCustName;
    }

    public void setSaleCustName(String saleCustName)
    {
        this.saleCustName = saleCustName;
    }

    public String getSaleDesc()
    {
        return saleDesc;
    }

    public void setSaleDesc(String saleDesc)
    {
        this.saleDesc = saleDesc;
    }

    public String getSaleEparchyCode()
    {
        return saleEparchyCode;
    }

    public void setSaleEparchyCode(String saleEparchyCode)
    {
        this.saleEparchyCode = saleEparchyCode;
    }

    public String getSaleBoidId()
    {
        return saleBoidId;
    }

    public void setSaleBoidId(String saleBoidId)
    {
        this.saleBoidId = saleBoidId;
    }

    public String getStartDate()
    {
        return startDate;
    }

    public void setStartDate(String startDate)
    {
        this.startDate = startDate;
    }

    public String getEndDate()
    {
        return endDate;
    }

    public void setEndDate(String endDate)
    {
        this.endDate = endDate;
    }

    public AimList getAimList()
    {
        return aimList;
    }

    public void setAimList(AimList aimList)
    {
        this.aimList = aimList;
    }
}
