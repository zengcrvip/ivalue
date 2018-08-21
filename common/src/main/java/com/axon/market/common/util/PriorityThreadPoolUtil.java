package com.axon.market.common.util;

import java.util.concurrent.*;

/**
 * 具备优先级的线程池
 * Created by zengcr on 2017/3/28.
 */
public class PriorityThreadPoolUtil
{
    /**
     *  创建静态线程池
     */
    private static final ExecutorService EXECUTOR_SERVICE = new ThreadPoolExecutor(6, 6, 1, TimeUnit.SECONDS, new PriorityBlockingQueue<Runnable>());

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
