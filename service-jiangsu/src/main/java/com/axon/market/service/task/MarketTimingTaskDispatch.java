package com.axon.market.service.task;

import com.axon.market.common.timer.FixedDelayTask;
import com.axon.market.common.timer.NoRedoIntervalTask;
import com.axon.market.common.timer.Timer;
import com.axon.market.common.timer.TimerTask;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.core.task.*;

import java.text.ParseException;

/**
 * 普通营销定时任务调度
 * Created by zengcr on 2017/6/19.
 */
public class MarketTimingTaskDispatch
{
    /**
     * 营销任务池待办任务监控
     * 每5分钟执行一次
     * @throws ParseException
     */
    public void initMonitorMarketTask(Timer timer) throws ParseException
    {
        int intervalMills = 5 * 60 * 1000;
        TimerTask marketTask = new FixedDelayTask("market_task", null, null, intervalMills, new MonitorMarketTask());
        timer.addTask(marketTask);
    }

    /**
     * 每天普通任务数据处理
     * @throws ParseException
     */
    public void initTruncateMarketDataTask(Timer timer) throws ParseException
    {
        int intervalMills = 86400 * 1000;
        TimerTask truncateMarketTaskData = new NoRedoIntervalTask("truncate_market_data_task", TimeUtil.formatDate("2017-01-01 11:38:00"), null, intervalMills, new TruncateMarketTaskData());
        timer.addTask(truncateMarketTaskData);

        int intervalMonth = 86400 * 15 * 1000;
        TimerTask truncateMarketSuccessTaskData = new NoRedoIntervalTask("truncate_market_success_task", TimeUtil.formatDate("2017-01-01 23:30:00"), null, intervalMonth, new TruncateMarketSuccessUserData());
        timer.addTask(truncateMarketSuccessTaskData);
    }

    /**
     * 生成营销任务到任务池
     * @param timer
     * @throws ParseException
     */
    public  void initCreateMarketingTasksToPool(Timer timer) throws ParseException
    {
        int intervalMills = 86400 * 1000;
        TimerTask marketingTask = new NoRedoIntervalTask("create_marketingTasksToPool_task", TimeUtil.formatDate("2017-06-09 01:45:00"), null, intervalMills, new MarketingTasksToPoolTask());
        timer.addTask(marketingTask);

        // 任务池任务目标用户数刷新时间需要晚于模型的刷新时间
        int intervalCountRefresh = 86400 * 1000;
        TimerTask refreshMarketingCountTask = new NoRedoIntervalTask("refresh_marketingPoolTasksCount_task", TimeUtil.formatDate("2017-07-08 03:00:00"), null, intervalCountRefresh, new RefreshMarketingPoolTasksCountTask());
        timer.addTask(refreshMarketingCountTask);
    }


}
