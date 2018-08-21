package com.axon.market.common.domain.ikeeper;

/**
 * Created by yuanfei on 2017/8/19.
 */
public class TaskInstResultDomain
{
    private Integer id;

    /**
     * 任务实例详情id
     */
    private Integer detailId;

    /**
     * 结果类型 1：短信，2：话+ ，3：互联网社交
     */
    private Integer resultType;

    /**
     * 结果描述
     */
    private String resultDesc;

    /**
     * 结果状态
     */
    private Integer resultStatus;

    /**
     * 二次确认结果，主要针对话+
     */
    private String confirmResult;

    /**
     * （针对话+维系动作)业务意向接受状态 0:默认未选择，1：接受，2：不接受
     */
    private Integer businessTendency;

    /**
     * 生成时间
     */
    private String triggerTime;

    /**
     * 再次确认时间
     */
    private String confirmTime;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getDetailId()
    {
        return detailId;
    }

    public void setDetailId(Integer detailId)
    {
        this.detailId = detailId;
    }

    public Integer getResultType()
    {
        return resultType;
    }

    public void setResultType(Integer resultType)
    {
        this.resultType = resultType;
    }

    public String getResultDesc()
    {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc)
    {
        this.resultDesc = resultDesc;
    }

    public Integer getResultStatus()
    {
        return resultStatus;
    }

    public void setResultStatus(Integer resultStatus)
    {
        this.resultStatus = resultStatus;
    }

    public String getConfirmResult()
    {
        return confirmResult;
    }

    public void setConfirmResult(String confirmResult)
    {
        this.confirmResult = confirmResult;
    }

    public Integer getBusinessTendency()
    {
        return businessTendency;
    }

    public void setBusinessTendency(Integer businessTendency)
    {
        this.businessTendency = businessTendency;
    }

    public String getTriggerTime()
    {
        return triggerTime;
    }

    public void setTriggerTime(String triggerTime)
    {
        this.triggerTime = triggerTime;
    }

    public String getConfirmTime()
    {
        return confirmTime;
    }

    public void setConfirmTime(String confirmTime)
    {
        this.confirmTime = confirmTime;
    }
}
