package com.axon.market.common.domain.ikeeper;

/**
 * Created by yuanfei on 2017/8/10.
 */
public class KeeperUserDomain
{
    /**
     * 和market_user表关联的用户id
     */
    private Integer userId;

    /**
     * 员工姓名
     */
    private String userName;

    private Integer areaId;

    private String areaName;

    /**
     * 和market_user表关联的用户号码
     */
    private String telephone;

    /**
     * 是否是接口人  0：是，1：否
     */
    private Integer interfaceMan;

    /**
     * (创建的掌柜用户默认为末梢人员)用户是否拥有管理权 0：是，1：否
     */
    private Integer isCanManage;

    /**
     * 系统组织架构
     */
    private Integer systemOrgId;

    private String systemOrgName;

    /**
     * 业务组织架构
     */
    private Integer businessOrgId;

    private String businessOrgName;

    /**
     * 维系能力
     */
    private String ability;

    private String abilityNames;

    /**
     * 末梢人员使用的短信签名
     */
    private String smsSignature;

    /**
     * 审核短信签名的用户(有管理权限的人才能被指定来审批)
     */
    private Integer auditUser;

    /**
     * (1) 短信签名状态 0:生效中，1：有需要审批的新短信签名
     * (2) 该用户状态  -1：已删除,3：禁用(管理market_user的1状态)
     */
    private Integer status;

    private String createTime;

    private Integer createUser;

    private String updateTime;

    private Integer updateUser;

    /**
     * 掌柜用户自创建以来在线的天数
     */
    private Integer onlineDays;

    /**
     * app端系统校验标识
     */
    private String token;

    public Integer getUserId()
    {
        return userId;
    }

    public void setUserId(Integer userId)
    {
        this.userId = userId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public Integer getAreaId()
    {
        return areaId;
    }

    public void setAreaId(Integer areaId)
    {
        this.areaId = areaId;
    }

    public String getAreaName()
    {
        return areaName;
    }

    public void setAreaName(String areaName)
    {
        this.areaName = areaName;
    }

    public String getTelephone()
    {
        return telephone;
    }

    public void setTelephone(String telephone)
    {
        this.telephone = telephone;
    }

    public Integer getInterfaceMan()
    {
        return interfaceMan;
    }

    public void setInterfaceMan(Integer interfaceMan)
    {
        this.interfaceMan = interfaceMan;
    }

    public Integer getIsCanManage()
    {
        return isCanManage;
    }

    public void setIsCanManage(Integer isCanManage)
    {
        this.isCanManage = isCanManage;
    }

    public Integer getSystemOrgId() {
        return systemOrgId;
    }

    public void setSystemOrgId(Integer systemOrgId) {
        this.systemOrgId = systemOrgId;
    }

    public String getSystemOrgName()
    {
        return systemOrgName;
    }

    public void setSystemOrgName(String systemOrgName)
    {
        this.systemOrgName = systemOrgName;
    }

    public String getBusinessOrgName()
    {
        return businessOrgName;
    }

    public void setBusinessOrgName(String businessOrgName)
    {
        this.businessOrgName = businessOrgName;
    }

    public Integer getBusinessOrgId()
    {
        return businessOrgId;
    }

    public void setBusinessOrgId(Integer businessOrgId)
    {
        this.businessOrgId = businessOrgId;
    }

    public String getAbility()
    {
        return ability;
    }

    public void setAbility(String ability)
    {
        this.ability = ability;
    }

    public String getAbilityNames()
    {
        return abilityNames;
    }

    public void setAbilityNames(String abilityNames)
    {
        this.abilityNames = abilityNames;
    }

    public String getSmsSignature()
    {
        return smsSignature;
    }

    public void setSmsSignature(String smsSignature)
    {
        this.smsSignature = smsSignature;
    }

    public Integer getAuditUser()
    {
        return auditUser;
    }

    public void setAuditUser(Integer auditUser)
    {
        this.auditUser = auditUser;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public String getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }

    public Integer getCreateUser()
    {
        return createUser;
    }

    public void setCreateUser(Integer createUser)
    {
        this.createUser = createUser;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime)
    {
        this.updateTime = updateTime;
    }

    public Integer getUpdateUser()
    {
        return updateUser;
    }

    public void setUpdateUser(Integer updateUser)
    {
        this.updateUser = updateUser;
    }

    public Integer getOnlineDays()
    {
        return onlineDays;
    }

    public void setOnlineDays(Integer onlineDays)
    {
        this.onlineDays = onlineDays;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }
}
