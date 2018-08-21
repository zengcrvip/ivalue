package com.axon.market.common.domain.ishop;

import java.util.Date;

/**
 * 炒店任务实体类
 * Created by zengcr on 2017/2/14.
 */
public class ShopTaskDomain
{
    //主键ID
    private Integer id;
    //炒店任务名称
    private String taskName;
    //炒店描述
    private String  taskDesc;
    //炒店来源 1、省级任务   2、地市级任务   3、门店任务
    private Integer taskType;
    //炒店业务类型
    private String businessId;
    //炒店业务类型名称
    private String businessName;
    //任务开始时间
    private String startTime;
    //任务结束时间
    private String stopTime;
    //监控开始时间
    private String beginTime;
    //监控结束时间
    private String endTime;
    //营销用户类型   1:常驻用户  2：流动拜访   3：混合   4:个性化推荐
    private Integer marketUser;
    //常驻用户数
    private Integer marketUserMum;
    //指定用户
    private String appointUsers;
    private String appointUserDesc;
    //免打扰用户
    private String blackUsers;
    private String blackUserDesc;
    //接入号
    private String accessNumber;
    //营销内容
    private String marketContentText;
    // 替换元素信息(用于替换{Reserve1},Reserve2}...)，用 & 分隔
    private String marketContentExtend;
    //是否添加短信签名
    private String isAddAutograph;
    //营销链接
    private String marketUrl;
    //营销间隔
    private Integer sendInterval;
    //场景类别
    private Integer sceneType;
    private String sceneTypeName;
    //监控的区县
    private String monitorArea;
    //监控周期
    private Integer monitorInterval;
    //场景渠道
    private Integer channelId;
    //监控类型
    private Integer monitorType;
    //触发渠道
    private Integer triggerChannelId;
    //炒店所属的地市
    private Integer baseAreaId;
    private String baseAreaName;
    //炒店类型
    private String baseAreaTypes;
    private String  baseAreaTypeNames;
    //炒店ID
    private String baseIds;
    // 炒店名称
    private String baseNames;
    // 炒店编码
    private String baseCodes;
    //营业厅待办任务池的单个炒店
    private Integer baseId;
    private String baseName;
    private Integer baseAreaType;
    //任务权重
    private Integer taskWeight;
    //接收任务发送报告
    private Integer isSendReport;
    //接收任务发送报告号码
    private Long reportPhone;
    //营销人数限制
    private Integer marketLimit;
    //状态
    private Integer status;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //创建人
    private String createUser;
    //对应流动拜访营销任务
    private Integer pTaskId;
    //对应常驻用户营销任务
    private Integer smsTaskId;
    //短信签名
    private String messageAutograph;

    private String aimSubId;

    private String departTypeCode;

    private String executeUserName;

    private String saleId;

    private String saleBoidId;

    private String aimSubName;

    private String taskFileName;

    private String createTimeStr;
    private Integer taskClassifyId;

    //漫入范围
    private String manruRange;

    /**
     * 选择的作为白名单的模型ids
     */
    private String marketSegmentIds;

    private String marketSegmentNames;

    /**
     * 短信模板ID
     */
    private String marketContentId;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getTaskName()
    {
        return taskName;
    }

    public void setTaskName(String taskName)
    {
        this.taskName = taskName;
    }

    public String getBusinessId()
    {
        return businessId;
    }

    public void setBusinessId(String businessId)
    {
        this.businessId = businessId;
    }

    public String getBusinessName()
    {
        return businessName;
    }

    public void setBusinessName(String businessName)
    {
        this.businessName = businessName;
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

    public String getBaseAreaName()
    {
        return baseAreaName;
    }

    public void setBaseAreaName(String baseAreaName)
    {
        this.baseAreaName = baseAreaName;
    }

    public Integer getTaskType()
    {
        return taskType;
    }

    public void setTaskType(Integer taskType)
    {
        this.taskType = taskType;
    }

    public String getTaskDesc()
    {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc)
    {
        this.taskDesc = taskDesc;
    }

    public String getStartTime()
    {
        return startTime;
    }

    public void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }

