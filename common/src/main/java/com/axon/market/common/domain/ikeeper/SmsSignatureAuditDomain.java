package com.axon.market.common.domain.ikeeper;

/**
 * Created by yuanfei on 2017/8/22.
 */
public class SmsSignatureAuditDomain
{
    private Integer id;

    /**
     * 归属用户，提交人
     */
    private Integer userId;

    /**
     * 当前正在使用的短信签名
     */
    private String currentSmsSignature;

    /**
     * 待审批的短信签名
     */
    private String auditingSmsSignature;

    /**
     * 状态： 1：待审批，2：审批通过，3：审批拒绝
     */
    private Integer state;

    private String createTime;

    /**
     * 审批结果
     */
    private String auditResultDesc;

    /**
     * 审批时间
     */
    private String auditTime;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getUserId()
    {
        return userId;
    }

    public void setUserId(Integer userId)
    {
        this.userId = userId;
    }

    public String getCurrentSmsSignature()
    {
        return currentSmsSignature;
    }

    public void setCurrentSmsSignature(String currentSmsSignature)
    {
        this.currentSmsSignature = currentSmsSignature;
    }

    public String getAuditingSmsSignature()
    {
        return auditingSmsSignature;
    }

    public void setAuditingSmsSignature(String auditingSmsSignature)
    {
        this.auditingSmsSignature = auditingSmsSignature;
    }

    public Integer getState()
    {
        return state;
    }

    public void setState(Integer state)
    {
        this.state = state;
    }

    public String getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }

    public String getAuditResultDesc()
    {
        return auditResultDesc;
    }

    public void setAuditResultDesc(String auditResultDesc)
    {
        this.auditResultDesc = auditResultDesc;
    }

    public String getAuditTime()
    {
        return auditTime;
    }

    public void setAuditTime(String auditTime)
    {
        this.auditTime = auditTime;
    }
}
