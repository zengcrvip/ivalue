package com.axon.market.common.bean;

/**
 * 位置炒店远程接口返回信息
 * Created by zengcr on 2017/2/10.
 */
public class HttpResult
{
    //返回状态
    private String success;
    //返回信息
    private String message;

    public String getSuccess()
    {
        return success;
    }

    public void setSuccess(String success)
    {
        this.success = success;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}
