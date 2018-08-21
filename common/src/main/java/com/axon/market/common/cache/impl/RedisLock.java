package com.axon.market.common.cache.impl;

import com.axon.market.common.cache.IRedisAction;
import redis.clients.jedis.Jedis;

/**
 * redis实现数据库乐观锁
 * Created by chenyu on 2016/10/9.
 */
public class RedisLock implements IRedisAction
{
    /**
     * redis操作类型（加锁，解锁）
     */
    private String type;

    /**
     * redis存储key
     */
    private String key;

    public RedisLock(String type, String key)
    {
        this.type = type;
        this.key = key;
    }

    @Override
    public boolean action(Jedis jedis)
    {
        if ("lock".equals(type))
        {
            return jedis.setnx(key, "1") == 1;
        }
        else if ("releaseLock".equals(type))
        {
            return jedis.del(key) == 1;
        }
        else
        {
            return false;
        }
    }
}
