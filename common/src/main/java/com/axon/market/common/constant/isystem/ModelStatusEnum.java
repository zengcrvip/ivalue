package com.axon.market.common.constant.isystem;

/**
 * Created by yangyang on 2016/3/12.
 */
public enum ModelStatusEnum
{
    READY(0),//审批通过/使用中
    REFRESHING(1),//在刷新中
    AUDITING(2),//审批中
    AUDIT_REJECT(3);//审批不通过打回

    private Integer value;

    ModelStatusEnum(Integer value)
    {
        this.value = value;
    }

    public Integer getValue()
    {
        return value;
    }
}
