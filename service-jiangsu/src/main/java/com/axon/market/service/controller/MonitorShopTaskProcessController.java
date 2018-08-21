package com.axon.market.service.controller;

import com.axon.market.common.bean.ServiceResult;
import com.axon.market.core.service.iservice.MonitorShopTaskProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by yuanfei on 2017/5/5.
 */
@Controller
public class MonitorShopTaskProcessController
{
    @Autowired
    @Qualifier("monitorShopTaskProcessService")
    private MonitorShopTaskProcessService monitorShopTaskProcessService;

    @RequestMapping(value = "checkRecommendationProcess", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult checkRecommendationProcess(@RequestBody String fileName)
    {
        return monitorShopTaskProcessService.checkRecommendationProcess(fileName);
    }
}
