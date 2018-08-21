package com.axon.market.common.domain.itag;

/**
 * Created by yangyang on 2016/1/27.
 */
public class PropertyDomain
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
     * 表名称
     */
    private String tableName;

    /**
     * 列名称
     */
    private String columnName;

    /**
     * 列类型
     */
    private String valueType;

    /**
     * 维度id
     */
    private Integer dimensionId;

    /**
     * 维度名称
     */
    private String dimensionName;

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

    public String getTableName()
    {
        return tableName;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    public String getColumnName()
    {
        return columnName;
    }

    public void setColumnName(String columnName)
    {
        this.columnName = columnName;
    }

    public String getValueType()
    {
        return valueType;
    }

    public void setValueType(String valueType)
    {
        this.valueType = valueType;
    }

    public Integer getDimensionId()
    {
        return dimensionId;
    }

    public void setDimensionId(Integer dimensionId)
    {
        this.dimensionId = dimensionId;
    }

    public String getDimensionName()
    {
        return dimensionName;
    }

    public void setDimensionName(String dimensionName)
    {
        this.dimensionName = dimensionName;
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
}
