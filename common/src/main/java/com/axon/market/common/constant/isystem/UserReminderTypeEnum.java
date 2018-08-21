package com.axon.market.common.constant.isystem;

/**
 * Created by Administrator on 2016/12/6.
 */
public enum UserReminderTypeEnum
{
    USER_REMINDER_BUSINESS("interfaceMarketFeedBackReminder"),
    USER_REMINDER_DEVELOPER("interfaceSegmentRefreshReminder"),
    USER_REMINDER_AUDIT("interfaceAuditReminder");

    private String value;

    UserReminderTypeEnum(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }
}