package com.axon.market.common.cache;

import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * redis底层扩展接口
 * Created by zengcr on 2017/5/2.
 */
public class IRedisClient implements IRedisAction
{
    @Override
    public boolean action(Jedis jedis)
    {
        return false;
    }

    public Long del(Jedis jedis)
    {
        return 0L;
    }

    public String set(Jedis jedis)
    {
        return null;
    }

    public String mset(Jedis jedis)
    {
        return null;
    }

    public String get(Jedis jedis)
    {
        return null;
    }

    public List<String> mget(Jedis jedis)
    {
        return null;
    }

    public Long hset(Jedis jedis)
    {
        return null;
    }

    public String hmset(Jedis jedis)
    {
        return null;
    }

    public String hget(Jedis jedis)
    {
        return null;
    }

    public List<String> hmget(Jedis jedis)
    {
        return null;
    }
}
