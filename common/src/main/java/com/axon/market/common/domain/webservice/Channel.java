package com.axon.market.common.domain.webservice;

import java.io.Serializable;

/**
 * Created by yuanfei on 2017/5/25.
 */
public class Channel implements Serializable
{
    private static final long serialVersionUID = 677484458789332877L;

    private String markDesc;

    private String channelSaleInfo;

    private String saleTypeInfo;

    private String dealType;

    private String webUrl;

    private String realType;

    public String getMarkDesc()
    {
        return markDesc;
    }

    public void setMarkDesc(String markDesc)
    {
        this.markDesc = markDesc;
    }

    public String getChannelSaleInfo()
    {
        return channelSaleInfo;
    }

    public void setChannelSaleInfo(String channelSaleInfo)
    {
        this.channelSaleInfo = channelSaleInfo;
    }

    public String getSaleTypeInfo()
    {
        return saleTypeInfo;
    }

    public void setSaleTypeInfo(String saleTypeInfo)
    {
        this.saleTypeInfo = saleTypeInfo;
    }

    public String getDealType() {
        return dealType;
    }

    public void setDealType(String dealType) {
        this.dealType = dealType;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getRealType() {
        return realType;
    }

    public void setRealType(String realType) {
        this.realType = realType;
    }
}
