package com.axon.market.web.controller.istatistics;


import com.axon.market.common.bean.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.axon.market.core.service.istatistics.FirstLogin4GService;

import java.util.List;
import java.util.Map;

/**
 * Created by Chris on 2017/6/26.
 */
@Controller("FirstLogin4GController")
public class FirstLogin4GController
{
    @Autowired
    @Qualifier("FirstLogin4GService")
    private FirstLogin4GService FirstLogin4GService;

    /**
     * 查询4G首登网相关数据
     *@param(startTime,endTime)
     *
     * @return
     */
    @RequestMapping(value = "getFirstLogin4GList.view" ,method = RequestMethod.POST)
    @ResponseBody
    public Table<List<Map<String,Object>>> getGlobalSettingList(@RequestParam Map<String,String> param,
                                                                @RequestParam("start") Integer offset,
                                                                @RequestParam("length") Integer limit)
    {
        String startTime =param.get("startTime");
        String endTime = param.get("endTime");
        Integer count = FirstLogin4GService.queryCount(startTime, endTime);
        List<Map<String,Object>> data= FirstLogin4GService.queryFirstLogin4G(startTime, endTime, offset, limit);
        Table table = new Table(data, count);
        return table;
    }

}
