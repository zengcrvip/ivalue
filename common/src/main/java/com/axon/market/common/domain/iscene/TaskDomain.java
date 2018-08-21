package com.axon.market.common.domain.iscene;

import java.util.Date;

/**
 * Created by hale on 2016/12/9.
 */
public class TaskDomain
{
    private int id;

    /**
     * 任务名称
     */
    private String name;

    private String sceneSID;

    /**
     * 导航图片地址
     */
    private String navigationStyle;

    /**
     * 触发形态 1：全页面导航 2：场景导航
     */
    private int taskType;

    /**
     * 优先级
     */
    private int sort;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 任务状态
     */
    private int taskState;

    /**
     * 是否审核
     */
    private int isAudit;

    /**
     * 导航地址（全页面）
     */
    private String navigationUrl;

    /**
     * 场景ID
     */
    private String sID;

    /**
     * 投放间隔
     */
    private int impression;

    /**
     * 每日开始时段
     */
    private String onLineTm;

    /**
     * 每日结束时段
     */
    private String offLineTm;

    /**
     * 网址分类ID
     */
    private String urlGroupIds;

    /**
     * 用户群组ID
     */
    private String userGroupIds;

    /**
     * 区域ID
     */
    private String locationGroupIds;

    /**
     * 任务模式：0：白名单 1：黑名单
     */
    private int blockMode;

    /**
     * 配置的附加结束条件字符串
     */
    private String extStopCond;

    /**
     * 省市
     */
    private int provinceId;

    /**
     * 是否删除
     */
    private int isDelete;

    /**
     * 创建人ID
     */
    private int createId;

    /**
     * 修改人
     */
    private int editUserId;

    /**
     * 修改人名称
     */
    private String editUserName;

    /**
     * 修改时间
     */
    private Date editTime;

    //扩展属性

    /**
     * 网址分类名称
     */
    private String urlGroupNames;

    /**
     * 用户群组名称
     */
    private String userGroupNames;

    /**
     * 区域名称
     */
    private String locationGroupNames;

    /**
     * 任务状态名称
     */
    private String taskStateName;

    /**
     * stats表 ExtStopCond字段
     */
    private String extStopCondStats;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
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

    public String getSceneSID()
    {
        return sceneSID;
    }

    public void setSceneSID(String sceneSID)
    {
        this.sceneSID = sceneSID;
    }

    public String getNavigationStyle()
    {
        return navigationStyle;
    }

    public void setNavigationStyle(String navigationStyle)
    {
        this.navigationStyle = navigationStyle;
    }

    public int getTaskType()
    {
        return taskType;
    }

    public void setTaskType(int taskType)
    {
        this.taskType = taskType;
    }

    public int getSort()
    {
        return sort;
    }

    public void setSort(int sort)
    {
        this.sort = sort;
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

    public int getTaskState()
    {
        return taskState;
    }

    public void setTaskState(int taskState)
    {
        this.taskState = taskState;
    }

    public int getIsAudit()
    {
        return isAudit;
    }

    public void setIsAudit(int isAudit)
    {
        this.isAudit = isAudit;
    }

    public String getNavigationUrl()
    {
        return navigationUrl;
    }

    public void setNavigationUrl(String navigationUrl)
    {
        this.navigationUrl = navigationUrl;
    }

    public String getsID()
    {
        return sID;
    }

    public void setsID(String sID)
    {
        this.sID = sID;
    }

    public int getImpression()
    {
        return impression;
    }

    public void setImpression(int impression)
    {
        this.impression = impression;
    }

    public String getOnLineTm()
    {
        return onLineTm;
    }

    public void setOnLineTm(String onLineTm)
    {
        this.onLineTm = onLineTm;
    }

    public String getOffLineTm()
    {
        return offLineTm;
    }

    public void setOffLineTm(String offLineTm)
    {
        this.offLineTm = offLineTm;
    }

    public String getUrlGroupIds()
    {
        return urlGroupIds;
    }

    public void setUrlGroupIds(String urlGroupIds)
    {
        this.urlGroupIds = urlGroupIds;
    }

    public String getUserGroupIds()
    {
        return userGroupIds;
    }

    public void setUserGroupIds(String userGroupIds)
    {
        this.userGroupIds = userGroupIds;
    }

    public String getLocationGroupIds()
    {
        return locationGroupIds;
    }

    public void setLocationGroupIds(String locationGroupIds)
    {
        this.locationGroupIds = locationGroupIds;
    }

    public int getBlockMode()
    {
        return blockMode;
    }

    public void setBlockMode(int blockMode)
    {
        this.blockMode = blockMode;
    }

    public String getExtStopCond()
    {
        return extStopCond;
    }

    public void setExtStopCond(String extStopCond)
    {
        this.extStopCond = extStopCond;
    }

    public int getProvinceId()
    {
        return provinceId;
    }

    public void setProvinceId(int provinceId)
    {
        this.provinceId = provinceId;
    }

    public int getIsDelete()
    {
        return isDelete;
    }

    public void setIsDelete(int isDelete)
    {
        this.isDelete = isDelete;
    }

    public int getCreateId()
    {
        return createId;
    }

    public void setCreateId(int createId)
    {
        this.createId = createId;
    }

    public int getEditUserId()
    {
        return editUserId;
    }

    public void setEditUserId(int editUserId)
    {
        this.editUserId = editUserId;
    }

    public String getEditUserName()
    {
        return editUserName;
    }

    public void setEditUserName(String editUserName)
    {
        this.editUserName = editUserName;
    }

    public Date getEditTime()
    {
        return editTime;
    }

    public void setEditTime(Date editTime)
    {
        this.editTime = editTime;
    }

    public String getUrlGroupNames()
    {
        return urlGroupNames;
    }

    public void setUrlGroupNames(String urlGroupNames)
    {
        this.urlGroupNames = urlGroupNames;
    }

    public String getUserGroupNames()
    {
        return userGroupNames;
    }

    public void setUserGroupNames(String userGroupNames)
    {
        this.userGroupNames = userGroupNames;
    }

    public String getLocationGroupNames()
    {
        return locationGroupNames;
    }

    public void setLocationGroupNames(String locationGroupNames)
    {
        this.locationGroupNames = locationGroupNames;
    }

    public String getTaskStateName()
    {
        return taskStateName;
    }

    public void setTaskStateName(String taskStateName)
    {
        this.taskStateName = taskStateName;
    }

    public String getExtStopCondStats()
    {
        return extStopCondStats;
    }

    public void setExtStopCondStats(String extStopCondStats)
    {
        this.extStopCondStats = extStopCondStats;
    }
}
