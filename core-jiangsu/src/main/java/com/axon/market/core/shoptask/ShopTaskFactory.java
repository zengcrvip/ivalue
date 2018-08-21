package com.axon.market.core.shoptask;

import com.axon.market.common.util.SpringUtil;
import com.axon.market.core.shoptask.impl.*;

/**
 * 炒店任务执行器工厂类
 * Created by zengcr on 2017/2/20.
 */
public class ShopTaskFactory
{
    public TaskExecutor createShopTask(Integer type)
    {
        switch (type)
        {
            case 1:
            {
                return (PerTaskExecutor) SpringUtil.getSingletonBean("perTaskExecutor");
            }
            case 2:
            {
                return (FlowTaskExecutor) SpringUtil.getSingletonBean("flowTaskExecutor");
            }
            case 4:
            {
                return (RecommendationTaskExecutor) SpringUtil.getSingletonBean("recommendationTaskExecutor");
            }
            case 5:
            {
                return (AppointTaskExecutor) SpringUtil.getSingletonBean("appointTaskExecutor");
            }
            case 6:
            {
                return (AfternoonPerTaskExecutor)SpringUtil.getSingletonBean("afternoonPerTaskExecutor");
            }
            case 7:
            {
                return (NightPerTaskExecutor)SpringUtil.getSingletonBean("nightPerTaskExecutor");
            }

        }
        return null;
    }
}
