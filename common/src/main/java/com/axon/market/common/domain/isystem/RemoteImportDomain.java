package com.axon.market.common.domain.isystem;

/**
 * Created by Administrator on 2016/6/7.
 */
public class RemoteImportDomain
{
    private String remoteServerId;

    private String remoteFile;

    private String executeTime;

    private String needDelete;

    private Integer intervalInSeconds;

    public String getRemoteServerId()
    {
        return remoteServerId;
    }

    public void setRemoteServerId(String remoteServerId)
    {
        this.remoteServerId = remoteServerId;
    }

    public String getRemoteFile()
    {
        return remoteFile;
    }

    public void setRemoteFile(String remoteFile)
    {
        this.remoteFile = remoteFile;
    }

    public String getExecuteTime()
    {
        return executeTime;
    }

    public void setExecuteTime(String executeTime)
    {
        this.executeTime = executeTime;
    }

    public String getNeedDelete()
    {
        return needDelete;
    }

    public void setNeedDelete(String needDelete)
    {
        this.needDelete = needDelete;
    }

    public Integer getIntervalInSeconds()
    {
        return intervalInSeconds;
    }

    public void setIntervalInSeconds(Integer intervalInSeconds)
    {
        this.intervalInSeconds = intervalInSeconds;
    }

    @Override
    public String toString()
    {
        return "RemoteImportDomain{" +
                "remoteServerId='" + remoteServerId + '\'' +
                ", remoteFile='" + remoteFile + '\'' +
                ", executeTime='" + executeTime + '\'' +
                ", needDelete='" + needDelete + '\'' +
                ", intervalInSeconds=" + intervalInSeconds +
                '}';
    }
}
