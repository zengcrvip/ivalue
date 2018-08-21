package com.axon.market.web.controller.ikeeper;

import com.axon.market.common.bean.ServiceResult;
import com.axon.market.core.service.ikeeper.KeeperListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 掌柜活动清单数据读取
 * Created by zengcr on 2017/4/21.
 */
@Controller("keeperListController")
public class KeeperListController
{

    @Qualifier("keeperListService")
    @Autowired
    private KeeperListService keeperListService;

    @RequestMapping(value = "fetchActivityInfo", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ServiceResult fetchActivityInfo(@RequestBody Map<String, Object> param,HttpServletRequest request)
    {
        return keeperListService.fetchActivityInfo(param);
    }
}
