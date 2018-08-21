package com.axon.market.common.domain.iscene;

import java.util.Date;

/**
 * Created by hale on 2016/12/5.
 */
public class BannedHostsDomain
{
    private Integer id;

    private String host;

    private Integer taskId;

    private Integer bannedType;

    private Integer bannedBy;

    private Date bannedAt;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getHost()
    {
        return host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public Integer getTaskId()
    {
        return taskId;
    }

    public void setTaskId(Integer taskId)
    {
        this.taskId = taskId;
    }

    public Integer getBannedType()
    {
        return bannedType;
    }

    public void setBannedType(Integer bannedType)
    {
        this.bannedType = bannedType;
    }

    public Integer getBannedBy()
    {
        return bannedBy;
    }

    public void setBannedBy(Integer bannedBy)
    {
        this.bannedBy = bannedBy;
    }

    public Date getBannedAt()
    {
        return bannedAt;
    }

    public void setBannedAt(Date bannedAt)
    {
        this.bannedAt = bannedAt;
    }
}

