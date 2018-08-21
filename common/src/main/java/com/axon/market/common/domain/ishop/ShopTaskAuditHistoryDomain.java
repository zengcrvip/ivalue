package com.axon.market.common.domain.ishop;

/**
 * 炒店任务审批历史表
 * Created by zengcr on 2017/2/17.
 */
public class ShopTaskAuditHistoryDomain
{
    //炒店任务ID
    private Integer taskId;
    //审核用户ID
    private Integer auditUser;
    //审核用户名称
    private String auditUserName;
    //审核结果
    private String auditResult;
    //备注
    private String remarks;

    private String auditTime;

    private String auditUsers;

    public Integer getTaskId()
    {
        return taskId;
    }

    public void setTaskId(Integer taskId)
    {
        this.taskId = taskId;
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
