package com.axon.market.common.constant.isystem;

/**
 * Created by yangyang on 2016/3/10.
 */
public enum MarketSystemElementCreateEnum
{
    RULE("rule"),
    LOCAL_IMPORT("localImport"),
    REMOTE_IMPORT("remoteImport");

    private String value;

    MarketSystemElementCreateEnum(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }
}
