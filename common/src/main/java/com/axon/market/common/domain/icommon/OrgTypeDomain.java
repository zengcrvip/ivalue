package com.axon.market.common.domain.icommon;

/**
 * Created by Duzm on 2017/8/1.
 */
public class OrgTypeDomain {

    private Integer orgTypeId;

    private String orgTypeName;

    private String comments;

    public void setOrgTypeId(Integer orgTypeId) {
        this.orgTypeId = orgTypeId;
    }

    public void setOrgTypeName(String orgTypeName) {
        this.orgTypeName = orgTypeName;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Integer getOrgTypeId() {
        return orgTypeId;
    }

    public String getOrgTypeName() {
        return orgTypeName;
    }

    public String getComments() {
        return comments;
    }
}
