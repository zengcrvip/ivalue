package com.axon.market.common.domain.isystem;

/**
 * Created by chenyu on 2016/5/25.
 */
public class ModelAuditHistoryDomain
{
    private Integer modelId;

    private Integer auditUser;

    private String auditUserName;

    private String auditResult;

    private String remarks;

    private String auditTime;

    private String auditUsers;

    public Integer getModelId()
    {
        return modelId;
    }

    public void setModelId(Integer modelId)
    {
        this.modelId = modelId;
    }

    public Integer getAuditUser()
    {
        return auditUser;
    }

    public void setAuditUser(Integer auditUser)
    {
        this.auditUser = auditUser;
    }

    public String getAuditUserName()
    {
        return auditUserName;
    }

    public void setAuditUserName(String auditUserName)
    {
        this.auditUserName = auditUserName;
    }

    public String getAuditResult()
    {
        return auditResult;
    }

    public void setAuditResult(String auditResult)
    {
        this.auditResult = auditResult;
    }

    public String getRemarks()
    {
        return remarks;
    }

    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }

    public String getAuditTime()
    {
        return auditTime;
    }

    public void setAuditTime(String auditTime)
    {
        this.auditTime = auditTime;
    }

    public String getAuditUsers()
    {
        return auditUsers;
    }

    public void setAuditUsers(String auditUsers)
    {
        this.auditUsers = auditUsers;
    }

}
