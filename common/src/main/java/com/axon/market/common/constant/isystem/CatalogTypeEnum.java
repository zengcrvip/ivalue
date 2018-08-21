package com.axon.market.common.constant.isystem;

/**
 * Created by yangyang on 2016/3/10.
 */
public enum CatalogTypeEnum
{
    META_DATA_CATALOG("metaData"),
    TAG_CATALOG("tag"),
    SEGMENT_CATALOG("segment"),
    MARKET_JOB_CATALOG("marketJob"),
    MARKET_SMS_CONTENT_CATALOG("smsContent"),
    MARKET_MMS_CONTENT_CATALOG("mmsContent"),
    PRODUCT_CATALOG("product");

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
