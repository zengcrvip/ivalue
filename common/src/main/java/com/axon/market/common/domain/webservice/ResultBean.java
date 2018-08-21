package com.axon.market.common.domain.webservice;

/**
 * Created by yuanfei on 2017/5/23.
 */
public class ResultBean
{
    private int resultCode = 1;

    private String resultDesc = "success";

    public int getResultCode()
    {
        return resultCode;
    }

    public void setResultCode(int resultCode)
    {
        this.resultCode = resultCode;
    }

    public String getResultDesc()
    {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc)
    {
        this.resultDesc = resultDesc;
    }
}
