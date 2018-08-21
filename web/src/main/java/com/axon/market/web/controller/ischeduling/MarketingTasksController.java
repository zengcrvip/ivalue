package com.axon.market.web.controller.ischeduling;

import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.bean.Table;
import com.axon.market.common.constant.ischeduling.ShopTaskStatusEnum;
import com.axon.market.common.domain.ischeduling.MarketJobAuditHistoryDomain;
import com.axon.market.common.domain.ischeduling.MarketingTasksDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.SearchConditionUtil;
import com.axon.market.common.util.UserUtils;
import com.axon.market.core.service.ischeduling.MarketingTasksService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yuanfei on 2017/6/6.
 */
@Controller
public class MarketingTasksController
{
    private static final Logger LOG = Logger.getLogger(MarketingTasksController.class.getName());

    @Autowired
    @Qualifier("marketingTasksService")
    private MarketingTasksService marketingTasksService;

    @RequestMapping(value = "queryMarketingTasksByPage.view", method = RequestMethod.POST)
    @ResponseBody
    public Table<MarketingTasksDomain> queryMarketingTasksByPage(@RequestParam Map<String, Object> params, HttpSession session) throws IOException
    {
        int curPageIndex = Integer.valueOf((String) params.get("start"));
        int pageSize = Integer.valueOf((String) params.get("length"));
        String name = String.valueOf(params.get("name"));
        String status = String.valueOf(params.get("status"));
        String marketType = String.valueOf(params.get("marketType"));
        String businessType = String.valueOf(params.get("businessType"));
        Map<String,Object> condition = new HashMap<String,Object>();
        condition.put("name", StringUtils.isNotEmpty(name)?SearchConditionUtil.optimizeCondition(name) : null);
        condition.put("status",StringUtils.isNotEmpty(status)?status : null);
        condition.put("marketType", StringUtils.isNotEmpty(marketType)? marketType : null);
        condition.put("businessType", StringUtils.isNotEmpty(businessType) ? Integer.valueOf(businessType) : null);

        int count = marketingTasksService.queryTasksCount(condition);
        List<MarketingTasksDomain> tasks = marketingTasksService.queryTasksByPage(curPageIndex, pageSize, condition);

        return new Table(tasks,count);
    }

    @RequestMapping(value = "createMarketingTask.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult createMarketingTask(@RequestBody MarketingTasksDomain taskDomain,HttpSession session) throws ParseException
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        taskDomain.setCreateUser(userDomain.getId());
        ServiceResult result = new ServiceResult();

        //判断该用户是否需要审批，如果不需要审批，直接设置状态为审批通过
        if (StringUtils.isEmpty(userDomain.getMarketingAuditUsers()))
        {
            taskDomain.setStatus(ShopTaskStatusEnum.TASK_READY.getValue());
        } else
        {
            taskDomain.setStatus(ShopTaskStatusEnum.TASK_AUDITING.getValue());
        }

        try
        {
            result = marketingTasksService.insertMarketingTask(taskDomain, userDomain);
        }
        catch (Exception e)
        {
            result.setRetValue(-1);
            result.setDesc("营销任务新增失败");
            LOG.error("create marketingTask fail", e);
        }
        return result;
    }

    @RequestMapping(value = "updateMarketingTask.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult updateMarketingTask(@RequestBody MarketingTasksDomain taskDomain,HttpSession session) throws ParseException
    {
        UserDomain loginUser = UserUtils.getLoginUser(session);
       // taskDomain.setNextMarketTime(MarketTimeUtils.getCurrentTheoreticalDataDate(taskDomain));
        taskDomain.setLastUpdateUser(loginUser.getId());

        if (StringUtils.isBlank(loginUser.getMarketingAuditUsers()))
        {
            taskDomain.setStatus(ShopTaskStatusEnum.TASK_READY.getValue());
        }
        else
        {
            taskDomain.setStatus(ShopTaskStatusEnum.TASK_AUDITING.getValue());
        }
        return marketingTasksService.updateMarketingTask(taskDomain,loginUser);
    }

    @RequestMapping(value = "deleteMarketingTask.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult deleteMarketingTask(@RequestBody Map<String,Object> params,HttpSession session) throws ParseException
    {
        String taskId = String.valueOf(params.get("id"));
        return marketingTasksService.deleteMarketingTask(taskId);
    }

    @RequestMapping(value = "queryMarketingTaskDetail.view", method = RequestMethod.POST)
    @ResponseBody
    public MarketingTasksDomain queryMarketingTaskDetail(@RequestBody Map<String,Object> params,HttpSession session) throws ParseException
    {
        String taskId = String.valueOf(params.get("id"));
        return marketingTasksService.queryMarketingTaskById(Integer.valueOf(taskId));
    }

    @RequestMapping(value = "sendMarketingTaskTestSms.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult sendMarketingTaskTestSms(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        String testNumbers = String.valueOf(paras.get("testNumbers"));
        String contentId = paras.get("contentId") == null ? null : String.valueOf(paras.get("contentId"));
        String content = String.valueOf(paras.get("content"));
        return marketingTasksService.sendMarketingTaskTestSms(testNumbers, content, contentId);
    }

    /**
     * 启停
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "stopMarketingTask.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult stopMarketingTask(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        String id = String.valueOf(paras.get("id"));
        return marketingTasksService.stopMarketingTask(Integer.valueOf(id));
    }

    @RequestMapping(value = "checkMarketingTaskName.view")
    @ResponseBody
    public ServiceResult checkMarketingTaskName(@RequestBody Map<String, Object> paras)
    {
        String id = String.valueOf(paras.get("id"));
        String name = String.valueOf(paras.get("name"));
        return marketingTasksService.checkMarketingTaskName(id, name);
    }

    @RequestMapping(value = "queryMarketingTaskAuditProgress.view", method = RequestMethod.POST)
    @ResponseBody
    public Table<MarketJobAuditHistoryDomain> queryMarketingTaskAuditProgress(@RequestParam String id, HttpSession session)
    {
        UserDomain loginUser = UserUtils.getLoginUser(session);

        List<MarketJobAuditHistoryDomain> marketJobAuditHistoryList = marketingTasksService.queryMarketingTaskAuditProgress(Integer.valueOf(id), loginUser.getId());

        return new Table(marketJobAuditHistoryList,marketJobAuditHistoryList.size());
    }

}
