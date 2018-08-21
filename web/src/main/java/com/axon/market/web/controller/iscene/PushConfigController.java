package com.axon.market.web.controller.iscene;

import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.Table;
import com.axon.market.common.util.SearchConditionUtil;
import com.axon.market.common.util.UserUtils;
import com.axon.market.core.service.iscene.PushConfigService;
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
 * Created by xuan on 2016/12/7.
 */
@Controller("pushConfigController")
public class PushConfigController
{
    @Autowired
    @Qualifier("pushConfigService")
    private PushConfigService pushConfigService;

    /**
     * 获取任务配置类型
     *
     * @param name 类型名称
     * @param start  从第几页开始
     * @param length 获取几条数据
     * @return Table 列表统一返回对象
     * @return
     */
    @RequestMapping(value = "getPushConfigList.view", method = RequestMethod.POST)
    @ResponseBody
    public Table getPushConfigList(String name, Integer start, Integer length)
    {
        Table table = pushConfigService.queryPushConfig(SearchConditionUtil.optimizeCondition(name), start, length);
        return table;
    }

    /**
     * 获取流量包类型 下拉列表
     * @return
     */
    @RequestMapping(value = "getSelectType.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation getSelectType()
    {
        Operation operation = pushConfigService.getSelectType();
        return operation;
    }

    /**
     * 获取是否启用 下拉列表
     * @return
     */
    @RequestMapping(value = "getConfigIsUsed.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation getConfigIsUsed()
    {
        Operation operation = pushConfigService.getIsUsed();
        return operation;
    }

    /**
     * 新增/修改
     * @param paras 参数
     * @param session
     * @return
     */
    @RequestMapping(value = "addOrEditPushConfig.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation addOrEditPushConfig(@RequestBody Map<String, Object> paras,HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        Operation operation = pushConfigService.addOrEditPushConfig(paras,userDomain);
        return operation;
    }

    /**
     * 删除
     * @param paras   id 推送配置id
     * @return Operation 增删改统一返回对象
     */
    @RequestMapping(value = "deletePushConfig.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation deletePushConfig(@RequestBody Map<String, Object> paras)
    {
        Operation operation = pushConfigService.deletePushConfig(paras);
        return operation;
    }

}
