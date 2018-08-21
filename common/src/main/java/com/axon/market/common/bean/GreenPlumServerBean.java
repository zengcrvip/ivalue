package com.axon.market.common.bean;

import com.axon.market.common.util.SpringUtil;

/**
 * Created by chenyu on 2016/6/13.
 */
public class GreenPlumServerBean
{
    private String gpServer;

    private String gpServerUser;

    private String gpServerPassword;

    private int gpServerPort;

    private String gpDataFilePath;

    private char gpDelimiterChar;

    private String gpCurrentSchemaName;

    private int gpBatchCopyCount;

    private String gpShopFullTableName;

    public static GreenPlumServerBean getInstance()
    {
        return (GreenPlumServerBean) SpringUtil.getSingletonBean("greenPlumServerBean");
    }

    public String getGpServer()
    {
        return gpServer;
    }

    public void setGpServer(String gpServer)
    {
        this.gpServer = gpServer;
    }

    public String getGpServerUser()
    {
        return gpServerUser;
    }

    public void setGpServerUser(String gpServerUser)
    {
        this.gpServerUser = gpServerUser;
    }

    public String getGpServerPassword()
    {
        return gpServerPassword;
    }

    public void setGpServerPassword(String gpServerPassword)
    {
        this.gpServerPassword = gpServerPassword;
    }

    public int getGpServerPort()
    {
        return gpServerPort;
    }

    public void setGpServerPort(int gpServerPort)
    {
        this.gpServerPort = gpServerPort;
    }

    public String getGpDataFilePath()
    {
        if (!gpDataFilePath.endsWith("/"))
        {
            return gpDataFilePath + "/";
        }
        return gpDataFilePath;
    }

    public void setGpDataFilePath(String gpDataFilePath)
    {
        this.gpDataFilePath = gpDataFilePath;
    }

    public char getGpDelimiterChar()
    {
        return gpDelimiterChar;
    }

    public void setGpDelimiterChar(char gpDelimiterChar)
    {
        this.gpDelimiterChar = gpDelimiterChar;
    }

    public String getGpCurrentSchemaName()
    {
        return gpCurrentSchemaName;
    }

    public void setGpCurrentSchemaName(String gpCurrentSchemaName)
    {
        this.gpCurrentSchemaName = gpCurrentSchemaName;
    }

    public int getGpBatchCopyCount()
    {
        if (gpBatchCopyCount == 0)
        {
            return 1000000;
        }
        return gpBatchCopyCount;
    }

    public void setGpBatchCopyCount(int gpBatchCopyCount)
    {
        this.gpBatchCopyCount = gpBatchCopyCount;
    }

    public String getGpShopFullTableName()
    {
        return gpShopFullTableName;
    }

    public void setGpShopFullTableName(String gpShopFullTableName)
    {
        this.gpShopFullTableName = gpShopFullTableName;
    }
}
