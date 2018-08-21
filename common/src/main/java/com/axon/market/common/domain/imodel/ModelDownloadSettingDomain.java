package com.axon.market.common.domain.imodel;

/**
 * Created by yuanfei on 2017/7/24.
 */
public class ModelDownloadSettingDomain
{
    private Integer id;

    private Integer modelId;

    private String modelName;

    private String metaPropertyIds;

    private String metaPropertyNames;

    private Integer createUser;

    private String createTime;

    private String updateTime;

    private String remarks;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getModelId()
    {
        return modelId;
    }

    public void setModelId(Integer modelId)
    {
        this.modelId = modelId;
    }

    public String getModelName()
    {
        return modelName;
    }

    public void setModelName(String modelName)
    {
        this.modelName = modelName;
    }

    public String getMetaPropertyIds()
    {
        return metaPropertyIds;
    }

    public void setMetaPropertyIds(String metaPropertyIds)
    {
        this.metaPropertyIds = metaPropertyIds;
    }

    public String getMetaPropertyNames()
    {
        return metaPropertyNames;
    }

    public void setMetaPropertyNames(String metaPropertyNames)
    {
        this.metaPropertyNames = metaPropertyNames;
    }

    public Integer getCreateUser()
    {
        return createUser;
    }

    public void setCreateUser(Integer createUser)
    {
        this.createUser = createUser;
    }

    public String getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }

    public String getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(String updateTime)
    {
        this.updateTime = updateTime;
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
