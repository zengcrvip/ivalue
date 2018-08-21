package com.axon.market.common.cache;

import redis.clients.jedis.Jedis;

/**
 * Created by yangyang on 2016/8/15.
 */
public interface IRedisAction
{
    boolean action(Jedis jedis);
}
