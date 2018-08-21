package com.axon.market.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yangyang on 2016/2/18.
 */
public class SpringUtil implements ApplicationContextAware
{
    private static ApplicationContext applicationContext;

    private static ConcurrentHashMap<String,Object> beans = new ConcurrentHashMap<String,Object>();

    public static Object getSingletonBean(String beanName)
    {
        Object result = beans.get(beanName);
        if(result != null)
        {
            return result;
        }

        result = applicationContext.getBean(beanName);
        if(result != null)
        {
            beans.putIfAbsent(beanName,result);
        }
        return result;
    }

    public static <T> T getPrototypeBean(Class<T> className)
    {
        return applicationContext.getBean(className);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        SpringUtil.applicationContext = applicationContext;
    }
}
