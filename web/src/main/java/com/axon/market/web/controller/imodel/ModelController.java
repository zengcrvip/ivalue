package com.axon.market.web.controller.imodel;

import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.bean.Table;
import com.axon.market.common.constant.isystem.ModelStatusEnum;
import com.axon.market.common.domain.icommon.CategoryDomain;
import com.axon.market.common.domain.imodel.ModelDomain;
import com.axon.market.common.domain.isystem.ModelAuditHistoryDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.SearchConditionUtil;
import com.axon.market.common.util.UserUtils;
import com.axon.market.core.service.icommon.RuleService;
import com.axon.market.core.service.imodel.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by chenyu on 2016/1/4.
 */
@Controller("modelController")
public class ModelController
{
    @Autowired()
    @Qualifier("modelService")
    private ModelService modelService;

    @Autowired()
    @Qualifier("ruleService")
    private RuleService ruleService;

    @RequestMapping(value = "queryModelsByPage.view", method = RequestMethod.POST)
    @ResponseBody
    public Table queryModelsByPage(@RequestParam Map<String, String> param, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        Integer start = Integer.parseInt(param.get("start"));
        Integer length = Integer.parseInt(param.get("length"));
        String nameSearch = param.get("nameSearch");
        String createTypeSearch = param.get("createTypeSearch");
        String userNameSearch = param.get("userNameSearch");
        String catalogSearch = param.get("catalogSearch");
        String status = param.get("status");
        if (ModelStatusEnum.READY.getValue().equals(Integer.valueOf(status)))
        {
            status = ModelStatusEnum.READY.getValue() + "," + ModelStatusEnum.REFRESHING.getValue();
        }

        return modelService.queryModelsByPage(start, length, status, userDomain, SearchConditionUtil.optimizeCondition(nameSearch), createTypeSearch, SearchConditionUtil.optimizeCondition(userNameSearch), catalogSearch,false);
    }

    @RequestMapping(value = "queryModelsByList.view", method = RequestMethod.POST)
    @ResponseBody
    public Table queryModelsByList(@RequestBody Map<String, String> param, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        Integer start = Integer.parseInt(param.get("start"));
        Integer length = Integer.parseInt(param.get("length"));
        String nameSearch = param.get("nameSearch");
        String createTypeSearch = param.get("createTypeSearch");
        String userNameSearch = param.get("userNameSearch");
        String catalogSearch = param.get("catalogSearch");
        String status = param.get("status");
        if (ModelStatusEnum.READY.getValue().equals(Integer.valueOf(status)))
        {
            status = ModelStatusEnum.READY.getValue() + "," + ModelStatusEnum.REFRESHING.getValue();
        }
        return modelService.queryModelsByPage(start, length, status, userDomain, SearchConditionUtil.optimizeCondition(nameSearch), createTypeSearch, SearchConditionUtil.optimizeCondition(userNameSearch), catalogSearch,false);
    }

    @RequestMapping(value = "addOrEditModel.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation addOrEditModel(@RequestBody ModelDomain modelDomain, HttpServletRequest request)
    {
        UserDomain userDomain = UserUtils.getLoginUser(request.getSession());
        return modelService.addOrEditModel(request, modelDomain, userDomain);
    }

    @RequestMapping(value = "addOrEditModelByLocalFile.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation addOrEditModelByLocalFile(HttpServletRequest request)
    {
        UserDomain userDomain = UserUtils.getLoginUser(request.getSession());
        return modelService.addOrEditModel(request, null, userDomain);
    }

    @RequestMapping(value = "deleteModel.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation deleteModel(@RequestBody ModelDomain modelDomain, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        return modelService.deleteModel(modelDomain.getId(), userDomain.getId());
    }

    @RequestMapping(value = "queryAllModelsUnderCatalog.view", method = RequestMethod.POST)
    @ResponseBody
    public List<CategoryDomain> queryAllModelsUnderCatalog(HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        return modelService.queryAllModelsUnderCatalog(userDomain);
    }

    @RequestMapping(value = "queryAllModels.view", method = RequestMethod.POST)
    @ResponseBody
    public List<ModelDomain> queryAllModels(HttpSession session)
    {
        return modelService.queryAllModels();
    }

    @RequestMapping(value = "downloadModel.view", method = RequestMethod.POST)
    public void downloadModel(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        UserDomain userDomain = UserUtils.getLoginUser(request.getSession());
        Integer modelId = Integer.parseInt(request.getParameter("id"));
        modelService.downloadModel(response, userDomain, modelId);
    }

    @RequestMapping(value = "queryModelAuditProgress.view", method = RequestMethod.POST)
    @ResponseBody
    public Table queryModelAuditProgress(@RequestParam String id, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);

        List<ModelAuditHistoryDomain> segmentAuditHistoryList = modelService.queryModelAuditProgress(Integer.valueOf(id), userDomain.getId());
        return new Table(segmentAuditHistoryList, segmentAuditHistoryList.size());
    }

    @RequestMapping(value = "queryMatchRuleUserCounts.view", method = RequestMethod.POST)
    @ResponseBody
    public Integer queryMatchRuleUserCounts(@RequestBody Map<String, Object> paras)
    {
        String rules = String.valueOf(paras.get("rules"));
        return ruleService.queryMatchRuleUserCounts(rules);
    }

    @RequestMapping(value = "queryModelById.view", method = RequestMethod.POST)
    @ResponseBody
    public ModelDomain queryModelById(@RequestBody Map<String, Object> paras)
    {
        String id = String.valueOf(paras.get("id"));
        return modelService.queryModelById(Integer.valueOf(id));
    }

    @RequestMapping(value = "checkDuplicationOfModelName.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult checkDuplicationOfModelName(@RequestBody Map<String, Object> paras)
    {
        String id = String.valueOf(paras.get("id"));
        String name = String.valueOf(paras.get("name"));
        return modelService.checkDuplicationOfModelName(id, name);
    }

    @RequestMapping(value = "queryModelsUnderMe.view", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String,Object>> queryModelsUnderMe(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        Integer beCopyModelId = null;
        if (null != paras.get("currentModelId"))
        {
            beCopyModelId = Integer.parseInt(String.valueOf(paras.get("currentModelId")));
        }
        return modelService.queryModelsUnderMe(beCopyModelId, userDomain);
    }
}
