package com.axon.market.common.timer;

import org.springframework.util.Assert;

import java.util.Date;

/**
 * Created by yangyang on 2016/6/7.
 */
public abstract class TimerTask
{
    protected final String taskName;

    protected final Date startTime;

    protected final Date endTime;

    protected final RunJob job;

    public TimerTask(String taskName, Date startTime, Date endTime, RunJob job)
    {
        Assert.notNull(taskName, "task name can not be null");
        Assert.notNull(job,"task can not be null");
        this.taskName = taskName;
        this.startTime = (startTime == null? new Date():new Date(startTime.getTime()));
        this.endTime =  (endTime == null? null:new Date(endTime.getTime()));
        this.job = job;
    }

    abstract long getNextTheoreticalExecuteTime();

    public String getTaskName()
    {
        return taskName;
    }

    public Date getStartTime()
    {
        return new Date(startTime.getTime());
    }

    public Date getEndTime()
    {
        return new Date(endTime.getTime());
    }

    public RunJob getJob()
    {
        return job;
    }

}
