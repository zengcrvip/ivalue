package com.axon.market.core.shoptask.cache;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by chenyu on 2017/3/13.
 */
public class TaskIdCache
{
    private static AtomicInteger taskId = new AtomicInteger(1000);

    public static int getTaskId()
    {
        return taskId.getAndIncrement();
    }
}
