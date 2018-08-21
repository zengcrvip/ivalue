package com.axon.market.common.timer;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by yangyang on 2016/6/6.
 */
public class Timer
{

    private static final Logger LOG = Logger.getLogger(Timer.class.getName());

    /**
     * 任务私有类
     */
    private static class InnerTask
    {
        public final TimerTask task;

        public volatile boolean invalid = false;

        public InnerTask(TimerTask task)
        {
            this.task = task;
        }
    }

    /**
     * 保存所有任务
     */
    private Map<String,InnerTask> allTasks = new HashMap<String,InnerTask>();

    /**
     *
     */
    private ScheduledExecutorService executorService;

    /**
     *
     * @param threadPool
     */
    public Timer(Integer threadPool)
    {
        executorService = Executors.newScheduledThreadPool(threadPool);
    }

    /**
     *
     * @param innerTask
     */
    private void doTask(final InnerTask innerTask)
    {
        TimerTask timerTask = innerTask.task;
        final long nextTheoreticalExecuteTime = timerTask.getNextTheoreticalExecuteTime();
        if(nextTheoreticalExecuteTime < 0)
        {
            LOG.info("task is overtime:"+ timerTask.getTaskName());
            //此处不要删除任务，让用户显式调用方法：removeTask
            return;
        }

        executorService.schedule(new Runnable()
        {
            @Override
            public void run()
            {
                // 任务被删除或者被更新（无效状态）
                if (!isTaskExist(innerTask.task.getTaskName()) || innerTask.invalid)
                {
                    return;
                }
                else
                {
                    RunJob runJob = innerTask.task.getJob();
                    runJob.setCurrentTheoreticalExecuteTime(nextTheoreticalExecuteTime);
                    runJob.run();
                    doTask(innerTask);
                }
            }

        }, nextTheoreticalExecuteTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    /**
     *
     * @param taskName
     */
    synchronized public Boolean isTaskExist(final String taskName)
    {
        return allTasks.containsKey(taskName);
    }

    /**
     *
     * @param timerTask
     */
    synchronized public void addTask(final TimerTask timerTask)
    {
        if(allTasks.containsKey(timerTask.getTaskName()))
        {
            throw new RuntimeException("task is exist:"+ timerTask.getTaskName());
        }
        InnerTask newInnerTask = new InnerTask(timerTask);
        allTasks.put(timerTask.getTaskName(), newInnerTask);
        doTask(newInnerTask);
    }


    /**
     *
     * @param timerTask
     */
    synchronized public void removeTask(final TimerTask timerTask)
    {
        if(!allTasks.containsKey(timerTask.getTaskName()))
        {
            throw new RuntimeException("task is not exist:"+ timerTask.getTaskName());
        }
        allTasks.remove(timerTask.getTaskName());
    }

    /**
     *
     * @param timerTask
     */
    synchronized public void updateTask(final TimerTask timerTask)
    {
        InnerTask innerTask = allTasks.get(timerTask.getTaskName());
        if(innerTask == null)
        {
            throw new RuntimeException("task is not exist:"+ timerTask.getTaskName());
        }
        else
        {
            innerTask.invalid = true;
        }
        InnerTask newInnerTask = new InnerTask(timerTask);
        allTasks.put(timerTask.getTaskName(), newInnerTask);
        doTask(newInnerTask);
    }

}
