package com.axon.market.common.domain.iStatistics;

/**
 * Created by Chris on 2017/7/24.
 */
public class ProductOrderStatisticsDomain
{
    private Integer id;

    private String name;

    private String value;

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

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
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
