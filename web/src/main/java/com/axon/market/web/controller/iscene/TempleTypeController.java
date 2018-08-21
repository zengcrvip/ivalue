package com.axon.market.web.controller.iscene;

import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.Table;
import com.axon.market.common.util.SearchConditionUtil;
import com.axon.market.common.util.UserUtils;
import com.axon.market.core.service.iscene.TempleTypeService;
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
 * Created by xuan on 2017/1/4.
 */
@Controller("templeTypeController")
public class TempleTypeController
{
    @Autowired
    @Qualifier("templeTypeService")
    private TempleTypeService templeTypeService;

    /**
     * 获取模版
     *
     * @param name   模型名称
     * @param start  从第几页开始
     * @param length 获取几条数据
     * @return Table 列表统一返回对象
     */
    @RequestMapping(value = "getTempleList.view", method = RequestMethod.POST)
    @ResponseBody
    public Table getTempleList(String name, Integer start, Integer length)
    {
        Table table = templeTypeService.queryTempletype(SearchConditionUtil.optimizeCondition(name), start, length);
        return table;
    }

    /**
     * 新增/修改
     *
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "addOrEditTempleType.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation addOrEditTempleType(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        Operation operation = templeTypeService.addOrEditTempleType(paras, userDomain);
        return operation;
    }

    /**
     * 删除
     *
     * @param paras id 模型id
     * @return Operation 增删改统一返回对象
     */
    @RequestMapping(value = "deleteTempleType.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation deleteTempleType(@RequestBody Map<String, Object> paras)
    {
        Operation operation = templeTypeService.deleteTempleType(paras);
        return operation;
    }

    /**
     * 获取模版类型
     *
     * @return
     */
    @RequestMapping(value = "getMultilType.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation getMultilType()
    {
        Operation operation = templeTypeService.getMultilType();
        return operation;
    }
}
