package com.axon.market.common.domain.iresource;

/**
 * Created by Administrator on 2016/9/20.
 */
public class SmsContentDomain
{
    private Integer id;

    /**
     * 短信内容
     */
    private String content;

    /**
     * 短信链接
     */
    private String url;

    /**
     * 短信关键词
     */
    private String keywords;

    /**
     * 对应产品ids
     */
    private String productIds;

    /**
     * 对应产品名称
     */
    private String productNames;

    /**
     * 创建人
     */
    private Integer createUser;

    /**
     * 创建人名称
     */
    private String createUserName;

    //创建人归属区域
    private Integer createUserArea;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新人
     */
    private Integer updateUser;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 营销任务使用次数
     */
    private Integer usedCount;

    /**
     * 回馈率
     */
    private Integer feedbackRate;

    private String createUserTelePhone;

    /**
     * 内容类型 0：炒店业务， 1：场景业务  2：掌柜短信业务  3：掌柜话+业务
     */
    private Integer businessType;

    /**
     * 上海版本第三方ID
     */
    private String extendId;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getKeywords()
    {
        return keywords;
    }

    public void setKeywords(String keywords)
    {
        this.keywords = keywords;
    }

    public String getProductIds()
    {
        return productIds;
    }

    public void setProductIds(String productIds)
    {
        this.productIds = productIds;
    }

    public String getProductNames()
    {
        return productNames;
    }

    public void setProductNames(String productNames)
    {
        this.productNames = productNames;
    }

    public Integer getCreateUser()
    {
        return createUser;
    }

    public void setCreateUser(Integer createUser)
    {
        this.createUser = createUser;
    }

    public String getCreateUserName()
    {
        return createUserName;
    }

    public void setCreateUserName(String createUserName)
    {
        this.createUserName = createUserName;
    }

    public Integer getCreateUserArea()
    {
        return createUserArea;
    }

    public void setCreateUserArea(Integer createUserArea)
    {
        this.createUserArea = createUserArea;
    }

    public String getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }

    public Integer getUpdateUser()
    {
        return updateUser;
    }

    public void setUpdateUser(Integer updateUser)
    {
        this.updateUser = updateUser;
    }

    public String getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(String updateTime)
    {
        this.updateTime = updateTime;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Integer getUsedCount()
    {
        return usedCount;
    }

    public void setUsedCount(Integer usedCount)
    {
        this.usedCount = usedCount;
    }

    public Integer getFeedbackRate()
    {
        return feedbackRate;
    }

    public void setFeedbackRate(Integer feedbackRate)
    {
        this.feedbackRate = feedbackRate;
    }

    public String getCreateUserTelePhone()
    {
        return createUserTelePhone;
    }

    public void setCreateUserTelePhone(String createUserTelePhone)
    {
        this.createUserTelePhone = createUserTelePhone;
    }

    public Integer getBusinessType()
    {
        return businessType;
    }

    public void setBusinessType(Integer businessType)
    {
        this.businessType = businessType;
    }

    public String getExtendId() {
        return extendId;
    }

    public void setExtendId(String extendId) {
        this.extendId = extendId;
    }
}