    public String getStopTime()
    {
        return stopTime;
    }

    public void setStopTime(String stopTime)
    {
        this.stopTime = stopTime;
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

    public Integer getMarketUser()
    {
        return marketUser;
    }

    public void setMarketUser(Integer marketUser)
    {
        this.marketUser = marketUser;
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

    public String getAccessNumber()
    {
        return accessNumber;
    }

    public void setAccessNumber(String accessNumber)
    {
        this.accessNumber = accessNumber;
    }

    public String getMarketContentText()
    {
        return marketContentText;
    }

    public void setMarketContentText(String marketContentText)
    {
        this.marketContentText = marketContentText;
    }

    public String getMarketContentExtend()
    {
        return marketContentExtend;
    }

    public void setMarketContentExtend(String marketContentExtend)
    {
        this.marketContentExtend = marketContentExtend;
    }

    public String getIsAddAutograph()
    {
        return isAddAutograph;
    }

    public void setIsAddAutograph(String isAddAutograph)
    {
        this.isAddAutograph = isAddAutograph;
    }

    public String getMarketUrl()
    {
        return marketUrl;
    }

    public void setMarketUrl(String marketUrl)
    {
        this.marketUrl = marketUrl;
    }

    public Integer getSendInterval()
    {
        return sendInterval;
    }

    public void setSendInterval(Integer sendInterval)
    {
        this.sendInterval = sendInterval;
    }

    public Integer getSceneType()
    {
        return sceneType;
    }

    public void setSceneType(Integer sceneType)
    {
        this.sceneType = sceneType;
    }

    public String getSceneTypeName()
    {
        return sceneTypeName;
    }

    public void setSceneTypeName(String sceneTypeName)
    {
        this.sceneTypeName = sceneTypeName;
    }

    public String getMonitorArea()
    {
        return monitorArea;
    }

    public void setMonitorArea(String monitorArea)
    {
        this.monitorArea = monitorArea;
    }

    public Integer getMonitorInterval()
    {
        return monitorInterval;
    }

    public String getAppointUserDesc()
    {
        return appointUserDesc;
    }

    public void setAppointUserDesc(String appointUserDesc)
    {
        this.appointUserDesc = appointUserDesc;
    }

    public String getBlackUserDesc()
    {
        return blackUserDesc;
    }

    public void setBlackUserDesc(String blackUserDesc)
    {
        this.blackUserDesc = blackUserDesc;
    }

    public void setMonitorInterval(Integer monitorInterval)
    {
        this.monitorInterval = monitorInterval;
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

    public Integer getTriggerChannelId()
    {
        return triggerChannelId;
    }

    public void setTriggerChannelId(Integer triggerChannelId)
    {
        this.triggerChannelId = triggerChannelId;
    }

    public Integer getBaseAreaId()
    {
        return baseAreaId;
    }

    public void setBaseAreaId(Integer baseAreaId)
    {
        this.baseAreaId = baseAreaId;
    }

    public String getBaseAreaTypes()
    {
        return baseAreaTypes;
    }

    public void setBaseAreaTypes(String baseAreaTypes)
    {
        this.baseAreaTypes = baseAreaTypes;
    }

    public String getBaseAreaTypeNames()
    {
        return baseAreaTypeNames;
    }

    public void setBaseAreaTypeNames(String baseAreaTypeNames)
    {
        this.baseAreaTypeNames = baseAreaTypeNames;
    }

    public Integer getTaskWeight()
    {
        return taskWeight;
    }

    public void setTaskWeight(Integer taskWeight)
    {
        this.taskWeight = taskWeight;
    }

    public Integer getIsSendReport()
    {
        return isSendReport;
    }

    public void setIsSendReport(Integer isSendReport)
    {
        this.isSendReport = isSendReport;
    }

    public Long getReportPhone()
    {
        return reportPhone;
    }

    public void setReportPhone(Long reportPhone)
    {
        this.reportPhone = reportPhone;
    }

    public Integer getMarketLimit()
    {
        return marketLimit;
    }

    public void setMarketLimit(Integer marketLimit)
    {
        this.marketLimit = marketLimit;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public Date getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime)
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

    public String getBaseIds()
    {
        return baseIds;
    }

    public void setBaseIds(String baseIds)
    {
        this.baseIds = baseIds;
    }

    public String getBaseNames()
    {
        return baseNames;
    }

    public void setBaseNames(String baseNames)
    {
        this.baseNames = baseNames;
    }

    public String getCreateUser()
    {
        return createUser;
    }

    public void setCreateUser(String createUser)
    {
        this.createUser = createUser;
    }

    public Integer getpTaskId()
    {
        return pTaskId;
    }

    public void setpTaskId(Integer pTaskId)
    {
        this.pTaskId = pTaskId;
    }

    public Integer getMarketUserMum()
    {
        return marketUserMum;
    }

    public void setMarketUserMum(Integer marketUserMum)
    {
        this.marketUserMum = marketUserMum;
    }

    public Integer getSmsTaskId()
    {
        return smsTaskId;
    }

    public void setSmsTaskId(Integer smsTaskId)
    {
        this.smsTaskId = smsTaskId;
    }

    public String getAimSubId()
    {
        return aimSubId;
    }

    public void setAimSubId(String aimSubId)
    {
        this.aimSubId = aimSubId;
    }

    public String getDepartTypeCode()
    {
        return departTypeCode;
    }

    public void setDepartTypeCode(String departTypeCode)
    {
        this.departTypeCode = departTypeCode;
    }

    public String getExecuteUserName()
    {
        return executeUserName;
    }

    public void setExecuteUserName(String executeUserName)
    {
        this.executeUserName = executeUserName;
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

    public String getAimSubName()
    {
        return aimSubName;
    }

    public void setAimSubName(String aimSubName)
    {
        this.aimSubName = aimSubName;
    }

    public String getMessageAutograph()
    {
        return messageAutograph;
    }

    public void setMessageAutograph(String messageAutograph)
    {
        this.messageAutograph = messageAutograph;
    }

    public Integer getBaseAreaType()
    {
        return baseAreaType;
    }

    public void setBaseAreaType(Integer baseAreaType)
    {
        this.baseAreaType = baseAreaType;
    }

    public String getTaskFileName()
    {
        return taskFileName;
    }

    public void setTaskFileName(String taskFileName)
    {
        this.taskFileName = taskFileName;
    }

    public String getBaseCodes()
    {
        return baseCodes;
    }

    public void setBaseCodes(String baseCodes)
    {
        this.baseCodes = baseCodes;
    }

    public String getCreateTimeStr()
    {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr)
    {
        this.createTimeStr = createTimeStr;
    }

    public Integer getTaskClassifyId() {
        return taskClassifyId;
    }

    public void setTaskClassifyId(Integer taskClassifyId) {
        this.taskClassifyId = taskClassifyId;
    }

    public String getMarketSegmentIds() {
        return marketSegmentIds;
    }

    public void setMarketSegmentIds(String marketSegmentIds) {
        this.marketSegmentIds = marketSegmentIds;
    }

    public String getMarketSegmentNames() {
        return marketSegmentNames;
    }

    public void setMarketSegmentNames(String marketSegmentNames) {
        this.marketSegmentNames = marketSegmentNames;
    }

    public String getManruRange() {
        return manruRange;
    }

    public void setManruRange(String manruRange) {
        this.manruRange = manruRange;
    }

    public String getMarketContentId() {
        return marketContentId;
    }

    public void setMarketContentId(String marketContentId) {
        this.marketContentId = marketContentId;
    }
}
