package com.axon.market.core.task;

import com.axon.market.common.timer.RunJob;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.core.service.iflow.OldCustomerService;
import org.apache.log4j.Logger;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by wangtt on 2017/8/1.
 */
public class OldCustomerTask extends RunJob
{

    private static Logger LOG = Logger.getLogger(OldCustomerTask.class.getName());

    private OldCustomerService oldCustomerService = OldCustomerService.getInstance();



    @Override
    public void runBody()
    {
        try
        {
            LOG.info("expireOldCustomer start");
            Calendar calendar = Calendar.getInstance();
            String date = TimeUtil.formatDateToYMD(calendar.getTime());
            int i = oldCustomerService.expireOldCustomer();
            LOG.info(" today is "+date + " ,expire " + i +" oldCustomerTasks");
            LOG.info("expireOldCustomer end");
        }
        catch (Exception e)
        {
            LOG.error("expireOldCustomer error",e);
        }

    }
}
