package com.axon.market.common.domain.iscene;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * Created by Administrator on 2016/10/24.
 */
public class SmsSendConfigDomain
{
    private Integer id;

    /**
     * 接入号
     */
    private String accessNumber;

    /**
     * 发送时间段：开始时间
     */
//    @DateTimeFormat(pattern = "HH:mm")
    private String sendBeginTime;

    /**
     * 发送时间段：结束时间
     */
//    @DateTimeFormat(pattern = "HH:mm")
    private String sendEndTime;

    /**
     * 暂停发送时间段：开始时间
     */
//    @DateTimeFormat(pattern = "HH:mm")
    private String sendPauseBeginTime;

    /**
     * 暂停发送时间段：结束时间
     */
//    @DateTimeFormat(pattern = "HH:mm")
    private String sendPauseEndTime;

    /**
     * 网关Ip
     */
    private String ip;

    /**
     * 端口号
     */
    private Integer port;

    private String loginName;

    private String loginPassword;

    /**
     * 企业代码
     */
    private String companyCode;

    /**
     * 业务代码
     */
    private String serviceType;

    /**
     * 节点编号
     */
    private Long nodeId;

    /**
     * 反馈报告标识
     */
    private Integer reportFlag;

    /**
     * 同一用户两条短信发送间隔时间
     */
    private Integer sameUserSendInterval;

    /**
     * 不同用户短信发送间隔时间
     */
    private Integer differentUserSendInterval;

    /**
     * 0:未删除
     * 1:已删除
     */
    private String status;

    private String createTime;

    private String updateTime;

    private Integer createUser;

    private Integer updateUser;

    private String updateUserName;

    private Integer areaCode;

    public Integer getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(Integer areaCode) {
        this.areaCode = areaCode;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getAccessNumber()
    {
        return accessNumber;
    }

    public void setAccessNumber(String accessNumber)
    {
        this.accessNumber = accessNumber;
    }

    public String getSendBeginTime()
    {
        return sendBeginTime;
    }

    public void setSendBeginTime(String sendBeginTime)
    {
        this.sendBeginTime = sendBeginTime;
    }

    public String getSendEndTime()
    {
        return sendEndTime;
    }

    public void setSendEndTime(String sendEndTime)
    {
        this.sendEndTime = sendEndTime;
    }

    public String getSendPauseBeginTime()
    {
        return sendPauseBeginTime;
    }

    public void setSendPauseBeginTime(String sendPauseBeginTime)
    {
        this.sendPauseBeginTime = sendPauseBeginTime;
    }

    public String getSendPauseEndTime()
    {
        return sendPauseEndTime;
    }

    public void setSendPauseEndTime(String sendPauseEndTime)
    {
        this.sendPauseEndTime = sendPauseEndTime;
    }

    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public Integer getPort()
    {
        return port;
    }

    public void setPort(Integer port)
    {
        this.port = port;
    }

    public String getLoginName()
    {
        return loginName;
    }

    public void setLoginName(String loginName)
    {
        this.loginName = loginName;
    }

    public String getLoginPassword()
    {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword)
    {
        this.loginPassword = loginPassword;
    }

    public String getCompanyCode()
    {
        return companyCode;
    }

    public void setCompanyCode(String companyCode)
    {
        this.companyCode = companyCode;
    }

    public String getServiceType()
    {
        return serviceType;
    }

    public void setServiceType(String serviceType)
    {
        this.serviceType = serviceType;
    }

    public Long getNodeId()
    {
        return nodeId;
    }

    public void setNodeId(Long nodeId)
    {
        this.nodeId = nodeId;
    }

    public Integer getReportFlag()
    {
        return reportFlag;
    }

    public void setReportFlag(Integer reportFlag)
    {
        this.reportFlag = reportFlag;
    }

    public Integer getSameUserSendInterval()
    {
        return sameUserSendInterval;
    }

    public void setSameUserSendInterval(Integer sameUserSendInterval)
    {
        this.sameUserSendInterval = sameUserSendInterval;
    }

    public Integer getDifferentUserSendInterval()
    {
        return differentUserSendInterval;
    }

    public void setDifferentUserSendInterval(Integer differentUserSendInterval)
    {
        this.differentUserSendInterval = differentUserSendInterval;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }

    public String getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(String updateTime)
    {
        this.updateTime = updateTime;
    }

    public Integer getCreateUser()
    {
        return createUser;
    }

    public void setCreateUser(Integer createUser)
    {
        this.createUser = createUser;
    }

    public Integer getUpdateUser()
    {
        return updateUser;
    }

    public void setUpdateUser(Integer updateUser)
    {
        this.updateUser = updateUser;
    }

    public String getUpdateUserName()
    {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName)
    {
        this.updateUserName = updateUserName;
    }
}
