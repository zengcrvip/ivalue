package com.axon.market.common.timer;

import org.springframework.util.Assert;

import java.util.Date;

/**
 * Created by yangyang on 2016/6/6.
 */
public class NoRedoIntervalTask extends TimerTask
{
    /**
     * 任务间隔时间
     */
    private long intervalMills;

    /**
     * @param startTime
     * @param endTime
     * @param intervalMills
     * @param task
     */
    public NoRedoIntervalTask(String taskName, Date startTime, Date endTime, long intervalMills, RunJob task)
    {
        super(taskName, startTime, endTime, task);
        Assert.isTrue(intervalMills > 0, "intervalMills must > 0, error Task : " + taskName);
        this.intervalMills = intervalMills;
    }

    /**
     * @return
     */
    public long getNextTheoreticalExecuteTime()
    {
        long startMills = startTime.getTime();
        long nowMills = System.currentTimeMillis();
        if (startMills < nowMills)
        {
            long count = (nowMills - startMills) / intervalMills;
            startMills += count * intervalMills;
            if (startMills < nowMills)
            {
                startMills += intervalMills;
            }
        }
        if (endTime == null || startMills <= endTime.getTime())
        {
            return startMills;
        }
        else
        {
            return -1;
        }
    }

    public long getIntervalMills()
    {
        return intervalMills;
    }

    public void setIntervalMills(long intervalMills)
    {
        this.intervalMills = intervalMills;
    }

}
