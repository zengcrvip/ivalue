package com.axon.market.web.controller.iscene;

import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.Table;
import com.axon.market.common.util.SearchConditionUtil;
import com.axon.market.common.util.UserUtils;
import com.axon.market.core.service.iscene.PushConfigService;
import com.axon.market.core.service.iscene.PushContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by xuan on 2016/12/9.
 */
@Controller("pushContentController")
public class PushContentController
{
    @Autowired
    @Qualifier("pushContentService")
    private PushContentService pushContentService;

    @Autowired
    @Qualifier("pushConfigService")
    private PushConfigService pushConfigService;

    /**
     * 获取推送内容
     *
     * @param name   任务名称
     * @param kind   类型id
     * @param start  从第几页开始
     * @param length 获取几条数据
     * @return Table 列表统一返回对象
     */
    @RequestMapping(value = "getPushList.view", method = RequestMethod.POST)
    @ResponseBody
    public Table getPushList(String name, String kind, Integer start, Integer length)
    {
        Table table = pushContentService.getPushList(SearchConditionUtil.optimizeCondition(name), kind, start, length);
        return table;
    }

    /**
     * 获取任务
     *
     * @param start  从第几页开始
     * @param length 获取几条数据
     * @return Table 列表统一返回对象
     */
    @RequestMapping(value = "getTaskList.view", method = RequestMethod.POST)
    @ResponseBody
    public Table getTaskList(Integer start, Integer length)
    {
        return pushContentService.getTaskList(start, length);
    }

    /**
     * 新增/修改
     *
     * @param paras   参数
     * @param session
     * @return
     */
    @RequestMapping(value = "addOrEditPush.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation addOrEditPush(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        return pushContentService.addOrEditPush(paras, userDomain);
    }

    /**
     * 删除
     *
     * @param paras id 推送内容id
     * @return Operation 增删改统一返回对象
     */
    @RequestMapping(value = "deletePush.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation deletePush(@RequestBody Map<String, Object> paras)
    {
        return pushContentService.deletePush(paras);
    }

    /**
     * 获取推送配置 下拉列表
     *
     * @return
     */
    @RequestMapping(value = "getSelectPushConfig.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation getSelectPushConfig()
    {
        return pushContentService.getSelectPushConfig();
    }

    /**
     * 网络类型下拉框
     *
     * @return
     */
    @RequestMapping(value = "getSelectNetWork.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation getSelectNetWork()
    {
        return pushContentService.getSelectNetWork();
    }

    /**
     * 是否启用下拉框
     *
     * @return
     */
    @RequestMapping(value = "getContentIsUsed.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation getContentIsUsed()
    {
        return pushConfigService.getIsUsed();
    }


    /**
     * 获取推送内容
     * @param paras
     * @return
     */
    @RequestMapping(value = "getContent.view", method = RequestMethod.POST)
    @ResponseBody
    public Table getContent(@RequestBody Map<String, Object> paras)
    {
        Table table = pushContentService.getContent(paras);
        return table;
    }
}
