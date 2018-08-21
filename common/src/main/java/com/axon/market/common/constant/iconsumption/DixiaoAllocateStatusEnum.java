package com.axon.market.common.constant.iconsumption;

/**
 * Created by Zhuwen on 2017/8/2.
 */
public enum  DixiaoAllocateStatusEnum {
    STATUS_UNALLOCATE(0),//未分配状态
    TASK_ALLOCATE(1);//已分配状态

    private Integer value;

    private DixiaoAllocateStatusEnum(Integer value)
    {
        this.value = value;
    }

    public Integer getValue()
    {
        return value;
    }
}
