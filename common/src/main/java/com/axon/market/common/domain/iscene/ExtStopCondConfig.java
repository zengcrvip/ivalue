package com.axon.market.common.domain.iscene;

import java.util.Date;

/**
 * Created by xuan on 2017/2/13.
 */
public class ExtStopCondConfig
{
    private int id;
    private String name;
    private String description;
    private int type;
    private int isDelete;
    private int editUserId;
    private String editUserName;
    private Date editTime;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public int getIsDelete()
    {
        return isDelete;
    }

    public void setIsDelete(int isDelete)
    {
        this.isDelete = isDelete;
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

    public Date getEditTime()
    {
        return editTime;
    }

    public void setEditTime(Date editTime)
    {
        this.editTime = editTime;
    }
}
