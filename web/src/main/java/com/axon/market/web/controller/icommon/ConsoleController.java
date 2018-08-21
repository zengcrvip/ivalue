package com.axon.market.web.controller.icommon;

import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.UserUtils;
import com.axon.market.core.service.icommon.ConsoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Created by yuanfei on 2017/2/22.
 */
@Controller
public class ConsoleController
{
    @Autowired
    @Qualifier("consoleService")
    private ConsoleService consoleService;

    @RequestMapping(value = "queryShopTaskTypeCount.view")
    @ResponseBody
    public Map<String,Object> queryShopTaskTypeCount(HttpSession session)
    {
        UserDomain loginUser = UserUtils.getLoginUser(session);
        return consoleService.queryShopTaskTypeCount(loginUser);
    }

    @RequestMapping(value = "queryDealShopTask.view")
    @ResponseBody
    public Table queryDealShopTask(@RequestParam Map<String,Object> params, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        int curPageIndex = Integer.valueOf((String) params.get("start"));
        int pageSize = Integer.valueOf((String) params.get("length"));
        String status = (String)params.get("status");
        int itemCounts =  consoleService.queryMyShopTaskCount(userDomain, Integer.valueOf(status));
        List<Map<String,Object>> taskList = consoleService.queryMyShopTaskByPage(userDomain, Integer.valueOf(status), curPageIndex, pageSize);
        return new Table(taskList, itemCounts);
    }
}
