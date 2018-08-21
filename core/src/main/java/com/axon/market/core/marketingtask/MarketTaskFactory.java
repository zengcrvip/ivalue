package com.axon.market.core.marketingtask;

import com.axon.market.common.util.SpringUtil;
import com.axon.market.core.marketingtask.impl.BatchTaskExecutor;
import com.axon.market.core.marketingtask.impl.JXHCycleTaskExecutor;
import com.axon.market.core.marketingtask.impl.SceneTaskExecutor;

/**
 * 营销任务调度工厂
 * Created by zengcr on 2017/6/13.
 */
public class MarketTaskFactory
{
    public MarketTaskExecutor createMarketTask(String marketType)
    {
        switch (marketType)
        {
            case "sms":
            {
                return (BatchTaskExecutor) SpringUtil.getSingletonBean("batchTaskExecutor");
            }

            case "jxhsms":
            {
                return (JXHCycleTaskExecutor) SpringUtil.getSingletonBean("jxhCycleTaskExecutor");
            }

            case "sceneSms":
            {
                return (SceneTaskExecutor)SpringUtil.getSingletonBean("sceneTaskExecutor");
            }

        }
        return null;
    }
}
