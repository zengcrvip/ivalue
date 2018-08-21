package com.axon.market.common.domain.iscene;

import java.util.Date;

/**
 * Created by xuan on 2016/12/7.
 */
public class PushConfigDomain {
    private int id;
    /// <summary>
    /// 类别区分（1:流量包 2:应用）
    /// </summary>
    private int type;
    /// <summary>
    /// 内容Id
    /// </summary>
    private int tId ;
    /// <summary>
    /// 名称
    /// </summary>
    private String name;

    /// <summary>
    /// 排序
    /// </summary>
    private int sort;
    /// <summary>
    /// 是否启用 0:未启用 1：启用
    /// </summary>
    private int isUsed;

    private Date editTime;

    private int editUserId;

    private String editUserName;

    private int CreateId;

    private int isDelete;

//    /// <summary>
//    /// 类别名称（1:流量包 2:应用）
//    /// </summary>
//    private String TypeName;

    private int provinceId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int gettId() {
        return tId;
    }

    public void settId(int tId) {
        this.tId = tId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(int isUsed) {
        this.isUsed = isUsed;
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    public int getEditUserId() {
        return editUserId;
    }

    public void setEditUserId(int editUserId) {
        this.editUserId = editUserId;
    }

    public String getEditUserName() {
        return editUserName;
    }

    public void setEditUserName(String editUserName) {
        this.editUserName = editUserName;
    }

    public int getCreateId() {
        return CreateId;
    }

    public void setCreateId(int createId) {
        CreateId = createId;
    }

//    public String getTypeName() {
//        return TypeName;
//    }
//
//    public void setTypeName(String typeName) {
//        TypeName = typeName;
//    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

}
