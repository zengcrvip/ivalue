package com.axon.market.common.domain.ikeeper;

import java.util.Set;

/**
 * 任务对应的导入白名单
 *
 * Created by zengcr on 2017/8/21.
 */
public class TaskCustPhoneDomain {
    private String taskId;
    private Set<String> phoneSet;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Set<String> getPhoneSet() {
        return phoneSet;
    }

    public void setPhoneSet(Set<String> phoneSet) {
        this.phoneSet = phoneSet;
    }
}
