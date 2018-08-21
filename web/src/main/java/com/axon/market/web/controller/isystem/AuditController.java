package com.axon.market.web.controller.isystem;

import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.bean.Table;
import com.axon.market.common.util.UserUtils;
import com.axon.market.core.service.imodel.ModelService;
import com.axon.market.core.service.ischeduling.MarketingTasksService;
import com.axon.market.core.service.itag.TagService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yuanfei on 2017/2/7.
 */
@Controller
public class AuditController
{
    @Autowired
    @Qualifier("modelService")
    private ModelService modelService;

    @Autowired
    @Qualifier("tagService")
    private TagService tagService;

    @Autowired
    @Qualifier("marketingTasksService")
    private MarketingTasksService marketingTasksService;

    /**
     * 查找需要我审批的模型
     * @param params
     * @param session
     * @return
     */
    @RequestMapping(value = "queryNeedMeAuditModels.view", method = RequestMethod.POST)
    @ResponseBody
    public Table<Map<String, Object>> queryNeedMeAuditModels(@RequestParam Map<String, Object> params, HttpSession session)
    {
        int userId = UserUtils.getLoginUser(session).getId();
        int startIndex = Integer.valueOf((String) params.get("start"));
        int pageSize = Integer.valueOf((String) params.get("length"));
        List<Map<String, Object>> modelDomainList = modelService.queryNeedMeAuditModels(userId);
        int itemCounts = modelDomainList.size();
        int endIndex = startIndex + pageSize > itemCounts ? itemCounts : startIndex + pageSize;
        return new Table(modelDomainList.subList(startIndex, endIndex),itemCounts);
    }

    @RequestMapping(value = "auditModel.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult auditModel(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        //        int id = Integer.parseInt(String.valueOf(paras.get("id")));
        String operate = "against";
        String ids = (String)paras.get("id");
        int id = Integer.parseInt(ids);
        if(Integer.parseInt((String)paras.get("decision")) == 0)
        {
            operate = "approve";
        }
//        String operate = (String) (paras.get("decision"));
        String reason = (String) paras.get("reason");

        return modelService.submitModelAudit(id, operate, reason, UserUtils.getLoginUser(session));
    }


    @RequestMapping(value = "queryNeedMeAuditTags.view", method = RequestMethod.POST)
    @ResponseBody
    public Table<Map<String, Object>> queryNeedMeAuditTags(@RequestParam Map<String, Object> params, HttpSession session)
    {
        int userId = UserUtils.getLoginUser(session).getId();
        int curPageIndex = Integer.valueOf((String) params.get("start"));
        int pageSize = Integer.valueOf((String) params.get("length"));
        List<Map<String, Object>> tagDomainList = tagService.queryNeedMeAuditTags(userId);
        int itemCounts = tagDomainList.size();
        int startIndex = curPageIndex * pageSize;
        int endIndex = startIndex + pageSize > itemCounts ? itemCounts : startIndex + pageSize;
        return new Table(tagDomainList.subList(startIndex, endIndex),itemCounts);
    }

    @RequestMapping(value = "auditTag.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult auditTag(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        int id = Integer.parseInt(String.valueOf(paras.get("id")));
        String operate = (String) (paras.get("operate"));
        String reason = (String) (paras.get("reason"));

        return tagService.submitTagAudit(id, operate, reason, UserUtils.getLoginUser(session));
    }

    @RequestMapping(value = "queryNeedMeAuditMarketingTasks.view", method = RequestMethod.POST)
    @ResponseBody
    public Table<Map<String, Object>> queryNeedMeAuditMarketingTasks(@RequestParam Map<String, Object> params, HttpSession session)
    {
        int userId = UserUtils.getLoginUser(session).getId();
        int startIndex = Integer.valueOf((String) params.get("start"));
        int pageSize = Integer.valueOf((String) params.get("length"));
        List<Map<String, Object>> modelDomainList = marketingTasksService.queryNeedMeAuditMarketingTasks(userId);
        int itemCounts = modelDomainList.size();
        int endIndex = startIndex + pageSize > itemCounts ? itemCounts : startIndex + pageSize;
        return new Table(modelDomainList.subList(startIndex, endIndex),itemCounts);
    }

    @RequestMapping(value = "auditMarketingTask.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult auditMarketingTask(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        //营销任务id
        int id = Integer.parseInt(String.valueOf(paras.get("id")));
        //审核结果
        String operate = "against";//默认不通过
        if(Integer.parseInt((String)paras.get("operate")) == 0)
        {
            operate = "approve";//通过
        }
        //原因
        String reason = (String) (paras.get("reason"));

        return marketingTasksService.submitMarketingTaskAudit(id, operate, reason, UserUtils.getLoginUser(session));
    }
}
