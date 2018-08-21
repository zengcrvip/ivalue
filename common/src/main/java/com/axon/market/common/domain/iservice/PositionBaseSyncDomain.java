package com.axon.market.common.domain.iservice;

import com.axon.market.common.domain.iscene.PositionBaseDomain;

/**
 * 基站点信息同步对象
 * Created by zengcr on 2017/1/21.
 */
public class PositionBaseSyncDomain
{
    //合作方标识
    private String partnerId;
    //流水号
    private String streamingNo;
    //调用时间戳YYYYMMDDHHmmss
    private String timeStamp;
    //基站点信息
    private PositionBaseDomain baseInfo;

    public String getPartnerId()
    {
        return partnerId;
    }

    public void setPartnerId(String partnerId)
    {
        this.partnerId = partnerId;
    }

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

    public PositionBaseDomain getBaseInfo()
    {
        return baseInfo;
    }

    public void setBaseInfo(PositionBaseDomain baseInfo)
    {
        this.baseInfo = baseInfo;
    }

}


