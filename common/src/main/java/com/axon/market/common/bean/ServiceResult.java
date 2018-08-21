package com.axon.market.common.bean;

/**
 * Created by yangyang on 2016/3/2.
 */
public class ServiceResult
{
    private int retValue = 0;

    private String desc = "success";

    public ServiceResult()
    {
    }

    public ServiceResult(int retValue, String desc)
    {
        this.retValue = retValue;
        this.desc = desc;
    }

    public int getRetValue()
    {
        return retValue;
    }

    public void setRetValue(int retValue)
    {
        this.retValue = retValue;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }
}
