package com.axon.market.common.domain.itag;

/**
 * Created by chenyu on 2016/6/6.
 */
public class RemoteServerDomain
{
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 连接方式（FTP，SFTP）
     */
    private String connectType;

    /**
     * 远程服务器id
     */
    private String serverIp;

    /**
     * 远程服务器登录用户名
     */
    private String serverUser;

    /**
     * 远程服务器登录密码
     */
    private String password;

    /**
     * 远程服务器连接端口
     */
    private Integer port;

    /**
     * 创建人
     */
    private Integer createUser;

    /**
     * 创建人姓名
     */
    private String createUserName;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新人
     */
    private Integer updateUser;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 备注
     */
    private String remarks;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getConnectType()
    {
        return connectType;
    }

    public void setConnectType(String connectType)
    {
        this.connectType = connectType;
    }

    public String getServerIp()
    {
        return serverIp;
    }

    public void setServerIp(String serverIp)
    {
        this.serverIp = serverIp;
    }

    public String getServerUser()
    {
        return serverUser;
    }

    public void setServerUser(String serverUser)
    {
        this.serverUser = serverUser;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public Integer getPort()
    {
        return port;
    }

    public void setPort(Integer port)
    {
        this.port = port;
    }

    public Integer getCreateUser()
    {
        return createUser;
    }

    public void setCreateUser(Integer createUser)
    {
        this.createUser = createUser;
    }

    public String getCreateUserName()
    {
        return createUserName;
    }

    public void setCreateUserName(String createUserName)
    {
        this.createUserName = createUserName;
    }

    public String getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }

    public Integer getUpdateUser()
    {
        return updateUser;
    }

    public void setUpdateUser(Integer updateUser)
    {
        this.updateUser = updateUser;
    }

    public String getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(String updateTime)
    {
        this.updateTime = updateTime;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
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
