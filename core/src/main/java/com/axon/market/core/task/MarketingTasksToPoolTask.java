package com.axon.market.core.task;

import com.axon.market.common.timer.RunJob;
import com.axon.market.core.service.ischeduling.MarketingTaskPoolService;
import org.apache.log4j.Logger;

/**
 * Created by yuanfei on 2017/6/8.
 */
public class MarketingTasksToPoolTask extends RunJob
{
    private static final Logger LOG = Logger.getLogger(MarketingTasksToPoolTask.class.getName());

    private MarketingTaskPoolService marketingTaskPoolService = MarketingTaskPoolService.getInstance();

    @Override
    public void runBody()
    {
        LOG.info("MarketingTasks To Pool Batch Start");
        try
        {
            marketingTaskPoolService.batchHandleMarketingTasksToPool();
        }
        catch (Exception e)
        {
            LOG.error("MarketingTasks To Pool Batch Fail", e);
        }

        LOG.info("MarketingTasks To Pool Batch End");
    }
}
