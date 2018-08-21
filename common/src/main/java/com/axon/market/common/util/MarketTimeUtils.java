package com.axon.market.common.util;

import com.axon.market.common.constant.isystem.MarketJobScheduleTypeEnum;
import com.axon.market.common.domain.ischeduling.MarketJobDomain;
import org.apache.commons.lang.StringUtils;
import org.quartz.CronExpression;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MarketTimeUtils
{

    private static final ThreadLocal<SimpleDateFormat> DEFAULT_DATE_TIME_FORMAT = new ThreadLocal<SimpleDateFormat>()
    {
        @Override
        protected SimpleDateFormat initialValue()
        {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private static final ThreadLocal<SimpleDateFormat> YMD_FORMAT = new ThreadLocal<SimpleDateFormat>()
    {
        @Override
        protected SimpleDateFormat initialValue()
        {
            return new SimpleDateFormat("yyyyMMdd");
        }
    };

    private static final ThreadLocal<SimpleDateFormat> YMDHMS_FORMAT = new ThreadLocal<SimpleDateFormat>()
    {
        @Override
        protected SimpleDateFormat initialValue()
        {
            return new SimpleDateFormat("yyyyMMddHHmmss");
        }
    };

    /**
     * @param date
     * @return
     */
    public static String formatDate(Date date)
    {
        if (date == null)
        {
            return null;
        }
        return DEFAULT_DATE_TIME_FORMAT.get().format(date);
    }

    /**
     * @param date
     * @return
     */
    public static String formatDateToYMD(Date date)
    {
        if (date == null)
        {
            return null;
        }
        return YMD_FORMAT.get().format(date);
    }

    /**
     * @param date
     * @return
     */
    public static String formatDateToYMDHMS(Date date)
    {
        if (date == null)
        {
            return null;
        }
        return YMDHMS_FORMAT.get().format(date);
    }

    /**
     * @param dataStr
     * @return
     * @throws ParseException
     */
    public static Date formatDate(String dataStr) throws ParseException
    {
        if (StringUtils.isEmpty(dataStr))
        {
            return null;
        }
        return DEFAULT_DATE_TIME_FORMAT.get().parse(dataStr);
    }

    /**
     * @param startTimeStr
     * @param cronExpression
     * @param endTimeStr
     * @return
     * @throws ParseException
     */
    private static long getNextExecuteMills(String startTimeStr, String cronExpression, String endTimeStr) throws ParseException
    {
        Date startTime = formatDate(startTimeStr);
        Date endTime = null;
        if (endTimeStr != null)
        {
            endTime = formatDate(endTimeStr);
        }
        Date now = new Date();
        if (now.after(startTime))
        {
            startTime = now;
        }
        CronExpression cron = new CronExpression(cronExpression);
        if (!cron.isSatisfiedBy(startTime))
        {
            startTime = cron.getNextValidTimeAfter(startTime);
        }

        if (endTime == null || (startTime != null && !startTime.after(endTime)))
        {
            return startTime.getTime();
        }
        else
        {
            return 0;
        }
    }

    /**
     *
     * @param startTimeStr
     * @param intervalInSeconds
     * @param endTimeStr
     * @return
     * @throws ParseException
     */
    private static long getNextExecuteMills(String startTimeStr, int intervalInSeconds, String endTimeStr)
            throws ParseException
    {
        Date startTime = formatDate(startTimeStr);
        Date endTime = null;
        if (endTimeStr != null)
        {
            endTime = formatDate(endTimeStr);
        }
        long intervalMills = intervalInSeconds * 1000;
        long startMills = startTime.getTime();
        long nowMills = System.currentTimeMillis();
        if (startMills < nowMills)
        {
            long count = (nowMills - startMills) / intervalMills;
            startMills += count * intervalMills;
            if (startMills < nowMills)
            {
                startMills += intervalMills;
            }
        }
        if (endTime == null || startMills <= endTime.getTime())
        {
            return startMills;
        }
        else
        {
            return 0;
        }
    }

    /**
     * @param time 以冒号形式分开(00:00:00)
     * @return
     */
    public static String getSpecifiedTime(String time)
    {
        String[] times = time.split(":");
        int hour = Integer.parseInt(times[0]);
        int minute = Integer.parseInt(times[1]);
        int second = Integer.parseInt(times[2]);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, 0);
        return formatDate(calendar.getTime());
    }

    /**
     *
     * @param marketJobDomain
     * @return
     * @throws ParseException
     */
    public static String getCurrentTheoreticalDataDate(MarketJobDomain marketJobDomain) throws ParseException
    {
        if (marketJobDomain.getScheduleType().equals(MarketJobScheduleTypeEnum.SINGLE.getValue()))
        {
            return DEFAULT_DATE_TIME_FORMAT.get().format(getNextExecuteMills(marketJobDomain.getStartTime(), marketJobDomain.getIntervalInSeconds(), marketJobDomain.getEndTime()));
        }
        else if (marketJobDomain.getScheduleType().equals(MarketJobScheduleTypeEnum.CRON.getValue()))
        {
            return DEFAULT_DATE_TIME_FORMAT.get().format(getNextExecuteMills(marketJobDomain.getStartTime(), marketJobDomain.getCronValue(), marketJobDomain.getEndTime()));
        }
        else if (marketJobDomain.getScheduleType().equals(MarketJobScheduleTypeEnum.MANU.getValue()))
        {
            return "-";
        }
        throw new RuntimeException("not support schedule type" + marketJobDomain.getScheduleType());
    }

}
