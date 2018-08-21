package com.axon.market.common.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuanfei on 2017/4/21.
 */
public class ResultVo
{
    private String resultCode = "0000";

    private String resultMsg = "success";

    private Object resultObj ;

    public ResultVo()
    {
    }

    public ResultVo(String resultCode, String resultMsg)
    {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

    public String getResultCode()
    {
        return resultCode;
    }

    public void setResultCode(String resultCode)
    {
        this.resultCode = resultCode;
    }

    public String getResultMsg()
    {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg)
    {
        this.resultMsg = resultMsg;
    }

    public Object getResultObj()
    {
        return resultObj;
    }

    public void setResultObj(Object resultObj)
    {
        this.resultObj = resultObj;
    }
}
