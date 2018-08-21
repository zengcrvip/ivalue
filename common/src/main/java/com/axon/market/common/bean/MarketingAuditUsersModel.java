package com.axon.market.common.bean;

/**
 * 审核权限关系
 * Created by gloomysw on 2017/02/27.
 */
public class MarketingAuditUsersModel
{

    // 排序
    private String order;

    // 审核人ID
    private String auditUser;

    // 审核人名称
    private String auditUserName;

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getAuditUser() {
        return auditUser;
    }

    public void setAuditUser(String auditUser) {
        this.auditUser = auditUser;
    }

    public String getAuditUserName() {
        return auditUserName;
    }

    public void setAuditUserName(String auditUserName) {
        this.auditUserName = auditUserName;
    }
}
