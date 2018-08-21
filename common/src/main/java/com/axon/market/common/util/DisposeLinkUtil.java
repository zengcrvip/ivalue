package com.axon.market.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 生成唯一短链
 * Created by chenyu on 2016/8/25.
 */
public class DisposeLinkUtil
{

    /**
     * 格式化工具
     */
    private static final ThreadLocal<SimpleDateFormat> DEFAULT_DATE_TIME_FORMAT = new ThreadLocal<SimpleDateFormat>()
    {
        @Override
        protected SimpleDateFormat initialValue()
        {
            return new SimpleDateFormat("MMdd");
        }
    };

    /**
     *
     */
    private static final Map<String, Long> values = new HashMap<String, Long>();

    /**
     * 获取短链
     *
     * @return
     */
    public static String getShortLink()
    {
        String prefix = DEFAULT_DATE_TIME_FORMAT.get().format(new Date());

        synchronized (DisposeLinkUtil.class)
        {
            Long index = values.get(prefix);
            if (index == null)
            {
                values.clear();
                values.put(prefix, 0L);
                return prefix + "0";
            }
            else
            {
                values.put(prefix, index++);
                return prefix + index;
            }
        }

    }

}