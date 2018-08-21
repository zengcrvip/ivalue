package com.axon.market.common.timer;

import org.springframework.util.Assert;

/**
 * Created by yangyang on 2016/6/17.
 */
public abstract class RunJob
{
    private volatile Long currentTheoreticalExecuteTime;

    public long getCurrentTheoreticalExecuteTime()
    {
        return currentTheoreticalExecuteTime;
    }

    public void setCurrentTheoreticalExecuteTime(long currentTheoreticalExecuteTime)
    {
        this.currentTheoreticalExecuteTime = currentTheoreticalExecuteTime;
    }

    public abstract void runBody();

    public void run()
    {
        Assert.notNull(currentTheoreticalExecuteTime);

        runBody();
    }
}
