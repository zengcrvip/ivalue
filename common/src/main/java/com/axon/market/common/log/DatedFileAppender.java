package com.axon.market.common.log;

import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chenyu on 2016/6/30.
 */
public class DatedFileAppender extends MemoryAppender
{
    private long nextCheck = 0L;

    private String originFilename = null;

    private String getDatedFilename()
    {
        String str = this.originFilename;
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String time14 = format.format(new Date());
        str = str.replaceAll("%yyyy", time14.substring(0, 4));
        str = str.replaceAll("%yy", time14.substring(0, 4));
        str = str.replaceAll("%mm", time14.substring(4, 6));
        str = str.replaceAll("%dd", time14.substring(6, 8));
        str = str.replaceAll("%hh", time14.substring(8, 10));

        return str;
    }

    void switchFilename() throws IOException
    {
        String datedFilename = getDatedFilename();
        if (!datedFilename.equals(this.fileName))
        {
            try
            {
                setFile(datedFilename, getAppend(), getBufferedIO(), getBufferSize());
            }
            catch (IOException ex)
            {
                this.errorHandler.error("setFile(" + datedFilename + ", false) call failed.");
            }
            this.fileName = datedFilename;
        }
    }

    public void setFile(String file)
    {
        this.originFilename = file;
        super.setFile(getDatedFilename());
    }

    protected void subAppend(LoggingEvent event)
    {
        long n = System.currentTimeMillis();
        if (n >= this.nextCheck)
        {
            this.nextCheck = (n + 30000L);
            try
            {
                switchFilename();
            }
            catch (IOException ioe)
            {
                LogLog.error("switchFilename() failed.", ioe);
            }
        }
        super.subAppend(event);
    }
}
