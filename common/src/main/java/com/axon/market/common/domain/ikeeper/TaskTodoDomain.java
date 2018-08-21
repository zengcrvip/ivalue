package com.axon.market.common.domain.ikeeper;

/**
 * Created by Zhuwen on 2017/8/15.
 */
public class TaskTodoDomain {
    //任务id
    private int taskid;
    //员工标识
    private int userid;
    //员工名称
    private String username;
    //创建时间
    private String createdate;
    //待办事项描述
    private String taskcontent;
    //是否已经读取
    private int status;

    public int getTaskid() {
        return taskid;
    }

    public void setTaskid(int taskid) {
        this.taskid = taskid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getTaskcontent() {
        return taskcontent;
    }

    public void setTaskcontent(String taskcontent) {
        this.taskcontent = taskcontent;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
