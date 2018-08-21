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
import java.util.List;

/**
 * 针对指定用户营销
 * Created by zengcr on 2017/3/20.
 */
@Service("appointTaskExecutor")
public class AppointTaskExecutor implements TaskExecutor
{
    private static final Logger LOG = Logger.getLogger(AppointTaskExecutor.class.getName());

    @Autowired
    @Qualifier("shopTaskService")
    private ShopTaskService shopTaskService;

    @Autowired
    @Qualifier("sendShopSmsService")
    private SendShopSmsService sendShopSmsService;

    @Override
    public String execute(ShopTaskDomain shopTaskDomain)
    {
        Date date = new Date();
        LOG.info("AppointTaskExecutor: " + shopTaskDomain.getTaskName() + " " + date.toString());
        Integer taskId = shopTaskDomain.getSmsTaskId();
        String accessNumber = shopTaskDomain.getAccessNumber();
        String smsMessage = shopTaskDomain.getMarketContentText();
        String smsUrl = shopTaskDomain.getMarketUrl();
        if (StringUtils.isNotEmpty(smsUrl))
        {
            smsMessage += smsUrl;
        }
        //短信签名
        if(StringUtils.isNotEmpty(shopTaskDomain.getMessageAutograph()))
        {
            smsMessage += shopTaskDomain.getMessageAutograph();
        }
        String appointUsers = shopTaskDomain.getAppointUsers();
        if (StringUtils.isNotEmpty(appointUsers))
        {
            List<String> phoneList = shopTaskService.queryAppointPhoneListByFileId(Long.parseLong(appointUsers));
            sendShopSmsService.sendSms(phoneList, smsMessage, taskId, accessNumber, shopTaskDomain.getBaseId(),5000,2,3);
        }
        return null;
    }

}
