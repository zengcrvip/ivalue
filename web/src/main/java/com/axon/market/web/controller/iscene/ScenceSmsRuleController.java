package com.axon.market.web.controller.iscene;


import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.iscene.ScenceSmsClientDomain;
import com.axon.market.common.domain.iscene.ScenceSmsRuleDomain;
import com.axon.market.common.util.SearchConditionUtil;
import com.axon.market.core.service.iscene.SceneceSmsRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 场景短信营销规则,对场景短信增，删，改
 * Created by zengcr on 2016/11/9.
 */
@Controller("scenceSmsRuleManager")
public class ScenceSmsRuleController
{
   @Autowired
   @Qualifier("sceneceSmsRuleService")
   private SceneceSmsRuleService sceneceSmsRuleService;

    /**
     * 主页显示
     * @return 场景短信规则配置主页
     */
    @RequestMapping("querySceneSms.view")
    public ModelAndView queryList()
    {
        return new ModelAndView("sceneSmsRule");
    }

    /**
     * 查询场景规则客户端分页展示
     * @param paras 客户端名称，一级分类，二级分类
     * @param session
     * @return
     */
    @RequestMapping(value = "querySenceClientByPage.view", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object>  querySenceClientByPage(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        int curPage = (Integer) (paras.get("curPage"));
        int countsPerPage = (Integer) (paras.get("countsPerPage"));
        //客户端名称
        String clientName = (String) (paras.get("clientName"));
        //一级分类
        String clientType = (String) (paras.get("clientType"));
        //二级分类
        String clientType2 = (String) (paras.get("clientType2"));
        Integer itemCounts = 0;

        List<ScenceSmsClientDomain> scenceSmsClientList = null;
        itemCounts = sceneceSmsRuleService.queryScenceClientTotal(clientName,clientType,clientType2);
        scenceSmsClientList = sceneceSmsRuleService.queryScenceClientByPage((curPage - 1) * countsPerPage, countsPerPage, clientName, clientType,clientType2);

        result.put("itemCounts", itemCounts);
        result.put("items", scenceSmsClientList);
        return result;
    }

    /**
     * 根据ID化学删除场景规则
     * @param paras 主键ID
     * @param session
     * @return
     */
    @RequestMapping(value="deleteSenceSmsRuleById.view")
    @ResponseBody
    public ServiceResult deleteSenceSmsRuleById(@RequestBody Map<String,Object> paras, HttpSession session)
    {
        Integer senceRuleId = Integer.valueOf(String.valueOf(paras.get("id")));
        return sceneceSmsRuleService.deleteSenceSmsRuleById(senceRuleId);
    }

    /**
     * 查询场景规则分页展示
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "querySenceSmsRuleByPage.view", method = RequestMethod.POST)
    @ResponseBody
    public Table<ScenceSmsRuleDomain> querySenceSmsRuleByPage(@RequestParam Map<String, Object> paras, HttpSession session)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        int curPageIndex = Integer.valueOf((String)paras.get("start"));
        int pageSize = Integer.valueOf((String) paras.get("length"));
        //场景规则名称
        String sceneceName = SearchConditionUtil.optimizeCondition((String) (paras.get("sceneceName")));
        //场景规则状态
        String scenceStatus = (String) (paras.get("scenceStatus"));
        Integer itemCounts = 0;
        List<ScenceSmsRuleDomain> scenceSmsRuleList = null;

        itemCounts = sceneceSmsRuleService.queryScenceRuleTotal(sceneceName,scenceStatus);
        scenceSmsRuleList = sceneceSmsRuleService.queryScenceRuleByPage(curPageIndex, pageSize, sceneceName, scenceStatus);

        return new Table(scenceSmsRuleList,itemCounts);
    }

    /**
     * 查询场景规则类型
     * @return
     */
    @RequestMapping(value="querySenceRuleType.view")
    @ResponseBody
    public List<Map<String,String>> querySenceRuleType(@RequestBody Map<String,Object> paras, HttpSession session){
        List<Map<String,String>> result = sceneceSmsRuleService.querySenceRuleType();
        return result;
    }

    /**
     * 查询生效的场景规则{id，name}
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value="querySenceRuleSmsType.view")
    @ResponseBody
    public List<Map<String,String>> querySenceRuleSmsType(@RequestBody Map<String,Object> paras, HttpSession session){
        List<Map<String,String>> result = sceneceSmsRuleService.querySenceRuleSmsType();
        return result;
    }

    /**
     * 查询场景规则客户端一级类型
     * @return
     */
    @RequestMapping(value="querySenceClientTypeOne.view")
    @ResponseBody
    public List<Map<String,String>> querySenceClientTypeOne(@RequestBody Map<String,Object> paras, HttpSession session){
        List<Map<String,String>>  result = new ArrayList<Map<String,String>>();
        Map<String,String> map = new HashMap<String,String>();
        map.put("id","-1");map.put("name", "全部");
        result.add(map);
        List<Map<String,String>> data = sceneceSmsRuleService.querySenceClientTypeOne();
        for(Map<String,String> item:data){
            result.add(item);
        }
        return result;
    }

    /**
     * 查询场景规则客户端二级类型
     * @return
     */
    @RequestMapping(value="querySenceClientTypeTwo.view")
    @ResponseBody
    public List<Map<String,String>> querySenceClientTypeTwo(@RequestBody Map<String,Object> paras, HttpSession session){
        String  scenceClientType = String.valueOf(paras.get("scenceClientType"));
        List<Map<String,String>>  result = new ArrayList<Map<String,String>>();
        Map<String,String> map = new HashMap<String,String>();
        map.put("id","-1");map.put("name", "全部");
        result.add(map);
        List<Map<String,String>> data = sceneceSmsRuleService.querySenceClientTypeTwo(scenceClientType);
        for(Map<String,String> item:data){
            result.add(item);
        }
        return result;
    }


    /**
     * 查询场景规则终端分页展示
     * @param paras 终端名称
     * @param session
     * @return
     */
    @RequestMapping(value = "querySenceTerminalByPage.view", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object>  querySenceTerminalByPage(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        int curPage = (Integer) (paras.get("curPage"));
        int countsPerPage = (Integer) (paras.get("countsPerPage"));
        String terminalName = (String) (paras.get("terminalName"));
        Integer itemCounts = 0;
        List<Map<String,String>> scenceSmsTerminalList = null;
        itemCounts = sceneceSmsRuleService.queryScenceTerminalTotal(terminalName);
        scenceSmsTerminalList = sceneceSmsRuleService.queryScenceTerminalByPage((curPage - 1) * countsPerPage, countsPerPage, terminalName);
        result.put("itemCounts", itemCounts);
        result.put("items", scenceSmsTerminalList);
        return result;
    }

    /**
     * 新建或修改场景规则
     * 当主键ID存在时修改；不存在时新增
     * @param scenceSmsRuleDomain 场景规则实体对象
     * @param session
     * @return
     */
    @RequestMapping(value="createOrUpdateSceneRule.view")
    @ResponseBody
    public ServiceResult createOrUpdateSceneRule(@RequestBody ScenceSmsRuleDomain scenceSmsRuleDomain, HttpSession session)
    {
        return sceneceSmsRuleService.createOrUpdateSceneRule(scenceSmsRuleDomain);
    }


    /**
     * 根据ID查询场景规则
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value="querySenceRuleById.view")
    @ResponseBody
    public Map<String, Object>  querySenceRuleById(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        Integer senceRuleId = Integer.valueOf(String.valueOf(paras.get("id")));
        ScenceSmsRuleDomain scenceSmsRuleDomain = sceneceSmsRuleService.querySenceRuleById(senceRuleId);
        result.put("scenceSmsRuleDomain", scenceSmsRuleDomain);
        return result;
    }

}