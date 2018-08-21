package com.axon.market.core.schedule;

import org.springframework.stereotype.Service;

/**
 * Created by chenyu on 2017/2/6.
 */
@Service("timeConversionService")
public class TimeConversionService
{
    public Long getTaskIntervalSeconds(String time)
    {
        String[] timeAndType = time.split("_");
        Long seconds = null;
        if (timeAndType != null && timeAndType.length == 2)
        {
            switch (timeAndType[1])
            {
                case "d":
                {
                    seconds = Long.parseLong(timeAndType[0]) * 24 * 3600;
                    break;
                }
                case "w":
                {
                    seconds = Long.parseLong(timeAndType[0]) * 7 * 24 * 3600;
                    break;
                }
                case "m":
                {
                    seconds = Long.parseLong(timeAndType[0]) * 30 * 24 * 3600;
                    break;
                }
            }
        }
        return seconds;
    }
}
