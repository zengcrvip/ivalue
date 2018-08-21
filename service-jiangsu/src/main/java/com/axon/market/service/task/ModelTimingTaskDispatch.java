package com.axon.market.service.task;

import com.axon.market.common.timer.FixedDelayTask;
import com.axon.market.common.timer.NoRedoIntervalTask;
import com.axon.market.common.timer.Timer;
import com.axon.market.common.timer.TimerTask;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.core.task.MonitorRuleModelJob;
import com.axon.market.core.task.MonitorTagRefreshTask;

import java.text.ParseException;

/**
 * 模型相关的定时任务
 * Created by zengcr on 2017/6/15.
 */
public class ModelTimingTaskDispatch
{
    /**
     * 定时刷新模型标签
     */
    public  void initTagRefreshTask(Timer timer)
    {
        // 系统配置中读取监控间隔时间
        int intervalMills = 300 * 1000;
        TimerTask tagTask = new FixedDelayTask("monitor_import_tag", null, null, intervalMills, new MonitorTagRefreshTask());
        timer.addTask(tagTask);
    }

    /**
     *  定时刷新模型
     */
    public void initMonitorModelRefreshTask(Timer timer) throws ParseException
    {
        int intervalMills = 86400 * 1000;
        //规则创建
        TimerTask ruleTask = new NoRedoIntervalTask("monitor_rule_segment", TimeUtil.formatDate("2017-01-01 04:00:00"), null, intervalMills, new MonitorRuleModelJob());
        timer.addTask(ruleTask);
    }

}
