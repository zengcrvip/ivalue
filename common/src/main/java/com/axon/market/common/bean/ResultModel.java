package com.axon.market.common.bean;

/**
 * Created by xuan on 2017/2/13.
 */
public class ResultModel
{
    //返回状态
    private String ResultCode;

    private String Message;


    public String getResultCode()
    {
        return ResultCode;
    }

    public void setResultCode(String ResultCode)
    {
        this.ResultCode = ResultCode;
    }
    public String getMessage()
    {
        return Message;
    }

    public void setMessage(String Message)
    {
        this.Message = Message;
    }

}
