package com.axon.market.common.domain.ischeduling;

/**
 * Created by yuanfei on 2017/6/8.
 */
public class MarketingPoolTaskDomain
{
    /**
     * 日期
     */
    private Integer date;

    private Integer id;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 营销类型任务来源（sms:自建群发任务 scenesms:自建场景任务 jxhsms:精细化群发任务 jxhscene:精细化场景任务
     */
    private String marketType;

    /**
     * 只写入场景规则id market_type=scenesms 参考码表 conf_new_scene
     */
    private String marketTypeValue;

    /**
     * 业务类型：1:互联网综合业务，2：内容营销，3、流量经营 4、APP场景营销
     */
    private Integer businessType;

    /**
     *
     */
    private String sceneSmsName;

    private String accessNumber;

    /**
     * 营销任务话术ID
     */
    private Integer marketContentId;

    /**
     * 营销内容（可以不取自话术表：market.market_content）
     */
    private String marketContent;


    private String startTime;

    private String stopTime;

    private String beginTime;

    private String endTime;

    /**
     * 调度类型(single:固定时间  manu:手动调度)
     */
    private String scheduleType;

    /**
     * 任务触发营销间隔（单位：天）
     */
    private Integer sendInterval;

    /**
     * 单个任务的号码去重策略间隔N天营销一次，取值：1,3,5,7  单位：天
     */
    private Integer repeatStrategy;

    private String marketSegmentIds;

    private String marketSegmentNames;

    private Integer createUser;

    private String createUserName;

    private String createTime;

    private String updateTime;

    /**
     * 任务状态 参考码表 market.shop_task_status
     */
    private Integer status;

    /**
     * 是否首次执行的状态
     */
    private Integer isFistStatus;

    private String areaCodes;

    private String areaNames;

    private Integer marketUserCountLimit;

    /**
     * 监控任务ID，对应老平台任务id
     */
    private Integer lastTaskId;

    /**
     * 精细化任务活动编码
     */
    private String saleId;

    /**
     * 精细化任务波次编码
     */
    private String saleBoidId;

    /**
     * 精细化任务目标客户群编码
     */
    private String aimSubId;

    private Integer marketNums;

    /**
     * 是否分批次发送 0：否；1：是
     */
    private Integer isBoidSale;

    /**
     * 待发送的用户数目
     */
    private Integer targetNums;

    /**
     * 营销人数
     */
    private Integer marketSegmentUserCounts;

    private String remarks;

    public Integer getDate()
    {
        return date;
    }

    public void setDate(Integer date)
    {
        this.date = date;
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

    public String getMarketType()
    {
        return marketType;
    }

    public void setMarketType(String marketType)
    {
        this.marketType = marketType;
    }

    public String getMarketTypeValue()
    {
        return marketTypeValue;
    }

    public void setMarketTypeValue(String marketTypeValue)
    {
        this.marketTypeValue = marketTypeValue;
    }

    public Integer getBusinessType()
    {
        return businessType;
    }

    public void setBusinessType(Integer businessType)
    {
        this.businessType = businessType;
    }

    public String getSceneSmsName()
    {
        return sceneSmsName;
    }

    public void setSceneSmsName(String sceneSmsName)
    {
        this.sceneSmsName = sceneSmsName;
    }

    public String getAccessNumber()
    {
        return accessNumber;
    }

    public void setAccessNumber(String accessNumber)
    {
        this.accessNumber = accessNumber;
    }

    public Integer getMarketContentId()
    {
        return marketContentId;
    }

    public void setMarketContentId(Integer marketContentId)
    {
        this.marketContentId = marketContentId;
    }

    public String getMarketContent()
    {
        return marketContent;
    }

    public void setMarketContent(String marketContent)
    {
        this.marketContent = marketContent;
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

    public String getScheduleType()
    {
        return scheduleType;
    }

    public void setScheduleType(String scheduleType)
    {
        this.scheduleType = scheduleType;
    }

    public Integer getSendInterval()
    {
        return sendInterval;
    }

    public void setSendInterval(Integer sendInterval)
    {
        this.sendInterval = sendInterval;
    }

    public Integer getRepeatStrategy()
    {
        return repeatStrategy;
    }

    public void setRepeatStrategy(Integer repeatStrategy)
    {
        this.repeatStrategy = repeatStrategy;
    }

    public String getMarketSegmentIds()
    {
        return marketSegmentIds;
    }

    public void setMarketSegmentIds(String marketSegmentIds)
    {
        this.marketSegmentIds = marketSegmentIds;
    }

    public String getMarketSegmentNames()
    {
        return marketSegmentNames;
    }

    public void setMarketSegmentNames(String marketSegmentNames)
    {
        this.marketSegmentNames = marketSegmentNames;
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

    public String getAreaCodes()
    {
        return areaCodes;
    }

    public void setAreaCodes(String areaCodes)
    {
        this.areaCodes = areaCodes;
    }

    public String getAreaNames()
    {
        return areaNames;
    }

    public void setAreaNames(String areaNames)
    {
        this.areaNames = areaNames;
    }

    public Integer getMarketUserCountLimit()
    {
        return marketUserCountLimit;
    }

    public void setMarketUserCountLimit(Integer marketUserCountLimit)
    {
        this.marketUserCountLimit = marketUserCountLimit;
    }

    public Integer getLastTaskId()
    {
        return lastTaskId;
    }

    public void setLastTaskId(Integer lastTaskId)
    {
        this.lastTaskId = lastTaskId;
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

    public Integer getMarketNums()
    {
        return marketNums;
    }

    public void setMarketNums(Integer marketNums)
    {
        this.marketNums = marketNums;
    }

    public Integer getIsBoidSale()
    {
        return isBoidSale;
    }

    public void setIsBoidSale(Integer isBoidSale)
    {
        this.isBoidSale = isBoidSale;
    }

    public Integer getTargetNums()
    {
        return targetNums;
    }

    public void setTargetNums(Integer targetNums)
    {
        this.targetNums = targetNums;
    }
	
	 public Integer getIsFistStatus()
     {
        return isFistStatus;
    }

    public void setIsFistStatus(Integer isFistStatus)
    {
        this.isFistStatus = isFistStatus;
    }

    public Integer getMarketSegmentUserCounts()
    {
        return marketSegmentUserCounts;
    }

    public void setMarketSegmentUserCounts(Integer marketSegmentUserCounts)
    {
        this.marketSegmentUserCounts = marketSegmentUserCounts;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
