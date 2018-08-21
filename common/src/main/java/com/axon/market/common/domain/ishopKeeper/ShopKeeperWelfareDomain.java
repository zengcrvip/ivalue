package com.axon.market.common.domain.ishopKeeper;

import java.sql.Date;

/**
 * Created by Zhuwen on 2017/8/19.
 */
public class ShopKeeperWelfareDomain {
    //福利id
    private Integer welfareId;
    //福利名称
    private String welfareName;
    //福利类型
    private Integer typeId;
    //生效时间
    private String effDate;
    //失效时间
    private String expDate;
    //归属区域
    private Integer areaId;
    //归属部门
    private String orgIds;
    //短信模板
    private String smsContent;
    //创建人
    private Integer createUserId;
    //创建时间
    private Date createTime;
    //状态
    private Integer state;
    //备注
    private String comments;
    //包含的产品
    private String productIds;

    //福利类型名称
    private String typeName;

    //归属部门名称
    private String orgNames;

    //归属区域名称
    private String areaNames;

    //关联产品名称
    private String productNames;

    //关联产品编码
    private String productCodes;
    //网别
    private String netType;

    public Integer getWelfareId() {
        return welfareId;
    }

    public void setWelfareId(Integer welfareId) {
        this.welfareId = welfareId;
    }

    public String getWelfareName() {
        return welfareName;
    }

    public void setWelfareName(String welfareName) {
        this.welfareName = welfareName;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getEffDate() {
        return effDate;
    }

    public void setEffDate(String effDate) {
        this.effDate = effDate;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getOrgNames() {
        return orgNames;
    }

    public void setOrgNames(String orgNames) {
        this.orgNames = orgNames;
    }

    public String getAreaNames() {
        return areaNames;
    }

    public void setAreaNames(String areaNames) {
        this.areaNames = areaNames;
    }

    public String getProductNames() {
        return productNames;
    }

    public void setProductNames(String productNames) {
        this.productNames = productNames;
    }

    public String getProductCodes() {
        return productCodes;
    }

    public void setProductCodes(String productCodes) {
        this.productCodes = productCodes;
    }

    public String getOrgIds() {
        return orgIds;
    }

    public void setOrgIds(String orgIds) {
        this.orgIds = orgIds;
    }

    public String getSmsContent() {
        return smsContent;
    }

    public void setSmsContent(String smsContent) {
        this.smsContent = smsContent;
    }

    public Integer getCreateUserId()
    {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId)
    {
        this.createUserId = createUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getProductIds() {
        return productIds;
    }

    public void setProductIds(String productIds) {
        this.productIds = productIds;
    }

    public String getNetType() {
        return netType;
    }

    public void setNetType(String netType) {
        this.netType = netType;
    }
}
