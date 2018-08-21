package com.axon.market.common.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 营销任务线程池
 * Created by zengcr on 2017/6/13.
 */
public class MarketTaskThreadPoolUtil
{
    /**
     *
     */
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(4);

    /**
     * 线程池接口
     *
     * @param runnable
     */
    public static void submit(Runnable runnable)
    {
        EXECUTOR_SERVICE.submit(runnable);
    }
}
