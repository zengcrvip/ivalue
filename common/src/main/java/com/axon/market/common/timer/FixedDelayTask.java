package com.axon.market.common.timer;

import org.springframework.util.Assert;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by yangyang on 2016/6/8.
 */
public class FixedDelayTask extends TimerTask
{

    /**
     * 任务间隔时间
     */
    private long intervalMills;

    /**
     * 第一次就启动
     */
    private AtomicBoolean first = new AtomicBoolean(true);

    /**
     * @param startTime
     * @param endTime
     * @param intervalMills
     * @param task
     */
    public FixedDelayTask(String taskName, Date startTime, Date endTime, long intervalMills, RunJob task)
    {
        super(taskName, startTime, endTime, task);
        Assert.isTrue(intervalMills > 0, "intervalMills must > 0");
        this.intervalMills = intervalMills;
    }

    /**
     * @return
     */
    public long getNextTheoreticalExecuteTime()
    {
        if (first.compareAndSet(true, false))
        {
            return 0;
        }

        long startMills = System.currentTimeMillis() + intervalMills;
        if (startMills >= startTime.getTime() && (endTime == null || startMills <= endTime.getTime()))
        {
            return startMills;
        }
        return -1;
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
