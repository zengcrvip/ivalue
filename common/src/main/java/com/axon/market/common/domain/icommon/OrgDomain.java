package com.axon.market.common.domain.icommon;

import java.sql.Date;

/**
 * Created by Duzm on 2017/8/1.
 */
public class OrgDomain
{

    private Integer orgId;

    private Integer orgTypeId;

    private String orgName;

    private Integer parentId;

    private String parentOrgName;

    private Date createDate;

    private String state;

    private String comments;

    public void setOrgId(Integer orgId)
    {
        this.orgId = orgId;
    }

    public void setOrgTypeId(Integer orgTypeId)
    {
        this.orgTypeId = orgTypeId;
    }

    public void setOrgName(String orgName)
    {
        this.orgName = orgName;
    }

    public void setParentId(Integer parentId)
    {
        this.parentId = parentId;
    }

    public void setParentOrgName(String parentOrgName) { this.parentOrgName = parentOrgName; }

    public void setCreateDate(Date createDate)
    {
        this.createDate = createDate;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public void setComments(String comments)
    {
        this.comments = comments;
    }

    public Integer getOrgId()
    {
        return orgId;
    }

    public Integer getOrgTypeId()
    {
        return orgTypeId;
    }

    public String getOrgName()
    {
        return orgName;
    }

    public Integer getParentId()
    {
        return parentId;
    }

    public String getParentOrgName() { return parentOrgName; }

    public Date getCreateDate()
    {
        return createDate;
    }

    public String getState()
    {
        return state;
    }

    public String getComments()
    {
        return comments;
    }
}
