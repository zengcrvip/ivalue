package com.axon.market.common.util;

import org.apache.commons.lang.StringUtils;

/**
 * Created by yuanfei on 2017/4/5.
 */
public class SearchConditionUtil
{
    public static String optimizeCondition(String conditionStr)
    {
        if (StringUtils.isNotEmpty(conditionStr))
        {
            return conditionStr.replace("%","\\%").replace("_","\\_");
        }
        return conditionStr;
    }

    public static String optimizeConditionForGP(String conditionStr){
        if (StringUtils.isNotEmpty(conditionStr))
        {
            return conditionStr.replace("%","\\\\%").replace("_","\\\\_");
        }
        return conditionStr;
    }
}
