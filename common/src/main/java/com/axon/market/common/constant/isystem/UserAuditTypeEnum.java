package com.axon.market.common.constant.isystem;

/**
 * Created by yangyang on 2016/3/10.
 */
public enum UserAuditTypeEnum
{
    AUDIT_TAG("auditTag"),
    AUDIT_MODEL("auditModel"),
    AUDIT_MARKET_TASK("auditMarketTask");

    private String value;

    private UserAuditTypeEnum(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }

}
