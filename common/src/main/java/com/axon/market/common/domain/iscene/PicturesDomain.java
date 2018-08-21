package com.axon.market.common.domain.iscene;

/**
 * Created by hale on 2016/12/13.
 */
public class PicturesDomain
{
    private int id;

    private String url;

    private String thumbnail;

    private String title;

    private int tempId;

    private String pictureByte;

    private int editUserId;

    private String editUserName;

    private String editTime;

    private int isDelete;

    private int provinceId;

    private int createId;

    private String typeName;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getThumbnail()
    {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail)
    {
        this.thumbnail = thumbnail;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public int getTempId()
    {
        return tempId;
    }

    public void setTempId(int tempId)
    {
        this.tempId = tempId;
    }

    public String getPictureByte()
    {
        return pictureByte;
    }

    public void setPictureByte(String pictureByte)
    {
        this.pictureByte = pictureByte;
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

    public String getEditTime()
    {
        return editTime;
    }

    public void setEditTime(String editTime)
    {
        this.editTime = editTime;
    }

    public int getIsDelete()
    {
        return isDelete;
    }

    public void setIsDelete(int isDelete)
    {
        this.isDelete = isDelete;
    }

    public int getProvinceId()
    {
        return provinceId;
    }

    public void setProvinceId(int provinceId)
    {
        this.provinceId = provinceId;
    }

    public int getCreateId()
    {
        return createId;
    }

    public void setCreateId(int createId)
    {
        this.createId = createId;
    }

    public String getTypeName()
    {
        return typeName;
    }

    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }

}
