package com.axon.market.common.domain.isystem;

/**
 * Created by xuan on 2017/5/4.
 */
public class MonitorConfigEmailDomain
{
    private Integer id;
    private String email;
    private Integer monitorSyncinfoId;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public Integer getMonitorSyncinfoId()
    {
        return monitorSyncinfoId;
    }

    public void setMonitorSyncinfoId(Integer monitorSyncinfoId)
    {
        this.monitorSyncinfoId = monitorSyncinfoId;
    }
}
