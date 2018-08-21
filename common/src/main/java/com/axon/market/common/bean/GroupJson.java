package com.axon.market.common.bean;

/**
 * Created by Administrator on 2016/12/7.
 */
public class GroupJson
{
    /**
     * 返回状态码 0：成功 其他失败
     */
    private Integer resultCode;

    /**
     * 返回状态说明
     */
    private String verbose;

    public Integer getResultCode()
    {
        return resultCode;
    }

    public void setResultCode(Integer resultCode)
    {
        this.resultCode = resultCode;
    }

    public String getVerbose()
    {
        return verbose;
    }

    public void setVerbose(String verbose)
    {
        this.verbose = verbose;
    }


}
