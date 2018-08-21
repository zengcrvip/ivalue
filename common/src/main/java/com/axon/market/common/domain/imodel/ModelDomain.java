package com.axon.market.common.domain.imodel;

/**
 * Created by yangyang on 2016/1/11.
 */
public class ModelDomain
{
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 目录id
     */
    private String catalogId;

    /**
     * 目录名称
     */
    private String catalogName;

    /**
     * 创建类型（rule，localImport，remoteImport）
     */
    private String createType;

    /**
     * 规则创建对应规则
     */
    private String rule;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 指定角色ids
     */
    private String specifiedRoleIds;

    /**
     * 指定角色名称
     */
    private String specifiedRoleNames;

    /**
     * 是否需要发送通知短信
     */
    private Integer isNeedSendNotifySms;

    /**
     * 远程服务器id
     */
    private Integer remoteServerId;

    /**
     * 远程服务器名称
     */
    private String remoteServerName;

    /**
     * 远程服务器文件
     */
    private String remoteFile;

    /**
     * 执行时间
     */
    private String executeTime;

    /**
     * 是否需要删除服务器上文件
     */
    private Integer isNeedDelete;

    /**
     * 间隔时间
     */
    private String intervalTime;

    /**
     * 创建人
     */
    private Integer createUser;

    /**
     * 创建人名称
     */
    private String createUserName;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 最后更新人
     */
    private Integer lastUpdateUser;

    /**
     * 最后更新时间
     */
    private String lastUpdateTime;

    /**
     * 最后刷新时间
     */
    private String lastRefreshTime;

    /**
     * 最后刷新成功时间
     */
    private String lastRefreshSuccessTime;

    /**
     * 最后刷新成功人数
     */
    private Integer lastRefreshCount;

    /**
     * 备注
     */
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

    public String getCatalogId()
    {
        return catalogId;
    }

    public void setCatalogId(String catalogId)
    {
        this.catalogId = catalogId;
    }

    public String getCatalogName()
    {
        return catalogName;
    }

    public void setCatalogName(String catalogName)
    {
        this.catalogName = catalogName;
    }

    public String getCreateType()
    {
        return createType;
    }

    public void setCreateType(String createType)
    {
        this.createType = createType;
    }

    public String getRule()
    {
        return rule;
    }

    public void setRule(String rule)
    {
        this.rule = rule;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public String getSpecifiedRoleIds()
    {
        return specifiedRoleIds;
    }

    public void setSpecifiedRoleIds(String specifiedRoleIds)
    {
        this.specifiedRoleIds = specifiedRoleIds;
    }

    public String getSpecifiedRoleNames()
    {
        return specifiedRoleNames;
    }

    public void setSpecifiedRoleNames(String specifiedRoleNames)
    {
        this.specifiedRoleNames = specifiedRoleNames;
    }

    public Integer getIsNeedSendNotifySms()
    {
        return isNeedSendNotifySms;
    }

    public void setIsNeedSendNotifySms(Integer isNeedSendNotifySms)
    {
        this.isNeedSendNotifySms = isNeedSendNotifySms;
    }

    public Integer getRemoteServerId()
    {
        return remoteServerId;
    }

    public void setRemoteServerId(Integer remoteServerId)
    {
        this.remoteServerId = remoteServerId;
    }

    public String getRemoteServerName()
    {
        return remoteServerName;
    }

    public void setRemoteServerName(String remoteServerName)
    {
        this.remoteServerName = remoteServerName;
    }

    public String getRemoteFile()
    {
        return remoteFile;
    }

    public void setRemoteFile(String remoteFile)
    {
        this.remoteFile = remoteFile;
    }

    public String getExecuteTime()
    {
        return executeTime;
    }

    public void setExecuteTime(String executeTime)
    {
        this.executeTime = executeTime;
    }

    public Integer getIsNeedDelete()
    {
        return isNeedDelete;
    }

    public void setIsNeedDelete(Integer isNeedDelete)
    {
        this.isNeedDelete = isNeedDelete;
    }

    public String getIntervalTime()
    {
        return intervalTime;
    }

    public void setIntervalTime(String intervalTime)
    {
        this.intervalTime = intervalTime;
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

    public Integer getLastUpdateUser()
    {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(Integer lastUpdateUser)
    {
        this.lastUpdateUser = lastUpdateUser;
    }

    public String getLastUpdateTime()
    {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime)
    {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getLastRefreshTime()
    {
        return lastRefreshTime;
    }

    public void setLastRefreshTime(String lastRefreshTime)
    {
        this.lastRefreshTime = lastRefreshTime;
    }

    public String getLastRefreshSuccessTime()
    {
        return lastRefreshSuccessTime;
    }

    public void setLastRefreshSuccessTime(String lastRefreshSuccessTime)
    {
        this.lastRefreshSuccessTime = lastRefreshSuccessTime;
    }

    public Integer getLastRefreshCount()
    {
        return lastRefreshCount;
    }

    public void setLastRefreshCount(Integer lastRefreshCount)
    {
        this.lastRefreshCount = lastRefreshCount;
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
