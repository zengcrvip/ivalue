package com.axon.market.common.domain.itag;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/27.
 */
public class DimensionDomain
{
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 维度值
     */
    private String value;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 状态
     */
    private Integer status;

    private List<Map<String,String>> dimensionList;

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

    public String getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public List<Map<String, String>> getDimensionList()
    {
        return dimensionList;
    }

    public void setDimensionList(List<Map<String, String>> dimensionList)
    {
        this.dimensionList = dimensionList;
    }
}
