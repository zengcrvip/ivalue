package com.axon.market.common.domain.isystem;

/**
 * Created by xuan on 2017/5/4.
 */
public class MonitorConfigPhoneDomain
{
    private Integer id;
    private String phone;
    private Integer monitorSyncinfoId;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
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
