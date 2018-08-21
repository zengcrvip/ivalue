package com.axon.market.common.domain.inewScene;

import java.util.Date;
/**
 * Created by zhuwen on 2017/7/12.
 */
public class ConfNewSceneDomain {
    private int id;
    private String sceneName;
    private String sceneTypeID;
    private String sceneRule;
    private String startTime;
    private String endTime;
    private String remark;
    private String createUser;
    private String createTime;
    private String updateTime;
    private int state;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public String getSceneTypeID() {
        return sceneTypeID;
    }

    public void setSceneTypeID(String sceneTypeID) {
        this.sceneTypeID = sceneTypeID;
    }

    public String getSceneRule() {
        return sceneRule;
    }

    public void setSceneRule(String sceneRule) {
        this.sceneRule = sceneRule;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
