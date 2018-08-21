package com.axon.market.common.domain.isystem;

/**
 * 位置场景数据同步对象
 * Created by zengcr on 2016/12/8.
 */
public class PositionDataSynDomain
{
    //任务ID
    private Long taskId;
    private String taskName;
    //信息类型，默认新增1
    private Integer messageType = 1;
    private String taskStartTm;
    private String taskEndTm;
    private Integer monitorStartTm;
    private Integer monitorEndTm;
    private Integer monitoredBsId;
    //监控类型，默认进入1
    private Integer monitorType = 1;
    private String marketContent;
    private String marketUrl;
    private Integer marketInterval;
    private Integer userFilterTypeBlack = 0;
    private String userFilterListBlack;
    //审核状态，默认6，待自动审核
    private Integer executeStatus = 6;
    private Integer areano;
    //删除状态，默认0-未删除
    private Integer deleteStatus = 0;
    //沃讯渠道，默认0-无沃讯渠道
    private Integer cid = 0;
    private Integer userFilterTypeWhite = 0;
    private String userFilterListWhite;
    private String spNum;
    private String userFilterBlackSegment;
    private String userFilterWhiteSegment;
    private String county;
    private String usergroupName;
    private Integer usergroupId;
    private Integer triggerLimit;
    private Integer weights;
    private Integer channelId;
    //默认普通模板
    private Integer contentModuleId = 0;
    private Integer locationTypeId;
    private Integer triggerChannelId;
    private Integer validityDateTriggerType;
    private String validityDate;
    private Integer smsReportStatus;
    private Long smsReportPhone;
    private String manruRange;
    private String smsContentId;
    private String marketContentExtend;

    public Long getTaskId()
    {
        return taskId;
    }

    public void setTaskId(Long taskId)
    {
        this.taskId = taskId;
    }

    public String getTaskName()
    {
        return taskName;
    }

    public void setTaskName(String taskName)
    {
        this.taskName = taskName;
    }

    public Integer getMessageType()
    {
        return messageType;
    }

    public void setMessageType(Integer messageType)
    {
        this.messageType = messageType;
    }

    public String getTaskStartTm()
    {
        return taskStartTm;
    }

    public void setTaskStartTm(String taskStartTm)
    {
        this.taskStartTm = taskStartTm;
    }

    public String getTaskEndTm()
    {
        return taskEndTm;
    }

    public void setTaskEndTm(String taskEndTm)
    {
        this.taskEndTm = taskEndTm;
    }

    public Integer getMonitorStartTm()
    {
        return monitorStartTm;
    }

    public void setMonitorStartTm(Integer monitorStartTm)
    {
        this.monitorStartTm = monitorStartTm;
    }

    public Integer getMonitorEndTm()
    {
        return monitorEndTm;
    }

    public void setMonitorEndTm(Integer monitorEndTm)
    {
        this.monitorEndTm = monitorEndTm;
    }

    public Integer getMonitoredBsId()
    {
        return monitoredBsId;
    }

    public void setMonitoredBsId(Integer monitoredBsId)
    {
        this.monitoredBsId = monitoredBsId;
    }

    public Integer getMonitorType()
    {
        return monitorType;
    }

    public void setMonitorType(Integer monitorType)
    {
        this.monitorType = monitorType;
    }

    public String getMarketContent()
    {
        return marketContent;
    }

    public void setMarketContent(String marketContent)
    {
        this.marketContent = marketContent;
    }

    public String getMarketUrl()
    {
        return marketUrl;
    }

    public void setMarketUrl(String marketUrl)
    {
        this.marketUrl = marketUrl;
    }

    public Integer getMarketInterval()
    {
        return marketInterval;
    }

    public void setMarketInterval(Integer marketInterval)
    {
        this.marketInterval = marketInterval;
    }

    public Integer getUserFilterTypeBlack()
    {
        return userFilterTypeBlack;
    }

    public void setUserFilterTypeBlack(Integer userFilterTypeBlack)
    {
        this.userFilterTypeBlack = userFilterTypeBlack;
    }

    public String getUserFilterListBlack()
    {
        return userFilterListBlack;
    }

    public void setUserFilterListBlack(String userFilterListBlack)
    {
        this.userFilterListBlack = userFilterListBlack;
    }

    public Integer getExecuteStatus()
    {
        return executeStatus;
    }

    public void setExecuteStatus(Integer executeStatus)
    {
        this.executeStatus = executeStatus;
    }

