package com.axon.market.common.domain.ishop;

import java.util.Arrays;

/**
 * 上海应用层的Kafka短信请求
 * Created by zengcr on 2017/8/16.
 */
public class SmsRequestSHDomain {
    /**
     * 手机号码伪码
     */
    private String phone;
    /**
     * 短信模板ID
     */
    private String smsId;
    /**
     * 触发时间
     */
    private String time;

    /**
     * 预留字段
     */
    private String[] fields;

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getSmsId()
    {
        return smsId;
    }

    public void setSmsId(String smsId)
    {
        this.smsId = smsId;
    }

    public String getTime()
    {
        return time;
    }

    public void setTime(String time)
    {
        this.time = time;
    }

    public String[] getFields()
    {
        return fields;
    }

    public void setFields(String[] fields)
    {
        this.fields = fields;
    }

    @Override
    public String toString()
    {
        return "SmsRequest [phone=" + phone + ", smsId=" + smsId + ", time=" + time + ", fields=" + Arrays.toString(fields) + "]";
    }
}
