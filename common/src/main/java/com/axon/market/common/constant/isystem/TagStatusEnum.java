package com.axon.market.common.constant.isystem;

/**
 * Created by yangyang on 2016/3/12.
 */
public enum TagStatusEnum
{
    READY(0),
    REFRESHING(1),
    AUDITING(2),
    AUDIT_REJECT(3);

    private Integer value;

    TagStatusEnum(Integer value)
    {
        this.value = value;
    }

    public Integer getValue()
    {
        return value;
    }
}
