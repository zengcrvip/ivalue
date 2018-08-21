package com.axon.market.common.domain.ishop;

/**
 * 精细化对接的炒店任务渠道
 * Created by zengcr on 2017/6/15.
 */
public class ShopTaskChannelDomain
{
    //活动编码
    private String saleId;
    //波次编码
    private String saleBoidId;
    //目标客户群编码
    private String aimSubId;
    //渠道编码
    private String departId;
    //文件名称
    private String fileName;

    public String getSaleId()
    {
        return saleId;
    }

    public void setSaleId(String saleId)
    {
        this.saleId = saleId;
    }

    public String getSaleBoidId()
    {
        return saleBoidId;
    }

    public void setSaleBoidId(String saleBoidId)
    {
        this.saleBoidId = saleBoidId;
    }

    public String getAimSubId()
    {
        return aimSubId;
    }

    public void setAimSubId(String aimSubId)
    {
        this.aimSubId = aimSubId;
    }

    public String getDepartId()
    {
        return departId;
    }

    public void setDepartId(String departId)
    {
        this.departId = departId;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }
}
