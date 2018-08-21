package com.axon.market.common.domain.ischeduling;

/**
 * Created by yangyang on 2016/2/2.
 */
public class MarketHistoryDomain
{
    private Integer marketJobId;

    private String marketJobName;

    private String marketUser;

    private String marketType;

    private String marketStartTime;

    private String marketEndTime;

    private String marketStatus;

    private String marketSegments;

    private Integer marketUserCounts;

    public Integer getMarketJobId()
    {
        return marketJobId;
    }

    public void setMarketJobId(Integer marketJobId)
    {
        this.marketJobId = marketJobId;
    }

    public String getMarketJobName()
    {
        return marketJobName;
    }

    public void setMarketJobName(String marketJobName)
    {
        this.marketJobName = marketJobName;
    }

    public String getMarketUser()
    {
        return marketUser;
    }

    public void setMarketUser(String marketUser)
    {
        this.marketUser = marketUser;
    }

    public String getMarketType()
    {
        return marketType;
    }

    public void setMarketType(String marketType)
    {
        this.marketType = marketType;
    }

    public String getMarketStartTime()
    {
        return marketStartTime;
    }

    public void setMarketStartTime(String marketStartTime)
    {
        this.marketStartTime = marketStartTime;
    }

    public String getMarketEndTime()
    {
        return marketEndTime;
    }

    public void setMarketEndTime(String marketEndTime)
    {
        this.marketEndTime = marketEndTime;
    }

    public String getMarketStatus()
    {
        return marketStatus;
    }

    public void setMarketStatus(String marketStatus)
    {
        this.marketStatus = marketStatus;
    }

    public String getMarketSegments()
    {
        return marketSegments;
    }

    public void setMarketSegments(String marketSegments)
    {
        this.marketSegments = marketSegments;
    }

    public Integer getMarketUserCounts()
    {
        return marketUserCounts;
    }

    public void setMarketUserCounts(Integer marketUserCounts)
    {
        this.marketUserCounts = marketUserCounts;
    }
}
