package com.axon.market.common.domain.webservice;

/**
 * Created by zengcr on 2017/5/31.
 */
public class SessionStateChageOut
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
