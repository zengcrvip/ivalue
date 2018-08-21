package com.axon.market.common.domain.isystem;

import org.omg.CORBA.PRIVATE_MEMBER;

/**
 * Created by xuan on 2017/4/24.
 */
public class SyncConfigDomain
{
    private Integer id;
    private String mysqlDbName;
    private String mysqlTableName;
    private String gpDbName;
    private int frequency;
    private String gpTableName;
    private  int syncType;
    private String syncField;
    private String syncFieldStr;
    private String ftpName;
    private String delimit;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getMysqlDbName()
    {
        return mysqlDbName;
    }

    public void setMysqlDbName(String mysqlDbName)
    {
        this.mysqlDbName = mysqlDbName;
    }

    public String getMysqlTableName()
    {
        return mysqlTableName;
    }

    public void setMysqlTableName(String mysqlTableName)
    {
        this.mysqlTableName = mysqlTableName;
    }

    public String getGpDbName()
    {
        return gpDbName;
    }

    public void setGpDbName(String gpDbName)
    {
        this.gpDbName = gpDbName;
    }

    public int getFrequency()
    {
        return frequency;
    }

    public void setFrequency(int frequency)
    {
        this.frequency = frequency;
    }

    public String getGpTableName()
    {
        return gpTableName;
    }

    public void setGpTableName(String gpTableName)
    {
        this.gpTableName = gpTableName;
    }

    public int getSyncType()
    {
        return syncType;
    }

    public void setSyncType(int syncType)
    {
        this.syncType = syncType;
    }

    public String getSyncField()
    {
        return syncField;
    }

    public void setSyncField(String syncField)
    {
        this.syncField = syncField;
    }

    public String getSyncFieldStr()
    {
        return syncFieldStr;
    }

    public void setSyncFieldStr(String syncFieldStr)
    {
        this.syncFieldStr = syncFieldStr;
    }

    public String getFtpName()
    {
        return ftpName;
    }

    public void setFtpName(String ftpName)
    {
        this.ftpName = ftpName;
    }

    public String getDelimit()
    {
        return delimit;
    }

    public void setDelimit(String delimit)
    {
        this.delimit = delimit;
    }
}
