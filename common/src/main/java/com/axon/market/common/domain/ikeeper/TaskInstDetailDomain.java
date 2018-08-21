package com.axon.market.common.domain.ikeeper;

/**
 * Created by yuanfei on 2017/8/20.
 */
public class TaskInstDetailDomain
{
    /**
     * 任务详情ID
     */
    private Integer detailId;

    /**
     * 任务实例id
     */
    private Integer taskInstId;

    /**
     * 类型 对应keeper.keeper_type的type_id
     */
    private Integer typeId;

    /**
     * 被维系的客户名称
     */
    private String customerName;

    /**
     * 被维系的客户号码
     */
    private  String telephone;

    /**
     * 任务维系短信执行情况，0：未执行，1：已执行
     */
    private Integer smsResult;

    /**
     * 外呼结果  0：未执行外呼或失败，1：呼叫完成(超过外呼限制后自动改成完成状态)
     */
    private Integer callResult;

    /**
     * 话加渠道执行次数
     */
    private Integer callTimes;

    /**
     * 互联网渠道执行次数
     */
    private Integer itTimes;

    /**
     * 任务状态:1：待执行 2：执行中 3：已执行 4：已失效
     */
    private Integer state;

    /**
     * 失效时间 只对场景关怀类任务有效,格式yyyymmddHHmmss
     */
    private String expTime;

    public Integer getDetailId()
    {
        return detailId;
    }

    public void setDetailId(Integer detailId)
    {
        this.detailId = detailId;
    }

    public Integer getTaskInstId()
    {
        return taskInstId;
    }

    public void setTaskInstId(Integer taskInstId)
    {
        this.taskInstId = taskInstId;
    }

    public Integer getTypeId()
    {
        return typeId;
    }

    public void setTypeId(Integer typeId)
    {
        this.typeId = typeId;
    }

    public String getCustomerName()
    {
        return customerName;
    }

    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
    }

    public String getTelephone()
    {
        return telephone;
    }

    public void setTelephone(String telephone)
    {
        this.telephone = telephone;
    }

    public Integer getSmsResult()
    {
        return smsResult;
    }

    public void setSmsResult(Integer smsResult)
    {
        this.smsResult = smsResult;
    }

    public Integer getCallResult()
    {
        return callResult;
    }

    public void setCallResult(Integer callResult)
    {
        this.callResult = callResult;
    }

    public Integer getCallTimes()
    {
        return callTimes;
    }

    public void setCallTimes(Integer callTimes)
    {
        this.callTimes = callTimes;
    }

    public Integer getItTimes()
    {
        return itTimes;
    }

    public void setItTimes(Integer itTimes)
    {
        this.itTimes = itTimes;
    }

    public Integer getState()
    {
        return state;
    }

    public void setState(Integer state)
    {
        this.state = state;
    }

    public String getExpTime()
    {
        return expTime;
    }

    public void setExpTime(String expTime)
    {
        this.expTime = expTime;
    }
}
