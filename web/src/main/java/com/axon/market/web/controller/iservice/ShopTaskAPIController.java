package com.axon.market.web.controller.iservice;

import com.axon.market.common.domain.iservice.ShopTaskApiDomain;
import com.axon.market.common.domain.iservice.ShopTaskApiResult;
import com.axon.market.common.domain.iservice.ShopTaskResult;
import com.axon.market.core.service.iservice.ShopTaskAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 炒店大屏展示API接口
 * Created by zengcr on 2017/7/29.
 */
@RestController
@RequestMapping("shopTask")
public class ShopTaskAPIController {

    @Autowired
    @Qualifier("shopTaskAPIService")
    private ShopTaskAPIService shopTaskAPIService;


    /**
     * 根据日期查询当天的任务信息
     * @param date
     * @return
     */
    @RequestMapping(value = "/{date}",method = RequestMethod.GET)
    public ShopTaskApiResult queryShopTasksByDate(@PathVariable String date){
        ShopTaskApiResult result = new ShopTaskApiResult();
        String destDate = date.replaceAll("\\-","");
        SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd");
        // 设置日期转化成功标识
        boolean dateflag=true;

        try {
            format.parse(destDate);
        } catch (ParseException e) {
            dateflag = false;
        }
        if(!dateflag){
           result.setReturnValue(-2);
           result.setReturnMsg("日期参数格式不对！");
           return  result;
        }

        List<ShopTaskApiDomain> shopTasks = shopTaskAPIService.queryShopTasksByDate(destDate);
        result.setShopTasks(shopTasks);
        return result;
    }

    /**
     * 根据日期和任务ID查询当天该任务的详情
     * @param date
     * @param taskId
     * @return
     */
    @RequestMapping(value = "/{date}/{taskId}",method = RequestMethod.GET)
    public ShopTaskResult queryShopTasksByDateAndId(@PathVariable String date ,@PathVariable String taskId){
        ShopTaskResult result = new ShopTaskResult();
        String destDate = date.replaceAll("\\-","");
        SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd");
        // 设置日期转化成功标识
        boolean dateflag=true;

        try {
            format.parse(destDate);
        } catch (ParseException e) {
            dateflag = false;
        }
        if(!dateflag){
            result.setReturnValue(-2);
            result.setReturnMsg("日期参数格式不对！");
            return  result;
        }

        ShopTaskApiDomain shopTask = shopTaskAPIService.queryShopTasksByDateAndId(destDate,taskId);
        result.setShopTask(shopTask);
        return result;
    }


}
