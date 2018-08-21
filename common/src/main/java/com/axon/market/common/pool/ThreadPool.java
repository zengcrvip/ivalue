package com.axon.market.common.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池
 * Created by admindd on 2016/7/7.
 */
public class ThreadPool
{
    /**
     *
     */
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(10);

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
