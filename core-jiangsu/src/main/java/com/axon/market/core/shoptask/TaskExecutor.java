package com.axon.market.core.shoptask;

import com.axon.market.common.domain.ishop.ShopTaskDomain;

/**
 * 炒店任务处理
 * Created by zengcr on 2017/2/20.
 */
public interface TaskExecutor
{
    /**
     * 任务执行
     * @param domain 炒店任务实体
     * @return
     */
    String execute(ShopTaskDomain domain);
}
