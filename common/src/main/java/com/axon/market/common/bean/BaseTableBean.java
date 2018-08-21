package com.axon.market.common.bean;

import com.axon.market.common.util.SpringUtil;

/**
 * Created by yuanfei on 2017/7/28.
 */
public class BaseTableBean
{
    private String tableName;

    private String phoneColumn;

    private String areaColumn;

    private String userTypeColumn;

    public static BaseTableBean getInstance()
    {
        return (BaseTableBean) SpringUtil.getSingletonBean("baseTableBean");
    }

    public String getTableName()
    {
        return tableName;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    public String getPhoneColumn()
    {
        return phoneColumn;
    }

    public void setPhoneColumn(String phoneColumn)
    {
        this.phoneColumn = phoneColumn;
    }

    public String getAreaColumn()
    {
        return areaColumn;
    }

    public void setAreaColumn(String areaColumn)
    {
        this.areaColumn = areaColumn;
    }

    public String getUserTypeColumn()
    {
        return userTypeColumn;
    }

    public void setUserTypeColumn(String userTypeColumn)
    {
        this.userTypeColumn = userTypeColumn;
    }
}
