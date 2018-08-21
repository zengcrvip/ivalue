package com.axon.market.common.constant.ikeeper;

/**
 * Created by yuanfei on 2017/8/15.
 */
public enum KeeperTaskInstStateEnum
{
    READY("1"),
    EXECUTING("2"),
    EXECUTED("3"),
    INVALID("4");

    private String value;

    KeeperTaskInstStateEnum(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }
}
