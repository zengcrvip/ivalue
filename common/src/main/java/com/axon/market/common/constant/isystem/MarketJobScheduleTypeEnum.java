package com.axon.market.common.constant.isystem;

/**
 * Created by yangyang on 2016/3/4.
 */
public enum MarketJobScheduleTypeEnum
{
    SINGLE("single"),
    CRON("cron"),
    MANU("manu");

    private String value;

    MarketJobScheduleTypeEnum(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }
}
