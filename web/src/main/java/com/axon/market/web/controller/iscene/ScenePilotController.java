package com.axon.market.web.controller.iscene;

import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.iscene.*;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.SearchConditionUtil;
import com.axon.market.common.util.UserUtils;
import com.axon.market.core.service.iscene.ScenePilotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by xuan on 2017/2/8.
 */
@Controller("scenePilotController")
public class ScenePilotController
{
    @Autowired
    @Qualifier("scenePilotService")
    private ScenePilotService scenePilotService;


    /**
     * 查询任务列表
     * @param type
     * @param name
     * @param start
     * @param length
     * @return
     */
    @RequestMapping(value = "getScenePilotList.view", method = RequestMethod.POST)
    @ResponseBody
    public Table getScenePilotList(String type,String name, Integer start, Integer length)
    {
        Table table = scenePilotService.getScenePilotList(type, SearchConditionUtil.optimizeCondition(name), start, length);
        return table;
    }

    @RequestMapping(value = "addOrEditScenePilot.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation addOrEditScenePilot(@RequestBody ScenePilotDomain scenePilotDomain, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        return scenePilotService.addOrEditScenePilot(scenePilotDomain);
    }

    @RequestMapping(value = "deleteScenePilot.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation deleteScenePilot(@RequestBody ScenePilotDomain scenePilotDomain, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        return scenePilotService.deleteScenePilot(scenePilotDomain);
    }

    /**
     * 获取网址分类
     * @return
     */
    @RequestMapping(value = "getUrlGroup.view", method = RequestMethod.POST)
    @ResponseBody
    public List<UrlGroupDomain> getUrlGroup()
    {
        return scenePilotService.getUrlGroup();
    }

    /**
     * 获取地理位置
     * @return
     */
    @RequestMapping(value = "getLocationGroup.view", method = RequestMethod.POST)
    @ResponseBody
    public List<LocationGroupDomain> getLocationGroup()
    {
        return scenePilotService.getLocationGroup();
    }

    /**
     * 获取场景url
     * @return
     */
    @RequestMapping(value = "getSceneUrl.view", method = RequestMethod.POST)
    @ResponseBody
    public List<ScenesDomain> getSceneUrl()
    {
        return scenePilotService.getSceneUrl();
    }


    @RequestMapping(value = "getAdditionalList.view", method = RequestMethod.POST)
    @ResponseBody
    public List<ExtStopCondConfig> getAdditionalList(String name)
    {
        return scenePilotService.getAdditionalList(name);
    }
}