    public Integer getAreano()
    {
        return areano;
    }

    public void setAreano(Integer areano)
    {
        this.areano = areano;
    }

    public Integer getDeleteStatus()
    {
        return deleteStatus;
    }

    public void setDeleteStatus(Integer deleteStatus)
    {
        this.deleteStatus = deleteStatus;
    }

    public Integer getCid()
    {
        return cid;
    }

    public void setCid(Integer cid)
    {
        this.cid = cid;
    }

    public Integer getUserFilterTypeWhite()
    {
        return userFilterTypeWhite;
    }

    public void setUserFilterTypeWhite(Integer userFilterTypeWhite)
    {
        this.userFilterTypeWhite = userFilterTypeWhite;
    }

    public String getUserFilterListWhite()
    {
        return userFilterListWhite;
    }

    public void setUserFilterListWhite(String userFilterListWhite)
    {
        this.userFilterListWhite = userFilterListWhite;
    }

    public String getSpNum()
    {
        return spNum;
    }

    public void setSpNum(String spNum)
    {
        this.spNum = spNum;
    }

    public String getUserFilterBlackSegment()
    {
        return userFilterBlackSegment;
    }

    public void setUserFilterBlackSegment(String userFilterBlackSegment)
    {
        this.userFilterBlackSegment = userFilterBlackSegment;
    }

    public String getUserFilterWhiteSegment()
    {
        return userFilterWhiteSegment;
    }

    public void setUserFilterWhiteSegment(String userFilterWhiteSegment)
    {
        this.userFilterWhiteSegment = userFilterWhiteSegment;
    }

    public String getCounty()
    {
        return county;
    }

    public void setCounty(String county)
    {
        this.county = county;
    }

    public String getUsergroupName()
    {
        return usergroupName;
    }

    public void setUsergroupName(String usergroupName)
    {
        this.usergroupName = usergroupName;
    }

    public Integer getUsergroupId()
    {
        return usergroupId;
    }

    public void setUsergroupId(Integer usergroupId)
    {
        this.usergroupId = usergroupId;
    }

    public Integer getTriggerLimit()
    {
        return triggerLimit;
    }

    public void setTriggerLimit(Integer triggerLimit)
    {
        this.triggerLimit = triggerLimit;
    }

    public Integer getWeights()
    {
        return weights;
    }

    public void setWeights(Integer weights)
    {
        this.weights = weights;
    }

    public Integer getChannelId()
    {
        return channelId;
    }

    public void setChannelId(Integer channelId)
    {
        this.channelId = channelId;
    }

    public Integer getContentModuleId()
    {
        return contentModuleId;
    }

    public void setContentModuleId(Integer contentModuleId)
    {
        this.contentModuleId = contentModuleId;
    }

    public Integer getLocationTypeId()
    {
        return locationTypeId;
    }

    public void setLocationTypeId(Integer locationTypeId)
    {
        this.locationTypeId = locationTypeId;
    }

    public Integer getTriggerChannelId()
    {
        return triggerChannelId;
    }

    public void setTriggerChannelId(Integer triggerChannelId)
    {
        this.triggerChannelId = triggerChannelId;
    }

    public Integer getValidityDateTriggerType()
    {
        return validityDateTriggerType;
    }

    public void setValidityDateTriggerType(Integer validityDateTriggerType)
    {
        this.validityDateTriggerType = validityDateTriggerType;
    }

    public String getValidityDate()
    {
        return validityDate;
    }

    public void setValidityDate(String validityDate)
    {
        this.validityDate = validityDate;
    }

    public Integer getSmsReportStatus()
    {
        return smsReportStatus;
    }

    public void setSmsReportStatus(Integer smsReportStatus)
    {
        this.smsReportStatus = smsReportStatus;
    }

    public Long getSmsReportPhone()
    {
        return smsReportPhone;
    }

    public void setSmsReportPhone(Long smsReportPhone)
    {
        this.smsReportPhone = smsReportPhone;
    }

    public String getManruRange() {
        return manruRange;
    }

    public void setManruRange(String manruRange) {
        this.manruRange = manruRange;
    }

    public String getSmsContentId() {
        return smsContentId;
    }

    public void setSmsContentId(String smsContentId) {
        this.smsContentId = smsContentId;
    }

    public String getMarketContentExtend() {
        return marketContentExtend;
    }

    public void setMarketContentExtend(String marketContentExtend) {
        this.marketContentExtend = marketContentExtend;
    }
}
