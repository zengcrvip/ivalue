package com.axon.market.common.domain.kafkaservice;

import java.util.List;

/**
 * Created by zengcr on 2017/7/28.
 * 场景规则封装的场景任务对象
 */
public class SceneTask {
    private List<String>  phoneList;
    private Integer taskId;
    private String contentSms;
    private String accessNumber;

    public List<String> getPhoneList() {
        return phoneList;
    }

    public void setPhoneList(List<String> phoneList) {
        this.phoneList = phoneList;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getContentSms() {
        return contentSms;
    }

    public void setContentSms(String contentSms) {
        this.contentSms = contentSms;
    }

    public String getAccessNumber() {
        return accessNumber;
    }

    public void setAccessNumber(String accessNumber) {
        this.accessNumber = accessNumber;
    }
}
