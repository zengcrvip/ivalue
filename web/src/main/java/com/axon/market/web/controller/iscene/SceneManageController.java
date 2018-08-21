package com.axon.market.web.controller.iscene;

import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.Table;
import com.axon.market.common.util.SearchConditionUtil;
import com.axon.market.common.util.UserUtils;
import com.axon.market.core.service.iscene.ImageMgrService;
import com.axon.market.core.service.iscene.SceneManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by xuan on 2016/12/22.
 */
@Controller("sceneManageController")
public class SceneManageController
{
    @Autowired
    @Qualifier("sceneManageService")
    private SceneManageService sceneManageService;

    @Autowired
    @Qualifier("imageMgrService")
    private ImageMgrService imageMgrService;

    /**
     * 获取导航形态
     *
     * @param client 场景名称名称
     * @param start  从第几页开始
     * @param length 获取几条数据
     * @return Table 列表统一返回对象
     * @return
     */
    @RequestMapping(value = "getSceneList.view", method = RequestMethod.POST)
    @ResponseBody
    public Table getSceneList(@RequestParam(value = "client", required = false) String client, @RequestParam(value = "start", required = false) int start, @RequestParam(value = "length", required = false) int length)
    {
        Table table = sceneManageService.getSceneList(SearchConditionUtil.optimizeCondition(client), start, length);
        return table;
    }

    /**
     * 模型列表
     * @return
     */
    @RequestMapping(value = "getTempleType.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation getTempleType()
    {
        Operation operation = imageMgrService.getTempleType();
        return operation;
    }

    /**
     * 图片列表
     * @param paras
     * @return
     */
    @RequestMapping(value = "getPictureUrl.view", method = RequestMethod.POST)
    @ResponseBody
    public Table getPictureUrl(@RequestBody Map<String, Object> paras)
    {
        Table table = sceneManageService.getPictureUrl(paras);
        return table;
    }

    /**
     * 新增/修改
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "addOrEditScene.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation addOrEditScene(@RequestBody Map<String, Object> paras,HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        Operation operation = sceneManageService.addOrEditScene(paras, userDomain);
        return operation;
    }

    /**
     * 删除
     * @param paras   id 场景id
     * @return Operation 增删改统一返回对象
     */
    @RequestMapping(value = "deleteScene.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation deleteScene(@RequestBody Map<String, Object> paras)
    {
        Operation operation = sceneManageService.deleteScene(paras);
        return operation;
    }

    /**
     * 获取场景
     * @param paras
     * @return
     */
    @RequestMapping(value = "getSceneContent.view", method = RequestMethod.POST)
    @ResponseBody
    public Table getSceneContent(@RequestBody Map<String, Object> paras)
    {
        Table table = sceneManageService.getSceneContent(paras);
        return table;
    }

}
