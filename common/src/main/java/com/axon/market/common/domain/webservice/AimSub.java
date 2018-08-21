package com.axon.market.common.domain.webservice;

import java.io.Serializable;

/**
 * Created by yuanfei on 2017/5/25.
 */
public class AimSub implements Serializable
{
    private static final long serialVersionUID = 677484458789332877L;

    private String aimSubId;

    private String aimSubName;

    private String interfaceType;

    private String code;

    private String value;

    private Product product;

    private Channel channel;

    public String getAimSubId()
    {
        return aimSubId;
    }

    public void setAimSubId(String aimSubId)
    {
        this.aimSubId = aimSubId;
    }

    public String getAimSubName()
    {
        return aimSubName;
    }

    public void setAimSubName(String aimSubName)
    {
        this.aimSubName = aimSubName;
    }

    public String getInterfaceType()
    {
        return interfaceType;
    }

    public void setInterfaceType(String interfaceType)
    {
        this.interfaceType = interfaceType;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public Product getProduct()
    {
        return product;
    }

    public void setProduct(Product product)
    {
        this.product = product;
    }

    public Channel getChannel()
    {
        return channel;
    }

    public void setChannel(Channel channel)
    {
        this.channel = channel;
    }
}
