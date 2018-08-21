package com.axon.market.common.domain.iservice;

/**
 * Created by zengcr on 2017/1/21.
 */
public class PositionBaseReturnDo
{
    //流水号
    private String streamingNo;
    //响应时间戳YYYYMMDDHHmmss
    private String timeStamp;
    //响应代码
    private String resultCode = "00000";
   //响应结果详细原因
    private String resultDesc = "成功";
   //返回的基站信息
    private BaseInfo baseInfo;

    public String getStreamingNo()
    {
        return streamingNo;
    }

    public void setStreamingNo(String streamingNo)
    {
        this.streamingNo = streamingNo;
    }

    public String getTimeStamp()
    {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp)
    {
        this.timeStamp = timeStamp;
    }

    public String getResultCode()
    {
        return resultCode;
    }

    public void setResultCode(String resultCode)
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

    public BaseInfo getBaseInfo()
    {
        return baseInfo;
    }

    public void setBaseInfo(BaseInfo baseInfo)
    {
        this.baseInfo = baseInfo;
    }
}
