package com.axon.market.common.domain.ikeeper;

import java.util.Date;

/**
 * 掌柜活动清单
 * Created by zengcr on 2017/4/21.
 */
public class KeeperActivityListDomain
{
    //流水ID
    private Long streamId;
    //清单ID
    private Long id;
    //活动ID
    private Integer activityId;
    //渠道编码
    private String channelCode;
    //手机号码
    private Integer phone;
    //活动参与时间
    private Long downLoadTime;
    //创建时间
    private Date createTime = new Date();

    public Long getStreamId()
    {
        return streamId;
    }

    public void setStreamId(Long streamId)
    {
        this.streamId = streamId;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Integer getActivityId()
    {
        return activityId;
    }

    public void setActivityId(Integer activityId)
    {
        this.activityId = activityId;
    }

    public String getChannelCode()
    {
        return channelCode;
    }

    public void setChannelCode(String channelCode)
    {
        this.channelCode = channelCode;
    }

    public Integer getPhone()
    {
        return phone;
    }

    public void setPhone(Integer phone)
    {
        this.phone = phone;
    }

    public Long getDownLoadTime()
    {
        return downLoadTime;
    }

    public void setDownLoadTime(Long downLoadTime)
    {
        this.downLoadTime = downLoadTime;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }
}
