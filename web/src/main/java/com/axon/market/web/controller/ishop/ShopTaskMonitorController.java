package com.axon.market.web.controller.ishop;

import com.axon.market.common.bean.Table;
import com.axon.market.core.service.ishop.ShopTaskMonitorService;
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
 * Created by wangtt on 2017/6/3.
 */

@Controller("shopTaskMonitorController")
public class ShopTaskMonitorController
{
    @Autowired
    @Qualifier("shopTaskMonitorService")
    ShopTaskMonitorService shopTaskMonitorService;

    /**
     * 查询炒店任务监控七天范围的数据
     * @param startTime
     * @param endTime
     * @param offset
     * @param limit
     * @return
     */
    @RequestMapping(value = "queryShopTaskMonitor.view",method = RequestMethod.POST)
    @ResponseBody
    public Table queryShopTaskMonitor(@RequestParam(value = "startTime") String startTime,
                                      @RequestParam(value = "endTime") String endTime,
                                      @RequestParam("start") Integer offset,
                                      @RequestParam("length") Integer limit)
    {
        return shopTaskMonitorService.queryShopTaskMonitor(startTime,endTime,offset,limit);
    }

    /**
     * 导出炒店任务监控数据
     * @param param
     * @param request
     * @param response
     * @param session
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "downloadShopTaskMonitor.view",method = RequestMethod.POST)
    public void downloadShopTaskMonitor(@RequestParam Map<String, Object> param, HttpServletRequest request,
                                        HttpServletResponse response, HttpSession session)
    {
        String startTime = String.valueOf(param.get("startTime"));
        String endTime = String.valueOf(param.get("endTime"));
        shopTaskMonitorService.downloadShopTaskMonitor(startTime,endTime,request,response);
    }

}
