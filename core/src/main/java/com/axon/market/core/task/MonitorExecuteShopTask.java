package com.axon.market.core.task;

import com.axon.market.common.timer.RunJob;
import com.axon.market.common.util.RedisUtil;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.core.service.ishop.ShopTaskService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chris on 2017/8/9.
 */
/**
 * 更改营业厅单独管理员的状态
 * 营业厅管理员账号冻结短信提醒
 * 更改营业厅的状态
 *
 */
public class MonitorExecuteShopTask extends RunJob
{
    private ShopTaskService shopTaskService = ShopTaskService.getInstance();

    @Override
    public void runBody()
    {
        Calendar calendar = Calendar.getInstance();
        String endTime = TimeUtil.formatDateToYMD(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, -7);//日期回滚7天
        String startTime = TimeUtil.formatDateToYMD(calendar.getTime());

        //查询表conf_segment清单数据
        List<Map<String,Object>> Segment = shopTaskService.queryExecuteShopTask(startTime,endTime);
        Map<String,String> taskSegment = new HashMap<String,String>();
        //放入目标表
        for(Map<String,Object> map:Segment)
        {
            String telePhone = String.valueOf(map.get("telephone"));
            if(telePhone == null || telePhone.equals("") || telePhone.equals("null")){
                continue;
            }
            taskSegment.put("telephone",telePhone);
            taskSegment.put("name",String.valueOf(map.get("name")));
            taskSegment.put("baseId",String.valueOf(map.get("baseId")));
            shopTaskService.updateUserStatus(taskSegment);
            //营业厅失效暂不处理
//            shopTaskService.updateShopStatus(taskSegment);
        }

    }
}
