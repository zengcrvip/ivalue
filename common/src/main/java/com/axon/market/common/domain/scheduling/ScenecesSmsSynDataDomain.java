package com.axon.market.common.domain.scheduling;

import com.axon.market.common.domain.iscene.ScenceSmsRuleDomain;

/**
 * Created by fgtg on 2016/11/17.
 */
public class ScenecesSmsSynDataDomain
{
    //唯一
    private Integer id;

    //唯一
    private String name;

    //分类，给营销任务归类
    private String catalogId;

    //营销任务归类目录名称
    private String catalogName;

    //短信、彩信、位置营销等
    private String marketType;

    private String marketTypeValue;

    //营销人数上限
    private Integer marketUserCountLimit;

    //接入号（短信、彩信）
    private String accessNumber;

    //内容（短信、彩信）
    private String marketContent;

    //cron、simple、manual
    private String scheduleType;

    //scheduleType = simple
    //不能为int，影响updateMarketJob方法
    private Integer intervalInSeconds;

    //scheduleType = cron
    private String cronValue;

    //测试号码
    private String testPhones;

    //重复策略
    private String repeatStrategy;

    //营销目标客户群
    private String marketSegmentNames;

    private String marketSegmentIds;

    private int segmentUserCounts;

    private String createTime;

    private Integer lastUpdateUser;

    private String lastUpdateTime;

    private String startTime;

    private String endTime;

    //标示集群中，哪个主机对该任务进行了最后调度
    private String marketNodeName;

    //上次调度时间
    private String lastMarketTime;

    //下次调度时间
    private String nextMarketTime;

    //上次调度成功时间
    private String lastSuccessMarketTime;

    //创建用户
    private String createUser;

    //关联用户的原始用户
    private Integer originUser;

    //是否调度中
    private String status;

    private Integer lastMarketUserCount;

    private String remarks;

    private Integer marketSmsContentId;

    private ScenceSmsRuleDomain scenceSmsRuleDomain;

    public ScenceSmsRuleDomain getScenceSmsRuleDomain()
    {
        return scenceSmsRuleDomain;
    }

    public void setScenceSmsRuleDomain(ScenceSmsRuleDomain scenceSmsRuleDomain)
    {
        this.scenceSmsRuleDomain = scenceSmsRuleDomain;
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

    public Integer getMarketUserCountLimit()
    {
        return marketUserCountLimit;
    }

    public void setMarketUserCountLimit(Integer marketUserCountLimit)
    {
        this.marketUserCountLimit = marketUserCountLimit;
    }

    public String getAccessNumber()
    {
        return accessNumber;
    }

    public void setAccessNumber(String accessNumber)
    {
        this.accessNumber = accessNumber;
    }

    public String getMarketContent()
    {
        return marketContent;
    }

    public void setMarketContent(String marketContent)
    {
        this.marketContent = marketContent;
    }

    public String getScheduleType()
    {
        return scheduleType;
    }

    public void setScheduleType(String scheduleType)
    {
        this.scheduleType = scheduleType;
    }

    public Integer getIntervalInSeconds()
    {
        return intervalInSeconds;
    }

    public void setIntervalInSeconds(Integer intervalInSeconds)
    {
        this.intervalInSeconds = intervalInSeconds;
    }

    public String getCronValue()
    {
        return cronValue;
    }

    public void setCronValue(String cronValue)
    {
        this.cronValue = cronValue;
    }

    public String getTestPhones()
    {
        return testPhones;
    }

    public void setTestPhones(String testPhones)
    {
        this.testPhones = testPhones;
    }

    public String getRepeatStrategy()
    {
        return repeatStrategy;
    }

    public void setRepeatStrategy(String repeatStrategy)
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

    public int getSegmentUserCounts()
    {
        return segmentUserCounts;
    }

    public void setSegmentUserCounts(int segmentUserCounts)
    {
        this.segmentUserCounts = segmentUserCounts;
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

    public String getMarketNodeName()
    {
        return marketNodeName;
    }

    public void setMarketNodeName(String marketNodeName)
    {
        this.marketNodeName = marketNodeName;
    }

    public String getLastMarketTime()
    {
        return lastMarketTime;
    }

    public void setLastMarketTime(String lastMarketTime)
    {
        this.lastMarketTime = lastMarketTime;
    }

    public String getNextMarketTime()
    {
        return nextMarketTime;
    }

    public void setNextMarketTime(String nextMarketTime)
    {
        this.nextMarketTime = nextMarketTime;
    }

    public String getLastSuccessMarketTime()
    {
        return lastSuccessMarketTime;
    }

    public void setLastSuccessMarketTime(String lastSuccessMarketTime)
    {
        this.lastSuccessMarketTime = lastSuccessMarketTime;
    }

    public String getCreateUser()
    {
        return createUser;
    }

    public void setCreateUser(String createUser)
    {
        this.createUser = createUser;
    }

    public Integer getOriginUser()
    {
        return originUser;
    }

    public void setOriginUser(Integer originUser)
    {
        this.originUser = originUser;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public Integer getLastMarketUserCount()
    {
        return lastMarketUserCount;
    }

    public void setLastMarketUserCount(Integer lastMarketUserCount)
    {
        this.lastMarketUserCount = lastMarketUserCount;
    }

    public String getRemarks()
    {
        return remarks;
    }

    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }

    public Integer getMarketSmsContentId()
    {
        return marketSmsContentId;
    }

    public void setMarketSmsContentId(Integer marketSmsContentId)
    {
        this.marketSmsContentId = marketSmsContentId;
    }
}
