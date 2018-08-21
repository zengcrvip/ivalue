package com.axon.market.common.bean;

/**
 * Created by Administrator on 2017/3/15.
 */
public class MenuResult
{
    private Integer id;
    private Integer sort;
    private Integer parent_id;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getSort()
    {
        return sort;
    }

    public void setSort(Integer sort)
    {
        this.sort = sort;
    }

    public Integer getParent_id()
    {
        return parent_id;
    }

    public void setParent_id(Integer parent_id)
    {
        this.parent_id = parent_id;
    }
}
