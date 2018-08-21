package com.axon.market.common.constant.iconsumption;

/**
 * Created by Zhuwen on 2017/8/2.
 */
public enum  DixiaoIsOnlineEnum {
    OFFLINE(0),//线下
    ONLINE(1);//线上

    private Integer value;

    private DixiaoIsOnlineEnum(Integer value)
    {
        this.value = value;
    }

    public Integer getValue()
    {
        return value;
    }

}
