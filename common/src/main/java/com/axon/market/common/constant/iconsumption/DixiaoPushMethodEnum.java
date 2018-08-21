package com.axon.market.common.constant.iconsumption;

/**
 * Created by Administrator on 2017/8/2.
 */
public enum  DixiaoPushMethodEnum {
    METHOD_VOICEPLUS(0),//话+外呼
    METHOD_DOWNLOAD(1);//下载

    private Integer value;

    private DixiaoPushMethodEnum(Integer value)
    {
        this.value = value;
    }

    public Integer getValue()
    {
        return value;
    }
}
