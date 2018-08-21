package com.axon.market.service.task;

import com.axon.market.common.timer.NoRedoIntervalTask;
import com.axon.market.common.timer.Timer;
import com.axon.market.common.timer.TimerTask;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.core.task.OldCustomerTask;

import java.text.ParseException;

/**
 * Created by wangtt on 2017/8/1.
 */
public class OldCustomerDispatch
{
    public void initOldCustomerTask(Timer timer)throws ParseException
    {
        int intervalMills = 86400*1000;
        TimerTask oldCusTomerTask = new NoRedoIntervalTask("old_customer_task", TimeUtil.formatDate("2017-01-01 06:00:00"),null,intervalMills,new OldCustomerTask());
        timer.addTask(oldCusTomerTask);
    }


}
