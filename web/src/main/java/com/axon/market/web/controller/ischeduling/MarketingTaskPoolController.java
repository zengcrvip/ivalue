package com.axon.market.web.controller.ischeduling;

import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.ischeduling.MarketingPoolTaskDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.SearchConditionUtil;
import com.axon.market.common.util.UserUtils;
import com.axon.market.core.service.ischeduling.MarketingTaskPoolService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yuanfei on 2017/6/8.
 */
@Controller
public class MarketingTaskPoolController
{
    @Autowired
    @Qualifier("marketingTaskPoolService")
    private MarketingTaskPoolService marketingTaskPoolService;

    @RequestMapping(value = "queryMarketingTaskPoolByPage.view", method = RequestMethod.POST)
    @ResponseBody
    public Table<MarketingPoolTaskDomain> queryMarketingTaskPoolByPage(@RequestParam Map<String, Object> params, HttpSession session) throws IOException
    {
        int curPageIndex = Integer.valueOf((String) params.get("start"));
        int pageSize = Integer.valueOf((String) params.get("length"));
        String name = (String)params.get("name");
        String status = (String) (params.get("status"));
        String marketType = String.valueOf(params.get("marketType"));
        String businessType = String.valueOf(params.get("businessType"));

        Map<String,Object> condition = new HashMap<String,Object>();
        condition.put("name", StringUtils.isNotEmpty(name)?SearchConditionUtil.optimizeCondition(name) : null);
        condition.put("status",StringUtils.isNotEmpty(status)?status : null);
        condition.put("marketType", StringUtils.isNotEmpty(marketType)? marketType : null);
        condition.put("businessType", StringUtils.isNotEmpty(businessType) ? Integer.valueOf(businessType) : null);

        int count = marketingTaskPoolService.queryMarketingPoolTasksCount(condition);
        List<MarketingPoolTaskDomain> tasks = marketingTaskPoolService.queryMarketingPoolTasksByPage(curPageIndex, pageSize, condition);

        return new Table(tasks,count);
    }

    @RequestMapping(value = "executeMarketingTask.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult executeMarketingTask(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        String id = String.valueOf(paras.get("id"));
        UserDomain loginUserDomain = UserUtils.getLoginUser(session);
        return marketingTaskPoolService.executeMarketingTask(Integer.valueOf(id), loginUserDomain.getName());
    }

    @RequestMapping(value = "viewMarketingPoolTaskDetail.view", method = RequestMethod.POST)
    @ResponseBody
    public MarketingPoolTaskDomain viewMarketingPoolTaskDetail(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        String id = String.valueOf(paras.get("id"));
        return marketingTaskPoolService.queryMarketingPoolTask(Integer.valueOf(id));
    }
}
