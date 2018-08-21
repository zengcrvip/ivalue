package com.axon.market.common.domain.ikeeper;

/**
 * Created by yuanfei on 2017/8/20.
 */
public class TaskInstDomain
{
    private Integer taskInstId;

    /**
     * 执行日期
     */
    private String execDate;

    /**
     * 维系员工
     */
    private Integer execUserId;

    private Integer taskId;

    private String taskName;

    /**
     * 任务类型标识:参考KEEPER_TYPE
     */
    private Integer typeId;

    /**
     * 规则ID
     */
    private Integer ruleId;

    /**
     * 生效时间
     */
    private String effDate;

    /**
     * 失效时间
     */
    private String expDate;

    private String createTime;

    /**
     * 选择的福利id 福利id集合，来源keeper.keeper_welfare的welfare_id
     */
    private String welfareIds;

    /**
     * 任务状态：0：已终止  1：生效中
     */
    private Integer state;

    /**
     * 话+外呼频次
     */
    private Integer phoneLimit;

    /**
     * 任务包含的渠道类型，逗号隔开，1：短信渠道  2：话+渠道
     */
    private String channelTypes;

    public Integer getTaskInstId()
    {
        return taskInstId;
    }

    public void setTaskInstId(Integer taskInstId)
    {
        this.taskInstId = taskInstId;
    }

    public String getExecDate()
    {
        return execDate;
    }

    public void setExecDate(String execDate)
    {
        this.execDate = execDate;
    }

    public Integer getExecUserId()
    {
        return execUserId;
    }

    public void setExecUserId(Integer execUserId)
    {
        this.execUserId = execUserId;
    }

    public Integer getTaskId()
    {
        return taskId;
    }

    public void setTaskId(Integer taskId)
    {
        this.taskId = taskId;
    }

    public String getTaskName()
    {
        return taskName;
    }

    public void setTaskName(String taskName)
    {
        this.taskName = taskName;
    }

    public Integer getTypeId()
    {
        return typeId;
    }

    public void setTypeId(Integer typeId)
    {
        this.typeId = typeId;
    }

    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public String getEffDate()
    {
        return effDate;
    }

    public void setEffDate(String effDate)
    {
        this.effDate = effDate;
    }

    public String getExpDate()
    {
        return expDate;
    }

    public void setExpDate(String expDate)
    {
        this.expDate = expDate;
    }

    public String getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }

    public String getWelfareIds()
    {
        return welfareIds;
    }

    public void setWelfareIds(String welfareIds)
    {
        this.welfareIds = welfareIds;
    }

    public Integer getState()
    {
        return state;
    }

    public void setState(Integer state)
    {
        this.state = state;
    }

    public Integer getPhoneLimit()
    {
        return phoneLimit;
    }

    public void setPhoneLimit(Integer phoneLimit)
    {
        this.phoneLimit = phoneLimit;
    }

    public String getChannelTypes()
    {
        return channelTypes;
    }

    public void setChannelTypes(String channelTypes)
    {
        this.channelTypes = channelTypes;
    }
}
