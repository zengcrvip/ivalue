package com.axon.market.common.domain.icommon;

/**
 * Created by chenyu on 2017/1/22.
 */
public class CategoryDomain
{
    private Integer id;

    private String name;

    private Integer pId;

    private String pIdName;

    private Integer level;

    private String type;

    private Boolean isParent = true;

    private Integer createUser;

    private String createUserName;

    private String createTime;

    private Integer updateUser;

    private String updateTime;

    private Integer status;

    private String remarks;

    /**
     * 可能的值有num、string、date等
     */
    private String valueType = "logic";

    /**
     * 保存属性、标签、客户群具体的内容
     */
    private Object element;

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

    public Integer getpId()
    {
        return pId;
    }

    public void setpId(Integer pId)
    {
        this.pId = pId;
    }

    public String getpIdName()
    {
        return pIdName;
    }

    public void setpIdName(String pIdName)
    {
        this.pIdName = pIdName;
    }

    public Integer getLevel()
    {
        return level;
    }

    public void setLevel(Integer level)
    {
        this.level = level;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public Boolean getIsParent()
    {
        return isParent;
    }

    public void setIsParent(Boolean isParent)
    {
        this.isParent = isParent;
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

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public String getRemarks()
    {
        return remarks;
    }

    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }

    public String getValueType()
    {
        return valueType;
    }

    public void setValueType(String valueType)
    {
        this.valueType = valueType;
    }

    public Object getElement()
    {
        return element;
    }

    public void setElement(Object element)
    {
        this.element = element;
    }
}
