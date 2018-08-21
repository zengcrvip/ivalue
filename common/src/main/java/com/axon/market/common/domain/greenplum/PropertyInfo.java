package com.axon.market.common.domain.greenplum;

import com.axon.market.common.domain.icommon.IdAndNameDomain;

import java.util.List;

/**
 * Created by Administrator on 2017/1/19.
 */
public class PropertyInfo
{
    private Integer id;

    private String columnName;

    private String dataType;

    private List<IdAndNameDomain> select;

    private String remarks;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getColumnName()
    {
        return columnName;
    }

    public void setColumnName(String columnName)
    {
        this.columnName = columnName;
    }

    public String getDataType()
    {
        return dataType;
    }

    public void setDataType(String dataType)
    {
        this.dataType = dataType;
    }

    public String getRemarks()
    {
        return remarks;
    }

    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }

    public List<IdAndNameDomain> getSelect()
    {
        return select;
    }

    public void setSelect(List<IdAndNameDomain> select)
    {
        this.select = select;
    }
}
