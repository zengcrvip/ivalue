package com.axon.market.web.controller.iscene;

import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.iscene.PositionSceneDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.SearchConditionUtil;
import com.axon.market.common.util.UserUtils;
import com.axon.market.core.service.iscene.PositionSceneService;
import com.axon.market.core.service.isystem.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 位置场景规则配置管理, 对位置场景规则进行，增，删，改
 * Created by zengcr on 2016/11/28.
 */
@Controller("positionScenceController")
public class PositionScenceController
{
    @Qualifier("positionSceneService")
    @Autowired
    private PositionSceneService positionSceneService;

    @Qualifier("userService")
    @Autowired
    private UserService userService;

    /**
     * 主页显示
     *
     * @return 主页
     */
    @RequestMapping("queryPositionScene.view")
    public ModelAndView queryList()
    {
        return new ModelAndView("positionScene");
    }

    /**
     * 位置场景规则分页展示
     *
     * @param paras   位置场景名称，状态，分页开始标记及页数
     * @param session
     * @return
     */
    @RequestMapping(value = "queryPositionSceneByPage.view", method = RequestMethod.POST)
    @ResponseBody
    public Table<PositionSceneDomain> queryPositionSceneByPage(@RequestParam Map<String, Object> paras, HttpSession session)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        int curPageIndex = Integer.valueOf((String) paras.get("start"));
        int pageSize = Integer.valueOf((String) paras.get("length"));
        //位置场景规则名称
        String positionScenName = SearchConditionUtil.optimizeCondition((String) (paras.get("positionScenName")));
        //位置场景规则状态
        String positionScenStau = (String) (paras.get("positionScenStau"));
        Integer itemCounts = 0;

        List<PositionSceneDomain> positionSceneList = null;
        itemCounts = positionSceneService.queryPositionSceneTotal(positionScenName, positionScenStau);
        positionSceneList = positionSceneService.queryPositionSceneByPage(curPageIndex, pageSize, positionScenName, positionScenStau);

        result.put("itemCounts", itemCounts);
        result.put("items", positionSceneList);

