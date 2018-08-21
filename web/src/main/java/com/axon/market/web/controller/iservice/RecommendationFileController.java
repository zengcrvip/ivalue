package com.axon.market.web.controller.iservice;

import com.axon.market.common.bean.ServiceResult;
import com.axon.market.core.service.iservice.RecommendationFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by chenyu on 2017/3/9.
 */
@Controller("recommendationFileController")
public class RecommendationFileController
{
    @Autowired
    @Qualifier("recommendationFileService")
    private RecommendationFileService recommendationFileService;

    @RequestMapping(value = "baseUserFile", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult baseUserFile(@RequestBody Map<String, String> param)
    {
        String fileName = param.get("fileName");
        return recommendationFileService.copyFileToBaseUser(fileName);
    }
}
