package com.axon.market.common.domain.ikeeper;

/**
 * Created by yuanfei on 2017/8/19.
 */
public class TaskChannelDomain
{
    private Integer id;

    /**
     * 任务标识:keeper_task.task_id
     */
    private Integer taskId;

    /**
     * 渠道类型 1：短信渠道 2：话+渠道  3：互联网渠道
     */
    private Integer channelType;

    /**
     * 渠道接入号码
     */
    private String channelPhone;

    /**
     * 渠道营销话术
     */
    private String channelContent;

    /**
     * 渠道营销频次限制
     */
    private Integer triggerLimit;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getTaskId()
    {
        return taskId;
    }

    public void setTaskId(Integer taskId)
    {
        this.taskId = taskId;
    }

    public Integer getChannelType()
    {
        return channelType;
    }

    public void setChannelType(Integer channelType)
    {
        this.channelType = channelType;
    }

    public String getChannelPhone()
    {
        return channelPhone;
    }

    public void setChannelPhone(String channelPhone)
    {
        this.channelPhone = channelPhone;
    }

    public String getChannelContent()
    {
        return channelContent;
    }

    public void setChannelContent(String channelContent)
    {
        this.channelContent = channelContent;
    }

    public Integer getTriggerLimit()
    {
        return triggerLimit;
    }

    public void setTriggerLimit(Integer triggerLimit)
    {
        this.triggerLimit = triggerLimit;
    }
}
