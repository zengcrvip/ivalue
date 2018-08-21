package com.axon.market.common.domain.ikeeper;

import java.util.Set;

/**
 * 任务对应的执行用户
 * Created by zengcr on 2017/8/22.
 */
public class TaskUserIdsDomain {
    private Integer taskId;
    private Set<Integer> userSet;

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Set<Integer> getUserSet() {
        return userSet;
    }

    public void setUserSet(Set<Integer> userSet) {
        this.userSet = userSet;
    }
}
