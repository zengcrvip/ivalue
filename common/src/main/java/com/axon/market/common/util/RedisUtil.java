package com.axon.market.common.util;

import com.axon.market.common.cache.IRedisAction;
import com.axon.market.common.cache.IRedisClient;
import com.axon.market.common.cache.RedisCache;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;

/**
 * redis工具类
 * Created by zengcr on 2017/5/2.
 */
public class RedisUtil
{
    private RedisCache redisCache = RedisCache.getInstance();

    private static RedisUtil redisUtil=null;
    public static RedisUtil getInstance(){
        if(redisUtil== null) {
            redisUtil = new RedisUtil();
        }
        return redisUtil;
    }

    /**
     * 功能描述：删除指定的所有KEY缓存数据
     * 输入参数：<按照参数定义顺序>
     * 返回值:  类型 <说明>
     * @return 返回值
     * @throw 异常描述
     */
    public  Long del(final String ...keys) {
        return redisCache.del(new IRedisClient()
        {
            @Override
            public Long del(Jedis jedis)
            {
                return jedis.del(keys);
            }
        });
    }

    /**
     * 功能描述：单个KEY/VALUE设置，若KEY不存在则新增，若存在则覆盖VALUE
     * 输入参数：<按照参数定义顺序>
     * 返回值:  类型 <说明>
     * @return 返回值
     * @throw 异常描述
     */
    public  String set(final String key,final String value) {
        return redisCache.set(new IRedisClient()
        {
            @Override
            public String set(Jedis jedis)
            {
                return jedis.set(key, value);
            }
        });
    }

    /**
     * 功能描述：批量KEY/VALUE设置，若KEY不存在则新增，若存在则覆盖VALUE
     * 输入参数：<按照参数定义顺序>
     * 参数说明  格式[key1,value1,key2,value2,.....]
     * 返回值:  类型 <说明>
     * @return 返回值
     * @throw 异常描述
     */
    public  String mset(final String ...keysvalues) {
        return redisCache.mset(new IRedisClient()
        {
            public String mset(Jedis jedis)
            {
                return jedis.mset(keysvalues);
            }
        });
    }

    /**
     * 功能描述：取key对应的VALUE值
     * 输入参数：<按照参数定义顺序>
     * 返回值:  类型 <说明>
     * @return 返回值
     * @throw 异常描述
     */
    public  String get(final String key) {
        return redisCache.get(new IRedisClient()
        {
            @Override
            public String get(Jedis jedis)
            {
                return jedis.get(key);
            }
        });
    }

    /**
     * 功能描述：取给定所有的KEY的值
     * 输入参数：<按照参数定义顺序>
     *  参数说明[key1,key2,....]
     * 返回值:  类型 <说明>
     * @return 返回值
     * @throw 异常描述
     */
    public List<String> mget(final String ...key) {
        return redisCache.mget(new IRedisClient()
        {
            @Override
            public List<String> mget(Jedis jedis)
            {
                return jedis.mget(key);
            }
        });
    }

    /**
     * 功能描述：同时将一个 field-value (域-值)对设置到哈希表 key 中。
     * 输入参数：<按照参数定义顺序>
     * 参数说明
     * 返回值:  类型 <说明>
     * @return 返回值
     * @throw 异常描述
     */
    public  Long hset(final String key,final String field,final String value) {
        return redisCache.hset(new IRedisClient()
        {
            @Override
            public Long hset(Jedis jedis)
            {
                return jedis.hset(key, field, value);
            }
        });
    }

    /**
     * 功能描述：同时将多个 field-value (域-值)对设置到哈希表 key 中。
     * 输入参数：<按照参数定义顺序>
     * 参数说明
     * 返回值:  类型 <说明>
     * @return 返回值
     * @throw 异常描述
     */
    public  String hmset(final String key,final Map<String,String> fieldvalue) {
        return redisCache.hmset(new IRedisClient()
        {
            @Override
            public String hmset(Jedis jedis)
            {
                return jedis.hmset(key, fieldvalue);
            }
        });
    }

    /**
     * 功能描述：返回哈希表 key 中给定域 field 的值
     * 输入参数：<按照参数定义顺序>
     * 参数说明
     * 返回值:  类型 <说明>
     * @return 返回值
     * @throw 异常描述
     */
    public  String hget(final String key,final String field) {
        return redisCache.hget(new IRedisClient()
        {
            @Override
            public String hget(Jedis jedis)
            {
                return jedis.hget(key, field);
            }
        });
    }

    /**
     * 功能描述：返回哈希表 key 中，一个或多个给定域的值。如果给定的域不存在于哈希表，那么返回一个 nil 值
     * 输入参数：<按照参数定义顺序>
     *  参数说明
     * 返回值:  类型 <说明>
     * @return 返回值
     * @throw 异常描述
     */
    public  List<String> hmget(final String key,final String ...fields) {
        return redisCache.hmget(new IRedisClient()
        {
            @Override
            public List<String> hmget(Jedis jedis)
            {
                return jedis.hmget(key, fields);
            }
        });
    }


}
