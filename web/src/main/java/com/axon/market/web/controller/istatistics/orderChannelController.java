package com.axon.market.web.controller.istatistics;

import com.axon.market.common.bean.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.axon.market.core.service.istatistics.orderChannelService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Chris on 2017/7/7.
 */

@Controller("orderChannelController")
public class orderChannelController
{
    @Autowired
    @Qualifier("orderChannelService")
    private orderChannelService orderChannelService;


    /**
     * 查询订购渠道报表相关数据
     *@param （orderTime）
     *
     * @return
     */

    @RequestMapping(value="getOrderChannelList.view",method = RequestMethod.POST)
    @ResponseBody

    public Table<List<Map<String,Object>>> getOrderChannelList(@RequestParam Map<String,String> param,
                                                               @RequestParam("start") Integer offset,
                                                               @RequestParam("length") Integer limit)
    {
        String orderTime=param.get("orderTime");
        Integer orderYear=Integer.parseInt(param.get("orderYear"));
        Integer orderMon=Integer.parseInt(param.get("orderMon"));
        String flag=param.get("flag");
        Integer count=0;
        List<Map<String,Object>> data=null;

        Date date=new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String currentTime=new SimpleDateFormat("yyyyMM").format(calendar.getTime());

        if("month".equals(flag))
            {
                if(currentTime.equals(orderTime))
                {
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                    String currentMon=new SimpleDateFormat("yyyyMMdd").format(calendar.getTime());
                    count = orderChannelService.queryMonthlyCount(currentMon);
                    data = orderChannelService.queryMonthlyOrderChannel(currentMon, offset, limit);
                }
                else
                {
                    //Calendar.DAY_OF_YEAR
                    calendar.set(Calendar.YEAR, orderYear);
                    calendar.set(Calendar.MONTH, orderMon-1);
                    int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    calendar.set(Calendar.DAY_OF_MONTH, lastDay);
                    String currentMon=new SimpleDateFormat("yyyyMMdd").format(calendar.getTime());
                    count = orderChannelService.queryMonthlyCount(currentMon);
                    data = orderChannelService.queryMonthlyOrderChannel(currentMon, offset, limit);
                }
            }

            else if("day".equals(flag))
            {
                count = orderChannelService.queryCount(orderTime);
                data= orderChannelService.queryOrderChannel(orderTime, offset, limit);
            }

            Table table = new Table(data,count);
            return table;
    }
}
