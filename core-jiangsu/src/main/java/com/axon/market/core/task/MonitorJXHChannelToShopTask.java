package com.axon.market.core.task;

import com.axon.market.common.timer.RunJob;
import com.axon.market.core.service.ishop.ShopTaskService;
import org.apache.log4j.Logger;

/**
 * 定时将精细化传过来的渠道增加到炒店渠道中
 * Created by zengcr on 2017/6/15.
 */
public class MonitorJXHChannelToShopTask extends RunJob
{
    private static final Logger LOG = Logger.getLogger(MonitorJXHChannelToShopTask.class.getName());

    ShopTaskService shopTaskService = ShopTaskService.getInstance();

    @Override
    public void runBody()
    {
        LOG.info("MonitorJXHChannelToShopTask begin");
        try
        {
            shopTaskService.pushJXHChannelToshop();
        }
        catch (Exception e)
        {
            LOG.error("MonitorJXHChannelToShopTask run error",e);
        }
    }
}
