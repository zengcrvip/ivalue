package com.axon.market.web.controller.imodel;

import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.imodel.ModelDownloadSettingDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.UserUtils;
import com.axon.market.core.service.imodel.ModelDownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Created by yuanfei on 2017/7/24.
 */
@Controller
public class ModelDownloadController
{
    @Autowired
    @Qualifier("modelDownloadService")
    private ModelDownloadService modelDownloadService;

    @RequestMapping(value = "queryModelDownloadSettingByPage.view", method = RequestMethod.POST)
    @ResponseBody
    public Table queryModelDownloadSettingByPage(@RequestParam Map<String, Object> params, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        int curPageIndex = Integer.valueOf((String) params.get("start"));
        int pageSize = Integer.valueOf((String) params.get("length"));

        int counts = modelDownloadService.queryModelDownloadSettingCounts(userDomain.getId());
        List<Map<String, Object>> items = modelDownloadService.queryModelDownloadSettingByPage(userDomain.getId(), curPageIndex, pageSize);

        return new Table(items, counts);
    }

    @RequestMapping(value = "queryModelDownloadSettingById.view", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> queryModelDownloadSettingById(@RequestBody Map<String, Object> params, HttpSession session)
    {
        int settingId = Integer.valueOf(String.valueOf(params.get("settingId")));
        return modelDownloadService.queryModelDownloadSettingById(settingId);
    }

    @RequestMapping(value = "insertModelDownloadSetting.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult insertModelDownloadSetting(@RequestBody ModelDownloadSettingDomain modelDownloadSettingDomain, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        modelDownloadSettingDomain.setCreateUser(userDomain.getId());
        return modelDownloadService.insertModelDownloadSetting(modelDownloadSettingDomain);
    }

    @RequestMapping(value = "updateModelDownloadSetting.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult updateModelDownloadSetting(@RequestBody ModelDownloadSettingDomain modelDownloadSettingDomain, HttpSession session)
    {
        return modelDownloadService.updateModelDownloadSetting(modelDownloadSettingDomain);
    }

    @RequestMapping(value = "deleteModelDownloadSetting.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult deleteModelDownloadSetting(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        Integer id = Integer.parseInt(String.valueOf(paras.get("id")));
        return modelDownloadService.deleteModelDownloadSetting(id);
    }

    @RequestMapping(value = "copyModelDownloadSetting.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult copyModelDownloadSetting(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        Integer copyItemId = Integer.parseInt(String.valueOf(paras.get("copyItemId")));
        Integer modelId = Integer.parseInt(String.valueOf(paras.get("modelId")));
        return modelDownloadService.copyModelDownloadSetting(copyItemId, modelId, userDomain.getId());
    }

}
