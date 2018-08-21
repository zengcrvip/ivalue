package com.axon.market.common.domain.ikeeper;

/**
 * Created by yuanfei on 2017/8/20.
 */
public class TaskDomain
{
    private Integer taskId;

    /**
     * 任务类型标识:参考KEEPER_TYPE
     */
    private Integer typeId;

    private String taskName;

    /**
     * 生效时间，格式yyyy-mm-dd
     */
    private String effDate;

    /**
     * 生效时间，格式yyyy-mm-dd
     */
    private String expDate;

    private String createTime;

    private Integer createUserId;

    /**
     * 任务地区，当前创建人的所属地区
     */
    private String taskAreaCode;

    /**
     * 任务归属组织
     */
    private String taskOrgIds;

    /**
     * 任务归属组织名称
     */
    private String taskOrgNames;

    /**
     * 状态：0:待审核，1：审核通过 2：审核不通过 3:已删除 4：已终止
     */
    private Integer state;

    /**
     * 备注
     */
    private String comments;

    public Integer getTaskId()
    {
        return taskId;
    }

    public void setTaskId(Integer taskId)
    {
        this.taskId = taskId;
    }

    public Integer getTypeId()
    {
        return typeId;
    }

    public void setTypeId(Integer typeId)
    {
        this.typeId = typeId;
    }

    public String getTaskName()
    {
        return taskName;
    }

    public void setTaskName(String taskName)
    {
        this.taskName = taskName;
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

    public Integer getCreateUserId()
    {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId)
    {
        this.createUserId = createUserId;
    }

    public String getTaskAreaCode()
    {
        return taskAreaCode;
    }

    public void setTaskAreaCode(String taskAreaCode)
    {
        this.taskAreaCode = taskAreaCode;
    }

    public String getTaskOrgIds()
    {
        return taskOrgIds;
    }

    public void setTaskOrgIds(String taskOrgIds)
    {
        this.taskOrgIds = taskOrgIds;
    }

    public String getTaskOrgNames()
    {
        return taskOrgNames;
    }

    public void setTaskOrgNames(String taskOrgNames)
    {
        this.taskOrgNames = taskOrgNames;
    }

    public Integer getState()
    {
        return state;
    }

    public void setState(Integer state)
    {
        this.state = state;
    }

    public String getComments()
    {
        return comments;
    }

    public void setComments(String comments)
    {
        this.comments = comments;
    }
}
