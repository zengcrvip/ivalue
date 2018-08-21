package com.axon.market.common.domain.scheduling;

/**
 * 营销活动任务同步表
 * Created by zengcr on 2016/11/18.
 */
public class PTaskDomain
{
    //主键，自增长
    private Long id;
    //营销活动名称
    private String taskTitle;
    //营销活动中的链接
    private String taskUrl;
    //营销活动短信内容
    private String content;
    //对应cdr表的内容id
    private Long cId;
    //营销发送时间，默认当前时间
    private String exeTime;
    //推送用户数
    private Integer userNum;
    //查询号码清单的sql语句
    private String whereStr;
    //接入号
    private String spNum;
    //是否执行
    private Integer ifExecute;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getTaskTitle()
    {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle)
    {
        this.taskTitle = taskTitle;
    }

    public String getTaskUrl()
    {
        return taskUrl;
    }

    public void setTaskUrl(String taskUrl)
    {
        this.taskUrl = taskUrl;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public Long getcId()
    {
        return cId;
    }

    public void setcId(Long cId)
    {
        this.cId = cId;
    }

    public String getExeTime()
    {
        return exeTime;
    }

    public void setExeTime(String exeTime)
    {
        this.exeTime = exeTime;
    }

    public Integer getUserNum()
    {
        return userNum;
    }

    public void setUserNum(Integer userNum)
    {
        this.userNum = userNum;
    }

    public String getWhereStr()
    {
        return whereStr;
    }

    public void setWhereStr(String whereStr)
    {
        this.whereStr = whereStr;
    }

    public String getSpNum()
    {
        return spNum;
    }

    public void setSpNum(String spNum)
    {
        this.spNum = spNum;
    }

    public Integer getIfExecute()
    {
        return ifExecute;
    }

    public void setIfExecute(Integer ifExecute)
    {
        this.ifExecute = ifExecute;
    }
}
