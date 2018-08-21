package com.axon.market.common.constant.isystem;

/**
 * Created by chenyu on 2016/6/18.
 */
public enum MarketSystemElementEnum
{
    META("meta"),
    SEGMENT("segment"),
    TAG("tag"),
    MARKET("market");

    private String value;

    MarketSystemElementEnum(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }
}
