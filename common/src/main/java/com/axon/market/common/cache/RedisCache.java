package com.axon.market.common.cache;

import com.axon.market.common.util.SpringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * Created by yangyang on 2016/3/7.
 */
@Service("redisCache")
public class RedisCache
{
    private static final Logger LOG = Logger.getLogger(RedisCache.class.getName());

    @Autowired
    @Qualifier("jedisPool")
    private JedisPool jedisPool;

    public static RedisCache getInstance()
    {
        return (RedisCache) SpringUtil.getSingletonBean("redisCache");
    }

    /**
     * 建议批量批量数据调用该方法一次，可以达到重复利用一个连接的效果
     * <br>
     * 注：数据量不能太大，防止连接不释放，影响后续连接。
     *
     * @param redisAction
     */
    public synchronized boolean doAction(IRedisAction redisAction)
    {
        Jedis jedis = null;
        try
        {
            jedis = jedisPool.getResource();

            return redisAction.action(jedis);
        }
        catch (Exception e)
        {
            LOG.error("Redis Operate error. ", e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }
        return false;
    }

    public Long del(IRedisClient redisClient)
    {
        Jedis jedis = null;
        try
        {
            jedis = jedisPool.getResource();

            return redisClient.del(jedis);
        }
        catch (Exception e)
        {
            LOG.error("Redis Operate error. ", e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }
        return 0L;
    }

    public String get(IRedisClient redisClient)
    {
        Jedis jedis = null;
        try
        {
            jedis = jedisPool.getResource();

            return redisClient.get(jedis);
        }
        catch (Exception e)
        {
            LOG.error("Redis Operate error. ", e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }
        return null;
    }

    public List<String> mget(IRedisClient redisClient)
    {
        Jedis jedis = null;
        try
        {
            jedis = jedisPool.getResource();
            return redisClient.mget(jedis);
        }
        catch (Exception e)
        {
            LOG.error("Redis Operate error. ", e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }
        return null;
    }

    public String set(IRedisClient redisClient)
    {
        Jedis jedis = null;
        try
        {
            jedis = jedisPool.getResource();
            return redisClient.set(jedis);
        }
        catch (Exception e)
        {
            LOG.error("Redis Operate error. ", e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }
        return null;
    }

    public String mset(IRedisClient redisClient)
    {
        Jedis jedis = null;
        try
        {
            jedis = jedisPool.getResource();
            return redisClient.mset(jedis);
        }
        catch (Exception e)
        {
            LOG.error("Redis Operate error. ", e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }
        return null;
    }

    public Long hset(IRedisClient redisClient)
    {
        Jedis jedis = null;
        try
        {
            jedis = jedisPool.getResource();
            return redisClient.hset(jedis);
        }
        catch (Exception e)
        {
            LOG.error("Redis Operate error. ", e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }
        return null;
    }

    public String hmset(IRedisClient redisClient)
    {
        Jedis jedis = null;
        try
        {
            jedis = jedisPool.getResource();
            return redisClient.hmset(jedis);
        }
        catch (Exception e)
        {
            LOG.error("Redis Operate error. ", e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }
        return null;
    }

    public String hget(IRedisClient redisClient)
    {
        Jedis jedis = null;
        try
        {
            jedis = jedisPool.getResource();
            return redisClient.hget(jedis);
        }
        catch (Exception e)
        {
            LOG.error("Redis Operate error. ", e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }
        return null;
    }

    public List<String> hmget(IRedisClient redisClient)
    {
        Jedis jedis = null;
        try
        {
            jedis = jedisPool.getResource();
            return redisClient.hmget(jedis);
        }
        catch (Exception e)
        {
            LOG.error("Redis Operate error. ", e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }
        return null;
    }





}
