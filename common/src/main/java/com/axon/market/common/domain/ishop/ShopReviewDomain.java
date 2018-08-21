package com.axon.market.common.domain.ishop;

/**
 * 炒店审核实体
 * Created by gloomysw on 2017/02/27.
 */
public class ShopReviewDomain {

    // 审核人ID
    private Integer reviewUserId;

    // 审核人来自用户ID
    private Integer reviewUserIdFrom;

    // 多级审核
    private String marketingAuditUsers;

    public Integer getReviewUserId() {
        return reviewUserId;
    }

    public void setReviewUserId(Integer reviewUserId) {
        this.reviewUserId = reviewUserId;
    }

    public Integer getReviewUserIdFrom() {
        return reviewUserIdFrom;
    }

    public void setReviewUserIdFrom(Integer reviewUserIdFrom) {
        this.reviewUserIdFrom = reviewUserIdFrom;
    }

    public String getMarketingAuditUsers() {
        return marketingAuditUsers;
    }

    public void setMarketingAuditUsers(String marketingAuditUsers) {
        this.marketingAuditUsers = marketingAuditUsers;
    }
}
