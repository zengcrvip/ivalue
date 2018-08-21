package com.axon.market.common.constant.isystem;

/**
 * Created by chenyu on 2016/10/15.
 */
public enum UserOperateEnum
{
    CREATE("create"),
    UPDATE("update"),
    DELETE("delete"),
    DOWNLOAD("download"),
    REFRESH("refresh");

    private String value;

    UserOperateEnum(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }
}
