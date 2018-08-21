package com.axon.market.web.controller.iscene;

import com.axon.market.common.domain.iscene.TestNumberManageDomain;
import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.Table;
import com.axon.market.common.constant.iscene.ReturnMessage;
import com.axon.market.core.service.iscene.TestNumberManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * Created by DELL on 2016/12/12.
 */
@Controller("testNumberManageController")
public class TestNumberManageController
{
    @Autowired
    @Qualifier("testNumberManageService")
    private TestNumberManageService testNumberManageService;

    /**
     * 调接口获取测试号码数据
     *
     * @param limit
     * @param offset
     * @return
     */
    @RequestMapping(value = "getMobList.view" ,method = RequestMethod.POST)
    @ResponseBody
    public Table<TestNumberManageDomain> getMobList(@RequestParam("length") Integer limit, @RequestParam("start") Integer offset)
    {
        Table<TestNumberManageDomain> table = testNumberManageService.getMobList(limit, offset);
        return table;
    }

    /**
     * 新增测试号码
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "addMob.view" ,method = RequestMethod.POST)
    @ResponseBody
    public Operation addTestNumber(@RequestBody Map<String, String> param)
    {
        String mob = param.get("mob");
        String taskId = param.get("taskId");
        return testNumberManageService.addTestNumber(mob, taskId);
    }

    /**
     * 删除测试号码
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "delMob.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation delTestNumber(@RequestBody Map<String, String> param)
    {
        String mob = param.get("mob");
        if (mob == null)
        {
            return new Operation(false, ReturnMessage.ERROR);
        }
        else
        {
            return testNumberManageService.delTestNumber(mob);
        }
    }
}
