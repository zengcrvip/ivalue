package com.axon.market.common.domain.ikeeper;

/**
 * Created by yuanfei on 2017/8/31.
 */
public class TaskInstRecordDomain
{
    private Integer id;

    /**
     * 执行日期
     */
    private String execDate;

    /**
     * 维护的客户的号码
     */
    private String phone;

    /**
     * 维系方式  1：短信  2：话+  3：互联网社交
     */
    private Integer executeType;

    /**
     * 维系员工的id
     */
    private Integer execUserId;

    /**
     * 任务标识:参考keeper.keeper_task.task_id
     */
    private Integer taskId;

    /**
     * 策略类型,任务类型标识:参考KEEPER_TYPE
     */
    private Integer typeId;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getExecDate()
    {
        return execDate;
    }

    public void setExecDate(String execDate)
    {
        this.execDate = execDate;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public Integer getExecuteType()
    {
        return executeType;
    }

    public void setExecuteType(Integer executeType)
    {
        this.executeType = executeType;
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

    public Integer getTypeId()
    {
        return typeId;
    }

    public void setTypeId(Integer typeId)
    {
        this.typeId = typeId;
    }
}
