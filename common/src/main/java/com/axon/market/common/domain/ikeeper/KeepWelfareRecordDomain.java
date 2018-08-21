package com.axon.market.common.domain.ikeeper;

import java.util.Date;

/**
 * Created by Zhuwen on 2017/8/21.
 */
public class KeepWelfareRecordDomain {
    //赠送记录id
    private int recordId;
    //赠送方式
    private int welfareType;
    //任务id
    private int taskid;
    //福利id
    private String welfareIds;
    //生效时间
    private String effTime;
    //失效时间
    private String expTime;
    //创建人
    private int userId;
    //状态
    private int state;

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public int getWelfareType() {
        return welfareType;
    }

    public void setWelfareType(int welfareType) {
        this.welfareType = welfareType;
    }

    public int getTaskid() {
        return taskid;
    }

    public void setTaskid(int taskid) {
        this.taskid = taskid;
    }

    public String getWelfareIds() {
        return welfareIds;
    }

    public void setWelfareIds(String welfareIds) {
        this.welfareIds = welfareIds;
    }

    public String getEffTime() {
        return effTime;
    }

    public void setEffTime(String effTime) {
        this.effTime = effTime;
    }

    public String getExpTime() {
        return expTime;
    }

    public void setExpTime(String expTime) {
        this.expTime = expTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
