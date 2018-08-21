package com.axon.market.common.util;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by admindd on 2016/7/6.
 */
public class ValidateUtil
{
	public static boolean isNotEmpty(Object object)
    {
        if (object == null)
        {
            return false;
        }

        if (object instanceof String)
        {
            return !"".equals(object);
        }

        if (object instanceof List)
        {
            return ((List) object).size() > 0;
        }

        if (object instanceof Set)
        {
            return ((Set) object).size() > 0;
        }

        if (object instanceof Map)
        {
            return ((Map) object).size() > 0;
        }

        if (object instanceof Object[])
        {
            return ((Object[]) object).length > 0;
        }

        return false;
    }

    public static boolean isEmpty(Object object)
    {
        return !isNotEmpty(object);
    }

    /**
     * 判断字符串是否为空
     * @param str
     * @return
     */
    public static boolean isStrEmpty(String str)
    {
        if(null == str || "".equals(str)){
            return true;
        }
        return false;
    }

    /**
     * 判断Integer是否为空
     * @param num
     * @return
     */
    public static boolean isIntegerEmpty(Integer num)
    {
        if(null == num){
            return true;
        }
        return false;
    }
}
