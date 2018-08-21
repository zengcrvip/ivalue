package com.axon.market.common.domain.iscene;

import java.util.Date;

/**
 * Created by DELL on 2016/12/6.
 */
public class AdditionalConditionDomain
{
    private Integer Id;//id

    private String Name;//名称

    private String Description;//描述

    private Integer Type;//类型

    private Integer IsDelete;//是否删除

    private Date EditTime;//修改时间

    private String EditUserName;//修改人的姓名

    private Integer EditUserId;//修改人id

    public AdditionalConditionDomain()
    {
    }

    public AdditionalConditionDomain(Integer id, String name, String description, Integer type,
                                     Integer isDelete, Date editTime, String editUserName, Integer editUserId)
    {
        Id = id;
        Name = name;
        Description = description;
        Type = type;
        IsDelete = isDelete;
        EditTime = editTime;
        EditUserName = editUserName;
        EditUserId = editUserId;
    }

    public AdditionalConditionDomain(Integer id, String name, String description, Integer type,
                                     String editUserName, Integer editUserId, Integer isDelete)
    {
        Id = id;
        Name = name;
        Description = description;
        Type = type;
        EditUserName = editUserName;
        EditUserId = editUserId;
        IsDelete = isDelete;
    }

    public Integer getId()
    {
        return Id;
    }

    public void setId(Integer id)
    {
        Id = id;
    }

    public String getName()
    {
        return Name;
    }

    public void setName(String name)
    {
        Name = name;
    }

    public String getDescription()
    {
        return Description;
    }

    public void setDescription(String description)
    {
        Description = description;
    }

    public Integer getType()
    {
        return Type;
    }

    public void setType(Integer type)
    {
        Type = type;
    }

    public Integer getIsDelete()
    {
        return IsDelete;
    }

    public void setIsDelete(Integer isDelete)
    {
        IsDelete = isDelete;
    }

    public Date getEditTime()
    {
        return EditTime;
    }

    public void setEditTime(Date editTime)
    {
        EditTime = editTime;
    }

    public String getEditUserName()
    {
        return EditUserName;
    }

    public void setEditUserName(String editUserName)
    {
        EditUserName = editUserName;
    }

    public Integer getEditUserId()
    {
        return EditUserId;
    }

    public void setEditUserId(Integer editUserId)
    {
        EditUserId = editUserId;
    }
}
