package com.axon.market.service.task;

import com.axon.market.common.timer.FixedDelayTask;
import com.axon.market.common.timer.NoRedoIntervalTask;
import com.axon.market.common.timer.Timer;
import com.axon.market.common.timer.TimerTask;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.core.task.*;

import java.text.ParseException;

/**
 * 炒店定时任务调度
 * Created by zengcr on 2017/6/15.
 */
public class ShopTimingTaskDispatch
{
    /**
     * 炒店常驻用户按13个地市切片
     * 每天凌晨6点执行一次
     * @throws ParseException
     */
    public  void initShopUserClassificationTask(Timer timer) throws ParseException
    {
        int intervalMills = 86400 * 1000;
        TimerTask userTask = new NoRedoIntervalTask("user_area_classification_task", TimeUtil.formatDate("2017-04-02 06:00:00"), null, intervalMills, new UserAreaClassificationTask());
        timer.addTask(userTask);
    }

    /**
     * 炒店任务池待办任务监控
     * 每5分钟执行一次
     * @throws ParseException
     */
    public void initMonitorShopTask(Timer timer) throws ParseException
    {
        int intervalMills = 5 * 60 * 1000;
        TimerTask shopTask = new FixedDelayTask("shop_task", null, null, intervalMills, new MonitorShopTask());
        timer.addTask(shopTask);

        intervalMills = 86400 * 1000;
        TimerTask shopAfternoonBusyTaskTask = new NoRedoIntervalTask("shop_afternoon_busy_task", TimeUtil.formatDate("2017-01-01 11:30:00"), null, intervalMills, new MonitorShopAfternoonBusyTask());
        timer.addTask(shopAfternoonBusyTaskTask);

        TimerTask shopNightBusyTask = new NoRedoIntervalTask("shop_night_busy_task", TimeUtil.formatDate("2017-01-01 15:00:00"), null, intervalMills, new MonitorShopNightBusyTask());
        timer.addTask(shopNightBusyTask);
    }

    /**
     * 每天个性化任务数据处理
     * @throws ParseException
     */
    public void initTruncateDataTask(Timer timer) throws ParseException
    {
        int intervalMills = 86400 * 1000;
        TimerTask truncateDataTask = new NoRedoIntervalTask("truncate_data_task", TimeUtil.formatDate("2017-01-01 00:15:00"), null, intervalMills, new TruncateShopTaskData());
        timer.addTask(truncateDataTask);
    }

    /**
     * 将地区的号码文件加载到redis缓存
     * @throws ParseException
     */
    public  void initPhoneSegmentToRedis(Timer timer) throws ParseException
    {
        int intervalMills = 86400 * 1000;
        TimerTask monitorPhoneSegmentTask = new NoRedoIntervalTask("monitor_phoneSegment_task", TimeUtil.formatDate("2017-05-03 05:00:00"), null, intervalMills, new MonitorPhoneSegmentTask());
        timer.addTask(monitorPhoneSegmentTask);
    }

    /**
     * 定期八天检查营业厅执行情况
     * @throws ParseException
     */
    public void initShopTaskExecute(Timer timer) throws ParseException
    {
        int intervalMills = 86400 * 1000;
        TimerTask monitorExecuteShopTask = new NoRedoIntervalTask("monitor_Execute_ShopTask", TimeUtil.formatDate("2017-08-08 08:00:00"), null, intervalMills, new MonitorExecuteShopTask());
        timer.addTask(monitorExecuteShopTask);
    }
}
