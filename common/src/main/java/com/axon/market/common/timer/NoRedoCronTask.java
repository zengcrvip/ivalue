package com.axon.market.common.timer;

import org.apache.log4j.Logger;
import org.quartz.CronExpression;
import org.springframework.util.Assert;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by yangyang on 2016/6/6.
 */
public class NoRedoCronTask extends TimerTask
{

    /**
     * 日志类
     */
    private static final Logger LOG = Logger.getLogger(NoRedoCronTask.class.getName());

    /**
     * 任务间隔时间
     */
    private String cronValue;

    /**
     *
     * @param startTime
     * @param endTime
     * @param cronValue
     * @param task
     */
    public NoRedoCronTask(String taskName, Date startTime, Date endTime, String cronValue, RunJob task)
    {
        super(taskName, startTime, endTime, task);
        Assert.notNull(cronValue);
        this.cronValue = cronValue;
    }

    /**
     *
     * @return
     */
    public long getNextTheoreticalExecuteTime()
    {
        Date now = new Date();
        Date nextTime = startTime;
        if(now.after(nextTime))
        {
            nextTime = now;
        }

        try
        {
            CronExpression cron = new CronExpression(cronValue);
            if(!cron.isSatisfiedBy(nextTime))
            {
                nextTime = cron.getNextValidTimeAfter(startTime);
            }
            if(endTime == null || nextTime.getTime() <= endTime.getTime())
            {
                return nextTime.getTime();
            }
            else
            {
                return -1;
            }
        }
        catch (ParseException e)
        {
            LOG.error("getNextTheoreticalExecuteTime erro"+taskName,e);
            return -1;
        }
    }

    public String getCronValue()
    {
        return cronValue;
    }

    public void setCronValue(String cronValue)
    {
        this.cronValue = cronValue;
    }

}
