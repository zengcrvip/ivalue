package com.axon.market.web.controller.ishop;

import com.axon.market.common.bean.Table;
import com.axon.market.common.util.SearchConditionUtil;
import com.axon.market.core.service.ishop.JXHTaskStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by wangtt on 2017/6/14.
 */
@Controller("JXHTaskStatisticsController")
public class JXHTaskStatisticsController
{
    @Autowired
    @Qualifier("JXHTaskStatisticsService")
    JXHTaskStatisticsService JXHService;

    /**
     * 查询
     * @param param
     * @return
     */
    @RequestMapping("queryJXHTaskStatistic.view")
    @ResponseBody
    public Table<Map<String,Object>> queryJXHTask(@RequestParam Map<String,String> param){
        //对特殊字符转义
        String newTaskName = SearchConditionUtil.optimizeCondition(param.get("taskName"));
        param.put("taskName",newTaskName);
        return JXHService.queryJXHTaskStatistics(param);
    }


    /**
     * 下载报表
     * @param param
     * @param request
     * @param response
     * @param session
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "downloadJXHTaskStatistics.view",method = RequestMethod.POST)
    public void downloadJXHTaskStatistics(@RequestParam Map<String,String> param,HttpServletRequest request,HttpServletResponse response,HttpSession session)
    {
        param.put("start",null);
        param.put("length",null);
        JXHService.downloadJXHTaskStatistics(param,request,response,session);
    }





}
