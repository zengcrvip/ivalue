package com.axon.market.common.domain.isystem;

/**
 * Created by Administrator on 2017/1/5.
 */
public class RoleDomain
{
    private Integer id;

    private String name;

    private String permissionMenuIds;

    private String permissionMenuNames;

    private String permissionDataIds;

    private String permissionDataNames;

    private String permissionIdNames;

    private Integer homePageId;

    private Integer createUser;

    private String createUserName;

    private String createTime;

    private Integer updateUser;

    private String updateTime;

    private Integer type;

    private String remarks;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
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

    public String getPermissionMenuIds()
    {
        return permissionMenuIds;
    }

    public void setPermissionMenuIds(String permissionMenuIds)
    {
        this.permissionMenuIds = permissionMenuIds;
    }

    public String getPermissionMenuNames()
    {
        return permissionMenuNames;
    }

    public void setPermissionMenuNames(String permissionMenuNames)
    {
        this.permissionMenuNames = permissionMenuNames;
    }

    public String getPermissionDataIds()
    {
        return permissionDataIds;
    }

    public void setPermissionDataIds(String permissionDataIds)
    {
        this.permissionDataIds = permissionDataIds;
    }

    public String getPermissionDataNames()
    {
        return permissionDataNames;
    }

    public void setPermissionDataNames(String permissionDataNames)
    {
        this.permissionDataNames = permissionDataNames;
    }

    public String getPermissionIdNames()
    {
        return permissionIdNames;
    }

    public void setPermissionIdNames(String permissionIdNames)
    {
        this.permissionIdNames = permissionIdNames;
    }

    public Integer getHomePageId()
    {
        return homePageId;
    }

    public void setHomePageId(Integer homePageId)
    {
        this.homePageId = homePageId;
    }

    public Integer getCreateUser()
    {
        return createUser;
    }

    public void setCreateUser(Integer createUser)
    {
        this.createUser = createUser;
    }

    public String getCreateUserName()
    {
        return createUserName;
    }

    public void setCreateUserName(String createUserName)
    {
        this.createUserName = createUserName;
    }

    public String getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }

    public Integer getUpdateUser()
    {
        return updateUser;
    }

    public void setUpdateUser(Integer updateUser)
    {
        this.updateUser = updateUser;
    }

    public String getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(String updateTime)
    {
        this.updateTime = updateTime;
    }

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public String getRemarks()
    {
        return remarks;
    }

    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }
}
