package com.axon.market.common.cache.impl;

import com.axon.market.common.cache.IRedisAction;
import redis.clients.jedis.Jedis;

import java.util.Calendar;

/**
 * Created by Chris on 2017/3/28.
 */
public class ShopTaskLock implements IRedisAction
{
    private String key;

    public ShopTaskLock(String key)
    {
        this.key = key;
    }

    @Override
    public synchronized boolean action(Jedis jedis)
    {
        if (jedis.setnx(key, "1") == 1)
        {
            jedis.expire(key, getExpireTime());
            return true;
        }
        else
        {
            return false;
        }
    }

    private int getExpireTime()
    {
        Calendar calendar = Calendar.getInstance();
        long now = calendar.getTime().getTime() / 1000;
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long time = calendar.getTime().getTime() / 1000;
        return ((int) (time - now));
    }
}
