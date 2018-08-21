package com.axon.market.common.constant.isystem;

/**
 * Created by yangyang on 2016/3/12.
 */
public enum UserTypeEnum
{
    USER_TYPE_ADMIN("-1"),
    USER_TYPE_BUSINESS("0"),
    USER_TYPE_DEVELOPER("1"),
    USER_TYPE_AUDIT("2");

    private String value;

    UserTypeEnum(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }
}
