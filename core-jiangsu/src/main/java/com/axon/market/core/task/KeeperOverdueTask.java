package com.axon.market.core.task;

import com.axon.market.common.timer.RunJob;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.core.service.ikeeper.KeeperTaskService;
import org.apache.log4j.Logger;

import java.util.Calendar;

/**
 * Created by wangtt on 2017/9/1.
 */
public class KeeperOverdueTask extends RunJob
{
    private static Logger LOG = Logger.getLogger(KeeperOverdueTask.class.getName());

    KeeperTaskService keeperTaskService = KeeperTaskService.getInstance();

    @Override
    public void runBody()
    {
        try
        {
            LOG.info("expireKeeperTask start");
            Calendar calendar = Calendar.getInstance();
            String date = TimeUtil.formatDateToYMDDevide(calendar.getTime());//yyyy-mm-dd
            int i = keeperTaskService.expireKeeperTask(date);
            LOG.info(" today is "+date + " ,expire " + i +" keeperTasks");
            LOG.info("expireKeeperTask end");
        }
        catch (Exception e)
        {
            LOG.error("expireKeeperTask error",e);
        }
    }
}
