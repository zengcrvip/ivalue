package com.axon.market.common.domain.iscene;

/**
 * Created by DELL on 2016/12/23.
 */
public class UrlGroupDomain
{
    private Integer id;
    private String name;
    private Integer count;
    private Integer isDelete;
    private Integer provinceId;
    private Integer createId;
    public UrlGroupDomain()
    {
    }

    public UrlGroupDomain(Integer id, String name, Integer count, Integer isDelete, Integer provinceId, Integer createId)
    {
        this.id = id;
        this.name = name;
        this.count = count;
        this.isDelete = isDelete;
        this.provinceId = provinceId;
        this.createId = createId;
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
}