        return new Table(positionSceneList, itemCounts);
    }

    /**
     * 查询场景规则类别
     *
     * @return
     */
    @RequestMapping(value = "queryPositonSenceType.view")
    @ResponseBody
    public List<Map<String, String>> queryPositonSenceType(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        List<Map<String, String>> result = positionSceneService.queryPositonSenceType();
        return result;
    }

    /**
     * 查询基站地区
     *
     * @return
     */
    @RequestMapping(value = "queryBaseAreas.view")
    @ResponseBody
    public List<Map<String, String>> queryBaseAreas(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        Map<String, Object> parasMap = new HashMap<String, Object>();
        UserDomain userDomain = UserUtils.getLoginUser(session);
        parasMap.put("areaCode", userDomain.getAreaCode() == null ? "" : userDomain.getAreaCode());
        parasMap.put("businessCodes", userDomain.getBusinessHallIds() == null ? "" : userDomain.getBusinessHallIds());
        parasMap.put("type", paras.get("actionType"));
        List<Map<String, String>> result = positionSceneService.queryBaseAreas(parasMap);
        return result;
    }

    /**
     * 查询基站类型
     *
     * @return
     */
    @RequestMapping(value = "queryBaseAreaType.view")
    @ResponseBody
    public List<Map<String, String>> queryBaseAreaType(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        List<Map<String, String>> result = positionSceneService.queryBaseAreaType();
        return result;
    }

    /**
     * 查询基站站点
     *
     * @param paras   地区编码，基站类型ID，基站ID，基站名称
     * @param session
     * @return
     */
    @RequestMapping(value = "queryBasesByPage.view", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> queryBasesByPage(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        Map<String, Object> parasMap = new HashMap<String, Object>();
        UserDomain loginUserDomain = UserUtils.getLoginUser(session);
        //从数据库读取
        UserDomain userDomain = userService.queryUserById(loginUserDomain.getId());
        parasMap.put("areaCode", userDomain.getAreaCode() == null ? "" : userDomain.getAreaCode());
        parasMap.put("businessCodes", userDomain.getBusinessHallIds() == null ? "" : userDomain.getBusinessHallIds());

        Map<String, Object> result = new HashMap<String, Object>();
        int curPage = (Integer) (paras.get("curPage"));
        int countsPerPage = (Integer) (paras.get("countsPerPage"));
        //地区编码ID
        String baseAreaId = (String) paras.get("baseAreaId");
        //基站类型ID
        String baseTypeId = (String) paras.get("baseTypeId");
        //基站ID
        String baseId = (String) paras.get("baseId");
        //基站名称
        String baseName = (String) paras.get("baseName");
        Integer itemCounts = 0;

        parasMap.put("baseAreaId", baseAreaId);
        parasMap.put("baseTypeId", baseTypeId);
        parasMap.put("baseId", baseId);
        parasMap.put("baseName", baseName);
        parasMap.put("offset", (curPage - 1) * countsPerPage);
        parasMap.put("limit", countsPerPage);
        List<Map<String, String>> baseList = null;
        itemCounts = positionSceneService.queryBasesTotal(parasMap);
        baseList = positionSceneService.queryBasesByPage(parasMap);

        result.put("itemCounts", itemCounts);
        result.put("items", baseList);
        return result;
    }

    /**
     * 查询基站站点
     *
     * @param paras   地区编码，基站类型ID，基站ID，基站名称
     * @param session
     * @return
     */
    @RequestMapping(value = "queryBases.view", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> queryBases(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        Map<String, Object> parasMap = new HashMap<String, Object>();
        UserDomain loginUserDomain = UserUtils.getLoginUser(session);
        //从数据库读取
        UserDomain userDomain = userService.queryUserById(loginUserDomain.getId());
        parasMap.put("areaCode", userDomain.getAreaCode() == null ? "" : userDomain.getAreaCode());
        parasMap.put("businessCodes", userDomain.getBusinessHallIds() == null ? "" : userDomain.getBusinessHallIds());

        Map<String, Object> result = new HashMap<String, Object>();
        //地区编码ID
        String baseAreaId = (String) paras.get("baseAreaId");
        //基站类型ID
        String baseTypeId = (String) paras.get("baseTypeId");
        //基站ID
        String baseId = (String) paras.get("baseId");
        //基站名称
        String baseName = (String) paras.get("baseName");

        parasMap.put("baseAreaId", baseAreaId);
        parasMap.put("baseTypeId", baseTypeId);
        parasMap.put("baseId", baseId);
        parasMap.put("baseName", baseName);
        List<Map<String, String>> baseList = null;
        baseList = positionSceneService.queryBases(parasMap);
        result.put("items", baseList);
        return result;
    }

    /**
     * 查询监控号段地区列表
     *
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "queryPositionBaseAreas.view", method = RequestMethod.POST)
    @ResponseBody
    public String queryPositionBaseAreas(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        String areaIds = String.valueOf(paras.get("AREA_IDS"));

        UserDomain loginUserDomain = UserUtils.getLoginUser(session);
        UserDomain userDomain = userService.queryUserById(loginUserDomain.getId());
        Integer areaCode = userDomain.getAreaCode() == null ? 0 : userDomain.getAreaCode();
        String businessCodes = userDomain.getBusinessHallIds() == null ? "" : userDomain.getBusinessHallIds();

        return positionSceneService.queryPositionBaseAreas(areaIds,areaCode,businessCodes);
    }

    /**
     * 新建或修改位置场景
     *
     * @param positionSceneDomain 位置场景规则对象
     * @param session
     * @return
     */
    @RequestMapping(value = "createOrUpdatePositionScen.view")
    @ResponseBody
    public ServiceResult createOrUpdatePositionScen(@RequestBody PositionSceneDomain positionSceneDomain, HttpSession session)
    {
        return positionSceneService.createOrUpdatePositionScen(positionSceneDomain);
    }

    /**
     * 根据ID化学删除位置场景，化学删除，状态置为2
     *
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "deletePositionSceneById.view")
    @ResponseBody
    public ServiceResult deletePositionSceneById(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        Integer positionSceneId = Integer.valueOf(String.valueOf(paras.get("id")));
        return positionSceneService.deletePositionSceneById(positionSceneId);
    }

    /**
     * 根据ID查询位置场景
     *
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "queryPositionSceneById.view")
    @ResponseBody
    public Map<String, Object> queryPositionSceneById(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        Integer positionSceneId = Integer.valueOf(String.valueOf(paras.get("id")));
        PositionSceneDomain positionSceneDomain = positionSceneService.queryPositionSceneById(positionSceneId);
        result.put("positionSceneDomain", positionSceneDomain);
        return result;
    }

    /**
     * 获取场景规则，返回{id，name}形式
     *
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "queryPositionSceneType.view")
    @ResponseBody
    public List<Map<String, String>> queryPositionSceneType(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        List<Map<String, String>> result = positionSceneService.queryPositionSceneType();
        return result;
    }

    /**
     * 获取场景导航，返回{id，name}形式
     *
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "queryScenePilotType.view")
    @ResponseBody
    public List<Map<String, String>> queryScenePilotType(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        List<Map<String, String>> result = positionSceneService.queryScenePilotType();
        return result;
    }

    /**
     * 后台对优先级数据的预处理
     *
     * @return
     */
    @RequestMapping(value = "queryPriorityLevel.view", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> queryPriorityLevel(@RequestBody Map<String, Object> param)
    {
        Integer tid = (Integer) param.get("tid");
        return positionSceneService.queryPriorityLevel(tid);
    }
}
