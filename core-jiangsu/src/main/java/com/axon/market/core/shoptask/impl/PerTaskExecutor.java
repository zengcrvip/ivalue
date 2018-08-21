package com.axon.market.core.shoptask.impl;

import com.axon.market.common.cache.RedisCache;
import com.axon.market.common.cache.impl.ShopTaskLock;
import com.axon.market.common.domain.ishop.ShopTaskDomain;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.core.service.ishop.SendShopSmsService;
import com.axon.market.core.service.ishop.ShopTaskService;
import com.axon.market.core.shoptask.TaskExecutor;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by 针对常驻用户或老用户营销 on 2017/2/20.
 */
@Component("perTaskExecutor")
public class PerTaskExecutor implements TaskExecutor
{
    private static final Logger LOG = Logger.getLogger(PerTaskExecutor.class.getName());

    @Autowired
    @Qualifier("shopTaskService")
    private ShopTaskService shopTaskService;

    @Autowired
    @Qualifier("redisCache")
    private RedisCache redisCache;

    @Autowired
    @Qualifier("sendShopSmsService")
    private SendShopSmsService sendShopSmsService;

    @Override
    public String execute(ShopTaskDomain domain)
    {
        Date date = new Date();
        LOG.info("Per Task Executor : " + domain.getTaskName() + " " + date.toString());

        //任务去重，如果当前门店存在常驻用户的任务已经执行，不再执行
        String key = String.valueOf(domain.getBaseId());
        ShopTaskLock shopTaskLock = new ShopTaskLock(key);
        if (!redisCache.doAction(shopTaskLock))
        {
            return 0 + "-" + 0;
        }

        Integer count = 0;
        long currentMills = System.currentTimeMillis();
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
        if(StringUtils.isNotEmpty(domain.getMessageAutograph()))
        {
            smsMessage += domain.getMessageAutograph();
        }

        Integer baseAreaId = domain.getBaseAreaId(), baseId = domain.getBaseId();
        if (baseAreaId != null && baseId != null)
        {
            String fullTableName = "model.model_" + baseAreaId;
            if ((currentMills > getTimeMills("0900") && currentMills < getTimeMills("1130")))
            {
                count += sendShopSmsService.sendSms(sendShopSmsService.createSelectDataFromModelSql(fullTableName,null, baseId,baseAreaId, 200), 100, marketUserCountLimit, smsMessage, taskId, accessNumber,baseId,2,1);
            }
            else if ((currentMills > getTimeMills("1130") && currentMills < getTimeMills("1500")))
            {
                count += sendShopSmsService.sendSms(sendShopSmsService.createSelectDataFromModelSql(fullTableName, null, baseId,baseAreaId, 300), 100, marketUserCountLimit, smsMessage, taskId, accessNumber, baseId,2,1);
            }
            else if ((currentMills > getTimeMills("1500") && currentMills < getTimeMills("1800")))
            {
                count += sendShopSmsService.sendSms(sendShopSmsService.createSelectDataFromModelSql(fullTableName, null, baseId,baseAreaId, 100), 100, marketUserCountLimit, smsMessage, taskId, accessNumber, baseId,2,1);
            }
        }
        return count + "-" + taskId;
    }

    /**
     * @param time
     * @return
     */
    private long getTimeMills(String time)
    {
        String timeString = TimeUtil.formatDateToYMDDevide(new Date());
        long result = 0L;
        try
        {
            switch (time)
            {
                case "0900":
                {
                    result = TimeUtil.formatDate(timeString + " 09:00:00").getTime();
                    break;
                }
                case "1130":
                {
                    result = TimeUtil.formatDate(timeString + " 11:30:00").getTime();
                    break;
                }
                case "1500":
                {
                    result = TimeUtil.formatDate(timeString + " 15:00:00").getTime();
                    break;
                }
                case "1800":
                {
                    result = TimeUtil.formatDate(timeString + " 18:00:00").getTime();
                    break;
                }
            }
        }
        catch (ParseException e)
        {
            LOG.error("Parse Time Error. ", e);
        }
        return result;
    }
}
