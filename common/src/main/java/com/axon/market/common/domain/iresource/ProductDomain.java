package com.axon.market.common.domain.iresource;

/**
 * Created by chenyu on 2016/9/20.
 */
public class ProductDomain
{
    private Integer id;

    /**
     * 产品名称
     */
    private String name;

    /**
     * 目录id
     */
    private String catalogId;

    /**
     * 目录名称
     */
    private String catalogName;

    /**
     * 产品价钱
     */
    private String price;

    private String code;

    /**
     * 状态
     */
    private String status;

    private String fornet;

    private String spid;

    private String orderCode;

    private String productFeatures;

    private String userBaseFeatures;

    private String userHobbyFeatures;

    /**
     * 产品介绍
     */
    private String introduce;

    /**
     * 创建人
     */
    private Integer createUser;

    /**
     * 创建人名称
     */
    private String createUserName;

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
     * 订购关键字
     */
    private String orderKey;

    /**
     * 查询关键字
     */
    private String keyType;

    /**
     * 下发成功短信提示语
     */
    private String successMsg;

    /**
     * 网别
     */
    private String netType;

    /**
     * 生效模式
     */
    private Integer effectMode;

    /**
     * 二次确认提示语
     */
    private String confirmMsg;

    /**
     * 订购成功短信提示语
     */
    private String orderSucMsg;

    /**
     * 订购失败短信提示语
     */
    private String orderFailMsg;

    /**
     * 是否需要二次确认
     */
    private String confirmOrder;

    public String getOrderKey()
    {
        return orderKey;
    }

    public void setOrderKey(String orderKey)
    {
        this.orderKey = orderKey;
    }

    public String getKeyType()
    {
        return keyType;
    }

    public void setKeyType(String keyType)
    {
        this.keyType = keyType;
    }

    public String getSuccessMsg()
    {
        return successMsg;
    }

    public void setSuccessMsg(String successMsg)
    {
        this.successMsg = successMsg;
    }

    public String getNetType()
    {
        return netType;
    }

    public void setNetType(String netType)
    {
        this.netType = netType;
    }

    public Integer getEffectMode()
    {
        return effectMode;
    }

    public void setEffectMode(Integer effectMode)
    {
        this.effectMode = effectMode;
    }

    public String getConfirmMsg()
    {
        return confirmMsg;
    }

    public void setConfirmMsg(String confirmMsg)
    {
        this.confirmMsg = confirmMsg;
    }

    public String getOrderSucMsg()
    {
        return orderSucMsg;
    }

    public void setOrderSucMsg(String orderSucMsg)
    {
        this.orderSucMsg = orderSucMsg;
    }

    public String getOrderFailMsg()
    {
        return orderFailMsg;
    }

    public void setOrderFailMsg(String orderFailMsg)
    {
        this.orderFailMsg = orderFailMsg;
    }

    public String getConfirmOrder()
    {
        return confirmOrder;
    }

    public void setConfirmOrder(String confirmOrder)
    {
        this.confirmOrder = confirmOrder;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getCatalogId()
    {
        return catalogId;
    }

    public void setCatalogId(String catalogId)
    {
        this.catalogId = catalogId;
    }

    public String getCatalogName()
    {
        return catalogName;
    }

    public void setCatalogName(String catalogName)
    {
        this.catalogName = catalogName;
    }

    public String getPrice()
    {
        return price;
    }

    public void setPrice(String price)
    {
        this.price = price;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getFornet()
    {
        return fornet;
    }

    public void setFornet(String fornet)
    {
        this.fornet = fornet;
    }

    public String getSpid()
    {
        return spid;
    }

    public void setSpid(String spid)
    {
        this.spid = spid;
    }

    public String getOrderCode()
    {
        return orderCode;
    }

    public void setOrderCode(String orderCode)
    {
        this.orderCode = orderCode;
    }

    public String getProductFeatures()
    {
        return productFeatures;
    }

    public void setProductFeatures(String productFeatures)
    {
        this.productFeatures = productFeatures;
    }

    public String getUserBaseFeatures()
    {
        return userBaseFeatures;
    }

    public void setUserBaseFeatures(String userBaseFeatures)
    {
        this.userBaseFeatures = userBaseFeatures;
    }

    public String getUserHobbyFeatures()
    {
        return userHobbyFeatures;
    }

    public void setUserHobbyFeatures(String userHobbyFeatures)
    {
        this.userHobbyFeatures = userHobbyFeatures;
    }

    public String getIntroduce()
    {
        return introduce;
    }

    public void setIntroduce(String introduce)
    {
        this.introduce = introduce;
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
}
