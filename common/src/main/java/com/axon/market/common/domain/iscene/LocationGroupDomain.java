package com.axon.market.common.domain.iscene;

/**
 * Created by DELL on 2017/1/3.
 */
public class LocationGroupDomain
{
    private Integer id;
    private String name;
    private Integer count;
    private String tableName;
    private Integer isDelete;
    private Integer provinceId;
    private Integer createId;

    public LocationGroupDomain()
    {
    }

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

    public Integer getCount()
    {
        return count;
    }

    public void setCount(Integer count)
    {
        this.count = count;
    }

    public String getTableName()
    {
        return tableName;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    public Integer getIsDelete()
    {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete)
    {
        this.isDelete = isDelete;
    }

    public Integer getProvinceId()
    {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId)
    {
        this.provinceId = provinceId;
    }

    public Integer getCreateId()
    {
        return createId;
    }

    public void setCreateId(Integer createId)
    {
        this.createId = createId;
    }

    public LocationGroupDomain(Integer id, String name, Integer count, String tableName, Integer isDelete, Integer provinceId, Integer createId)
    {

        this.id = id;
        this.name = name;
        this.count = count;
        this.tableName = tableName;
        this.isDelete = isDelete;
        this.provinceId = provinceId;
        this.createId = createId;
    }
}
