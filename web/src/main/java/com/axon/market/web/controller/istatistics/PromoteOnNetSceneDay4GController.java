package com.axon.market.web.controller.istatistics;

import com.axon.market.common.bean.Table;
import com.axon.market.core.service.istatistics.PromoteOnNetSceneDay4GService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Created by dtt on 2017/8/9.
 */
@Controller("PromoteOnNetSceneDay4GController")
public class PromoteOnNetSceneDay4GController {
    @Autowired
    @Qualifier("PromoteOnNetSceneDay4GService")
    PromoteOnNetSceneDay4GService promoteOnNetSceneDay4GService;

    @RequestMapping(value = "queryPromoteOnNetSceneDay4GByPage.view",method = RequestMethod.POST)
    @ResponseBody
    public Table<Map<String,Object>> queryPromoteOnNetSceneDay4GByPage(@RequestParam Map<String,String> param){
        String startTime = param.get("startTime");
        String endTime = param.get("endTime");
        int offset = Integer.parseInt(String.valueOf(param.get("start")));
        int limit = Integer.parseInt(String.valueOf(param.get("length")));
        Integer count= promoteOnNetSceneDay4GService.queryCount(startTime,endTime);
        List<Map<String,Object>> data = promoteOnNetSceneDay4GService.queryPromoteOnNetSceneDay4GByPage(startTime,endTime,offset
        ,limit);
        Table<Map<String,Object>> table = new Table<Map<String,Object>>(data,count);
        return table;
    }
}
