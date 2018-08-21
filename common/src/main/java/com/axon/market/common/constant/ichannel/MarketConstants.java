package com.axon.market.common.constant.ichannel;

/**
 * Created by chenyu on 2016/11/7.
 */
public class MarketConstants
{
    public enum CatalogTypeEnum
    {
        META_DATA_CATALOG("metaData"), TAG_CATALOG("tag"), SEGMENT_CATALOG("segment"), MARKET_JOB_CATALOG("marketJob"),
        MARKET_SMS_CONTENT_CATALOG("smsContent"), MARKET_MMS_CONTENT_CATALOG("mmsContent"), PRODUCT_CATALOG("product");

        private String value;

        CatalogTypeEnum(String value)
        {
            this.value = value;
        }

        public String getValue()
        {
            return value;
        }
    }

    public enum MarketJobScheduleTypeEnum
    {
        SINGLE("single"), CRON("cron"), MANU("manu");

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

    public enum MarketJobStatusEnum
    {
        MARKET_AUDITING("auditing"), MARKET_AUDIT_REJECT("auditReject"), MARKET_READY("marketReady"),
        MARKETING("marketing"), MARKET_PAUSE("marketPause");

        private String value;

        MarketJobStatusEnum(String value)
        {
            this.value = value;
        }

        public String getValue()
        {
            return value;
        }
    }

    public enum MarketSystemTypeEnum
    {
        META("meta"), SEGMENT("segment"), TAG("tag"), MARKET("market"), NOT_KNOWN("notKnown");

        private String value;

        MarketSystemTypeEnum(String value)
        {
            this.value = value;
        }

        public String getValue()
        {
            return value;
        }
    }

    public enum SegmentCreateTypeEnum
    {
        RULE("rule"), LOCAL_IMPORT("localImport"), REMOTE_IMPORT("remoteImport"), NOT_KNOWN("notKnown");

        private String value;

        SegmentCreateTypeEnum(String value)
        {
            this.value = value;
        }

        public String getValue()
        {
            return value;
        }
    }

    public enum SegmentStatusEnum
    {
        AUDITING("auditing"), AUDIT_REJECT("auditReject"), READY("ready"), REFRESHING("refreshing");

        private String value;

        SegmentStatusEnum(String value)
        {
            this.value = value;
        }

        public String getValue()
        {
            return value;
        }
    }

    public enum TagCreateTypeEnum
    {
        RULE("rule"), LOCAL_IMPORT("localImport"), REMOTE_IMPORT("remoteImport"), NOT_KNOWN("notKnown");

        private String value;

        TagCreateTypeEnum(String value)
        {
            this.value = value;
        }

        public String getValue()
        {
            return value;
        }
    }

    public enum TagStatusEnum
    {
        READY("ready"), AUDITING("auditing"), AUDIT_REJECT("auditReject"), REFRESHING("refreshing");

        private String value;

        TagStatusEnum(String value)
        {
            this.value = value;
        }

        public String getValue()
        {
            return value;
        }
    }

    public enum UserAuditTypeEnum
    {
        TAG_AUDIT_USER("tag"), SEGMENT_AUDIT_USER("segment"), MARKET_AUDIT_USER("marketJob"), NOT_KNOWN("notKnown");

        private String value;

        UserAuditTypeEnum(String value)
        {
            this.value = value;
        }

        public String getValue()
        {
            return value;
        }
    }

    public enum UserOperateEnum
    {
        CREATE("create"), UPDATE("update"), DELETE("delete"), DOWNLOAD("download"), REFRESH("refresh");

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

    public enum UserTypeEnum
    {
        USER_TYPE_ADMIN("-1"), USER_TYPE_BUSINESS("0"), USER_TYPE_DEVELOPER("1"), USER_TYPE_AUDIT("2"), NOT_KNOWN("notKnown");

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

    public enum SmsSendConfigOperateEnum
    {
        CREATE("create"), UPDATE("update"), DELETE("delete");

        private String value;

        SmsSendConfigOperateEnum(String value)
        {
            this.value = value;
        }

        public String getValue()
        {
            return value;
        }
    }
}
