package com.axon.market.core.task;

import com.axon.market.common.timer.RunJob;
import com.axon.market.common.util.RedisUtil;
import com.axon.market.core.service.ishop.ShopTaskService;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 将号码地区同步到redis缓存
 * Created by zengcr on 2017/5/3.
 */
public class MonitorPhoneSegmentTask extends RunJob
{

    private static final Logger LOG = Logger.getLogger(MonitorPhoneSegmentTask.class.getName());

    private static final  String SHOP_AREA_WHITE_PHONE_LIST  = "shopWhitePhoneList";

    private ShopTaskService shopTaskService = ShopTaskService.getInstance();

    @Override
    public void runBody()
    {
        //查询表conf_segment清单数据
        List<Map<String,Object>> phoneSegment = shopTaskService.queryConfSegment();
        Map<String,String> areaSegment = new HashMap<String,String>();
        //放入目标表
        for(Map<String,Object> map:phoneSegment)
        {
           areaSegment.put(String.valueOf(map.get("code")),String.valueOf(map.get("segment")));
        }
        //放入redis缓存
        RedisUtil.getInstance().hmset(SHOP_AREA_WHITE_PHONE_LIST,areaSegment);
    }
}
