package com.axon.market.common.domain.ischeduling;

/**
 * Created by yuanfei on 2017/6/6.
 */
public class MarketingTasksDomain
{
    /**
     * 任务ID
     */
    private Integer id;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 营销类型任务来源
     */
    private String marketType;

    /**
     *
     */
    private String marketTypeValue;

    /**
     *
     */
    private String sceneSmsName;

    /**
     * 业务类型：1:互联网综合业务，2：内容营销，3、流量经营 4、APP场景营销
     */
    private Integer businessType;

    /**
     * 接入号
     */
    private String accessNumber;

    /**
     * 营销任务话术ID
     */
    private Integer marketContentId;

    /**
     * 营销内容
     */
    private String marketContent;

    /**
     * 任务开始日期（包含开始日期，例如：2017-06-05）
     */
    private String startTime;

    /**
     * 任务结束日期（包含结束日期有效，例如：2017-06-07）
     */
    private String stopTime;

    /**
     * 监控开始时间（例如：09:00)
     */
    private String beginTime;

    /**
     * 监控结束时间（例如：18:59)
     */
    private String endTime;

    /**
     * 调度类型(single:固定时间  manu:手动调度)
     */
     private String scheduleType;

    /**
     * 只针对schedule_type=manu 有效  值参考linux定时格式： 001***  表示按天调度
     */
    private String cronValue;

    /**
     * 任务触发营销间隔（单位：天）
     */
    private Integer sendInterval;

    /**
     * 测试号码
     */
    private String testPhones;

    /**
     * 单个任务的号码去重策略间隔N天营销一次，取值：1,3,5,7  单位：天
     */
    private Integer repeatStrategy;

    /**
     * 目标客户群名称
     */
    private String marketSegmentNames;

    /**
     * 目标客户群ID
     */
    private String marketSegmentIds;

    /**
     * 目标客户群对应的用户数(非踢重人数)
     */
    private Integer marketSegmentUserCounts;

    /**
     * 创建用户
     */
    private Integer createUser;

    /**
     * 创建人名
     */
    private String createUserName;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改人id
     */
    private Integer lastUpdateUser;

    private String lastUpdateTime;

    /**
     * 下次营销日期
     */
    private String nextMarketTime;

    private Integer status;

    /**
     * 营销地区编码 多个以英文逗号分割
     */
    private String areaCodes;

    /**
     * 地区名称参考码表
     */
    private String areaNames;

    /**
     * 单次限制人数
     */
    private Integer marketUserCountLimit;

    private String remarks;

    /**
     * 场景导航任务优先级
     */
    private Integer scenePilotSort;

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

    /**
     * 是否波次营销
     */
    private Integer isBoidSale;

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

    public String getSceneSmsName()
    {
        return sceneSmsName;
    }

    public void setSceneSmsName(String sceneSmsName)
    {
        this.sceneSmsName = sceneSmsName;
    }

    public Integer getBusinessType()
    {
        return businessType;
    }

    public void setBusinessType(Integer businessType)
    {
        this.businessType = businessType;
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

    public String getCronValue()
    {
        return cronValue;
    }

    public void setCronValue(String cronValue)
    {
        this.cronValue = cronValue;
    }

    public Integer getSendInterval()
    {
        return sendInterval;
    }

    public void setSendInterval(Integer sendInterval)
    {
        this.sendInterval = sendInterval;
    }

    public String getTestPhones()
    {
        return testPhones;
    }

    public void setTestPhones(String testPhones)
    {
        this.testPhones = testPhones;
    }

    public Integer getRepeatStrategy()
    {
        return repeatStrategy;
    }

    public void setRepeatStrategy(Integer repeatStrategy)
    {
        this.repeatStrategy = repeatStrategy;
    }

    public String getMarketSegmentNames()
    {
        return marketSegmentNames;
    }

    public void setMarketSegmentNames(String marketSegmentNames)
    {
        this.marketSegmentNames = marketSegmentNames;
    }

    public String getMarketSegmentIds()
    {
        return marketSegmentIds;
    }

    public void setMarketSegmentIds(String marketSegmentIds)
    {
        this.marketSegmentIds = marketSegmentIds;
    }

    public Integer getMarketSegmentUserCounts()
    {
        return marketSegmentUserCounts;
    }

    public void setMarketSegmentUserCounts(Integer marketSegmentUserCounts)
    {
        this.marketSegmentUserCounts = marketSegmentUserCounts;
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

    public Integer getLastUpdateUser()
    {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(Integer lastUpdateUser)
    {
        this.lastUpdateUser = lastUpdateUser;
    }

    public String getLastUpdateTime()
    {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime)
    {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getNextMarketTime()
    {
        return nextMarketTime;
    }

    public void setNextMarketTime(String nextMarketTime)
    {
        this.nextMarketTime = nextMarketTime;
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

    public String getRemarks()
    {
        return remarks;
    }

    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }

    public Integer getScenePilotSort()
    {
        return scenePilotSort;
    }

    public void setScenePilotSort(Integer scenePilotSort)
    {
        this.scenePilotSort = scenePilotSort;
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

    public Integer getIsBoidSale() {
        return isBoidSale;
    }

    public void setIsBoidSale(Integer isBoidSale) {
        this.isBoidSale = isBoidSale;
    }
}
