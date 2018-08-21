package com.axon.market.web.controller.inewScene;

import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.bean.Table;
import com.axon.market.common.constant.inewScene.SceneCodeEnum;
import com.axon.market.common.domain.inewScene.*;
import com.axon.market.common.domain.iscene.ScenceSmsClientDomain;
import com.axon.market.common.domain.iscene.ScenceSmsRuleDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.JsonUtil;
import com.axon.market.common.util.SearchConditionUtil;
import com.axon.market.common.util.UserUtils;
import com.axon.market.core.service.inewScene.NewSceneRuleService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by zhuwen on 2017/7/12.
 */
@Controller("newSceneRuleController")
public class NewSceneRuleController {
    private static final Logger LOG = Logger.getLogger(NewSceneRuleController.class.getName());

    @Autowired
    @Qualifier("newSceneceRuleService")
    private NewSceneRuleService newSceneRuleService;

    /**
     * 查询场景规则客户端一级类型
     * @return
     */
    @RequestMapping(value="queryNewSceneClientTypeOne.view")
    @ResponseBody
    public List<Map<String,String>> queryNewSceneClientTypeOne(@RequestBody Map<String,Object> paras, HttpSession session){
        List<Map<String,String>>  result = new ArrayList<Map<String,String>>();
        Map<String,String> map = new HashMap<String,String>();
        map.put("id","-1");map.put("name", "全部");
        result.add(map);
        List<Map<String,String>> data = newSceneRuleService.queryNewSceneClientTypeOne();
        for(Map<String,String> item:data){
            result.add(item);
        }
        return result;
    }

    /**
     * 查询场景规则客户端二级类型
     * @return
     */
    @RequestMapping(value="queryNewSceneClientTypeTwo.view")
    @ResponseBody
    public List<Map<String,String>> queryNewSceneClientTypeTwo(@RequestBody Map<String,Object> paras, HttpSession session){
        String  sceneClientType = String.valueOf(paras.get("scenceClientType"));
        List<Map<String,String>>  result = new ArrayList<Map<String,String>>();
        Map<String,String> map = new HashMap<String,String>();
        map.put("id","-1");map.put("name", "全部");
        result.add(map);
        List<Map<String,String>> data = newSceneRuleService.queryNewSceneClientTypeTwo(sceneClientType);
        for(Map<String,String> item:data){
            result.add(item);
        }
        return result;
    }

    /**
     * 查询场景规则客户端分页展示
     * @param paras 客户端名称，一级分类，二级分类
     * @param session
     * @return
     */
    @RequestMapping(value = "queryNewSceneClientByPage.view", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object>  queryNewSceneClientByPage(@RequestBody Map<String, Object> paras, HttpSession session)
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
        itemCounts = newSceneRuleService.queryNewSceneClientTotal(clientName,clientType,clientType2);
        scenceSmsClientList = newSceneRuleService.queryNewSceneClientByPage((curPage - 1) * countsPerPage, countsPerPage, clientName, clientType,clientType2);

        result.put("itemCounts", itemCounts);
        result.put("items", scenceSmsClientList);
        return result;
    }


    /**
     * 查询场景规则分页展示
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "queryNewSceneRuleByPage.view", method = RequestMethod.POST)
    @ResponseBody
    public Table<Map<String, Object>> queryNewSceneRuleByPage(@RequestParam Map<String, Object> paras, HttpSession session)
    {
        int curPageIndex = Integer.valueOf((String) paras.get("start"));
        int pageSize = Integer.valueOf((String) paras.get("length"));
        //场景规则名称
        String sceneName = SearchConditionUtil.optimizeCondition((String) (paras.get("sceneName")));
        //场景规则状态
        String sceneStatus = paras.get("sceneStatus") ==null? "":(String)paras.get("sceneStatus");
        //场景类型
        String sceneTypeID = paras.get("sceneTypeID")==null? "": (String) paras.get("sceneTypeID");
        Integer itemCounts = 0;
        List<Map<String, Object>> sceneRuleList = null;
        itemCounts = newSceneRuleService.queryNewSceneRuleTotal(sceneName, sceneStatus,sceneTypeID);
        sceneRuleList = newSceneRuleService.queryNewSceneRuleByPage(curPageIndex, pageSize, sceneName, sceneStatus, sceneTypeID);
        return new Table(sceneRuleList ,itemCounts);
    }

    /**
     * 查询场景规则类型
     * @param session
     * @return
     */
    @RequestMapping(value = "queryNewSceneType.view", method = RequestMethod.POST)
    @ResponseBody
    public List<ConfNewSceneTypeDomain> queryNewSceneType(HttpSession session)
    {
        return newSceneRuleService.queryNewSceneType();
    }

    /**
     * 新建或修改场景规则
     * 当主键ID存在时修改；不存在时新增
     * @param paras 参数
     * @param session
     * @return
     */
    @RequestMapping(value="createOrUpdateNewSceneRule.view")
    @ResponseBody
    public ServiceResult createOrUpdateNewSceneRule(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        return newSceneRuleService.createOrUpdateNewSceneRule(paras, session);
    }

    /**
     * 根据ID删除场景规则
     * @param paras 主键ID
     * @param session
     * @return
     */
    @RequestMapping(value="deleteNewSceneByID.view")
    @ResponseBody
    public ServiceResult deleteNewSceneByID(@RequestBody Map<String,Object> paras, HttpSession session)
    {
        Integer sceneID = Integer.valueOf(String.valueOf(paras.get("id")));
        return newSceneRuleService.deleteNewSceneByID(sceneID);
    }

    /**
     * 判断是否创建过相同的任务
     * @param paras
     * @param session
     * @return true：是，false：否
     */
    @RequestMapping(value = "validateSceneName.view", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> validateSceneName(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        Map<String, Object> parasMap = new HashMap<String, Object>();
        Map<String, Object> result = new HashMap<String, Object>();
        parasMap.put("sceneName", paras.get("sceneName"));
        boolean isExists = newSceneRuleService.validateSceneName(parasMap);
        parasMap.put("isExists", isExists);
        return parasMap;

    }
}
