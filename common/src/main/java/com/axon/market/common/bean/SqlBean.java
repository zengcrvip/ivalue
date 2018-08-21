package com.axon.market.common.bean;

import com.axon.market.common.util.SpringUtil;

/**
 * Created by chenyu on 2017/3/13.
 */
public class SqlBean
{
    private String truncateShopUserSql;

    private String truncateShopBaseSql;

    private String insertShopBaseSql;

    private String truncateShopTargetSql;

    private String backUpShopRecommendationExecuteSql;

    private String truncateShopRecommendationExecuteSql;

    public static SqlBean getInstance()
    {
        return (SqlBean) SpringUtil.getSingletonBean("sqlBean");
    }

    public String getTruncateShopUserSql()
    {
        return truncateShopUserSql;
    }

    public void setTruncateShopUserSql(String truncateShopUserSql)
    {
        this.truncateShopUserSql = truncateShopUserSql;
    }

    public String getTruncateShopBaseSql()
    {
        return truncateShopBaseSql;
    }

    public void setTruncateShopBaseSql(String truncateShopBaseSql)
    {
        this.truncateShopBaseSql = truncateShopBaseSql;
    }

    public String getInsertShopBaseSql()
    {
        return insertShopBaseSql;
    }

    public void setInsertShopBaseSql(String insertShopBaseSql)
    {
        this.insertShopBaseSql = insertShopBaseSql;
    }

    public String getTruncateShopTargetSql()
    {
        return truncateShopTargetSql;
    }

    public void setTruncateShopTargetSql(String truncateShopTargetSql)
    {
        this.truncateShopTargetSql = truncateShopTargetSql;
    }

    public String getBackUpShopRecommendationExecuteSql()
    {
        return backUpShopRecommendationExecuteSql;
    }

    public void setBackUpShopRecommendationExecuteSql(String backUpShopRecommendationExecuteSql)
    {
        this.backUpShopRecommendationExecuteSql = backUpShopRecommendationExecuteSql;
    }

    public String getTruncateShopRecommendationExecuteSql()
    {
        return truncateShopRecommendationExecuteSql;
    }

    public void setTruncateShopRecommendationExecuteSql(String truncateShopRecommendationExecuteSql)
    {
        this.truncateShopRecommendationExecuteSql = truncateShopRecommendationExecuteSql;
    }
}
