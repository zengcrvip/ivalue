package com.axon.market.core.marketingtask.impl;

import com.axon.market.common.domain.ischeduling.MarketingPoolTaskDomain;
import com.axon.market.common.domain.ischeduling.MarketingTasksDomain;
import com.axon.market.core.marketingtask.MarketTaskExecutor;
import com.axon.market.core.service.ishop.SendShopSmsService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 精细化周期任务执行器
 * Created by zengcr on 2017/6/13.
 */
@Service("jxhCycleTaskExecutor")
public class JXHCycleTaskExecutor implements MarketTaskExecutor
{
    private static final Logger LOG = Logger.getLogger(JXHCycleTaskExecutor.class.getName());

    @Autowired
    @Qualifier("sendShopSmsService")
    private SendShopSmsService sendShopSmsService;

    private static final String fullTableName = "shop.icloud_user";

    @Override
    public String execute(MarketingPoolTaskDomain poolTaskDomain)
    {
        Date date = new Date();
        LOG.info("JXHCycle Task Executor : " + poolTaskDomain.getName() + " " + date.toString());

        //限制人数
        Integer marketUserCountLimit = poolTaskDomain.getMarketUserCountLimit() == null ? Integer.MAX_VALUE : poolTaskDomain.getMarketUserCountLimit();
        //接入号
        String accessNumber = poolTaskDomain.getAccessNumber();
        //营销短信
        String smsMessage = poolTaskDomain.getMarketContent();
        //发送任务数
        Integer count = 0;
        //任务ID
        Integer taskId = poolTaskDomain.getId();

        //短信发送
        count += sendShopSmsService.sendSms(sendShopSmsService.createSelectDataFromModelSql(fullTableName,poolTaskDomain.getSaleId(),poolTaskDomain.getSaleBoidId(),poolTaskDomain.getAimSubId()), 1000, marketUserCountLimit, smsMessage, taskId, accessNumber,null,4,1);

        if (count == 0)
        {
            return "WAIT";
        }

        return count + "-" + poolTaskDomain.getId();

    }
}
