package com.axon.market.core.task;

import com.axon.market.common.domain.ishop.ShopTaskDomain;
import com.axon.market.common.timer.RunJob;
import com.axon.market.common.util.ThreadPoolUtil;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.core.service.ishop.SendShopSmsService;
import com.axon.market.core.service.ishop.ShopTaskService;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;

/**
 * Created by chenyu on 2017/3/20.
 */
public class MonitorShopAfternoonBusyTask extends RunJob
{
    private static final Logger LOG = Logger.getLogger(MonitorShopAfternoonBusyTask.class.getName());

    private ShopTaskService shopTaskService = ShopTaskService.getInstance();

    private SendShopSmsService sendShopSmsService = SendShopSmsService.getInstance();

    @Override
    public void runBody()
    {
        List<ShopTaskDomain> list = shopTaskService.queryExtralExecuteShopTask(TimeUtil.formatDateToYMD(new Date()));

        for (ShopTaskDomain domain : list)
        {
            ThreadPoolUtil.submit(new ShopTaskExecutor(domain, 6));
        }
    }
}
