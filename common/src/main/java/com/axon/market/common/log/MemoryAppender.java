package com.axon.market.common.log;

import org.apache.log4j.FileAppender;
import org.apache.log4j.spi.LoggingEvent;

import java.util.HashMap;
import java.util.Vector;

/**
 * Created by chenyu on 2016/6/30.
 */
public class MemoryAppender extends FileAppender
{
    private static int maxSize = 2000;

    private static HashMap loggerMap = new HashMap();

    protected void subAppend(LoggingEvent event)
    {
        super.subAppend(event);
        Vector logger = getLogger(event.getLoggerName());
        writeEvent(logger, this.layout.format(event).trim());
    }

    public static synchronized Vector getLogger(String name)
    {
        Vector vtr = (Vector) loggerMap.get(name);
        if (vtr == null)
        {
            vtr = new Vector(1000);
            loggerMap.put(name, vtr);
        }
        return vtr;
    }

    private static void writeEvent(Vector logger, String event)
    {
        synchronized (logger)
        {
            logger.add(event);
            while (logger.size() > maxSize)
            {
                logger.remove(0);
            }
        }
    }
}
