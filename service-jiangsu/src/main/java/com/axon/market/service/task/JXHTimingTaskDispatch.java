package com.axon.market.service.task;

import com.axon.market.common.timer.FixedDelayTask;
import com.axon.market.common.timer.NoRedoIntervalTask;
import com.axon.market.common.timer.Timer;
import com.axon.market.common.timer.TimerTask;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.core.task.MonitorJXHChannelToShopTask;
import com.axon.market.core.task.MonitorRecommendationFileTask;
import com.axon.market.core.task.RecommendationReportTask;

import java.text.ParseException;

/**
 * 精细化定时任务调度
 * Created by zengcr on 2017/6/15.
 */
public class JXHTimingTaskDispatch
{
    /**
     * 个性化任务用户文件监控,活动文件和用户文件
     * 每5分钟执行一次
     */
    public void initMonitorRecommendationFileTask(Timer timer)
    {
        int intervalMills = 10 * 60 * 1000;
        TimerTask recommendationFileTask = new FixedDelayTask("recommendation_file_task", null, null, intervalMills, new MonitorRecommendationFileTask());
        timer.addTask(recommendationFileTask);
    }

    /**
     * 每天定期执行，上传精细化文件到ftp服务器
     * @param timer
     * @throws ParseException
     */
    public void initRecommendationReportTask(Timer timer) throws ParseException
    {
        int intervalMills = 86400 * 1000;
        TimerTask recommendationReportTask = new NoRedoIntervalTask("recommendation_report_task", TimeUtil.formatDate("2017-01-01 04:30:00"), null, intervalMills, new RecommendationReportTask());
        timer.addTask(recommendationReportTask);
    }

    public void pushRecommendationChannelToShop(Timer timer)  throws ParseException
    {
        int intervalMills = 5 * 60 * 1000;
        TimerTask channelPushToShopTask = new FixedDelayTask("recommendation_channel_task", null, null, intervalMills, new MonitorJXHChannelToShopTask());
        timer.addTask(channelPushToShopTask);
    }
}
