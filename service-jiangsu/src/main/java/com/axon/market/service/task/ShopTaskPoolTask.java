package com.axon.market.service.task;

import com.axon.market.core.service.ishop.ShopBlackService;
import com.axon.market.core.service.ishop.ShopTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 炒店任务池每天炒店任务同步
 * Created by zengcr on 2017/3/6.
 */
@Component("shopTaskPoolTask")
public class ShopTaskPoolTask
{

    @Qualifier("shopTaskService")
    @Autowired
    private ShopTaskService shopTaskService;

    @Qualifier("shopBlackService")
    @Autowired
    private ShopBlackService shopBlackService;

    /**
     * 生成炒店任务每天待处理的代办任务
     * 每天凌晨1点执行一次
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void generateShopTaskEveryDay(){
        shopTaskService.generateShopTaskPool();
    }


    /**
     * 每天下午6点同步炒店配置数据，增量同步当天的数据
     */
    @Scheduled(cron = "0 0 18 * * ?")
    public void syncShopInfo(){
        shopTaskService.syncShopInfo();
    }


    /**
     * 每天凌晨6点刷新黑名单
     */
    @Scheduled(cron="0 0 6 * * ?")
    public void flushShopBlack()
    {
        shopBlackService.flushShopBlack();
    }

}
