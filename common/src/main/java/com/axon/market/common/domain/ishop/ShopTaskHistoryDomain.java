package com.axon.market.common.domain.ishop;

/**
 * 炒店任务营销历史表
 * Created by zengcr on 2017/2/20.
 */
public class ShopTaskHistoryDomain
{
    //主键ID
    private Integer id;
    //炒店任务名称
    private String taskName;
    //营销执行人
    private String executeUser;
    //营销用户
    private Integer marketUser;
    //任务开始时间
    private String startTime;
    //任务结束时间
    private String stopTime;
   //营销状态
    private String marketStatus;
    //营销人数
    private Integer marketUserCounts;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getTaskName()
    {
        return taskName;
    }

    public void setTaskName(String taskName)
    {
        this.taskName = taskName;
    }

    public String getExecuteUser()
    {
        return executeUser;
    }

    public void setExecuteUser(String executeUser)
    {
        this.executeUser = executeUser;
    }

    public Integer getMarketUser()
    {
        return marketUser;
    }

    public void setMarketUser(Integer marketUser)
    {
        this.marketUser = marketUser;
    }

    public String getStartTime()
    {
        return startTime;
    }

    public void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }

    public String getStopTime()
    {
        return stopTime;
    }

    public void setStopTime(String stopTime)
    {
        this.stopTime = stopTime;
    }

    public String getMarketStatus()
    {
        return marketStatus;
    }

    public void setMarketStatus(String marketStatus)
    {
        this.marketStatus = marketStatus;
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
