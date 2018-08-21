package com.axon.market.web.controller.ishop;

import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.UserUtils;
import com.axon.market.core.service.ishop.ShopTaskKPIService;
import com.axon.market.core.service.isystem.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 炒店任务营业厅KPI指标分析
 * Created by zengcr on 2017/3/24.
 */
@Controller("shopTaskKPIController")
public class ShopTaskKPIController
{

    @Qualifier("userService")
    @Autowired
    private UserService userService;

    @Qualifier("shopTaskKPIService")
    @Autowired
    private ShopTaskKPIService shopTaskKPIService;

    /**
     * 查询地市任务总数及执行量
     * @param session
     * @return
     */
    @RequestMapping(value = "queryShopTaskKPI.view", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> queryShopTaskKPI(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        List<Map<String,Object>> taskSummary = null;
        taskSummary = shopTaskKPIService.queryShopTaskKPI(paras);
        result.put("items", taskSummary);
        return result;
    }

    /**
     * 查询营业厅KPI
     * @param param
     * @return
     */
    @RequestMapping(value = "queryBusinessHall.view",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> queryBusinessHallKPI(@RequestBody Map<String,Object> param){
        Map<String, Object> result = new HashMap<String, Object>();
        List<Map<String,Object>> taskSummary = null;
        taskSummary = shopTaskKPIService.queryBusinessHallKPI(param);
        Map<String,Object> counts = shopTaskKPIService.queryBusinessHallCount(param);
        result.put("objs", taskSummary);
        result.put("pageCount",counts);
        return result;
    }

}
