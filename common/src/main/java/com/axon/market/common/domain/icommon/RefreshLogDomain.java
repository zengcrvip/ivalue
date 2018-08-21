package com.axon.market.common.domain.icommon;

/**
 * Created by Chris on 2016/11/29.
 */
public class RefreshLogDomain
{
    private Integer id;

    private String refreshTime;

    private String refreshType;

    private Integer refreshCount;

    private Integer refreshSuccessCount;

    private Integer refreshFailCount;

    private String refreshResult;

    private String refreshResultReason;

    private String remarks;

    public RefreshLogDomain(Integer id, String refreshType)
    {
        this.id = id;
        this.refreshType = refreshType;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getRefreshTime()
    {
        return refreshTime;
    }

    public void setRefreshTime(String refreshTime)
    {
        this.refreshTime = refreshTime;
    }

    public String getRefreshType()
    {
        return refreshType;
    }

    public void setRefreshType(String refreshType)
    {
        this.refreshType = refreshType;
    }

    public Integer getRefreshCount()
    {
        return refreshCount;
    }

    public void setRefreshCount(Integer refreshCount)
    {
        this.refreshCount = refreshCount;
    }

    public Integer getRefreshSuccessCount()
    {
        return refreshSuccessCount;
    }

    public void setRefreshSuccessCount(Integer refreshSuccessCount)
    {
        this.refreshSuccessCount = refreshSuccessCount;
    }

    public Integer getRefreshFailCount()
    {
        return refreshFailCount;
    }

    public void setRefreshFailCount(Integer refreshFailCount)
    {
        this.refreshFailCount = refreshFailCount;
    }

    public String getRefreshResult()
    {
        return refreshResult;
    }

    public void setRefreshResult(String refreshResult)
    {
        this.refreshResult = refreshResult;
    }

    public String getRefreshResultReason()
    {
        return refreshResultReason;
    }

    public void setRefreshResultReason(String refreshResultReason)
    {
        this.refreshResultReason = refreshResultReason;
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
