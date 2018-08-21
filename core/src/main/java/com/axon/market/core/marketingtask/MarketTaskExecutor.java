package com.axon.market.core.marketingtask;

import com.axon.market.common.domain.ischeduling.MarketingPoolTaskDomain;

/**
 * 营销任务执行器
 * Created by zengcr on 2017/6/13.
 */
public interface MarketTaskExecutor
{
    /**
     * 任务执行
     * @param poolTaskDomain 营销任务实体
     * @return
     */
    String execute(MarketingPoolTaskDomain poolTaskDomain);
}
