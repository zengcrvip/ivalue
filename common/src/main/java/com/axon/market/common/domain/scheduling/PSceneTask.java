package com.axon.market.common.domain.scheduling;

/**
 * Created by zengcr on 2016/11/18.
 */
public class PSceneTask
{
    private Long id;
    private Long taskId;
    private String sceneId;
    private String beginTime;
    private String endTime;
    private String whereStr;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getTaskId()
    {
        return taskId;
    }

    public void setTaskId(Long taskId)
    {
        this.taskId = taskId;
    }

    public String getSceneId()
    {
        return sceneId;
    }

    public void setSceneId(String sceneId)
    {
        this.sceneId = sceneId;
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

    public String getWhereStr()
    {
        return whereStr;
    }

    public void setWhereStr(String whereStr)
    {
        this.whereStr = whereStr;
    }
}
