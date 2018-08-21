package com.axon.market.common.domain.inewScene;

/**
 * Created by zhuwen on 2017/7/12.
 */
public class ConfNewSceneTypeDomain {
    //id
    private int ID;
    //parent id
    private int parentID;
    //场景类型
    private String sceneTypeName;
    //code
    private String code;
    //data source
    private int dataSource;
    //状态
    private int state;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getParentID() {
        return parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

    public String getSceneTypeName() {
        return sceneTypeName;
    }

    public void setSceneTypeName(String sceneTypeName) {
        this.sceneTypeName = sceneTypeName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getDataSource() {
        return dataSource;
    }

    public void setDataSource(int dataSource) {
        this.dataSource = dataSource;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
