package com.axon.market.common.domain.greenplum;

/**
 * Created by yangyang on 2016/1/25.
 */
public class TableColumnDomain
{
    private String columnName;

    private String dataType;

    private String remarks;

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
}
