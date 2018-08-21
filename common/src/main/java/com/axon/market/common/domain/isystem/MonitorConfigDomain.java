package com.axon.market.common.domain.isystem;

import java.util.Date;

/**
 * Created by xuan on 2017/4/17.
 */
public class MonitorConfigDomain
{
    private Integer id;
    private String serverIp;
    private String port;
    private String serverAccount;
    private String serverPassWord;
    private int type;
    private String expected;
    private String format;
    private int timeTick;
    private String dataBaseName;
    private String messageContent;
    private int isDelete;
    private int createUserId;
    private Date createTime;
    private int updateUserId;
    private Date updateTime;
    private String emailPath;
    private String phonePath;
    private int emailCount;
    private int phoneCount;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getServerIp()
    {
        return serverIp;
    }

    public void setServerIp(String serverIp)
    {
        this.serverIp = serverIp;
    }

    public String getPort()
    {
        return port;
    }

    public void setPort(String port)
    {
        this.port = port;
    }

    public String getServerAccount()
    {
        return serverAccount;
    }

    public void setServerAccount(String serverAccount)
    {
        this.serverAccount = serverAccount;
    }

    public String getServerPassWord()
    {
        return serverPassWord;
    }

    public void setServerPassWord(String serverPassWord)
    {
        this.serverPassWord = serverPassWord;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public String getExpected()
    {
        return expected;
    }

    public void setExpected(String expected)
    {
        this.expected = expected;
    }

    public String getFormat()
    {
        return format;
    }

    public void setFormat(String format)
    {
        this.format = format;
    }

    public int getTimeTick()
    {
        return timeTick;
    }

    public void setTimeTick(int timeTick)
    {
        this.timeTick = timeTick;
    }

    public String getDataBaseName()
    {
        return dataBaseName;
    }

    public void setDataBaseName(String dataBaseName)
    {
        this.dataBaseName = dataBaseName;
    }

    public String getMessageContent()
    {
        return messageContent;
    }

    public void setMessageContent(String messageContent)
    {
        this.messageContent = messageContent;
    }

    public int getIsDelete()
    {
        return isDelete;
    }

    public void setIsDelete(int isDelete)
    {
        this.isDelete = isDelete;
    }

    public int getCreateUserId()
    {
        return createUserId;
    }

    public void setCreateUserId(int createUserId)
    {
        this.createUserId = createUserId;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public int getUpdateUserId()
    {
        return updateUserId;
    }

    public void setUpdateUserId(int updateUserId)
    {
        this.updateUserId = updateUserId;
    }

    public Date getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }

    public String getEmailPath()
    {
        return emailPath;
    }

    public void setEmailPath(String emailPath)
    {
        this.emailPath = emailPath;
    }

    public String getPhonePath()
    {
        return phonePath;
    }

    public void setPhonePath(String phonePath)
    {
        this.phonePath = phonePath;
    }

    public int getEmailCount()
    {
        return emailCount;
    }

    public void setEmailCount(int emailCount)
    {
        this.emailCount = emailCount;
    }

    public int getPhoneCount()
    {
        return phoneCount;
    }

    public void setPhoneCount(int phoneCount)
    {
        this.phoneCount = phoneCount;
    }
}
