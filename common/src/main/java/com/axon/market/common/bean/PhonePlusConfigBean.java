package com.axon.market.common.bean;

import com.axon.market.common.util.SpringUtil;
import org.springframework.stereotype.Component;

/**
 * Created by Duzm on 2017/8/9.
 * 连接第三方渠道话+的配置数据
 */
public class PhonePlusConfigBean {

    private String initialcallUrl;

    private String submitResultUrl;

    private String getCallHistoryUrl;

    private String getCallRecordUrl;

    private String agentId;

    public static PhonePlusConfigBean getInstance()
    {
        return (PhonePlusConfigBean) SpringUtil.getSingletonBean("phonePlusConfigBean");
    }

    public String getInitialcallUrl() {
        return initialcallUrl;
    }

    public String getSubmitResultUrl() {
        return submitResultUrl;
    }

    public String getGetCallHistoryUrl() {
        return getCallHistoryUrl;
    }

    public String getGetCallRecordUrl() {
        return getCallRecordUrl;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setInitialcallUrl(String initialcallUrl) {
        this.initialcallUrl = initialcallUrl;
    }

    public void setSubmitResultUrl(String submitResultUrl) {
        this.submitResultUrl = submitResultUrl;
    }

    public void setGetCallHistoryUrl(String getCallHistoryUrl) {
        this.getCallHistoryUrl = getCallHistoryUrl;
    }

    public void setGetCallRecordUrl(String getCallRecordUrl) {
        this.getCallRecordUrl = getCallRecordUrl;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }
}
