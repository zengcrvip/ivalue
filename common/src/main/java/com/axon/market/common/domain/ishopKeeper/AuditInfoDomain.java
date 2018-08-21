package com.axon.market.common.domain.ishopKeeper;

/**
 * Created by Duzm on 2017/8/8.
 */
public class AuditInfoDomain {

    private Integer auditId;

    private String auditType;

    private Integer auditTypeInstId;

    private Integer parentAuditId;

    private String segment;

    private Integer createUserId;

    private String createUserName;

    private String createDate;

    private Integer auditUserId;

    private String auditUserName;

    private String auditDate;

    private String auditResult;

    private String comments;

    public Integer getAuditId() {
        return auditId;
    }

    public String getAuditType() {
        return auditType;
    }

    public Integer getAuditTypeInstId() {
        return auditTypeInstId;
    }

    public Integer getParentAuditId() {
        return parentAuditId;
    }

    public String getSegment() {
        return segment;
    }


    public String getCreateDate() {
        return createDate;
    }


    public String getAuditDate() {
        return auditDate;
    }

    public String getAuditResult() {
        return auditResult;
    }

    public String getComments() {
        return comments;
    }

    public void setAuditId(Integer auditId) {
        this.auditId = auditId;
    }

    public void setAuditType(String auditType) {
        this.auditType = auditType;
    }

    public void setAuditTypeInstId(Integer auditTypeInstId) {
        this.auditTypeInstId = auditTypeInstId;
    }

    public void setParentAuditId(Integer parentAuditId) {
        this.parentAuditId = parentAuditId;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }


    public void setAuditDate(String auditDate) {
        this.auditDate = auditDate;
    }

    public void setAuditResult(String auditResult) {
        this.auditResult = auditResult;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Integer getCreateUserId() {
        return createUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public Integer getAuditUserId() {
        return auditUserId;
    }

    public String getAuditUserName() {
        return auditUserName;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public void setAuditUserId(Integer auditUserId) {
        this.auditUserId = auditUserId;
    }

    public void setAuditUserName(String auditUserName) {
        this.auditUserName = auditUserName;
    }
}
