package com.axon.market.core.shoptask.impl;

import com.axon.market.common.domain.ishop.ShopTaskDomain;
import com.axon.market.core.service.ishop.SendShopSmsService;
import com.axon.market.core.service.ishop.ShopTaskService;
import com.axon.market.core.shoptask.TaskExecutor;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 午忙执行器，放在线程池中调度
 * Created by zengcr on 2017/5/16.
 */
@Service("afternoonPerTaskExecutor")
public class AfternoonPerTaskExecutor implements TaskExecutor
{
    private static final Logger LOG = Logger.getLogger(AfternoonPerTaskExecutor.class.getName());

    @Autowired
    @Qualifier("shopTaskService")
    private ShopTaskService shopTaskService;

    @Autowired
    @Qualifier("sendShopSmsService")
    private SendShopSmsService sendShopSmsService;

    @Override
    public String execute(ShopTaskDomain domain)
    {
        Date date = new Date();
        LOG.info("Shop Afternoon Busy Task Executor : " + domain.getTaskName() + " " + date.toString());

        Integer taskId = domain.getSmsTaskId();
        Integer marketUserCountLimit = (domain.getMarketLimit() == null || domain.getMarketLimit() > 2000) ? 2000 : domain.getMarketLimit();
        String accessNumber = domain.getAccessNumber();
        String smsMessage = domain.getMarketContentText();
        String smsUrl = domain.getMarketUrl();
        if (StringUtils.isNotEmpty(smsUrl))
        {
            smsMessage += smsUrl;
        }
        //短信签名
        if (StringUtils.isNotEmpty(domain.getMessageAutograph()))
        {
            smsMessage += domain.getMessageAutograph();
        }

        Integer baseAreaId = domain.getBaseAreaId(), baseId = domain.getBaseId();
        if (baseAreaId != null && baseId != null)
        {
            String fullTableName = "model.model_" + baseAreaId;
            sendShopSmsService.sendSms(sendShopSmsService.createSelectDataFromModelSql(fullTableName, null, baseId, baseAreaId, 300), 100, marketUserCountLimit, smsMessage, taskId, accessNumber, baseId,2,1);
        }
        return null;
    }
}
