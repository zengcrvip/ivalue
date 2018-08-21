package com.axon.market.common.constant.iscene;

/**
 * Created by Administrator on 2016/12/6.
 */
public enum UrlBlacklistEnum
{
    EDIT("1"),
    DETELE("2");

    private String value;

    UrlBlacklistEnum(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }
}
