package com.axon.market.web.controller.iscene;

import com.axon.market.common.bean.Table;
import com.axon.market.core.service.iscene.TaskStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by xuan on 2017/4/6.
 */
@Controller("taskStatisticsController")
public class TaskStatisticsController
{
    @Autowired
    @Qualifier("taskStatisticsService")
    private TaskStatisticsService taskStatisticsService;

    /**
     * 查询导航任务统计列表
     * @param taskName 任务名称
     * @param startTime 开始时间
     * @param endTime  结束时间
     * @param start
     * @param length
     * @return
     */
    @RequestMapping(value = "getTaskStatisticsList.view", method = RequestMethod.POST)
    @ResponseBody
    public Table getTaskStatisticsList(@RequestParam(value = "taskName", required = false) String taskName,@RequestParam(value = "startTime", required = false) String startTime,@RequestParam(value = "endTime", required = false) String endTime,@RequestParam(value = "type", required = false) int type, @RequestParam(value = "start", required = false) int start, @RequestParam(value = "length", required = false) int length)
    {
        Table table = taskStatisticsService.getTaskStatisticsList(taskName, startTime,endTime,type, start, length);
        return table;
    }
}
