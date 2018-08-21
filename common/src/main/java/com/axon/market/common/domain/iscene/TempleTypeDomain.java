package com.axon.market.common.domain.iscene;

import java.util.Date;

/**
 * Created by xuan on 2016/12/8.
 */
public class TempleTypeDomain
{
    public Integer id;

    /**
     * 模型名称
     */
    public String typeName;

    /**
     * JS名称
     */
    public String typeJS;

    /**
     * 是否支持上传多图（1 多图，2 单图）
     */
    public Integer multiPicture;

    /**
     * 修改时间
     */
    public Date editTime;

    /**
     * 修改人id
     */
    public int editUserId;

    public int isDelete;

    /**
     * 修改人名称
     */
    public String editUserName;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getTypeName()
    {
        return typeName;
    }

    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }

    public String getTypeJS()
    {
        return typeJS;
    }

    public void setTypeJS(String typeJS)
    {
        this.typeJS = typeJS;
    }

    public Integer getMultiPicture()
    {
        return multiPicture;
    }

    public void setMultiPicture(Integer multiPicture)
    {
        this.multiPicture = multiPicture;
    }

    public Date getEditTime()
    {
        return editTime;
    }

    public void setEditTime(Date editTime)
    {
        this.editTime = editTime;
    }

    public int getEditUserId()
    {
        return editUserId;
    }

    public void setEditUserId(int editUserId)
    {
        this.editUserId = editUserId;
    }

    public String getEditUserName()
    {
        return editUserName;
    }

    public void setEditUserName(String editUserName)
    {
        this.editUserName = editUserName;
    }

    public int getIsDelete()
    {
        return isDelete;
    }

    public void setIsDelete(int isDelete)
    {
        this.isDelete = isDelete;
    }
}
