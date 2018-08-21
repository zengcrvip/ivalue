package com.axon.market.common.domain.isystem;

import java.util.Date;

/**
 * Created by xuan on 2017/4/19.
 */
public class MonitorStatisticsDomain
{
    private Integer id;
    private Integer monitorConfigId;
    private String serverIp;
    private Integer status;
    private String remark;
    private Integer isDelete;
    private Date createTime;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getMonitorConfigId()
    {
        return monitorConfigId;
    }

    public void setMonitorConfigId(Integer monitorConfigId)
    {
        this.monitorConfigId = monitorConfigId;
    }

    public String getServerIp()
    {
        return serverIp;
    }

    public void setServerIp(String serverIp)
    {
        this.serverIp = serverIp;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public Integer getIsDelete()
    {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete)
    {
        this.isDelete = isDelete;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }
}
