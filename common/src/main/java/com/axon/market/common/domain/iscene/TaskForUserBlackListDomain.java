package com.axon.market.common.domain.iscene;

import java.util.Date;

/**
 * Created by wangtt on 2017/3/10.
 */
public class TaskForUserBlackListDomain
{
    private int id;//ID序号
    private String taskName;//任务名
    private String imgUrl;//触发形态
    private int sceneSort;//优先级
    private Date startTime;//开始时间
    private Date endTime;//结束时间
    private String onLineTm;//开始触发时段
    private String offLineTm;//结束触发时段
    private String urlGroupName;//网址分类
    private String userGroupName;//用户群
    private String locationGroupName;//地理位置
    private int state;//状态

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

    public int getId()
    {
        return id;
    }

    public void setId(int id)
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

    public String getImgUrl()
    {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl)
    {
        this.imgUrl = imgUrl;
    }

    public int getSceneSort()
    {
        return sceneSort;
    }

    public void setSceneSort(int sceneSort)
    {
        this.sceneSort = sceneSort;
    }

    public Date getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    public String getUrlGroupName()
    {
        return urlGroupName;
    }

    public void setUrlGroupName(String urlGroupName)
    {
        this.urlGroupName = urlGroupName;
    }

    public String getUserGroupName()
    {
        return userGroupName;
    }

    public void setUserGroupName(String userGroupName)
    {
        this.userGroupName = userGroupName;
    }

    public String getLocationGroupName()
    {
        return locationGroupName;
    }

    public void setLocationGroupName(String locationGroupName)
    {
        this.locationGroupName = locationGroupName;
    }

    public int getState()
    {
        return state;
    }

    public void setState(int state)
    {
        this.state = state;
    }
}
