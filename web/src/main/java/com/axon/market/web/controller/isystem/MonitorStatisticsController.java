package com.axon.market.web.controller.isystem;

import com.axon.market.common.bean.Table;
import com.axon.market.core.service.isystem.MonitorStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by xuan on 2017/4/19.
 */
@Controller("monitorStatisticsController")
public class MonitorStatisticsController
{
    @Autowired
    @Qualifier("monitorStatisticsService")
    private MonitorStatisticsService monitorStatisticsService;


    /**
     * 列表查询
     * @param param
     * @param session
     * @return
     */
    @RequestMapping(value = "queryMonitorStatistics.view", method = RequestMethod.POST)
    @ResponseBody
    public Table queryMonitorStatistics(@RequestParam Map<String, String> param, HttpSession session)
    {
        return monitorStatisticsService.queryMonitorStatistics(param);
    }
}
