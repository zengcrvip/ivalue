package com.axon.market.common.domain.iservice;

/**
 * Created by zengcr on 2017/7/29.
 * 老用户专享API返回的实体对象
 */
public class OldCustomerResultDomain
{
    //老用户专享优惠活动营销名称
     private String marketName;
    //老用户专享优惠活动营销内容
    private String marketContent;
    //老用户专享优惠活动对应最近的营业厅
     private String baseName;
    //老用户专享优惠活动对应最近的营业厅联系方式
     private String telePhone;
    //老用户专享优惠活动线上链接
    private String onlineLink;
    //优惠活动类型
    private Integer marketType;   //1:线上，2;线下，3：全部

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    public String getTelePhone() {
        return telePhone;
    }

    public void setTelePhone(String telePhone) {
        this.telePhone = telePhone;
    }

    public String getOnlineLink()
    {
        return onlineLink;
    }

    public void setOnlineLink(String onlineLink)
    {
        this.onlineLink = onlineLink;
    }

    public Integer getMarketType()
    {
        return marketType;
    }

    public void setMarketType(Integer marketType)
    {
        this.marketType = marketType;
    }

    public String getMarketContent()
    {
        return marketContent;
    }

    public void setMarketContent(String marketContent)
    {
        this.marketContent = marketContent;
    }
}
