package com.axon.market.common.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 线程池
 * Created by admindd on 2016/7/7.
 */
public class ThreadPoolUtil
{
    /**
     *
     */
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(30);

    private static final ReentrantLock lock = new ReentrantLock();

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
