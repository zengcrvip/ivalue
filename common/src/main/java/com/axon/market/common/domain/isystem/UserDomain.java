package com.axon.market.common.domain.isystem;

import com.axon.market.common.domain.ikeeper.KeeperUserDomain;

/**
 * Created by Administrator on 2017/1/4.
 */
public class UserDomain
{
    private Integer id;

    private String name;

    private String telephone;

    private Integer areaId;

    private Integer areaCode;

    private String areaName;

    //营业厅Id
    private String businessHallIds;

    //营业厅类型
    private String businessHallTypes;

    //营业厅name
    private String businessHallNames;

    private String account;

    private String password;

    private String email;

    private Integer createUserId;

    private String createTime;

    private String updateTime;

    private String roleIds;

    private String roleNames;

    private String homePageUrl;

    //根据角色判断当前用户是什么类型，是普通用户类型还是炒店用户类型或其他
    private Integer userType;

    private Integer status;

    private String tagAuditUsers;

    private String tagAuditUserNames;

    // TODO 统一命名model
    private String segmentAuditUsers;

    private String segmentAuditUserNames;

    private String marketingAuditUsers;

    private String marketingAuditUserNames;

    private String remarks;

    /**
     * app唯一校验标识
     */
    private String token;

    /**
     * 掌柜用户信息
     */
    private KeeperUserDomain keeperUser;

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

    public String getTelephone()
    {
        return telephone;
    }

    public void setTelephone(String telephone)
    {
        this.telephone = telephone;
    }

    public Integer getAreaId()
    {
        return areaId;
    }

    public void setAreaId(Integer areaId)
    {
        this.areaId = areaId;
    }

    public Integer getAreaCode()
    {
        return areaCode;
    }

    public void setAreaCode(Integer areaCode)
    {
        this.areaCode = areaCode;
    }

    public String getAreaName()
    {
        return areaName;
    }

    public void setAreaName(String areaName)
    {
        this.areaName = areaName;
    }

    public String getBusinessHallIds()
    {
        return businessHallIds;
    }

    public void setBusinessHallIds(String businessHallIds)
    {
        this.businessHallIds = businessHallIds;
    }

    public String getBusinessHallNames()
    {
        return businessHallNames;
    }

    public void setBusinessHallNames(String businessHallNames)
    {
        this.businessHallNames = businessHallNames;
    }

    public String getBusinessHallTypes()
    {
        return businessHallTypes;
    }

    public void setBusinessHallTypes(String businessHallTypes)
    {
        this.businessHallTypes = businessHallTypes;
    }

    public String getAccount()
    {
        return account;
    }

    public void setAccount(String account)
    {
        this.account = account;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public Integer getCreateUserId()
    {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId)
    {
        this.createUserId = createUserId;
    }

    public String getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }

    public String getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(String updateTime)
    {
        this.updateTime = updateTime;
    }

    public String getRoleIds()
    {
        return roleIds;
    }

    public void setRoleIds(String roleIds)
    {
        this.roleIds = roleIds;
    }

    public String getRoleNames()
    {
        return roleNames;
    }

    public void setRoleNames(String roleNames)
    {
        this.roleNames = roleNames;
    }

    public String getHomePageUrl()
    {
        return homePageUrl;
    }

    public void setHomePageUrl(String homePageUrl)
    {
        this.homePageUrl = homePageUrl;
    }

    public Integer getUserType()
    {
        return userType;
    }

    public void setUserType(Integer userType)
    {
        this.userType = userType;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public String getTagAuditUsers()
    {
        return tagAuditUsers;
    }

    public void setTagAuditUsers(String tagAuditUsers)
    {
        this.tagAuditUsers = tagAuditUsers;
    }

    public String getTagAuditUserNames()
    {
        return tagAuditUserNames;
    }

    public void setTagAuditUserNames(String tagAuditUserNames)
    {
        this.tagAuditUserNames = tagAuditUserNames;
    }

    public String getSegmentAuditUsers()
    {
        return segmentAuditUsers;
    }

    public void setSegmentAuditUsers(String segmentAuditUsers)
    {
        this.segmentAuditUsers = segmentAuditUsers;
    }

    public String getSegmentAuditUserNames()
    {
        return segmentAuditUserNames;
    }

    public void setSegmentAuditUserNames(String segmentAuditUserNames)
    {
        this.segmentAuditUserNames = segmentAuditUserNames;
    }

    public String getMarketingAuditUsers()
    {
        return marketingAuditUsers;
    }

    public void setMarketingAuditUsers(String marketingAuditUsers)
    {
        this.marketingAuditUsers = marketingAuditUsers;
    }

    public String getMarketingAuditUserNames()
    {
        return marketingAuditUserNames;
    }

    public void setMarketingAuditUserNames(String marketingAuditUserNames)
    {
        this.marketingAuditUserNames = marketingAuditUserNames;
    }

    public String getRemarks()
    {
        return remarks;
    }

    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public KeeperUserDomain getKeeperUser()
    {
        return keeperUser;
    }

    public void setKeeperUser(KeeperUserDomain keeperUser) {
        this.keeperUser = keeperUser;
    }
}
