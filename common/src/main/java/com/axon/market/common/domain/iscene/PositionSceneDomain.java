package com.axon.market.common.domain.iscene;

/**
 * 位置场景对象实体
 * Created by zengcr on 2016/11/28.
 */
public class PositionSceneDomain
{
    //主键ID，自增长
    private Integer id;
    //位置场景名称
    private String scenceName;
    //类型
    private Integer scenceType;
    //类型名称
    private String scenceTypeName;
    //监控周期
    private Integer monitorInvartal;
    //开始时间
    private String beginTime;
    //结束时间
    private String endTime;
    //监控的区县
    private String monitorArea;
    //任务渠道
    private Integer channelId;
    //监视类型
    private Integer monitorType;
    //监视的基站地市
    private Integer baseAreaId;
    //监视的基站类型
    private Integer baseAreaType;
    //监视的基站点
    private Integer baseId;
    //监视的基站点名称
    private String baseName;
    //任务权重
    private Integer taskWeight;
    //触发渠道
    private Integer triggerChannelId;
    //接收任务发送报告
    private Integer isSengReport;
    //接收报告手机号码
    private Long reportPhone;
    //营销间隔
    private Integer sendInterval;
    //白名单用户
    private String whiteUsers;
    private String whiteUserNames;
    //黑名单用户
    private String blackUsers;
    private String blackUserNames;
    //白名单地区号段
    private String whiteAreas;
    private String whiteAreaNames;
    //黑名单地区号段
    private String blackAreas;
    private String blackAreaNames;
    //状态
    private Integer status;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getScenceName()
    {
        return scenceName;
    }

    public void setScenceName(String scenceName)
    {
        this.scenceName = scenceName;
    }

    public Integer getScenceType()
    {
        return scenceType;
    }

    public String getScenceTypeName()
    {
        return scenceTypeName;
    }

    public void setScenceTypeName(String scenceTypeName)
    {
        this.scenceTypeName = scenceTypeName;
    }

    public void setScenceType(Integer scenceType)
    {
        this.scenceType = scenceType;
    }

    public Integer getMonitorInvartal()
    {
        return monitorInvartal;
    }

    public void setMonitorInvartal(Integer monitorInvartal)
    {
        this.monitorInvartal = monitorInvartal;
    }

    public String getBeginTime()
    {
        return beginTime;
    }

    public void setBeginTime(String beginTime)
    {
        this.beginTime = beginTime;
    }

    public String getEndTime()
    {
        return endTime;
    }

    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }

    public String getMonitorArea()
    {
        return monitorArea;
    }

    public void setMonitorArea(String monitorArea)
    {
        this.monitorArea = monitorArea;
    }

    public Integer getChannelId()
    {
        return channelId;
    }

    public void setChannelId(Integer channelId)
    {
        this.channelId = channelId;
    }

    public Integer getMonitorType()
    {
        return monitorType;
    }

    public void setMonitorType(Integer monitorType)
    {
        this.monitorType = monitorType;
    }

    public Integer getBaseAreaId()
    {
        return baseAreaId;
    }

    public void setBaseAreaId(Integer baseAreaId)
    {
        this.baseAreaId = baseAreaId;
    }

    public Integer getBaseAreaType()
    {
        return baseAreaType;
    }

    public void setBaseAreaType(Integer baseAreaType)
    {
        this.baseAreaType = baseAreaType;
    }

    public Integer getBaseId()
    {
        return baseId;
    }

    public void setBaseId(Integer baseId)
    {
        this.baseId = baseId;
    }

    public String getBaseName()
    {
        return baseName;
    }

    public void setBaseName(String baseName)
    {
        this.baseName = baseName;
    }

    public Integer getTaskWeight()
    {
        return taskWeight;
    }

    public void setTaskWeight(Integer taskWeight)
    {
        this.taskWeight = taskWeight;
    }

    public Integer getTriggerChannelId()
    {
        return triggerChannelId;
    }

    public void setTriggerChannelId(Integer triggerChannelId)
    {
        this.triggerChannelId = triggerChannelId;
    }

    public Integer getIsSengReport()
    {
        return isSengReport;
    }

    public void setIsSengReport(Integer isSengReport)
    {
        this.isSengReport = isSengReport;
    }

    public Long getReportPhone()
    {
        return reportPhone;
    }

    public void setReportPhone(Long reportPhone)
    {
        this.reportPhone = reportPhone;
    }

    public Integer getSendInterval()
    {
        return sendInterval;
    }

    public void setSendInterval(Integer sendInterval)
    {
        this.sendInterval = sendInterval;
    }

    public String getWhiteUsers()
    {
        return whiteUsers;
    }

    public void setWhiteUsers(String whiteUsers)
    {
        this.whiteUsers = whiteUsers;
    }

    public String getBlackUsers()
    {
        return blackUsers;
    }

    public void setBlackUsers(String blackUsers)
    {
        this.blackUsers = blackUsers;
    }

    public String getWhiteAreas()
    {
        return whiteAreas;
    }

    public void setWhiteAreas(String whiteAreas)
    {
        this.whiteAreas = whiteAreas;
    }

    public String getBlackAreas()
    {
        return blackAreas;
    }

    public void setBlackAreas(String blackAreas)
    {
        this.blackAreas = blackAreas;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public String getWhiteUserNames()
    {
        return whiteUserNames;
    }

    public void setWhiteUserNames(String whiteUserNames)
    {
        this.whiteUserNames = whiteUserNames;
    }

    public String getBlackUserNames()
    {
        return blackUserNames;
    }

    public void setBlackUserNames(String blackUserNames)
    {
        this.blackUserNames = blackUserNames;
    }

    public String getWhiteAreaNames()
    {
        return whiteAreaNames;
    }

    public void setWhiteAreaNames(String whiteAreaNames)
    {
        this.whiteAreaNames = whiteAreaNames;
    }

    public String getBlackAreaNames()
    {
        return blackAreaNames;
    }

    public void setBlackAreaNames(String blackAreaNames)
    {
        this.blackAreaNames = blackAreaNames;
    }
}


