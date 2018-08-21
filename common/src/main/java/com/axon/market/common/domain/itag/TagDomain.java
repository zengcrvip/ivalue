package com.axon.market.common.domain.itag;

/**
 * Created by chenyu on 2016/1/27.
 */
public class TagDomain
{
    private Integer id;

    /**
     * 标签名
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
     * 对应GP schema名称
     */
    private String dbSchema;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 手机号对应列名
     */
    private String phoneColumnName;

    /**
     * 地区对应列名
     */
    private String areaColumnName;

    /**
     * 数据时间
     */
    private String dataTime;

    /**
     * 是否需要远程导入
     */
    private String needAutoFetch;

    /**
     * 远程服务器id
     */
    private Integer remoteServerId;

    /**
     * 远程服务器名称
     */
    private String remoteServerName;

    /**
     * 远程服务器上文件对应路径
     */
    private String remoteFile;

    /**
     * 执行时间
     */
    private String executeTime;

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
     * 状态
     */
    private Integer status;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 刷新结果
     */
    private String refreshResult;

    /**
     * 刷新结果失败原因
     */
    private String refreshResultReason;

    /**
     * 最后导入总数
     */
    private Integer lastRefreshTotalCount;

    /**
     * 最后导入成功数
     */
    private Integer lastRefreshSuccessCount;

    /**
     * 最后导入失败数
     */
    private Integer lastRefreshFailCount;

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

    public String getDbSchema()
    {
        return dbSchema;
    }

    public void setDbSchema(String dbSchema)
    {
        this.dbSchema = dbSchema;
    }

    public String getTableName()
    {
        return tableName;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    public String getPhoneColumnName()
    {
        return phoneColumnName;
    }

    public void setPhoneColumnName(String phoneColumnName)
    {
        this.phoneColumnName = phoneColumnName;
    }

    public String getAreaColumnName()
    {
        return areaColumnName;
    }

    public void setAreaColumnName(String areaColumnName)
    {
        this.areaColumnName = areaColumnName;
    }

    public String getDataTime()
    {
        return dataTime;
    }

    public void setDataTime(String dataTime)
    {
        this.dataTime = dataTime;
    }

    public String getNeedAutoFetch()
    {
        return needAutoFetch;
    }

    public void setNeedAutoFetch(String needAutoFetch)
    {
        this.needAutoFetch = needAutoFetch;
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

    public String getRefreshResult()
    {
        return refreshResult;
    }

    public void setRefreshResult(String refreshResult)
    {
        this.refreshResult = refreshResult;
    }

    public String getRefreshResultReason()
    {
        return refreshResultReason;
    }

    public void setRefreshResultReason(String refreshResultReason)
    {
        this.refreshResultReason = refreshResultReason;
    }

    public Integer getLastRefreshTotalCount()
    {
        return lastRefreshTotalCount;
    }

    public void setLastRefreshTotalCount(Integer lastRefreshTotalCount)
    {
        this.lastRefreshTotalCount = lastRefreshTotalCount;
    }

    public Integer getLastRefreshSuccessCount()
    {
        return lastRefreshSuccessCount;
    }

    public void setLastRefreshSuccessCount(Integer lastRefreshSuccessCount)
    {
        this.lastRefreshSuccessCount = lastRefreshSuccessCount;
    }

    public Integer getLastRefreshFailCount()
    {
        return lastRefreshFailCount;
    }

    public void setLastRefreshFailCount(Integer lastRefreshFailCount)
    {
        this.lastRefreshFailCount = lastRefreshFailCount;
    }
}
