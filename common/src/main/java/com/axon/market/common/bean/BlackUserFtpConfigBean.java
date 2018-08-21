package com.axon.market.common.bean;

import com.axon.market.common.util.SpringUtil;

/**
 * Created by yuanfei on 2017/7/6.
 */
public class BlackUserFtpConfigBean
{
    /**
     * ip
     */
    private String serviceIP;

    /**
     * 端口号
     */
    private Integer servicePort;

    /**
     * 用户名
     */
    private String serviceUser;

    /**
     * 密码
     */
    private String servicePassword;

    /**
     * ftp下载文件路径
     */
    private String servicePath;

    public static BlackUserFtpConfigBean getInstance()
    {
        return (BlackUserFtpConfigBean) SpringUtil.getSingletonBean("blackUserFtpConfigBean");
    }

    public String getServiceIP()
    {
        return serviceIP;
    }

    public void setServiceIP(String serviceIP)
    {
        this.serviceIP = serviceIP;
    }

    public Integer getServicePort()
    {
        return servicePort;
    }

    public void setServicePort(Integer servicePort)
    {
        this.servicePort = servicePort;
    }

    public String getServiceUser()
    {
        return serviceUser;
    }

    public void setServiceUser(String serviceUser)
    {
        this.serviceUser = serviceUser;
    }

    public String getServicePassword()
    {
        return servicePassword;
    }

    public void setServicePassword(String servicePassword)
    {
        this.servicePassword = servicePassword;
    }

    public String getServicePath()
    {
        return servicePath;
    }

    public void setServicePath(String servicePath)
    {
        this.servicePath = servicePath;
    }
}
