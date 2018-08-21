package com.axon.market.common.constant.iconsumption;

/**
 * Created by Zhuwen on 2017/8/2.
 */
public enum DixiaoGPTaskStatusEnum {
    TASK_INIT(0),//初始
    TASK_BIGDATA_FINISH(1);//大数据规则匹配处理结束

    private Integer value;

    private DixiaoGPTaskStatusEnum(Integer value)
    {
        this.value = value;
    }

    public Integer getValue()
    {
        return value;
    }
}
