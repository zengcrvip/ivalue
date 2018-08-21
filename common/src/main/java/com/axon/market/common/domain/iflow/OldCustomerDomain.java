package com.axon.market.common.domain.iflow;

/**
 * Created by wangtt on 2017/7/24.
 */
public class OldCustomerDomain
{
    // 任务ID
    private Integer taskId;
    // 任务来源（sms:自建任务 jxhsms:精细化群发任务）
    private  String taskSource;
    //营销办理链接
    private  String marketContentLink;
    // 活动名称
    private String taskName;
    // 开始时间
    private String startTime;
    // 结束时间
    private String endTime;
    // 营销名称
    private String marketName;
    // 营销内容
    private String marketContent;
    // 营销地区编码
    private String marketAreaCode;
    // 指定用户
    private String appointUsers;
    // 免打扰用户
    private String blackUsers;
    // 炒店类型
    private String baseType;
    // 创建人
    private Integer createUserId;
    // 创建时间
    private String createTime;
    // 修改人
    private Integer updateUserId;
    // 修改时间
    private String updateTime;
    // 任务状态
    private Integer status;
    // 指定用户描述
    private String appointUsersDesc;
    // 黑名单用户描述
    private String blackUsersDesc;
    // 指定渠道
    private String appointBusinessHall;
    // 指定渠道描述
    private String appointBusinessHallDesc;
    // 地市描述
    private String areaDesc;
    // 任务类型，0 : 省级 ； 1 : 地市
    private int taskType;
    // 备注
    private String remarks;

    /* +++++++++++++++++ */
    // 精细化活动编码
    private String saleId;
    // 精细化任务波次编码
    private String saleBoidId;
    // 精细化任务目标客户群编码
    private String aimSubId;
    // 是否波次营销(0：否；1：是)
    private Integer isBoidSale;

    public Integer getTaskId()
    {
        return taskId;
    }

    public void setTaskId(Integer taskId)
    {
        this.taskId = taskId;
    }

    public String getTaskSource()
    {
        return taskSource;
    }

    public void setTaskSource(String taskSource)
    {
        this.taskSource = taskSource;
    }

    public String getMarketContentLink()
    {
        return marketContentLink;
    }

    public void setMarketContentLink(String marketContentLink)
    {
        this.marketContentLink = marketContentLink;
    }

    public String getTaskName()
    {
        return taskName;
    }

    public void setTaskName(String taskName)
    {
        this.taskName = taskName;
    }

    public String getStartTime()
    {
        return startTime;
    }

    public void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }

    public String getEndTime()
    {
        return endTime;
    }

    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }

    public String getMarketName()
    {
        return marketName;
    }

    public void setMarketName(String marketName)
    {
        this.marketName = marketName;
    }

    public String getMarketContent()
    {
        return marketContent;
    }

    public void setMarketContent(String marketContent)
    {
        this.marketContent = marketContent;
    }

    public String getMarketAreaCode()
    {
        return marketAreaCode;
    }

    public void setMarketAreaCode(String marketAreaCode)
    {
        this.marketAreaCode = marketAreaCode;
    }

    public String getAppointUsers()
    {
        return appointUsers;
    }

    public void setAppointUsers(String appointUsers)
    {
        this.appointUsers = appointUsers;
    }

    public String getBlackUsers()
    {
        return blackUsers;
    }

    public void setBlackUsers(String blackUsers)
    {
        this.blackUsers = blackUsers;
    }

    public String getBaseType()
    {
        return baseType;
    }

    public void setBaseType(String baseType)
    {
        this.baseType = baseType;
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

    public Integer getUpdateUserId()
    {
        return updateUserId;
    }

    public void setUpdateUserId(Integer updateUserId)
    {
        this.updateUserId = updateUserId;
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

    public String getSaleId()
    {
        return saleId;
    }

    public void setSaleId(String saleId)
    {
        this.saleId = saleId;
    }

    public String getSaleBoidId()
    {
        return saleBoidId;
    }

    public void setSaleBoidId(String saleBoidId)
    {
        this.saleBoidId = saleBoidId;
    }

    public String getAimSubId()
    {
        return aimSubId;
    }

    public void setAimSubId(String aimSubId)
    {
        this.aimSubId = aimSubId;
    }

    public Integer getIsBoidSale()
    {
        return isBoidSale;
    }

    public void setIsBoidSale(Integer isBoidSale)
    {
        this.isBoidSale = isBoidSale;
    }

    public String getAppointUsersDesc()
    {
        return appointUsersDesc;
    }

    public void setAppointUsersDesc(String appointUsersDesc)
    {
        this.appointUsersDesc = appointUsersDesc;
    }

    public String getBlackUsersDesc()
    {
        return blackUsersDesc;
    }

    public void setBlackUsersDesc(String blackUsersDesc)
    {
        this.blackUsersDesc = blackUsersDesc;
    }

    public String getAppointBusinessHall()
    {
        return appointBusinessHall;
    }

    public void setAppointBusinessHall(String appointBusinessHall)
    {
        this.appointBusinessHall = appointBusinessHall;
    }

    public String getAppointBusinessHallDesc()
    {
        return appointBusinessHallDesc;
    }

    public void setAppointBusinessHallDesc(String appointBusinessHallDesc)
    {
        this.appointBusinessHallDesc = appointBusinessHallDesc;
    }

    public String getAreaDesc()
    {
        return areaDesc;
    }

    public void setAreaDesc(String areaDesc)
    {
        this.areaDesc = areaDesc;
    }

    public int getTaskType()
    {
        return taskType;
    }

    public void setTaskType(int taskType)
    {
        this.taskType = taskType;
    }

    public String getRemarks()
    {
        return remarks;
    }

    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }
}
