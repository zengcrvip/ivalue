package com.axon.market.common.constant.ischeduling;

/**
 * Created by yuanfei on 2017/1/20.
 */
public enum MarketingTaskStatusEnum
{
    TASK_DELETED(-1),//删除
    TASK_READY(0),//审批通过/准备就绪
    TASK_MARKETING(1),//营销中
    TASK_PAUSE(2),//暂停中
    TASK_AUDITING(5),//审批中
    TASK_AUDIT_REJECT(6);//审批拒绝

    private Integer value;

    private MarketingTaskStatusEnum(Integer value)
    {
        this.value = value;
    }

    public Integer getValue()
    {
        return value;
    }
}
