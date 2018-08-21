package com.axon.market.common.constant.isystem;

/**
 * Created by yangyang on 2016/3/2.
 */
public enum MarketSystemElementStatusEnum
{
    READY("ready"),
    AUDITING("auditing"),
    AUDIT_REJECT("auditReject"),

    MARKETING("marketing"),
    MARKET_PAUSE("marketPause"),

    REFRESHING("refreshing");

    private String value;

    MarketSystemElementStatusEnum(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }
}
