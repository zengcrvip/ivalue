package com.axon.market.common.cache.impl;

import com.axon.market.common.cache.IRedisAction;
import redis.clients.jedis.Jedis;

import java.util.Calendar;

/**
 * 掌柜目标用户加锁，重复3天的用户不生成
 * Created by zengcr on 2017/8/29.
 */
public class KeeperCustLock implements IRedisAction {
    private String key;
    private Integer dayNum;
    private static final long DAY_TIME = 24 * 60 * 60 * 1000L;

    public KeeperCustLock(String key,Integer dayNum) {
        this.key = key;
        this.dayNum = dayNum;
    }

    @Override
    public boolean action(Jedis jedis)
    {
        if (jedis.setnx(key, "1") == 1)
        {
            jedis.expire(key, getExpireTime());
            return false;
        }
        else
        {
            return true;
        }
    }

    private int getExpireTime()
    {
        return ((int) (dayNum * DAY_TIME));
    }


}
